package fr.fg.server.events.impl;

import java.awt.Point;
import fr.fg.server.data.Fleet;
import fr.fg.server.events.GameEvent;

public class AfterMoveEvent extends GameEvent {

    private Fleet fleet;

    private Point locationBefore;

    private Point locationAfter;

    public AfterMoveEvent(Fleet fleet, Point locationBefore, Point locationAfter) {
        this.fleet = fleet;
        this.locationBefore = locationBefore;
        this.locationAfter = locationAfter;
    }

    public Fleet getFleet() {
        return fleet;
    }

    public Point getLocationBefore() {
        return locationBefore;
    }

    public Point getLocationAfter() {
        return locationAfter;
    }
}
