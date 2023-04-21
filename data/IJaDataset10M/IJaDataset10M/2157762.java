package org.kowboy.temporal;

import java.util.Calendar;
import junit.framework.TestCase;

public class UtilsTest extends TestCase {

    public void testForLeapYear() {
        assertFalse(Utils.isLeapYear(1800));
        assertFalse(Utils.isLeapYear(1900));
        assertFalse(Utils.isLeapYear(2100));
        assertFalse(Utils.isLeapYear(2200));
        assertFalse(Utils.isLeapYear(2300));
        assertFalse(Utils.isLeapYear(2500));
        assertTrue(Utils.isLeapYear(2000));
        assertTrue(Utils.isLeapYear(2004));
    }

    public void testForDaysInMonth() {
        int y = 1800;
        int m = 1;
        int d = 1;
        m = 2;
        assertEquals(28, Utils.getNoOfDaysInMonth(Utils.newDate(y, m, d)));
        m = 3;
        assertEquals(31, Utils.getNoOfDaysInMonth(Utils.newDate(y, m, d)));
        m = 1;
        assertEquals(31, Utils.getNoOfDaysInMonth(Utils.newDate(y, m, d)));
        m = 4;
        assertEquals(30, Utils.getNoOfDaysInMonth(Utils.newDate(y, m, d)));
        y = 2000;
        m = 2;
        assertEquals(29, Utils.getNoOfDaysInMonth(Utils.newDate(y, m, d)));
        m = 8;
        assertEquals(31, Utils.getNoOfDaysInMonth(Utils.newDate(y, m, d)));
        y = 2005;
        m = 2;
        assertEquals(28, Utils.getNoOfDaysInMonth(Utils.newDate(y, m, d)));
    }

    public void testForIsOneDayBefore() {
        java.util.Date fromDate;
        java.util.Date toDate;
        toDate = Utils.newDate(2005, 2, 2);
        fromDate = Utils.newDate(2005, 2, 3);
        assertTrue(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2005, 2, 2);
        fromDate = Utils.newDate(2005, 2, 4);
        assertFalse(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2005, 2, 4);
        fromDate = Utils.newDate(2005, 2, 2);
        assertFalse(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2005, 2, 4);
        fromDate = Utils.newDate(2005, 2, 2);
        assertFalse(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2005, 2, 28);
        fromDate = Utils.newDate(2005, 3, 1);
        assertTrue(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2005, 2, 28);
        fromDate = Utils.newDate(2005, 3, 2);
        assertFalse(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2000, 2, 2);
        fromDate = Utils.newDate(2000, 2, 3);
        assertTrue(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2000, 2, 2);
        fromDate = Utils.newDate(2000, 2, 4);
        assertFalse(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2000, 2, 29);
        fromDate = Utils.newDate(2000, 3, 1);
        assertTrue(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2000, 2, 28);
        fromDate = Utils.newDate(2000, 3, 1);
        assertFalse(Utils.isOneDayBefore(toDate, fromDate));
        toDate = Utils.newDate(2001, 2, 28);
        fromDate = Utils.newDate(2002, 3, 10);
        assertFalse(Utils.isOneDayBefore(toDate, fromDate));
    }

    /**
     * Tests the addDays() method.
     */
    public void testAddDays() {
        java.util.Date source = null;
        java.util.Date result = null;
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        source = Utils.newDate(2005, 6, 30);
        result = Utils.addDays(source, 5);
        assertEquals(5 * oneDayInMillis, result.getTime() - source.getTime());
        assertEquals(result, Utils.newDate(2005, 7, 5));
        source = result;
        result = Utils.addDays(source, -6);
        assertEquals(6 * oneDayInMillis, source.getTime() - result.getTime());
        assertEquals(result, Utils.newDate(2005, 6, 29));
    }

    /**
     * Test the todaysDate() method.
     */
    public void testTodaysDate() {
        java.util.Date today = Utils.todaysDate();
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(today);
        assertEquals(0, c.get(Calendar.HOUR));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
        Calendar now = Calendar.getInstance();
        assertEquals(now.get(Calendar.YEAR), c.get(Calendar.YEAR));
        assertEquals(now.get(Calendar.MONTH), c.get(Calendar.MONTH));
        assertEquals(now.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_MONTH));
    }
}
