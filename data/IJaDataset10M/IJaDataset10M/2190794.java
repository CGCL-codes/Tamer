package de.bea.domingo.groupware.repeat;

import java.util.Calendar;
import java.util.Date;

/**
 * Weekend strategy that deletes an event on a weekend.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class Delete extends WeekendStrategy {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     *
     * This strategy deletes the event.
     *
     * @see de.bea.domingo.groupware.repeat.WeekendStrategy#adjust(java.util.Date)
     */
    protected Date adjust(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekday == Calendar.SUNDAY) {
            return null;
        } else if (weekday == Calendar.SATURDAY) {
            return null;
        }
        return date;
    }
}
