package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Table(name = "section")
@Entity
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Column(name = "distance", nullable = false)
    private int distance;

    protected Section() {
    }

    public Section(final Line line, final Station upStation, final Station downStation, final int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void updateSectionStationByAddNewSection(final Section newSection) {
        if (isSameUpStation(newSection.getUpStation())) {
            updateUpStation(newSection);
        }
        if (isSameDownStation(newSection.getDownStation())) {
            updateDownStation(newSection);
        }
    }

    public boolean isSameDownStation(final Station otherDownStation) {
        return this.downStation.equals(otherDownStation);
    }

    public boolean isSameUpStation(final Station otherUpStation) {
        return this.upStation.equals(otherUpStation);
    }

    public void updateUpStation(final Section newSection) {
        if (this.distance <= newSection.getDistance()) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = newSection.getDownStation();
        this.distance -= newSection.getDistance();
    }

    public void updateDownStation(final Section newSection) {
        if (this.distance <= newSection.getDistance()) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = newSection.getUpStation();
        this.distance -= newSection.getDistance();
    }

    public List<Station> getUpAndDownStation() {
        return Stream.of(upStation, downStation).collect(toList());
    }

    public boolean isAfter(Section other) {
        return this.upStation.equals(other.getDownStation());
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
