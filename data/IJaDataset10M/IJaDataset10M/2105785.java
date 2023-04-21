package com.baldwin.www.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * ʱ�����ڴ�����
 */
public class DateUtils {

    public static final String DATEFORMATLONG = "yyyy-MM-dd HH:mm:ss";

    public static final String DATEFORMATMEDIUM = "yyyy-MM-dd HH:mm";

    public static final String DATEFORMATSHORT = "yyyy-MM-dd";

    private static Date date;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    public DateUtils() {
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static synchronized String getCurrentFormatDate(String formatDate) {
        date = getCurrentDate();
        simpleDateFormat.applyPattern(formatDate);
        return simpleDateFormat.format(date);
    }

    public static String getCurrentFormatDateLong() {
        return getCurrentFormatDate(DATEFORMATLONG);
    }

    public static String getCurrentFormatDateMedium() {
        return getCurrentFormatDate(DATEFORMATMEDIUM);
    }

    public static String getCurrentFormatDateShort() {
        return getCurrentFormatDate(DATEFORMATSHORT);
    }

    private static synchronized String getDate2String(String format, Date date) {
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(date);
    }

    public static String getDateToString(String format, Date date) {
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(date);
    }

    public static String getDate2LongString(Date date) {
        return getDate2String(DATEFORMATLONG, date);
    }

    public static String getDate2MediumString(Date date) {
        return getDate2String(DATEFORMATMEDIUM, date);
    }

    public static String getDate2ShortString(Date date) {
        return getDate2String(DATEFORMATSHORT, date);
    }

    public static String getLong2LongString(long l) {
        date = getLong2Date(l);
        return getDate2LongString(date);
    }

    public static String getLong2MediumString(long l) {
        date = getLong2Date(l);
        return getDate2MediumString(date);
    }

    public static String getLong2ShortString(long l) {
        date = getLong2Date(l);
        return getDate2ShortString(date);
    }

    private static synchronized Date getString2Date(String format, String str) {
        simpleDateFormat.applyPattern(format);
        ParsePosition parseposition = new ParsePosition(0);
        return simpleDateFormat.parse(str, parseposition);
    }

    public static Date getString2LongDate(String str) {
        return getString2Date(DATEFORMATLONG, str);
    }

    public static Date getString2MediumDate(String str) {
        return getString2Date(DATEFORMATMEDIUM, str);
    }

    public static Date getString2ShortDate(String str) {
        return getString2Date(DATEFORMATSHORT, str);
    }

    /**
     * ���ڽ���һ���յ����ڶ���,�����Ĭ��ֵ��1900-01-01
     * @return
     */
    public static Date getEmptyDate() {
        return getString2ShortDate("1900-01-01");
    }

    /**
     * ���ڽ���һ���յ����ڶ���,�����Ĭ��ֵ��1900-01-01
     * @return
     */
    public static String getEmptyDateString() {
        return "1900-01-01";
    }

    public static Date getLong2Date(long l) {
        return new Date(l);
    }

    public static int getOffMinutes(long l) {
        return getOffMinutes(l, getCurrentTimeMillis());
    }

    public static int getOffMinutes(long from, long to) {
        return (int) ((to - from) / 60000L);
    }

    public static int getYear() {
        return Calendar.getInstance().get(1);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(2) + 1;
    }

    public static int getDay() {
        return Calendar.getInstance().get(5);
    }

    public static int getDayOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        return Calendar.getInstance().get(11);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(12);
    }

    public static int getSecond() {
        return Calendar.getInstance().get(13);
    }

    public static String getWeekOfDateChinese(Date dt) {
        String[] weekDays = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return w;
    }

    public static String add(int day) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.DATE, day);
        date = gregorianCalendar.getTime();
        return getDate2String(DATEFORMATSHORT, date);
    }

    public static String add(int day, Date dt) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dt);
        gregorianCalendar.add(GregorianCalendar.DATE, day);
        date = gregorianCalendar.getTime();
        return getDate2String(DATEFORMATSHORT, date);
    }

    public static Date addDay(int day, Date dt) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dt);
        gregorianCalendar.add(GregorianCalendar.DATE, day);
        return gregorianCalendar.getTime();
    }

    public static String subtract(int day) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.DATE, -day);
        date = gregorianCalendar.getTime();
        return getDate2String(DATEFORMATSHORT, date);
    }

    public static String subtractDay2String(int day, Date dt) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dt);
        gregorianCalendar.add(GregorianCalendar.DATE, -day);
        date = gregorianCalendar.getTime();
        return getDate2String(DATEFORMATSHORT, date);
    }

    public static Date subtractDay(int day, Date dt) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dt);
        gregorianCalendar.add(GregorianCalendar.DATE, -day);
        return gregorianCalendar.getTime();
    }

    public static Date subtractDay(int day) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.DATE, -day);
        return gregorianCalendar.getTime();
    }

    public static boolean isShortDate(String str) {
        boolean b = false;
        Date date = null;
        simpleDateFormat.applyPattern(DATEFORMATSHORT);
        ParsePosition parseposition = new ParsePosition(0);
        try {
            date = simpleDateFormat.parse(str, parseposition);
        } catch (Exception e) {
            b = false;
        }
        if (date == null) {
            b = false;
        } else {
            b = true;
        }
        return b;
    }

    boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) return true;
        }
        return false;
    }

    /**
 	  * ����2������֮����������
 	  * @param d1
 	  * @param d2
 	  * @return
 	  */
    public static int getDaysBetween(Date date1, Date date2) {
        int days = 0;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal1.after(cal2)) {
            java.util.Calendar swap = cal1;
            cal1 = cal2;
            cal2 = swap;
        }
        days = cal2.get(java.util.Calendar.DAY_OF_YEAR) - cal1.get(java.util.Calendar.DAY_OF_YEAR);
        int y2 = cal2.get(java.util.Calendar.YEAR);
        if (cal1.get(java.util.Calendar.YEAR) != y2) {
            cal1 = (java.util.Calendar) cal1.clone();
            do {
                days += cal1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
                cal1.add(java.util.Calendar.YEAR, 1);
            } while (cal1.get(java.util.Calendar.YEAR) != y2);
        }
        return days;
    }

    public static double getDaysBetweenForDouble(Date date1, Date date2) {
        double days = date1.getTime() - date2.getTime();
        days = days / 1000;
        days = days / 86400;
        return days;
    }

    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) week = "0" + week;
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    public static String getMonday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getMonthStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getMonthEnd(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getFriday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getYearStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getYearEnd(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.MONTH, 12);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getSunday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getSaturday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String afterNDay(int n) {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        Date d2 = c.getTime();
        String s = df.format(d2);
        return s;
    }

    public static List getDateList(Date start, Date end) {
        List dateList = new ArrayList();
        Calendar s = Calendar.getInstance();
        s.setTime(start);
        Calendar e = Calendar.getInstance();
        e.setTime(end);
        while (!s.after(e)) {
            dateList.add(s.getTime());
            s.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }

    public static void main(String[] args) {
        String start = "2007-05-20";
        String end = "2007-06-10";
        List dateList = getDateList(getString2Date("yyyy-MM-dd", start), getString2Date("yyyy-MM-dd", end));
        System.out.println(dateList);
        System.out.println("dateList's size is " + dateList.size());
    }
}
