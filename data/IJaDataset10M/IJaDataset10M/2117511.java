package net.fortuna.ical4j.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility methods relevant to Java timezones.
 * 
 * @author benfortuna
 */
public final class TimeZoneUtils {

    /**
	 * Constructor made private to enforce static nature.
	 */
    private TimeZoneUtils() {
    }

    /**
	 * Determines the first start date of daylight savings for the specified
	 * timezone since January 1, 1970.
	 * 
	 * @param timezone
	 *            a timezone to determine the start of daylight savings for
	 * @return a date
	 */
    public static Date getDaylightStart(final TimeZone timezone) {
        Calendar calendar = Calendar.getInstance(timezone);
        calendar.setTime(new Date(0));
        if (timezone.useDaylightTime()) {
            while (timezone.inDaylightTime(calendar.getTime())) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            }
            while (!timezone.inDaylightTime(calendar.getTime())) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            }
        }
        return calendar.getTime();
    }

    /**
	 * Determines the first end date of daylight savings for the specified
	 * timezone since January 1, 1970.
	 * 
	 * @param timezone
	 *            a timezone to determine the end of daylight savings for
	 * @return a date
	 */
    public static Date getDaylightEnd(final TimeZone timezone) {
        Calendar calendar = Calendar.getInstance(timezone);
        calendar.setTime(new Date(0));
        if (timezone.useDaylightTime()) {
            while (!timezone.inDaylightTime(calendar.getTime())) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            }
            while (timezone.inDaylightTime(calendar.getTime())) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            }
        }
        return calendar.getTime();
    }
}
