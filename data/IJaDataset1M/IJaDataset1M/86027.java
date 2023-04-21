package org.allesta.wsabi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DOCUMENT ME!
 *
 * @author Allesta, LLC
 * @version $Revision: 1.1 $ 
 */
public class TimeUtil {

    private static SimpleDateFormat _dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    private static SimpleDateFormat _topOfTheDayFormat = new SimpleDateFormat("MM/dd/yyyy 00:00:00");

    private static SimpleDateFormat _topOfTheHourFormat = new SimpleDateFormat("MM/dd/yyyy hh:00:00");

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static java.util.Date getNow() {
        return new java.util.Date(System.currentTimeMillis());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static java.util.Date getTopOfCurrentDay() throws Exception {
        StringBuffer today = todaysDate();
        today.append(" 00:00:00");
        return _topOfTheDayFormat.parse(today.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static java.util.Date getTopOfCurrentHour() throws Exception {
        StringBuffer today = todaysDate();
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        today.append(" ");
        if (hour < 10) {
            today.append("0");
        }
        today.append(hour);
        today.append(":00:00");
        return _topOfTheHourFormat.parse(today.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param daysBackFromToday DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Date getTopOfDay(int daysBackFromToday) throws Exception {
        long now = System.currentTimeMillis();
        long thePast = now - (daysBackFromToday * 86400000);
        Date date = new Date(thePast);
        String x = _topOfTheDayFormat.format(date);
        return _topOfTheDayFormat.parse(x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param day DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static java.util.Date getTopOfDay(String day) throws Exception {
        day += " 00:00:00";
        return _topOfTheDayFormat.parse(day.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param day DOCUMENT ME!
     * @param hour DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static java.util.Date getTopOfHour(String day, int hour) throws Exception {
        if (hour < 10) {
            day += (" 0" + hour);
        } else {
            day += (" " + hour);
        }
        day += ":00:00";
        return _topOfTheHourFormat.parse(day);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static StringBuffer todaysDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month++;
        int year = cal.get(Calendar.YEAR);
        StringBuffer time = new StringBuffer();
        if (month < 10) {
            time.append("0");
        }
        time.append(month);
        time.append("/");
        if (day < 10) {
            time.append("0");
        }
        time.append(day);
        time.append("/");
        time.append(year);
        return time;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param weeksBackFromToday DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws Exception DOCUMENT ME!
	 */
    public static Date getTopOfWeek(int weeksBackFromToday) throws Exception {
        Calendar cal = Calendar.getInstance();
        int daysBack = cal.get(Calendar.DAY_OF_WEEK) + 7 * weeksBackFromToday - 1;
        cal.add(Calendar.DATE, -1 * daysBack);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param monthsBackFromToday DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws Exception DOCUMENT ME!
	 */
    public static Date getTopOfMonth(int monthsBackFromToday) throws Exception {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) - monthsBackFromToday;
        if (month > 0) {
            cal.set(Calendar.MONTH, month);
        } else {
            int lastYear = cal.get(Calendar.YEAR) - 1;
            cal.set(Calendar.YEAR, lastYear);
            cal.set(Calendar.MONTH, (12 + month));
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
