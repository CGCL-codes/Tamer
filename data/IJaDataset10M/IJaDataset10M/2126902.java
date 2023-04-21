package be.kuleuven.cs.mop.domain.model.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class Clock {

    private final List<TimeListener> listeners = new LinkedList<TimeListener>();

    private final Calendar time = new GregorianCalendar();

    public Clock() {
        this(System.currentTimeMillis());
    }

    public Clock(final Calendar time) {
        this(time.getTime());
    }

    public Clock(final Date time) {
        this.time.setTime(time);
    }

    public Clock(final long time) {
        this.time.setTimeInMillis(time);
    }

    /**
	 * Adds a time listener object
	 * @param listener - The listener object
	 */
    public synchronized void addListener(final TimeListener listener) {
        if (listener != null) listeners.add(listener);
    }

    /**
	 * Notifies all time listener objects that are registered by calling onTimeChanged
	 */
    protected void fireEvent() {
        for (final TimeListener listener : listeners) listener.onTimeChanged(this);
    }

    /**
	 * Returns the current time of this clock
	 */
    public Calendar getTime() {
        final Calendar time = new GregorianCalendar();
        time.setTimeInMillis(this.time.getTimeInMillis());
        return time;
    }

    /**
	 * Removes a time listener object
	 * @param listener - The listener object
	 */
    public synchronized void removeListener(final TimeListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Sets a new time for this Clock
	 * @param time - The new time
	 */
    public void setTime(final Calendar time) {
        setTime(time.getTimeInMillis());
    }

    /**
	 * Sets the new time for this Clock, in milliseconds
	 * @param time - The new time, in milliseconds
	 */
    public void setTime(final long time) {
        validate(time);
        this.time.setTimeInMillis(time);
        fireEvent();
    }

    /**
	 * Validates the time in milliseconds
	 * @param time - the time
	 * @return false if time is negative
	 */
    protected void validate(final long time) {
        if (time < 0) throw new IllegalArgumentException("Invalid time:  " + time);
    }
}
