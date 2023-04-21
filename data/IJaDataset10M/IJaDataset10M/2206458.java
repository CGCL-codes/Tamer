package org.wd.extractor.model;

import java.util.List;

public class Train {

    private int number;

    private String name;

    private Station origin;

    private Station destination;

    private List<Day> daysOfOperation;

    private Route route;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Station getOrigin() {
        return origin;
    }

    public void setOrigin(Station origin) {
        this.origin = origin;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public List<Day> getDaysOfOperation() {
        return daysOfOperation;
    }

    public void setDaysOfOperation(List<Day> daysOfOperation) {
        this.daysOfOperation = daysOfOperation;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
