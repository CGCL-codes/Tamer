package jaxlib.time;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Abstract implementation of the {@link Clock} interface.
 * Simple subclasses need to implement only {@link #now()}. The default implementations of the
 * {@link #calendar(long) calendar methods} are returning instances of {@link GregorianCalendar},
 * and {@link #getTimeZone()} returns the {@link TimeZone#getDefault() default zone}.
 * <p>
 * To avoid undefined behaviour this class is not using the {@link Calendar#getInstance() default} calendar system.
 * </p>
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: AbstractClock.java 2741 2009-09-02 03:27:51Z joerg_wassmer $
 */
public abstract class AbstractClock extends Object implements Clock {

    protected AbstractClock() {
        super();
    }

    @Override
    public Calendar calendar() {
        return calendar(now());
    }

    @Override
    public Calendar calendar(final Date t) {
        return (t == null) ? calendar() : calendar(t.getTime());
    }

    @Override
    public Calendar calendar(final long t) {
        final Calendar c = new GregorianCalendar(getTimeZone());
        c.setTimeInMillis(t);
        return c;
    }

    @Override
    public Date date() {
        return Dates.asDateTime(now());
    }

    @Override
    public long getAdjustmentMillis() {
        return 0;
    }

    @Override
    public Clock getSourceClock() {
        return null;
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    @Override
    public boolean isGregorianCalendar() {
        return false;
    }

    @Override
    public Timestamp timestamp() {
        return Dates.asTimestamp(now());
    }
}
