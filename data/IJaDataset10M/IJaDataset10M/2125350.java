package com.entsofteng.jdateutil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility class to create and simplify manipulation of
 * java.util.Date class. Uses GregorianCalendar by default.
 * @author James Kranig 
 * @version 0.1
 */
public class JDateUtil {

    private static Calendar calendar = new GregorianCalendar();

    /**
     * Creates date, setting time to 00:00:00. 
     * @param month 0-11
     * @param day 1-Number of days in month 
     * @param year e.g. 2005 
     * @return Date
     */
    public static Date createDate(int month, int day, int year) {
        return JDateUtil.createDateTime(month, day, year, 0, 0, 0, 0);
    }

    /**
     * Creates date/time.
     * @param month 0-11
     * @param day  1-Number of days in month.
     * @param year e.g. 2005
     * @param hoursOfDay 0-23
     * @param minutes 0-59
     * @param seconds 0-59
     * @return Date
     */
    public static Date createDateTime(int month, int day, int year, int hoursOfDay, int minutes, int seconds) {
        return JDateUtil.createDateTime(month, day, year, hoursOfDay, minutes, seconds, 0);
    }

    /**
     * Creates Date with both date and time set. 
     * @param month 0-11
     * @param day  1-Number of days in month.
     * @param year e.g. 2005
     * @param hoursOfDay 0-23
     * @param minutes 0-59
     * @param seconds 0-59
     * @param milliseconds 0-999
     * @return Date
     */
    public static Date createDateTime(int month, int day, int year, int hoursOfDay, int minutes, int seconds, int milliseconds) {
        Calendar dateTime = (Calendar) calendar.clone();
        dateTime.set(year, month, day, hoursOfDay, minutes, seconds);
        dateTime.set(Calendar.MILLISECOND, milliseconds);
        return dateTime.getTime();
    }

    /**
     * Returns month part of date.
     * @param date
     * @return int month part of year 0 - 11.
     */
    public static int getMonth(Date date) {
        return JDateUtil.getDatePart(date, Calendar.MONTH);
    }

    /**
     * Returns day of month part of date.
     * @param date
     * @return int indicating day of month.
     */
    public static int getDayOfMonth(Date date) {
        return JDateUtil.getDatePart(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns year part of date.
     * @param date
     * @return int year. 
     */
    public static int getYear(Date date) {
        return JDateUtil.getDatePart(date, Calendar.YEAR);
    }

    /**
     * Sets day of month part of date.
     * @param date
     * @param dayOfMonth
     */
    public static Date setDayOfMonth(Date date, int dayOfMonth) {
        if (date == null) {
            throw new IllegalArgumentException("Parameter \"date\" is null.");
        }
        Calendar dateTime = (Calendar) calendar.clone();
        dateTime.setTime(date);
        dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return dateTime.getTime();
    }

    /**
     * Sets hour, minute, seconds in an existing date,
     * returning a new Date with the new time values.
     * @param date
     * @param hoursOfDay
     * @param minutes
     * @param seconds
     * @return Date
     */
    public static Date setTime(Date date, int hoursOfDay, int minutes, int seconds) {
        return setTime(date, hoursOfDay, minutes, seconds, 0);
    }

    /**
     * Sets hour, minute, seconds in an existing date,
     * returning a new Date with the new time values.
     * @param date
     * @param hoursOfDay
     * @param minutes
     * @param seconds
     * @param milliseconds 0-999
     * @return Date
     */
    public static Date setTime(Date date, int hoursOfDay, int minutes, int seconds, int milliseconds) {
        if (date == null) {
            throw new IllegalArgumentException("Parameter \"date\" is null.");
        }
        Calendar dateTime = (Calendar) calendar.clone();
        dateTime.setTime(date);
        dateTime.set(Calendar.HOUR_OF_DAY, hoursOfDay);
        dateTime.set(Calendar.MINUTE, minutes);
        dateTime.set(Calendar.SECOND, seconds);
        dateTime.set(Calendar.MILLISECOND, milliseconds);
        return dateTime.getTime();
    }

    /**
     * Add yearsToAdd years to date.
     * @param date
     * @param yearsToAdd
     * @return Date
     */
    public static Date addYears(Date date, int yearsToAdd) {
        return JDateUtil.addToDatePart(date, Calendar.YEAR, yearsToAdd);
    }

    /**
     * Subtract yearsToSubtract years from date.
     * @param date
     * @param yearsToSubtract
     * @return Date
     */
    public static Date subtractYears(Date date, int yearsToSubtract) {
        return JDateUtil.addToDatePart(date, Calendar.YEAR, -yearsToSubtract);
    }

    /**
     * Add monthsToAdd months to date. 
     * @param date
     * @param monthsToAdd
     * @return Date
     */
    public static Date addMonths(Date date, int monthsToAdd) {
        return JDateUtil.addToDatePart(date, Calendar.MONTH, monthsToAdd);
    }

    /**
     * Subtract monthsToSubtract months from date.
     * @param date
     * @param monthsToSubtract
     * @return Date
     */
    public static Date subtractMonths(Date date, int monthsToSubtract) {
        return JDateUtil.addToDatePart(date, Calendar.MONTH, -monthsToSubtract);
    }

    /**
     * Adds daysToAdd days to date, returning a new Date. 
     * @param date
     * @param daysToAdd
     * @return Date
     */
    public static Date addDays(Date date, int daysToAdd) {
        return JDateUtil.addToDatePart(date, Calendar.DAY_OF_YEAR, daysToAdd);
    }

    /**
     * Subtracts days from date, returning a new Date.
     * Does not modify date.
     * @param date
     * @param daysToSubtract
     * @return Date
     */
    public static Date subtractDays(Date date, int daysToSubtract) {
        return JDateUtil.addToDatePart(date, Calendar.DAY_OF_YEAR, -daysToSubtract);
    }

    /**
     * Adds minutesToAdd minutes to date, returning a new Date.
     * @param date
     * @param minutesToAdd
     * @return Date
     */
    public static Date addMinutes(Date date, int minutesToAdd) {
        return JDateUtil.addToDatePart(date, Calendar.MINUTE, minutesToAdd);
    }

    /**
     * Subtracts minutesToSubtract minutes from date, returning a new Date.
     * @param date
     * @param minutesToSubtract
     * @return Date
     */
    public static Date subtractMinutes(Date date, int minutesToSubtract) {
        return JDateUtil.addToDatePart(date, Calendar.MINUTE, -minutesToSubtract);
    }

    /**
     * Adds secondsToAdd seconds to date, returning a new Date.
     * @param date
     * @param secondsToAdd
     * @return Date
     */
    public static Date addSeconds(Date date, int secondsToAdd) {
        return JDateUtil.addToDatePart(date, Calendar.SECOND, secondsToAdd);
    }

    /**
     * Subtracts secondsToSubtract seconds from date, returning a new Date.
     * @param date
     * @param secondsToSubtract
     * @return Date
     */
    public static Date subtractSeconds(Date date, int secondsToSubtract) {
        return JDateUtil.addToDatePart(date, Calendar.SECOND, -secondsToSubtract);
    }

    /**
     * Adds millisecondsToAdd milliseconds to date, returning a new Date.
     * @param date
     * @param millisecondsToAdd
     * @return Date
     */
    public static Date addMilliseconds(Date date, int millisecondsToAdd) {
        return JDateUtil.addToDatePart(date, Calendar.MILLISECOND, millisecondsToAdd);
    }

    /**
     * Subtracts millisecondsToSubtract milliseconds from date, returning a new Date.
     * @param date
     * @param millisecondsToSubtract
     * @return Date
     */
    public static Date subtractMilliseconds(Date date, int millisecondsToSubtract) {
        return JDateUtil.addToDatePart(date, Calendar.MILLISECOND, -millisecondsToSubtract);
    }

    /**
     * Returns the number of days in the month.
     * @param date
     * @return int 
     */
    public static int getDaysInMonth(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Parameter \"date\" is null.");
        }
        Calendar dateTime = (Calendar) calendar.clone();
        dateTime.setTime(date);
        return dateTime.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the actual number of days between two dates.
     * @param startDate
     * @param endDate
     * @return int
     */
    public static int calculateActualDays(Date startDate, Date endDate) {
        return getActualDaysElapsed(endDate) - getActualDaysElapsed(startDate);
    }

    /**
     * Sets calendar used internally.
     * @param dateCalendar
     */
    public static void setCalendar(Calendar dateCalendar) {
        if (dateCalendar == null) {
            throw new IllegalArgumentException("Parameter \"dateCalendar\" is null.");
        }
        calendar = dateCalendar;
    }

    private static int getDatePart(Date date, int datePart) {
        if (date == null) {
            throw new IllegalArgumentException("Parameter \"date\" is null.");
        }
        Calendar cal = (Calendar) calendar.clone();
        cal.setTime(date);
        return cal.get(datePart);
    }

    private static Date addToDatePart(Date date, int datePart, int amountToAdd) {
        if (date == null) {
            throw new IllegalArgumentException("Parameter \"date\" is null.");
        }
        Calendar cal = (Calendar) calendar.clone();
        cal.setTime(date);
        cal.add(datePart, amountToAdd);
        return cal.getTime();
    }

    private static int getActualDaysElapsed(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Parameter \"date\" is null.");
        }
        Calendar dateTime = (Calendar) calendar.clone();
        dateTime.setTime(date);
        int year = dateTime.get(Calendar.YEAR);
        int daysElapsed = 365 * year;
        int yearsFour = (year - 1) / 4;
        int centuries = (year - 1) / 100;
        int centuriesFour = (year - 1) / 400;
        daysElapsed += yearsFour - centuries + centuriesFour;
        daysElapsed += dateTime.get(Calendar.DAY_OF_YEAR);
        return daysElapsed;
    }

    private JDateUtil() {
    }
}
