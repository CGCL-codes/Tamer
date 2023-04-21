package model.db.data;

import java.io.Serializable;
import java.util.ArrayList;
import model.db.data.event.Event;

public class Lane implements Serializable {

    static final long serialVersionUID = 1234567;

    private int minSpeed;

    private int maxSpeed;

    private ArrayList<Event> events;

    public Lane(int minSpeed, int maxSpeed) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void addEvent(Event e) {
        this.events.add(e);
    }

    public void removeEvent(Event e) {
        this.events.remove(e);
    }

    public Lane() {
        this.events = new ArrayList<Event>();
    }
}
