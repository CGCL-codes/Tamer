package org.liris.schemerger.core.event;

/**
 * A generic class for implementing events. All events must have at least one
 * #date.
 * 
 * @author Damien Cram
 * 
 */
public abstract class AbstractEvent implements IEvent {

    protected EDate date;

    protected AbstractEvent(EDate date) {
        super();
        this.date = date;
    }

    public int compareTo(IEvent o) {
        return this.date.compareTo(o.getDate());
    }

    /**
	 * 
	 * @return the date of the event.
	 */
    public EDate getDate() {
        return this.date;
    }
}
