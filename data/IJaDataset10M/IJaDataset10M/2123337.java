package org.gtdfree.journal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author ikesan
 *
 */
public final class JournalTools {

    public static final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

    public static final long MILLIS_IN_HOUR = 1000 * 60 * 60;

    private JournalTools() {
    }

    public static long today() {
        long today = System.currentTimeMillis();
        today /= MILLIS_IN_DAY;
        return today;
    }

    public static long toDay(long time) {
        return time / MILLIS_IN_DAY;
    }

    public static Date toDate(long days) {
        return new Date(days * MILLIS_IN_DAY);
    }

    public static int minutesOfDay() {
        GregorianCalendar g = new GregorianCalendar();
        return g.get(Calendar.HOUR_OF_DAY) * 60 + g.get(Calendar.MINUTE);
    }

    public static int secondsOfDay() {
        GregorianCalendar g = new GregorianCalendar();
        return (g.get(Calendar.HOUR_OF_DAY) * 60 + g.get(Calendar.MINUTE)) * 60 + g.get(Calendar.SECOND);
    }
}
