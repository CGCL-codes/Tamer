package edu.brook.aa;

import org.ascape.model.Cell;

public class Farm extends Cell {

    /**
     * 
     */
    private static final long serialVersionUID = 7663675971484207117L;

    public HouseholdBase household;

    public Location location;

    public HouseholdBase getHousehold() {
        return household;
    }

    public void setHousehold(HouseholdBase household) {
        this.household = household;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void leave() {
        if (location != null) {
            location.setFarm(null);
            location = null;
        }
    }

    public void occupy(Location location) {
        if (this.location == null) {
            location.setFarm(this);
            this.location = location;
        } else {
            throw new RuntimeException("Farm must leave previous location before occuping new one.");
        }
    }
}
