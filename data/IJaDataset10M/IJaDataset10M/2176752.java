package time_manager;

public abstract class Event {

    private String nameEvent;

    private String time;

    public Event(String time, String nameEvent) {
        this.time = time;
        this.nameEvent = nameEvent;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public String getTime() {
        return time;
    }
}
