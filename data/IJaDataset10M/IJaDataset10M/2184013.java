package com.rhythm.commons.asserts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;

/**
 *
 * @author Michael J. Lee
 */
public class DateTimeAsserts {

    public static final String SIMPLE_DATE_PATTERN = "MM/dd/yyyy";

    /**
     * Asserts a simple date in the format of 'MM/dd/yyyy' to a given calenar
     * @param date date in the  the format of 'MM/dd/yyyy'
     */
    public static void assertSimpleDate(String dateText, Calendar calendar) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_PATTERN);
            Date date = dateFormat.parse(dateText);
            Calendar calendarFromDate = new GregorianCalendar();
            calendarFromDate.setTimeInMillis(date.getTime());
            assertEquals("Year is not equal", calendarFromDate.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
            assertEquals("Month is not equal", calendarFromDate.get(Calendar.MONTH), calendar.get(Calendar.MONTH));
            assertEquals("Day of the month is not equal", calendarFromDate.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        } catch (Exception ex) {
            fail("Incorrect date format or pattern. Exception was " + ex.getMessage());
        }
    }

    public static void assertIsDayOfTheMonth(int dayOfMonth, Calendar calendar) {
        assertEquals("Was not the correct day of the month", dayOfMonth, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static void assertIsSunday(Calendar calendar) {
        assertEquals("Was not SUNDAY", Calendar.SUNDAY, calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static void assertIsMonday(Calendar calendar) {
        assertEquals("Was not MONDAY", Calendar.MONDAY, calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static void assertIsTuesday(Calendar calendar) {
        assertEquals("Was not TUESDAY", Calendar.TUESDAY, calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static void assertIsWednesday(Calendar calendar) {
        assertEquals("Was not WEDNESDAY", Calendar.WEDNESDAY, calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static void assertIsThursday(Calendar calendar) {
        assertEquals("Was not THURSDAY", Calendar.THURSDAY, calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static void assertIsFriday(Calendar calendar) {
        assertEquals("Was not FRIDAY", Calendar.FRIDAY, calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static void assertIsSaturday(Calendar calendar) {
        assertEquals("Was not SATURDAY", Calendar.SATURDAY, calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>JANUARY</code>
     * @param calendar
     */
    public static void assertIsJanuray(Calendar calendar) {
        assertMonth(Calendar.JANUARY, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>FEBRUARY</code>
     * @param calendar
     */
    public static void assertIsFeburay(Calendar calendar) {
        assertMonth(Calendar.FEBRUARY, calendar);
    }

    /**
     * Asserts the given calendar is in the month of <code>MARCH</code>
     * @param calendar
     */
    public static void assertIsMarch(Calendar calendar) {
        assertMonth(Calendar.MARCH, calendar);
    }

    /**
     * Asserts the given calendar is in the month of <code>APRIL</code>
     * @param calendar
     */
    public static void assertIsApril(Calendar calendar) {
        assertMonth(Calendar.APRIL, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>MAY</code>
     * @param calendar
     */
    public static void assertIsMay(Calendar calendar) {
        assertMonth(Calendar.MAY, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>JUNE</code>
     * @param calendar
     */
    public static void assertIsJune(Calendar calendar) {
        assertMonth(Calendar.JUNE, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>JULY</code>
     * @param calendar
     */
    public static void assertIsJuly(Calendar calendar) {
        assertMonth(Calendar.JULY, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>AUGUST</code>
     * @param calendar
     */
    public static void assertIsAugust(Calendar calendar) {
        assertMonth(Calendar.AUGUST, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>SEPTEMBER</code>
     * @param calendar
     */
    public static void assertIsSeptember(Calendar calendar) {
        assertMonth(Calendar.SEPTEMBER, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>OCTOBER</code>
     * @param calendar
     */
    public static void assertIsOctober(Calendar calendar) {
        assertMonth(Calendar.OCTOBER, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>NOVEMBER</code>
     * @param calendar
     */
    public static void assertIsNovember(Calendar calendar) {
        assertMonth(Calendar.NOVEMBER, calendar);
    }

    /**
     * Asserts the given <code>Calendar</code> is in the month of <code>DECEMBER</code>
     * @param calendar
     */
    public static void assertIsDecember(Calendar calendar) {
        assertMonth(Calendar.DECEMBER, calendar);
    }

    /**
     * Asserts that the give month is equal to the given <code>Calendar</code>
     * @param faultMessage message to display if assert fails
     * @param month the expected month
     * @param calendar the calendar to assert
     */
    public static void assertMonth(String faultMessage, int month, Calendar calendar) {
        assertEquals(faultMessage, month, calendar.get(Calendar.MONTH));
    }

    /**
     * Asserts that the give month is equal to the given <code>Calendar</code>
     * @param month the expected month
     * @param calendar the calendar to assert
     */
    public static void assertMonth(int month, Calendar calendar) {
        assertMonth("Month is not equal", month, calendar);
    }

    public static void assertYear(String faultMessage, int year, Calendar calendar) {
        assertEquals(faultMessage, year, calendar.get(Calendar.YEAR));
    }

    public static void assertYear(int year, Calendar calendar) {
        assertYear("Years do not equal", year, calendar);
    }

    public static void assertDayOfMonth(String faultMessage, int dayOfMonth, Calendar calendar) {
        assertEquals(faultMessage, dayOfMonth, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static void assertDayOfMonth(int dayOfMonth, Calendar calendar) {
        assertDayOfMonth("Day of month not equal", dayOfMonth, calendar);
    }
}
