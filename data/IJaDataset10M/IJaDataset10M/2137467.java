package com.tcmj.common.tools.helper.date;

import java.util.Date;
import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 * @author tdeut
 */
public class DateHelperTest {

    @Test
    public void testIsDateInRange() {
        System.out.println("isDateInRange");
        assertEquals("case 1", false, DateHelper.isDateInRange(DateHelper.date(2007, 12, 31), DateHelper.date(2008, 1, 1), DateHelper.date(2008, 12, 31), false));
        assertEquals("case 2", true, DateHelper.isDateInRange(DateHelper.date(2008, 1, 1), DateHelper.date(2008, 1, 1), DateHelper.date(2008, 12, 31), false));
        assertEquals("case 3", true, DateHelper.isDateInRange(DateHelper.date(2008, 1, 2), DateHelper.date(2008, 1, 1), DateHelper.date(2008, 12, 31), false));
        assertEquals("case 4", true, DateHelper.isDateInRange(DateHelper.date(2008, 12, 31), DateHelper.date(2008, 1, 1), DateHelper.date(2008, 12, 31), false));
        assertEquals("case 5", false, DateHelper.isDateInRange(DateHelper.date(2009, 1, 1), DateHelper.date(2008, 1, 1), DateHelper.date(2008, 12, 31), false));
        assertEquals("BS-case 1", false, DateHelper.isDateInRange(DateHelper.date(2007, 12, 31, 13, 45, 22), DateHelper.date(2008, 1, 1), DateHelper.date(2008, 12, 31), true));
        assertEquals("BS-case 2", false, DateHelper.isDateInRange(DateHelper.date(2007, 12, 31), DateHelper.date(2008, 1, 1, 19, 45, 22), DateHelper.date(2008, 12, 31, 3, 45, 22), true));
        assertEquals("BS-case 3", false, DateHelper.isDateInRange(DateHelper.date(2007, 12, 31, 23, 59, 59), DateHelper.date(2008, 1, 1, 19, 45, 22), DateHelper.date(2008, 12, 31, 3, 45, 22), false));
        assertEquals("ES-case 1", true, DateHelper.isDateInRange(DateHelper.date(2008, 1, 1, 14, 59, 59), DateHelper.date(2008, 1, 1, 19, 45, 22), DateHelper.date(2008, 12, 31, 3, 45, 22), false));
        assertEquals("ES-case 2", true, DateHelper.isDateInRange(DateHelper.date(2008, 1, 1, 14, 59, 59), DateHelper.date(2008, 1, 1, 14, 59, 59), DateHelper.date(2008, 12, 31, 14, 59, 59), true));
        assertEquals("EE-case 1", true, DateHelper.isDateInRange(DateHelper.date(2008, 12, 31, 11, 59, 59), DateHelper.date(2008, 1, 1, 14, 59, 59), DateHelper.date(2008, 12, 31, 14, 59, 59), false));
        assertEquals("AE-case 1", false, DateHelper.isDateInRange(DateHelper.date(2009, 12, 31, 11, 59, 59), DateHelper.date(2008, 1, 1, 14, 59, 59), DateHelper.date(2008, 12, 31, 14, 59, 59), false));
        assertEquals("OK-case 1", true, DateHelper.isDateInRange(DateHelper.date(2007, 3, 25), DateHelper.date(2007, 3, 25), DateHelper.date(2007, 3, 26), true));
        assertEquals("OK-case 2", true, DateHelper.isDateInRange(DateHelper.date(2007, 3, 26), DateHelper.date(2007, 3, 25), DateHelper.date(2007, 3, 26), true));
    }

    @Test
    public void testCopyTime() {
        System.out.println("copyTime");
        Date targetdate = DateHelper.date(2009, 12, 10, 0, 0, 0);
        Date sourcedate = DateHelper.date(2009, 9, 11, 13, 59, 0);
        Date expResult = DateHelper.date(2009, 12, 10, 13, 59, 0);
        Date result = DateHelper.copyTime(sourcedate, targetdate);
        assertEquals("case 1", String.valueOf(expResult.getTime()), String.valueOf(result.getTime()));
    }

    @Test
    public void testremoveTimeAsDate() {
        System.out.println("removeTimeAsDate");
        Date sourcedate = DateHelper.date(2009, 9, 11, 13, 59, 0);
        Date expResult = DateHelper.date(2009, 9, 11, 0, 0, 0);
        Date result = DateHelper.removeTimeAsDate(sourcedate);
        assertEquals("case 1", String.valueOf(expResult.getTime()), String.valueOf(result.getTime()));
    }

    @Test
    public void testDaysbetween() {
        System.out.println("daysbetween");
        Date start = DateHelper.date(2009, 9, 11, 13, 59, 0);
        Date end = DateHelper.date(2009, 9, 11, 0, 0, 0);
        assertEquals("case 1", 0, DateHelper.daysbetween(start, end));
        start = DateHelper.date(2009, 9, 11, 0, 0, 0);
        end = DateHelper.date(2009, 9, 11, 13, 59, 0);
        assertEquals("case 2", 0, DateHelper.daysbetween(start, end));
        start = DateHelper.date(2008, 1, 1);
        end = DateHelper.date(2008, 1, 2);
        assertEquals("case 3", 1, DateHelper.daysbetween(start, end));
        start = DateHelper.date(2008, 1, 1);
        end = DateHelper.date(2008, 1, 10);
        assertEquals("case 4", 9, DateHelper.daysbetween(start, end));
        start = DateHelper.date(1999, 8, 27);
        end = DateHelper.date(2008, 8, 29);
        assertEquals("case 5", 3290, DateHelper.daysbetween(start, end));
        start = DateHelper.date(1990, 12, 31);
        end = DateHelper.date(2001, 1, 4);
        assertEquals("case 6", 3657, DateHelper.daysbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(1999, 12, 31);
        assertEquals("case 7", 7628, DateHelper.daysbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 3, 27);
        assertEquals("case 8", 10637, DateHelper.daysbetween(start, end));
        System.out.println("-----case 9-----> Start = CET and End = CEST!");
        start = DateHelper.date(2007, 3, 25);
        end = DateHelper.date(2007, 3, 26);
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 9", 1, DateHelper.daysbetween(start, end));
        start = DateHelper.date(2008, 3, 30);
        end = DateHelper.date(2008, 4, 1);
        assertEquals("case 10", 2, DateHelper.daysbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 8, 27);
        assertEquals("case 11", 10790, DateHelper.daysbetween(start, end));
        System.out.println("-----case 12-----");
        start = DateHelper.date(2008, 5, 25);
        end = DateHelper.date(2008, 11, 26);
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 12", 185, DateHelper.daysbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 8, 27);
        assertEquals("case 13", 10790, DateHelper.daysbetween(start, end));
    }

    @Test
    public void testHourssbetween() {
        System.out.println("hoursbetween");
        Date start = DateHelper.date(2009, 9, 11, 13, 59, 0);
        Date end = DateHelper.date(2009, 9, 11, 0, 0, 0);
        assertEquals("case 1", -13, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(2009, 9, 11, 0, 0, 0);
        end = DateHelper.date(2009, 9, 11, 13, 59, 0);
        assertEquals("case 2", 13, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(2008, 1, 1);
        end = DateHelper.date(2008, 1, 2);
        assertEquals("case 3", 24, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(2008, 1, 1);
        end = DateHelper.date(2008, 1, 10);
        assertEquals("case 4", 9 * 24, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(1999, 8, 27);
        end = DateHelper.date(2008, 8, 29);
        assertEquals("case 5", 3290 * 24, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(1990, 12, 31);
        end = DateHelper.date(2001, 1, 4);
        assertEquals("case 6", 3657 * 24, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(1999, 12, 31);
        assertEquals("case 7", 7628 * 24, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 3, 27);
        assertEquals("case 8", 10637 * 24, DateHelper.hoursbetween(start, end));
        System.out.println("-----case 9-----> Start = CET and End = CEST!");
        start = DateHelper.date(2007, 3, 24, 23);
        end = DateHelper.date(2007, 3, 25, 0);
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 9", 1, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(2008, 3, 31, 23);
        end = DateHelper.date(2008, 4, 1, 0);
        assertEquals("case 10", 1, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 8, 27);
        assertEquals("case 11", 10790 * 24, DateHelper.hoursbetween(start, end));
        System.out.println("-----case 12-----");
        start = DateHelper.date(2008, 5, 25);
        end = DateHelper.date(2008, 11, 26);
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 12", 185 * 24, DateHelper.hoursbetween(start, end));
        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 8, 27);
        assertEquals("case 13", 10790 * 24, DateHelper.hoursbetween(start, end));
    }

    @Test
    public void testFormatDate() {
        System.out.println("formatDate");
        Date date1 = DateHelper.date(2009, 12, 10, 0, 0, 0);
        Date date2 = DateHelper.date(2009, 9, 11, 13, 59, 0);
        assertEquals("formatDate 1", "2009-12-10", DateHelper.formatDate(date1));
        assertEquals("formatDate 2", "2009-09-11", DateHelper.formatDate(date2));
    }

    @Test
    public void testFormatDateTime() {
        System.out.println("formatDateTime");
        Date date1 = DateHelper.date(2009, 12, 10, 0, 0, 0);
        Date date2 = DateHelper.date(2009, 9, 11, 13, 59, 0);
        assertEquals("formatDateTime 1", "2009-12-10 00:00", DateHelper.formatDateTime(date1));
        assertEquals("formatDateTime 2", "2009-09-11 13:59", DateHelper.formatDateTime(date2));
    }

    @Test
    public void testRoundDate() {
        System.out.println("roundDate");
        Date given = DateHelper.date(2009, 12, 10, 0, 0, 0);
        Date expected = DateHelper.date(2009, 12, 10, 0, 0, 0);
        Date result = DateHelper.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 1", expected, result);
        given = DateHelper.date(2009, 12, 10, 13, 59, 50);
        expected = DateHelper.date(2009, 12, 10, 14, 0, 0);
        result = DateHelper.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 2", expected, result);
        given = DateHelper.date(2009, 12, 10, 13, 59, 30);
        expected = DateHelper.date(2009, 12, 10, 14, 0, 0);
        result = DateHelper.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 3", expected, result);
        given = DateHelper.date(2009, 12, 10, 13, 59, 29);
        expected = DateHelper.date(2009, 12, 10, 13, 59, 0);
        result = DateHelper.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 4", expected, result);
    }
}
