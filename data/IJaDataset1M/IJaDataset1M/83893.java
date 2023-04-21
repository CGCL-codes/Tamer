package com.google.code.jholidays.events;

import java.util.Calendar;
import java.util.Date;
import com.google.code.jholidays.core.EventDescriptor;
import com.google.code.jholidays.events.parameters.DayMonthPair;
import com.google.code.jholidays.events.properties.FixedEventProperties;

/**
 * Represents fixed event. Those events have fixed date which doesn't change
 * from year to year (e.g. some person's birthday)
 * <p>
 * Fixed event doesn't have parent event (e.g. it is <tt>independent</tt>) so
 * {@link #getParent()} method always returns <tt>null</tt>.
 * <p>
 * <b>Examples:</b><br>
 * <code>07 Jan 2009, 07 Jan 2010, 07 Jan 2011 ...</code><br>
 * This is fixed event which occurs on the 7th of January each year
 * <p>
 * <code>12 May 2009, 12 May 2010, 12 May 2011 ...</code><br>
 * This is fixed event which occurs on the 9th of May each year
 * <p>
 * <code>1 Dec 2009, 1 Dec 2010, 1 Dec 2011 ...</code><br>
 * This is fixed event which occurs on the 1th of December each year
 * 
 * @see AbstractEvent
 * @see EventDescriptor
 * @see FixedEventProperties
 * @author tillias
 * 
 */
public class FixedEvent extends AbstractEvent {

    /**
     * Creates new event instance using given {@link FixedEventProperties}
     * 
     * @param properties
     *            Event properties
     * @throws IllegalArgumentException
     *             Thrown if properties parameter passed to constructor is null
     */
    protected FixedEvent(FixedEventProperties properties) throws IllegalArgumentException {
        super(properties);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns fixed event date. When {@link FixedEvent}
     * instance is created {@link FixedEventProperties} object is passed to
     * event constructor. This properties object contains month and day indexes
     * which are used to calculate event's date for specified year.
     */
    @Override
    public Date getDate(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, getDayMonth().getMonth() - 1, getDayMonth().getDay());
        return calendar.getTime();
    }

    /**
     * Gets day-month associated with given event.
     * 
     * @return Day-month pair
     */
    protected DayMonthPair getDayMonth() {
        return ((FixedEventProperties) getProperties()).getDayMonth();
    }
}
