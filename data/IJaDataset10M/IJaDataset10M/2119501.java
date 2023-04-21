package com.javaeedev.util;

import static org.junit.Assert.*;
import java.util.Calendar;
import org.junit.Test;

public class DateUtilTest {

    @Test
    public void testPreviousDay() {
        assertArrayEquals(new int[] { 2008, 2, 29 }, DateUtil.getPreviousDay(2008, 3, 1));
        assertArrayEquals(new int[] { 2008, 2, 1 }, DateUtil.getPreviousDay(2008, 2, 2));
        assertArrayEquals(new int[] { 2007, 12, 31 }, DateUtil.getPreviousDay(2008, 1, 1));
        assertArrayEquals(new int[] { 2008, 1, 30 }, DateUtil.getPreviousDay(2008, 1, 31));
    }

    @Test
    public void testNextDay() {
        assertArrayEquals(new int[] { 2008, 3, 2 }, DateUtil.getNextDay(2008, 3, 1));
        assertArrayEquals(new int[] { 2008, 2, 29 }, DateUtil.getNextDay(2008, 2, 28));
        assertArrayEquals(new int[] { 2007, 3, 1 }, DateUtil.getNextDay(2007, 2, 28));
        assertArrayEquals(new int[] { 2008, 1, 1 }, DateUtil.getNextDay(2007, 12, 31));
    }

    @Test
    public void testPrevious() {
        final long ONE_DAY = 3600L * 1000L * 24L;
        long now = System.currentTimeMillis();
        long pre = DateUtil.previous(1);
        long last = now - pre;
        assertTrue((last > (ONE_DAY - 1000)) && (last < (ONE_DAY + 1000)));
    }

    @Test
    public void testParseDateTime() {
        assertNull(DateUtil.parseDateTime("2000-2-2 aa:00:00"));
        assertNull(DateUtil.parseDateTime("2000-2-2 22:bb:00"));
        assertNull(DateUtil.parseDateTime("2000-2-2-10:00:"));
        assertNull(DateUtil.parseDateTime("-2000-2-2"));
        assertNull(DateUtil.parseDateTime("abc"));
        assertNull(DateUtil.parseDateTime("12:00:00"));
        assertNull(DateUtil.parseDateTime("?#%(*(*(&#$"));
    }

    @Test
    public void testParseDate() {
        assertNull(DateUtil.parseDate("12:00:00"));
        assertNull(DateUtil.parseDate("2009-pp-mm"));
        assertNull(DateUtil.parseDate("xyz"));
        assertNull(DateUtil.parseDate("*&#%@@&#"));
    }

    @Test
    public void testIsLeapYear() {
        assertTrue(DateUtil.isLeapYear(1940));
        assertTrue(DateUtil.isLeapYear(1984));
        assertTrue(DateUtil.isLeapYear(2000));
        assertTrue(DateUtil.isLeapYear(2008));
        assertTrue(DateUtil.isLeapYear(2012));
        assertFalse(DateUtil.isLeapYear(1700));
        assertFalse(DateUtil.isLeapYear(1800));
        assertFalse(DateUtil.isLeapYear(1900));
        assertFalse(DateUtil.isLeapYear(1998));
        assertFalse(DateUtil.isLeapYear(2007));
    }

    @Test
    public void testGetCalendar() {
        Calendar c = DateUtil.getCalendar(2007, 10, 1);
        assertEquals(2007, c.get(Calendar.YEAR));
        assertEquals(Calendar.OCTOBER, c.get(Calendar.MONTH));
        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
        assertEquals(0, c.get(Calendar.MILLISECOND));
    }

    @Test
    public void testGetCalendarWithMultiThread() {
        Calendar c1 = DateUtil.getCalendar(2007, 10, 1);
        Calendar c2 = DateUtil.getCalendar(2007, 10, 1);
        assertNotSame(c1, c2);
    }
}
