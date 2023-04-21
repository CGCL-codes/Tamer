package org.zkoss.mvvm.examples.wheather;

import java.util.List;
import org.zkoss.bind.BindComposer;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

public class WeathStationViewModel extends BindComposer {

    public WeathStationViewModel() {
        for (int i = 0; i < 10; i++) {
            Station s = Station.random();
            stations.add(s);
        }
    }

    Station station = new Station();

    @DependsOn({ "station", "stations" })
    public boolean getStationIsNotNew() {
        return this.stations.contains(station);
    }

    public Station getStation() {
        return station;
    }

    @NotifyChange
    public void setStation(Station station) {
        this.station = station;
    }

    ListModelList<Station> stations = new ListModelList<Station>();

    public ListModelList<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = new ListModelList(stations, true);
    }

    @Command
    @NotifyChange("station")
    public void create() {
        this.station = new Station();
        return;
    }

    @Command
    @NotifyChange("stations")
    public void add() {
        if (!this.stations.contains(station)) {
            this.stations.add(this.station);
        }
        return;
    }

    @DependsOn("station.name")
    public boolean isValidToAdd() {
        boolean validToAdd = false;
        if (this.station != null) {
            if (this.station.getName() != null) {
                if (!this.station.getName().equals("")) {
                    validToAdd = true;
                }
            }
        }
        return validToAdd;
    }

    @DependsOn("station.name")
    public boolean isNotValidToAdd() {
        return !this.isValidToAdd();
    }

    @DependsOn({ "station.variance" })
    public boolean isLessThan10() {
        if (this.station != null) {
            if (this.station.getVariance() < 0) {
                return (((double) -this.station.getVariance()) / this.station.getTarget()) >= 0.1;
            }
        }
        return false;
    }

    @DependsOn({ "station.variance" })
    public boolean isMoreThan5() {
        if (this.station != null) {
            if (this.station.getVariance() > 0) {
                return (((double) this.station.getVariance()) / this.station.getTarget()) >= 0.05;
            }
        }
        return false;
    }
}
