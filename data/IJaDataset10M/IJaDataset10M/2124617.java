package org.fto.jthink.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DateTimeHelperTestCase extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DateTimeHelperTestCase.class);
    }

    /**
	 *方法测试
	 */
    public void test() {
        System.out.println("\n[正在测试方法: DateTimeHelperTestCase.()...]");
        assertNotNull(DateTimeHelper.parseToDate("2005-3-1", DateTimeHelper.FMT_yyyyMMdd));
        assertNotNull(DateTimeHelper.parseToDate("2005-3-1 4", DateTimeHelper.FMT_yyyyMMddHH));
        try {
            assertNotNull(DateTimeHelper.parseToDate("2005-3-1 4", (String) null));
            fail();
        } catch (Exception e) {
        }
        try {
            assertNotNull(DateTimeHelper.parseToDate("2005-3-a 4", DateTimeHelper.FMT_yyyyMMdd));
            fail();
        } catch (Exception e) {
        }
        try {
            assertNotNull(DateTimeHelper.parseToDate("", " "));
            fail();
        } catch (Exception e) {
        }
        System.out.println("Test parseToDate() : " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.parseToDate(99999, 9, 1)));
        Date d1 = DateTimeHelper.parseToDate("2005-10-03 1:2:3.4");
        Date d2 = DateTimeHelper.parseToDate("2005-10-01 12:00:00");
        System.out.println("Test getSecondsOfTwoDate() : " + DateTimeHelper.getSecondsOfTwoDate(d1, d2));
        System.out.println("Test getMinutesOfTwoDate() : " + DateTimeHelper.getMinutesOfTwoDate(d1, d2));
        System.out.println("Test getHoursOfTwoDate() : " + DateTimeHelper.getHoursOfTwoDate(d1, d2));
        System.out.println("Test getDaysOfTwoDate() : " + DateTimeHelper.getDaysOfTwoDate(d1, d2));
        System.out.println("Test addTime() MILLISECOND : " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.addTime(d1, 30.1, Calendar.MILLISECOND), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println("Test addTime() SECOND : " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.addTime(d1, 30.900, Calendar.SECOND), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println("Test addTime() MINUTE : " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.addTime(d1, 30.5, Calendar.MINUTE), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println("Test addTime() HOUR : " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.addTime(d1, 12.855555, Calendar.HOUR), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println("Test addTime() DATE : " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.addTime(d1, -1.5, Calendar.DATE), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println("Test setYearOfDate(): " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.setYearOfDate(d1, 9999), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println("Test setMillisecondOfDate(): " + DateTimeHelper.formatDateTimetoString(DateTimeHelper.setMillisecondOfDate(d1, -1000), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println("Test formateDateToCNString() : " + DateTimeHelper.formateDateToCNString(d1));
        System.out.println("Test formateDateTimeToCNString() : " + DateTimeHelper.formateDateTimeToCNString(d1));
        System.out.println("Test getDaysOfMonth(): " + DateTimeHelper.getDaysOfMonth(d1));
        System.out.println("Test getDaysOfMonth(): " + DateTimeHelper.getDaysOfMonth(2005, 2));
    }

    /**
	 * 测试主程序
	 * @param args
	 */
    public void testmain() {
        Date now = DateTimeHelper.parseToDate("2000-1-1 0:0:0.0");
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println(DateTimeHelper.formatDateTimetoString(DateTimeHelper.setMillisecondOfDate(now, 1002), DateTimeHelper.FMT_yyyyMMddHHmmssS));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMdd, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHH, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmm, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmmss, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmmssS, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmAzzzz_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmA_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmAz_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmss, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmssS, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmm, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmz, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmzzzz, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmssA_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmssAz_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_HHmmssAzzzz_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmmssSa_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmmssa_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHmma_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMddHHa_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_yyyyMMdda_12, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_WWMMDDYY_EN, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_MMDDYY_EN, Locale.US));
        System.out.println(DateTimeHelper.formatDateTimetoString(now, DateTimeHelper.FMT_MMDDYY_CN));
        System.out.println(DateTimeHelper.getDayOfMonth(now));
        System.out.println(DateTimeHelper.getDayOfWeek(now));
        System.out.println(DateTimeHelper.getDayOfYear(now));
        System.out.println(DateTimeHelper.getMonthOfYear(now));
        System.out.println(DateTimeHelper.getWeekOfMonth(now));
        System.out.println(DateTimeHelper.getWeekOfYear(now));
        System.out.println(DateTimeHelper.getYearOfDate(now));
        System.out.println(DateTimeHelper.getHoursOfDay(now));
        System.out.println(DateTimeHelper.getMinutesOfHour(now));
        System.out.println(DateTimeHelper.getSecondsOfMinute(now));
        System.out.println(DateTimeHelper.getMillisecondsOfSecond(now));
        System.out.println(DateTimeHelper.addYears(now, 10));
        System.out.println(DateTimeHelper.addMonths(now, 10));
        System.out.println(DateTimeHelper.addDays(now, 30));
        System.out.println(DateTimeHelper.addHours(now, 24));
        System.out.println(DateTimeHelper.addMinutes(now, 60));
        System.out.println(DateTimeHelper.addSeconds(now, 60));
        System.out.println(DateTimeHelper.addMilliseconds(now, 1000));
        System.out.println(DateTimeHelper.addYears(now, -10));
        System.out.println(DateTimeHelper.addMonths(now, -10));
        System.out.println(DateTimeHelper.addDays(now, -30));
        System.out.println(DateTimeHelper.addHours(now, -24));
        System.out.println(DateTimeHelper.addMinutes(now, -60));
        System.out.println(DateTimeHelper.addSeconds(now, -60));
        System.out.println(DateTimeHelper.addMilliseconds(now, -1000));
        System.out.println(DateTimeHelper.formatDateTimetoString(now));
        System.out.println(DateTimeHelper.getYearOfDate(now));
        System.out.println(DateTimeHelper.getMonthOfYear(now));
        System.out.println(DateTimeHelper.getDayOfMonth(now));
        System.out.println(DateTimeHelper.getDayOfYear(now));
        System.out.println(DateTimeHelper.getDayOfWeek(now));
        System.out.println(DateTimeHelper.getWeekOfMonth(now));
        System.out.println(DateTimeHelper.getWeekOfYear(now));
        System.out.println(DateTimeHelper.getHoursOfDay(now));
        System.out.println(DateTimeHelper.getMinutesOfHour(now));
        System.out.println(DateTimeHelper.getSecondsOfMinute(now));
        System.out.println(DateTimeHelper.getMillisecondsOfSecond(now));
        System.out.println(DateTimeHelper.getTime(now));
        System.out.println(DateTimeHelper.compareTwoDate(now, now));
        Date now1 = DateTimeHelper.parseToDate("2003-9-8 06:08:09");
        Date now2 = DateTimeHelper.parseToDate(2004, 11, 1, 0, 40, 45);
        System.out.println(now2);
        System.out.println(DateTimeHelper.compareTwoDate(now1, now));
        System.out.println(DateTimeHelper.compareTwoDate(now2, now));
        System.out.println(DateTimeHelper.formateDateToCNString("2000-01-01"));
        System.out.println("formateDateTimeToCNString : " + DateTimeHelper.formateDateTimeToCNString(now));
    }
}
