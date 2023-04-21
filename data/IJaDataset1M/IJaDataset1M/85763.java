package com.n4.util.helper;

import java.util.Calendar;

/**
 * Utility methods for manipulating Calendar objects.
 * @author Eric Redmond
 */
public class CalendarHelper {

    /**
	 * 
	 * @param calendar
	 * @param flag
	 */
    public static void setMinimum(Calendar calendar, int flag) {
        calendar.set(flag, calendar.getMinimum(flag));
    }

    /**
	 * 
	 * @param calendar
	 * @param flag
	 */
    public static void setMaximum(Calendar calendar, int flag) {
        calendar.set(flag, calendar.getMaximum(flag));
    }

    /**
	 * Changes all items to minimums below the chosen flag.
	 * ie. floor(c, Calendar.MONTH), if c is "Feb 5, 15:32:53.986", then the 
	 * calendar will change to "Feb 1, 00:00:00.000"
	 * @param calendar
	 * @param flag
	 */
    public static void floor(Calendar calendar, int flag) {
        switch(flag) {
            case Calendar.YEAR:
                setMinimum(calendar, Calendar.MONTH);
            case Calendar.MONTH:
                setMinimum(calendar, Calendar.DAY_OF_MONTH);
            case Calendar.DAY_OF_MONTH:
                setMinimum(calendar, Calendar.HOUR_OF_DAY);
            case Calendar.HOUR_OF_DAY:
                setMinimum(calendar, Calendar.MINUTE);
            case Calendar.MINUTE:
                setMinimum(calendar, Calendar.SECOND);
            case Calendar.SECOND:
                setMinimum(calendar, Calendar.MILLISECOND);
        }
    }

    /**
	 * Changes all items to minimums below the chosen flag.
	 * ie. floor(c, Calendar.MONTH), if c is "Mar 5, 15:32:53.986", then the 
	 * calendar will change to "Mar 31, 23:59:59.999"
	 * @param calendar
	 * @param flag
	 */
    public static void cieling(Calendar calendar, int flag) {
        switch(flag) {
            case Calendar.YEAR:
                setMaximum(calendar, Calendar.MONTH);
            case Calendar.MONTH:
                setMaximum(calendar, Calendar.DAY_OF_MONTH);
            case Calendar.DAY_OF_MONTH:
                setMaximum(calendar, Calendar.HOUR_OF_DAY);
            case Calendar.HOUR_OF_DAY:
                setMaximum(calendar, Calendar.MINUTE);
            case Calendar.MINUTE:
                setMaximum(calendar, Calendar.SECOND);
            case Calendar.SECOND:
                setMaximum(calendar, Calendar.MILLISECOND);
        }
    }
}
