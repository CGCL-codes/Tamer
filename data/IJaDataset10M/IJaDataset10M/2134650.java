package ui.logger;

public class CacheEvent extends LogEvent {

    public CacheEvent() {
    }

    public CacheEvent(Exception ex) {
        super(ex);
        message += " �������� �������� � �������.";
    }
}
