package identifierMessageBus;

import identifierMessageBus.bus.Event;
import java.util.Calendar;

/**
 * This {@link Event} is intended to query the time of the
 * {@link identifierMessageBus.bus.Device} that can handle this Task. It has a private field
 * of type {@link Calendar} that is set by the Subscriber.
 * 
 * @author Moritz Hoffmann
 * 
 */
public class GetCalendarWorkUnit extends Event {

    public GetCalendarWorkUnit(long sender) {
        super(sender);
    }

    public GetCalendarWorkUnit(GetCalendarWorkUnit unit) {
        super(unit.getSender());
        calendar = unit.getCalendar();
    }

    private Calendar calendar;

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
