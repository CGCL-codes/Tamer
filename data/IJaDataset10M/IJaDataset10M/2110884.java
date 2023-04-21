package model.db.data.event;

import java.io.Serializable;
import util.DeepCloner;

public abstract class Event extends DeepCloner implements Serializable {

    static final long serialVersionUID = 1234567;

    private int startTime;

    private int endTime;

    private Location location;

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
