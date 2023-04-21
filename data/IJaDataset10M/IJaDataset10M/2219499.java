package org.open.force.common;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author nlugert@openforcesoftware.org
 *
 */
public class OFCalendarUtil {

    public static final String JANUARY = "January";

    public static final String FEBRUARY = "February";

    public static final String MARCH = "March";

    public static final String APRIL = "April";

    public static final String MAY = "May";

    public static final String JUNE = "June";

    public static final String JULY = "July";

    public static final String AUGUST = "August";

    public static final String SEPTEMBER = "September";

    public static final String OCTOBER = "October";

    public static final String NOVEMBER = "November";

    public static final String DECEMBER = "December";

    public static final int ONE_DAY = 86400;

    public static final int ONE_HOUR = 3600;

    /**
	 * returns a Calendar Time in Millis from a Long
	 * 
	 * @param Long
	 *            longDate
	 * @return Calendar
	 */
    public static Calendar getCalendarTime(Long longDate) {
        Calendar newDate = Calendar.getInstance();
        newDate.setTimeInMillis(longDate);
        return newDate;
    }

    /**
	 * returns a Long from a Calendar
	 * 
	 * @param Calendar
	 *            cal
	 * @return Long
	 */
    public static Long getLongFromCalendar(Calendar cal) {
        long newDate = cal.getTimeInMillis();
        return newDate;
    }

    /**
	 * returns a Calendar Time in Millis from a String
	 * 
	 * @param String
	 *            calString
	 * @return Calendar
	 */
    public static Calendar getCalendarFromString(String calString) {
        Calendar cal = Calendar.getInstance();
        Long calLong;
        try {
            calLong = new Long(calString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return cal;
        }
        cal.setTimeInMillis(calLong);
        return cal;
    }

    public static String getStringFromCalendar(Calendar cal) {
        return getLongFromCalendar(cal).toString();
    }

    /**
	 * returns a Calendar from a Date
	 * 
	 * @param date
	 * @return
	 */
    public static Calendar getDateFromCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date == null) {
            return cal;
        }
        cal.setTime(date);
        return cal;
    }

    /**
	 * 
	 * @param Date a
	 * @param Date b
	 * @return number of days between the two Dates
	 */
    public static int calculateDifference(Date a, Date b) {
        int tempDifference = 0;
        int difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        if (a.compareTo(b) < 0) {
            earlier.setTime(a);
            later.setTime(b);
        } else {
            earlier.setTime(b);
            later.setTime(a);
        }
        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
            difference += tempDifference;
            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }
        if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;
        }
        return difference;
    }

    public static long calculateSeconds(Date a, Date b) {
        long difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        if (a.compareTo(b) < 0) {
            earlier.setTime(a);
            later.setTime(b);
        } else {
            earlier.setTime(b);
            later.setTime(a);
        }
        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            difference += ONE_DAY;
            earlier.add(Calendar.DAY_OF_YEAR, 1);
        }
        while (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            difference += ONE_DAY;
            earlier.add(Calendar.DAY_OF_YEAR, 1);
        }
        if (earlier.get(Calendar.HOUR_OF_DAY) != later.get(Calendar.HOUR_OF_DAY)) {
            difference += ONE_HOUR * (later.get(Calendar.HOUR_OF_DAY) - earlier.get(Calendar.HOUR_OF_DAY));
        }
        if (earlier.get(Calendar.MINUTE) != later.get(Calendar.MINUTE)) {
            difference += 60 * (later.get(Calendar.MINUTE) - earlier.get(Calendar.MINUTE));
        }
        if (earlier.get(Calendar.SECOND) != later.get(Calendar.SECOND)) {
            difference += later.get(Calendar.SECOND) - earlier.get(Calendar.SECOND);
        }
        return difference;
    }

    /**
	 * 
	 * @param year
	 * @return boolean true if year is a leap year, false otherwise
	 */
    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 4 == 0 && year % 400 == 0)) {
            return true;
        }
        return false;
    }

    public static int getNumberOfDaysInAMonth(Calendar cal) {
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getMonthAsString(int month) {
        switch(month) {
            case Calendar.JANUARY:
                return OFCalendarUtil.JANUARY;
            case Calendar.FEBRUARY:
                return OFCalendarUtil.FEBRUARY;
            case Calendar.MARCH:
                return OFCalendarUtil.MARCH;
            case Calendar.APRIL:
                return OFCalendarUtil.APRIL;
            case Calendar.MAY:
                return OFCalendarUtil.MAY;
            case Calendar.JUNE:
                return OFCalendarUtil.JUNE;
            case Calendar.JULY:
                return OFCalendarUtil.JULY;
            case Calendar.AUGUST:
                return OFCalendarUtil.AUGUST;
            case Calendar.SEPTEMBER:
                return OFCalendarUtil.SEPTEMBER;
            case Calendar.OCTOBER:
                return OFCalendarUtil.OCTOBER;
            case Calendar.NOVEMBER:
                return OFCalendarUtil.NOVEMBER;
            case Calendar.DECEMBER:
                return OFCalendarUtil.DECEMBER;
            default:
                return OFCalendarUtil.JANUARY;
        }
    }

    public static int getMonthAsInteger(String month) {
        if (month.equals(OFCalendarUtil.JANUARY)) return Calendar.JANUARY; else if (month.equals(OFCalendarUtil.FEBRUARY)) return Calendar.FEBRUARY; else if (month.equals(OFCalendarUtil.MARCH)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.APRIL)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.MAY)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.JUNE)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.JULY)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.AUGUST)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.SEPTEMBER)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.OCTOBER)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.NOVEMBER)) return Calendar.MARCH; else if (month.equals(OFCalendarUtil.DECEMBER)) return Calendar.MARCH; else return Calendar.JANUARY;
    }

    public static Calendar getCalendarFromSFString(String in) {
        TimeZone zone = TimeZone.getDefault();
        int dst = zone.getDSTSavings();
        int offset = zone.getRawOffset();
        try {
            java.text.SimpleDateFormat ppp = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
            Date date = ppp.parse(in);
            if (zone.inDaylightTime(date)) offset = offset + dst;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MILLISECOND, offset);
            return cal;
        } catch (Exception e) {
            try {
                java.text.SimpleDateFormat ppp = new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date date = ppp.parse(in);
                if (zone.inDaylightTime(date)) offset = offset + dst;
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MILLISECOND, offset);
                return cal;
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static Calendar getGMTCalendarFromSFString(String in) {
        try {
            java.text.SimpleDateFormat ppp = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
            Date date = ppp.parse(in);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            try {
                java.text.SimpleDateFormat ppp = new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date date = ppp.parse(in);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal;
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static Date getDateFromSFString(String in) {
        TimeZone zone = TimeZone.getDefault();
        int dst = zone.getDSTSavings();
        int offset = zone.getRawOffset();
        try {
            java.text.SimpleDateFormat ppp = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
            Date date = ppp.parse(in);
            if (zone.inDaylightTime(date)) offset = offset + dst;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MILLISECOND, offset);
            return cal.getTime();
        } catch (ParseException e) {
            try {
                java.text.SimpleDateFormat ppp = new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date date = ppp.parse(in);
                if (zone.inDaylightTime(date)) offset = offset + dst;
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MILLISECOND, offset);
                return cal.getTime();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static String getDateAsSFString(Date date) {
        TimeZone zone = TimeZone.getDefault();
        int dst = zone.getDSTSavings();
        int offset = zone.getRawOffset();
        if (zone.inDaylightTime(date)) offset = offset + dst;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, -offset);
        java.text.SimpleDateFormat fff = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
        return fff.format(cal.getTime());
    }

    public static String getGMTDateAsSFString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        java.text.SimpleDateFormat fff = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
        return fff.format(cal.getTime());
    }
}
