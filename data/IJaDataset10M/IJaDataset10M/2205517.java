package com.vlee.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarUtil {

    public static void main(String arg[]) {
        new CalendarUtil();
    }

    public CalendarUtil() {
    }

    /**
	 * To calculate total number of weeks for that year.
	 * @param startTimestamp, First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param endTimestamp, Last date of the working calendar year. (eg; for year 2005, is 2006/01/01)
	 * @param noofdaysperweek, Number of days per week. Default 7 days. 
	 * @return int of number of weeks
	 */
    public static int numberOfWeeks(Timestamp startTimestamp, Timestamp endTimestamp, Integer noofdaysperweek) {
        return numberOfWeeks(startTimestamp, endTimestamp, noofdaysperweek.intValue());
    }

    /**
	 * To get the start date of the specific given week number.
	 * @param ts, First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param weekno, Week number.
	 * @param noofdaysperweek, Number of days per week. Default 7 days.
	 * @return 
	 */
    public static Timestamp startDateOfWeek(Timestamp ts, Integer weekno, Integer noofdaysperweek) {
        Timestamp tsp = new Timestamp(new java.util.Date().getTime());
        tsp.setTime(startDateOfWeek(ts, weekno.intValue(), noofdaysperweek.intValue()).getTime().getTime());
        return tsp;
    }

    /**
	 * To get the last date of the specific given week number.
	 * @param First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param weekno, Week number.
	 * @param noofdaysperweek, Number of days per week. Default 7 days.
	 * @return
	 */
    public static Timestamp endDateOfWeek(Timestamp ts, Integer weekno, Integer noofdaysperweek) {
        Timestamp tsp = new Timestamp(new java.util.Date().getTime());
        tsp.setTime(endDateOfWeek(ts, weekno.intValue(), noofdaysperweek.intValue()).getTime().getTime());
        return tsp;
    }

    /**
	 * To calculate total number of weeks for that year.
	 * @param startTimestamp, First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param endTimestamp, Last date of the working calendar year. (eg; for year 2005, is 2006/01/01)
	 * @param noofdaysperweek, Number of days per week. Default 7 days. 
	 * @return int of number of weeks
	 */
    public static int numberOfWeeks(Timestamp startTimestamp, Timestamp endTimestamp, int noofdaysperweek) {
        Calendar startcalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startcalendar.setTimeInMillis(startTimestamp.getTime());
        endCalendar.setTime(new Date(endTimestamp.getTime()));
        return numberOfWeeks(startcalendar, endCalendar, noofdaysperweek);
    }

    /**
	 * To get the start date of the specific given week number.
	 * @param ts, First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param weekno, Week number.
	 * @param noofdaysperweek, Number of days per week. Default 7 days.
	 * @return
	 */
    public static Calendar startDateOfWeek(Timestamp ts, int weekno, int noofdaysperweek) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(ts.getTime()));
        return startDateOfWeek(cal, weekno, noofdaysperweek);
    }

    /**
	 * To get the last date of the specific given week number.
	 * @param First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param weekno, Week number.
	 * @param noofdaysperweek, Number of days per week. Default 7 days.
	 * @return
	 */
    public static Calendar endDateOfWeek(Timestamp ts, int weekno, int noofdaysperweek) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(ts.getTime()));
        return endDateOfWeek(cal, weekno, noofdaysperweek);
    }

    /**
	 * To get the number of days for the year.
	 * @param startcalendar, First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param endCalendar, Last date of the working calendar year. (eg; for year 2005, is 2006/01/01)
	 * @param noofdaysperweek, Number of days per week. Default 7 days. 
	 * @return
	 */
    public static int numberOfDays(Calendar startcalendar, Calendar endCalendar, int noofdaysperweek) {
        endCalendar.add(Calendar.DATE, 1);
        double milliElapsed = endCalendar.getTime().getTime() - startcalendar.getTime().getTime();
        double daysElapsed = (milliElapsed / 24F / 3600F / 1000F);
        return new Double(Math.round(daysElapsed * 100F) / 100F).intValue();
    }

    /**
	 * To calculate total number of weeks for that year.
	 * @param startcalendar, First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param endCalendar, Last date of the working calendar year. (eg; for year 2005, is 2006/01/01)
	 * @param noofdaysperweek, Number of days per week. Default 7 days. 
	 * @return int of number of weeks
	 */
    public static int numberOfWeeks(Calendar startcalendar, Calendar endCalendar, int noofdaysperweek) {
        return (numberOfDays(startcalendar, endCalendar, noofdaysperweek)) / noofdaysperweek;
    }

    /**
	 * To get the start date of the specific given week number.
	 * @param ts, First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param weekno, Week number.
	 * @param noofdaysperweek, Number of days per week. Default 7 days.
	 * @return
	 */
    public static Calendar startDateOfWeek(Calendar startcalendar, int weekno, int noofdaysperweek) {
        startcalendar.add(Calendar.DATE, (weekno - 1) * noofdaysperweek);
        return startcalendar;
    }

    /**
	 * To get the last date of the specific given week number.
	 * @param First date of the working calendar year. (eg; for year 2005, is 2004/11/27)
	 * @param weekno, Week number.
	 * @param noofdaysperweek, Number of days per week. Default 7 days.
	 * @return
	 */
    public static Calendar endDateOfWeek(Calendar startcalendar, int weekno, int noofdaysperweek) {
        startcalendar.add(Calendar.DATE, weekno * noofdaysperweek);
        startcalendar.add(Calendar.DATE, -1);
        return startcalendar;
    }

    public static String getGreeting() {
        String message = null;
        Calendar calendar = new GregorianCalendar();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (currentHour < 12) {
            message = "morning";
        } else {
            if ((currentHour >= 12) && (calendar.get(Calendar.HOUR_OF_DAY) < 18)) {
                message = "afternoon";
            } else {
                message = "evening";
            }
        }
        return message;
    }
}
