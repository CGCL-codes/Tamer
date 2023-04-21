package com.easyExam.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;

/**
 * @author lgli
 *
 * Created on  2009-12-17
 */
public class DateUtil {

    public static String getDateToString(Date date) {
        return getDateToString(date, "MM-dd-yyyy");
    }

    public static String getDateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String str = df.format(date);
        return str;
    }

    public static Date getStrToDate(String date) {
        return getStrToDate(date, "MM-dd-yyyy HH:mm:ss");
    }

    public static Date getStrToDate(String str, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = new Date();
        ;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
      * get cuurent Date return java.util.Date type
      */
    public static Date getNowUtilDate() {
        return new Date();
    }

    /**
     * get current Date return java.sql.Date type
     * 
     * @return
     */
    public static java.sql.Date getNowSqlDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * get the current timestamp return java.sql.timestamp
     * 
     * @return
     */
    public static Timestamp getNowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Timestamp createTimestamp(int year, int month, int date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static Timestamp createTimestamp(int year, int month, int date) {
        return createTimestamp(year, month, date, 0, 0, 0);
    }

    /**
     * 
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static java.util.Date createUtilDate(int year, int month, int date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * 
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static java.util.Date createUtilDate(int year, int month, int date) {
        return createUtilDate(year, month, date, 0, 0, 0);
    }

    /**
     * 
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static java.sql.Date createSqlDate(int year, int month, int date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * 
     * @param java.util.Date:date
     * @return java.sql.Date: YYYY-MM-DD:HH:00:00
     */
    public static java.sql.Timestamp createSqlDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(date), getMonth(date) - 1, getDayOfMonth(date), getHour(date), 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Timestamp(cal.getTimeInMillis());
    }

    /**
     * 
     * @param java.util.Date:date
     * @return java.sql.Date: YYYY-MM-DD:HH:00:00
     */
    public static java.sql.Timestamp createPrevHourSqlDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(date), getMonth(date) - 1, getDayOfMonth(date), getHour(date) - 1, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Timestamp(cal.getTimeInMillis());
    }

    /**
     * 
     * @param java.util.Date:date
     * @param int hour interval
     * @return java.sql.Date: YYYY-MM-DD:HH:00:00
     */
    public static java.sql.Date createPrevHourSqlDate(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(date), getMonth(date), getDayOfMonth(date), getHour(date) - hours, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * 
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static java.sql.Date createSqlDate(int year, int month, int date) {
        return createSqlDate(year, month, date, 0, 0, 0);
    }

    /**
     * format java.util.Date java.sql.Date,java.sql.Timestamp to the string with format,
     * the format default is "yyyy-MM-dd HH:mm:ss"
     * @param time
     * @param format
     * @return
     */
    public static String convertDateToString(java.util.Date date, String format) {
        if (format == null || format.length() == 0) format = "yyyy-MM-dd HH:mm:ss";
        DateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * use "yyyy-MM-dd HH:mm:ss" format the java.util.Date java.sql.Date,java.sql.Timestamp to the string
     * @param time
     * @return
     */
    public static String convertDateToString(java.util.Date date) {
        return convertDateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * create Timestamp with giving String
     * @param dateString
     * @return
     */
    public static Timestamp createTimestamp(String dateString) {
        try {
            if (dateString == null || dateString.length() == 0) return null;
            Date tmpDate = createUtilDate(dateString);
            if (tmpDate != null) return new java.sql.Timestamp(tmpDate.getTime()); else return Timestamp.valueOf(dateString);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * get the Timestamp with String with pattern
     * @param dateString
     * @param pattern
     * @return
     */
    public static Timestamp createTimestamp(String dateString, String pattern) {
        try {
            if (dateString == null || dateString.length() == 0) return null;
            if (pattern == null || pattern.length() == 0) return createTimestamp(dateString);
            DateFormat sf = new SimpleDateFormat(pattern.trim());
            Date tmpDate = sf.parse(dateString.trim());
            if (tmpDate != null) return new Timestamp(tmpDate.getTime());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * create java.sql.Date with giving String
     * @param dateString
     * @return
     */
    public static java.sql.Date createSqlDate(String dateString) {
        try {
            if (dateString == null || dateString.length() == 0) return null;
            Date tmpDate = createUtilDate(dateString);
            if (tmpDate != null) return new java.sql.Date(tmpDate.getTime()); else return java.sql.Date.valueOf(dateString);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * get the sql date with String with pattern
     * @param dateString
     * @param pattern
     * @return
     */
    public static java.sql.Date createSqlDate(String dateString, String pattern) {
        try {
            if (dateString == null || dateString.length() == 0) return null;
            if (pattern == null || pattern.length() == 0) return createSqlDate(dateString);
            DateFormat sf = new SimpleDateFormat(pattern.trim());
            Date tmpDate = sf.parse(dateString.trim());
            if (tmpDate != null) return new java.sql.Date(tmpDate.getTime());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * get the util date with String with pattern
     * @param dateString
     * @param pattern
     * @return
     */
    public static java.util.Date createUtilDate(String dateString, String pattern) {
        try {
            if (dateString == null || dateString.length() == 0) return null;
            if (pattern == null || pattern.length() == 0) return createUtilDate(dateString);
            DateFormat sf = new SimpleDateFormat(pattern.trim());
            return sf.parse(dateString.trim());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * create Timestamp with giving String, the string Foramt is must "yyyy-MM-dd HH:mm:ss" or "yyyy/MM/dd HH:mm:ss" 
     * or "yyyy-MM-dd" or "yyyy/MM/dd"
     * @param dateString
     * @return
     */
    public static java.util.Date createUtilDate(String dateString) {
        try {
            if (dateString == null || dateString.length() == 0) return null;
            String pattern = "";
            dateString = dateString.trim();
            if (dateString.length() == 10) {
                if (dateString.indexOf("/") != -1) pattern = "yyyy/MM/dd"; else if (dateString.indexOf("-") != -1) pattern = "yyyy-MM-dd"; else return null;
            } else if (dateString.length() == 19) {
                if (dateString.indexOf("/") != -1) pattern = "yyyy-MM-dd HH:mm:ss"; else if (dateString.indexOf("-") != -1) pattern = "yyyy-MM-dd HH:mm:ss";
            } else if (dateString.length() == 8) {
                pattern = "yyyyMMdd";
            }
            DateFormat sf = new SimpleDateFormat(pattern);
            return sf.parse(dateString.trim());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * convert the java.util.Date ,java.sql.Date ,java.sql.Timestamp to Calendar
     * 
     * @param time
     * @return
     */
    public static Calendar convertDateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * convert the calendar to Timestamp
     * @param cal
     * @return
     */
    public static Timestamp convertCalendarToTimes(Calendar cal) {
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * convert the calendar to java.util.Date
     * @param cal
     * @return
     */
    public static java.util.Date convertCalendarToUtilDate(Calendar cal) {
        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * convert the calendar to java.sql.Date
     * @param cal
     * @return
     */
    public static java.sql.Date convertCalendarToSqlDate(Calendar cal) {
        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getYear(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.YEAR);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getMonth(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.MONTH) + 1;
    }

    public static String getTimeString(java.util.Date date) {
        String timeString = null;
        if (getDayOfMonth(date) < 10) {
            timeString = "0" + getDayOfMonth(date);
        } else {
            timeString = "" + getDayOfMonth(date);
        }
        if (getMonth(date) < 10) timeString = timeString + "0" + getMonth(date) + getYear(date); else timeString = timeString + getMonth(date) + getYear(date);
        return timeString;
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getDayOfMonth(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getHour(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getMinute(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.MINUTE);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getSecond(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.SECOND);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getMillisecond(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.MILLISECOND);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static long getMilliseconds(java.util.Date date) {
        return date.getTime();
    }

    /**
     * 
     * @param date
     * @return
     */
    public static int getDayOfWeek(java.util.Date date) {
        return convertDateToCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 
     * @param addpart
     * @param times
     * @param addnum
     * @return
     */
    public static Timestamp dateAdd(String addpart, Timestamp times, int addnum) {
        Calendar cal = convertDateToCalendar(times);
        if ("y".equals(addpart)) {
            cal.add(Calendar.YEAR, addnum);
        } else if ("M".equals(addpart)) {
            cal.add(Calendar.MONTH, addnum);
        } else if ("d".equals(addpart)) {
            cal.add(Calendar.DATE, addnum);
        } else if ("H".equals(addpart)) {
            cal.add(Calendar.HOUR, addnum);
        } else if ("m".equals(addpart)) {
            cal.add(Calendar.MINUTE, addnum);
        } else if ("s".equals(addpart)) {
            cal.add(Calendar.SECOND, addnum);
        } else {
            return null;
        }
        return convertCalendarToTimes(cal);
    }

    /**
     * check the year is leap Year or not.
     * @param date
     * @return
     */
    public static boolean isLeapYear(java.util.Date date) {
        return isLeapYear(getYear(date));
    }

    /**
     * check the year is leap Year or not
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        GregorianCalendar cal = new GregorianCalendar();
        return cal.isLeapYear(year);
    }

    /**
     * get the date , set the hour ,minute ,second is 0
     * @param time
     * @return
     */
    public static Timestamp formatAsTimestamp(Date time) {
        return new Timestamp(formatAsUtilDate(time).getTime());
    }

    /**
     * get the date , set the hour ,minute ,second is 0
     * @param dt
     * @return
     */
    public static java.util.Date formatAsUtilDate(java.util.Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        return cd.getTime();
    }

    /**
     * get the date , set the hour ,minute ,second is 0
     * @param date
     * @return
     */
    public static java.sql.Date formatAsSqlDate(java.sql.Date date) {
        return new java.sql.Date(formatAsUtilDate(date).getTime());
    }

    /**
     * 
     * @param java.util.Date:date
     * @return java.sql.Date: YYYY-MM-DD:HH:00:00
     */
    public static Date createPrevHourDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(date), getMonth(date) - 1, getDayOfMonth(date), getHour(date) - 1, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date createPrevDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(date), getMonth(date) - 1, getDayOfMonth(date) - 1, getHour(date), 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date[] getDailyPeriod(Date statTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String strTime = df.format(statTime);
        Date startTime = statTime;
        try {
            startTime = df.parse(strTime);
        } catch (ParseException e) {
        }
        Date endTime = new Date(startTime.getTime() + 24 * 3600 * 1000);
        return new Date[] { startTime, endTime };
    }

    public static Date[] getWeeklyPeriod(Date statTime) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(statTime);
        begin.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        Calendar end = (Calendar) begin.clone();
        end.add(Calendar.DATE, 7);
        end.add(Calendar.MILLISECOND, -1);
        return new Date[] { begin.getTime(), end.getTime() };
    }

    public static Date[] getMonthlyPeriod(Date statTime) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(statTime);
        begin.set(Calendar.DATE, 1);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        Calendar end = (Calendar) begin.clone();
        end.add(Calendar.MONTH, 1);
        end.add(Calendar.MILLISECOND, -1);
        return new Date[] { begin.getTime(), end.getTime() };
    }

    /**
        * get the week days by the day
        * the days is from the sunday to the saturday
        * @param day
        * @return
        */
    @SuppressWarnings("unused")
    private static Date[] getWeekDays(Calendar c) {
        int weekIndex = c.get(Calendar.DAY_OF_WEEK);
        Date[] weekDay = new Date[7];
        int count = 0;
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            weekDay[count++] = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE) + i - weekIndex).getTime();
        }
        return weekDay;
    }

    /**
     * if the hour is "12-01-2008 09" then return the date like {12-01-2008 09:00:00, 12-01-2008 10:00:00}
     *
     * @param hour
     * @return
     * @return Date[]
     * @since TeleNav, Inc 1.0
     */
    public static Date[] getDailyTopTime(String hour) {
        Date begin = getStrToDate(hour + ":00:00", "MM-dd-yyyy HH:mm:ss");
        Date end = new Date(begin.getTime() + 3600 * 1000);
        return new Date[] { begin, end };
    }

    /**
    * get the month days by the day
    * the days is from the 1 to the last day
    * @param Calendar 
    * @return
    */
    @SuppressWarnings("unused")
    private static Date[] getMonthDays(Calendar c) {
        cleanTime(c);
        c.set(Calendar.DATE, 1);
        int firstDate = c.getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date[] monthDay = new Date[lastDate - firstDate + 1];
        for (int i = 0; i < monthDay.length; i++) {
            monthDay[i] = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), firstDate + i).getTime();
        }
        return monthDay;
    }

    /**
     * if the hour is "12-01-2008 09" then return the date like {12-01-2008 09:00:00, 12-01-2008 10:00:00}
     *
     * @param hour
     * @return
     * @return Date[]
     * @since TeleNav, Inc 1.0
     */
    public static Date[] getDailyTime(String hour) {
        Date begin = getStrToDate(hour + ":00:00", "MM-dd-yyyy HH:mm:ss");
        Date end = new Date(begin.getTime() + 3600 * 1000);
        return new Date[] { begin, end };
    }

    /**
     * This api will set the hour, minute, second, millisecond to 0
     *
     * @param c
     * @return void
     * @since TeleNav, Inc 1.0
     */
    private static void cleanTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    /**
     * This api will get  the first day of the Last month
     *
     * @param startTime
     * @return
     * @return Date
     * @since TeleNav, Inc 1.0
     */
    @SuppressWarnings("unused")
    private static Date getLastMonthStatTime(Date startTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONTH, -1);
        Date lastMonthStatTime = c.getTime();
        return lastMonthStatTime;
    }

    /**
     * This api will get the timeString like "00:00","00:15"-"23:45"
     *
     * @param quarter
     * @return
     * @return String
     * @since TeleNav, Inc 1.0
     */
    @SuppressWarnings("unused")
    private static String getTime(int quarter) {
        if (quarter < 0 || quarter > 95) {
            return null;
        }
        if (quarter == 0) {
            return "00:00";
        }
        int hour = quarter / 4;
        int minute = (quarter % 4) * 15;
        return fill(hour, 2, "0") + ":" + fill(minute, 2, "0");
    }

    /**
     * This api will 
     *
     * @param value
     * @param length
     * @param fillChar
     * @return
     * @return String
     * @since TeleNav, Inc 1.0
     */
    private static String fill(int value, int length, String fillChar) {
        String fillValue = String.valueOf(value);
        int fillLength = length - fillValue.length();
        for (int i = 0; i < fillLength; i++) {
            fillValue = fillChar + fillValue;
        }
        return fillValue;
    }
}
