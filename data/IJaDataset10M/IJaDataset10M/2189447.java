package org.mbari.util;

import org.mbari.math.Matlib;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utilities for converting between different types of commonly used
 * scientific date formats
 *
 * @author Brian Schlining
 * @revision 1.0.0, 19 Jul 1999
 */
public class TimeUtilities {

    /**
     *
     * @param date
     * @return
     */
    public static int dateToDayOfYear(Date date) {
        GmtCalendar gmt = new GmtCalendar(date);
        return gmt.get(Calendar.DAY_OF_YEAR);
    }

    /**
     *
     * @param millis
     * @return
     */
    public static int dateToDayOfYear(long millis) {
        GmtCalendar gmt = new GmtCalendar(millis);
        return gmt.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Convert the Day of a Year to a Date object. This method can handle days of
     * the year greater than 365 (or 366 for leap years). It simple wraps the day
     * around to the next year. For example: doyToDate(367, 1999) will return a
     * Date object for Jan 2, 2000. All times must be in GMT.
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date dayOfYearToDate(double dayOfYear, double year) {
        double feb = 28.0d;
        double max = 365d;
        if (isLeapYear(year)) {
            feb = 29.0d;
            max = 366d;
        }
        double[] daysPerMonth = { 0d, 31d, feb, 31d, 30d, 31d, 30d, 31d, 31d, 30d, 31d, 30d, 31d };
        double[] days = Matlib.cumsum(daysPerMonth);
        while (Math.floor(dayOfYear) > max) {
            dayOfYear -= max;
            year++;
            if (DateConverter.isLeap(year)) {
                max = 366d;
            } else {
                max = 365.0;
            }
        }
        int month;
        loop: {
            for (month = 0; month < days.length; month++) {
                if (dayOfYear < days[month]) {
                    break loop;
                }
            }
        }
        double day = dayOfYear - days[month - 1];
        double hour = (day - Math.floor(day)) * 24.0d;
        double minute = (hour - Math.floor(hour)) * 60.0d;
        double second = (minute - Math.floor(minute)) * 60.0d;
        GmtCalendar gc = new GmtCalendar((int) year, month - 1, (int) Math.floor(day), (int) Math.floor(hour), (int) Math.floor(minute), (int) Math.rint(second));
        return gc.getTime();
    }

    /**
     * Convert the Day of a Year to a Date object
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date dayOfYearToDate(int dayOfYear, int year) {
        return TimeUtilities.dayOfYearToDate((double) dayOfYear, (double) year);
    }

    /**
     * Convert the Day of a Year to a Date object
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date dayOfYearToDate(float dayOfYear, int year) {
        return TimeUtilities.dayOfYearToDate((double) dayOfYear, (double) year);
    }

    /**
     * Convert the Day of a Year to a Date object
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date dayOfYearToDate(double dayOfYear, int year) {
        return TimeUtilities.dayOfYearToDate(dayOfYear, (double) year);
    }

    /**
     *
     * @param date
     * @return
     */
    public static double getJulianDate(Date date) {
        return TimeUtilities.getJulianDate(date.getTime());
    }

    /**
     *
     * @param millis
     * @return
     */
    public static double[] getJulianDate(long[] millis) {
        double[] julianDate = new double[millis.length];
        for (int i = 0; i < millis.length; i++) {
            julianDate[i] = TimeUtilities.getJulianDate(millis[i]);
        }
        return julianDate;
    }

    /**
     * Julian Date (not Day of Year). Check values against matlabs juldate_.m on
     * 14 Nov 2000. Found some problems so I checked it again on 16 Nov 2000. It
     * appears to match exactly now.
     * @param millis
     * @return
     */
    public static double getJulianDate(long millis) {
        double startGregorian = 588829;
        GmtCalendar gmt = new GmtCalendar(millis);
        int year = gmt.get(Calendar.YEAR);
        int month = gmt.get(Calendar.MONTH) + 1;
        int day = gmt.get(Calendar.DAY_OF_MONTH);
        double JA;
        double JY;
        double JM;
        double JD;
        double JJ;
        if (gmt.get(Calendar.ERA) == 0) {
            year = -year + 1;
        }
        JY = year - 1;
        JM = month + 13;
        if (month > 2) {
            JY = year;
            JM = month + 1;
        }
        JD = Math.floor(365.25 * JY) + Math.floor(30.6001 * JM) + day + 1720995 - 0.5;
        JJ = day + 31 * (month + 12 * year);
        if (JJ >= startGregorian) {
            JA = Math.floor(0.01 * JY);
            JD = JD + 2 - JA + Math.floor(0.25 * JA);
        }
        return JD;
    }

    /**
     * Check to see if a year is a leap year
     *
     * @param year  The year in question
     * @return True if the year is a leap year, false otherwise
     */
    public static boolean isLeapYear(double year) {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.isLeapYear((int) year);
    }

    /**
     * Check to see if a year is a leap year
     *
     * @param year  The year in question
     * @return True if the year is a leap year, false otherwise
     */
    public static boolean isLeapYear(int year) {
        return DateConverter.isLeap((double) year);
    }

    /**
     * Convert from serial days, the format used by matlab,
     * (Serial days.01 jan 0000 00:00:00 = Day 1) to UTC
     * (milliseconds since 01 Jan 1970 00:00:00).
     *
     * @param serialDay The date value to be conerted
     * @return UTC time
     */
    public static long serialDaysToUtc(double serialDay) {
        return (long) ((serialDay - 719529D) * 1000D * 60D * 60D * 24D);
    }

    /**
     * Convert a date object ot serial days
     *
     * @param d The data oject to be converted
     * @return Serial date corresponding to the <i>d</i>
     */
    public static double toSerialDays(Date d) {
        return utcToSerialDays(d.getTime());
    }

    /**
     * Convert from UTC (milliseconds since 01 Jan 1970 00:00:00) to serial days,
     * the format used by matlab (Serial days. 01 jan 0000 00:00:00 = Day 1).
     *
     * @param utc time in UTC
     * @return Serial day format utilized by matlab
     */
    public static double utcToSerialDays(long utc) {
        return TimeUtilities.utcToSerialDays((double) utc);
    }

    /**
     * Convert from UTC (milliseconds since 01 Jan 1970 00:00:00) to serial days,
     * the format used by matlab (Serial days. 01 jan 0000 00:00:00 = Day 1).
     *
     * @param utc time in UTC
     * @return Serial day format utilized by matlab
     */
    public static double utcToSerialDays(double utc) {
        return utc / 1000D / 60D / 60D / 24D + 719529D;
    }
}
