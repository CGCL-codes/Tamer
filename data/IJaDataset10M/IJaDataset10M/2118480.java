package pt.utl.ist.lucene.utils;

import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Jorge Machado
 * @date 15/Ago/2008
 * @see pt.utl.ist.lucene
 */
public class Dates {

    private static Logger logger = Logger.getLogger(Dates.class);

    public static Calendar getCalendar(Date d) {
        Calendar myCalendar = new MyCalendar();
        myCalendar.setTime(d);
        return myCalendar;
    }

    public static int getActualDay() {
        Date d = new Date();
        SimpleDateFormat dfD = new SimpleDateFormat("dd");
        try {
            return new Integer(dfD.format(d));
        } catch (Exception e) {
            return 1;
        }
    }

    public static int getActualMonth() {
        Date d = new Date();
        SimpleDateFormat dfM = new SimpleDateFormat("MM");
        try {
            return new Integer(dfM.format(d));
        } catch (Exception e) {
            return 1;
        }
    }

    public static int getActualYear() {
        Date d = new Date();
        SimpleDateFormat dfY = new SimpleDateFormat("yyyy");
        try {
            return new Integer(dfY.format(d));
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * @param month to check
     * @return a month between 1 and 12
     */
    public static int getValidMonth(int month) {
        if (month > 12) return 12;
        if (month < 1) return 1;
        return month;
    }

    /**
     * @param day to check
     * @return a day between 1 and 31
     */
    public static int getValidDay(int day) {
        if (day > 31) return 31;
        if (day < 1) return 1;
        return day;
    }

    /**
     * parse fields and build calendar  from a String type yyyy-MM-dd or yyyy-MM or MM-yyyy
     *
     * @param date to parse
     * @param min min or not
     * @return GregorianCalendar
     */
    public static GregorianCalendar getGregorianCalendar(String date, boolean min) {
        Integer[] fields = Dates.parseDateFields(date);
        return Dates.getGregorianCalendar(fields, min);
    }

    /**
     * parse fields and build calendar  from a String type yyyy-MM-dd or yyyy-MM or MM-yyyy
     *
     * @param date to parse
     * @return  GregorianCalendar
     */
    public static GregorianCalendar getGregorianCalendar(String date) {
        Integer[] fields = Dates.parseDateFields(date);
        return Dates.getGregorianCalendar(fields, true);
    }

    public static GregorianCalendar getGregorianCalendarOrNull(String date) {
        if (date == null || date.trim().length() == 0) return null;
        Integer[] fields = Dates.parseDateFields(date);
        if (fields == null || fields.length == 0) return null;
        return Dates.getGregorianCalendar(fields, false);
    }

    /**
     * @param str like "1990-4-1" or "1-4-1990" or "4-1990" or "1990-4"
     * @return Array of Integers [year,month,day] values can be null
     */
    public static Integer[] parseDateFields(String str) {
        if (str != null && str.length() > 0) {
            StringBuilder field0 = new StringBuilder();
            StringBuilder field1 = new StringBuilder();
            StringBuilder field2 = new StringBuilder();
            int field = 0;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == ' ') {
                } else if (c >= '0' && c <= '9') {
                    if (field == 0) field0.append(c); else if (field == 1) field1.append(c); else if (field == 2) field2.append(c);
                } else {
                    field++;
                }
            }
            String field0Str = field0.toString();
            String field1Str = field1.toString();
            String field2Str = field2.toString();
            Integer year = null;
            Integer month = null;
            Integer day = null;
            if (field1Str.length() == 4) {
                if (field0Str.length() > 0) month = new Integer(field0Str);
                year = new Integer(field1Str);
            } else {
                if (field0Str.length() < 4 && field0Str.length() > 0) {
                    day = new Integer(field0Str);
                } else if (field0Str.length() == 4) {
                    year = new Integer(field0Str);
                }
                if (field2Str.length() < 4 && field2Str.length() > 0) {
                    day = new Integer(field2Str);
                } else if (field2Str.length() == 4) {
                    year = new Integer(field2Str);
                }
                if (field1Str.length() > 0) {
                    month = new Integer(field1Str);
                }
            }
            if (day != null && day > 31) Dates.logger.warn("Day found bigger than 31 was:" + day);
            if (day != null && day < 1) Dates.logger.warn("Day found lesser than 1 was:" + day);
            if (month != null && month > 12) Dates.logger.warn("Month found bigger than 31 was:" + month);
            if (month != null && month < 1) Dates.logger.warn("Month found lesser than 1 was:" + month);
            if (year == null || year <= 0) {
                Dates.logger.warn("Year not found");
                return null;
            }
            Integer[] fields = new Integer[3];
            fields[0] = year;
            fields[1] = month;
            fields[2] = day;
            return fields;
        }
        return null;
    }

    /**
     * Create a Gregorian Calendar based on int array with year, month, day
     *
     * @param fields array with year, month and day. Month and day can be null
     * @param min    if true means that we need a lower date in case of month or day ommiting
     * @return gregorian calendar
     */
    public static GregorianCalendar getGregorianCalendar(Integer[] fields, boolean min) {
        GregorianCalendar gc;
        if (fields == null || fields.length == 0 || fields[0] == null) if (min) gc = new GregorianCalendar(1, 0, 1); else gc = new GregorianCalendar(); else if (fields.length == 3 && fields[2] != null) gc = new GregorianCalendar(fields[0], Dates.getValidMonth(fields[1]) - 1, Dates.getValidDay(fields[2])); else if (fields.length >= 2 && fields[1] != null) if (min) gc = new GregorianCalendar(fields[0], Dates.getValidMonth(fields[1]) - 1, 1); else if (fields[1] == 12) gc = new GregorianCalendar(fields[0], 11, 31); else gc = new GregorianCalendar(fields[0], Dates.getValidMonth(fields[1]), 1); else if (min) gc = new GregorianCalendar(fields[0], 0, 1); else gc = new GregorianCalendar(fields[0], 11, 31);
        return gc;
    }

    public static Date getMiddleDate(String startDate, String endDate) {
        GregorianCalendar gcStart = getGregorianCalendarOrNull(startDate);
        GregorianCalendar gcEnd = getGregorianCalendarOrNull(endDate);
        if (gcStart == null && gcEnd == null) return null; else if (gcStart == null) return gcEnd.getTime(); else if (gcEnd == null) return gcStart.getTime();
        long middle = (gcStart.getTimeInMillis() + gcEnd.getTimeInMillis()) / 2;
        return new Date(middle);
    }

    public static long getMiddleDate(long startDate, long endDate) {
        if (startDate < 0 && endDate < 0) return -1; else if (startDate < 0) return endDate; else if (endDate < 0) return startDate;
        return (startDate + endDate) / 2;
    }

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static String getNormalizedDate(Date d) {
        if (d == null) return null;
        return formatter.format(d);
    }

    public static int getDistanceYears(String d1, String d2) {
        GregorianCalendar gcStart = getGregorianCalendarOrNull(d1);
        GregorianCalendar gcEnd = getGregorianCalendarOrNull(d2);
        if (gcStart == null && gcEnd == null) return Integer.MAX_VALUE; else if (gcStart == null) return Integer.MAX_VALUE; else if (gcEnd == null) return Integer.MAX_VALUE;
        int bottomYear = gcStart.get(Calendar.YEAR);
        int topYear = gcEnd.get(Calendar.YEAR);
        if (bottomYear == topYear) return Integer.MAX_VALUE; else return Math.abs(topYear - bottomYear);
    }
}
