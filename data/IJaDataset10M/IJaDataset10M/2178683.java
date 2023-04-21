package org.baatar.util;

import java.util.Calendar;
import java.util.Date;

public final class DatetimeUtil {

    private DatetimeUtil() {
    }

    public static Date getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date getTime(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        return calendar.getTime();
    }

    public static Date toDayMinimum() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String toDayString() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", calendar);
    }

    public static Date getDate(int yearVal, int monthVal, int dayVal) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearVal, monthVal - 1, dayVal, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDate(int yearVal, int monthVal, int dayVal, int hourVal, int minVal, int secVal) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearVal, monthVal - 1, dayVal, hourVal, minVal, secVal);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDate(Date dateVal, int partValue, int datePart) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateVal);
        calendar.set(datePart, partValue);
        return calendar.getTime();
    }

    public static Date getDateMinimum(Date dateVal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateVal);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static int getDateDiffOfMin(Date precedingDate, Date followingDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(precedingDate);
        long lPrecedingDate = calendar.getTimeInMillis();
        calendar.setTime(followingDate);
        long lFollowingDate = calendar.getTimeInMillis();
        return (int) (lFollowingDate - lPrecedingDate) / 60000;
    }

    public static int getDateDiffOfHour(Date precedingDate, Date followingDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(precedingDate);
        long lPrecedingDate = calendar.getTimeInMillis();
        calendar.setTime(followingDate);
        long lFollowingDate = calendar.getTimeInMillis();
        return (int) Math.floor((lFollowingDate - lPrecedingDate) / 3600000.0);
    }

    public static int getDateDiffOfHourCeil(Date precedingDate, Date followingDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(precedingDate);
        long lPrecedingDate = calendar.getTimeInMillis();
        calendar.setTime(followingDate);
        long lFollowingDate = calendar.getTimeInMillis();
        return (int) Math.ceil((lFollowingDate - lPrecedingDate) / 3600000.0);
    }

    public static int getDateDiffOfDay(Date precedingDate, Date followingDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(precedingDate);
        long lPrecedingDate = calendar.getTimeInMillis();
        calendar.setTime(followingDate);
        long lFollowingDate = calendar.getTimeInMillis();
        return (int) Math.floor((lFollowingDate - lPrecedingDate) / 86400000.0);
    }

    public static int getDateOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long lDate = calendar.getTimeInMillis();
        return (int) Math.floor(lDate / 3600000.0);
    }

    public static Date incDate(Date dateVal, int incBy, int datePart) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateVal);
        calendar.add(datePart, incBy);
        return calendar.getTime();
    }

    public static Date parseDateYear4Month2Day2(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(4, 6));
        int day = Integer.valueOf(str.substring(6, 8));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date parseTimeHour2Min2(String str) {
        int hour = Integer.valueOf(str.substring(0, 2));
        int min = Integer.valueOf(str.substring(2, 4));
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hour, min, 0);
        return calendar.getTime();
    }

    public static Date parseDateYear4Month2Day2Hour2Min2(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(4, 6));
        int day = Integer.valueOf(str.substring(6, 8));
        int hour = Integer.valueOf(str.substring(8, 10));
        int min = Integer.valueOf(str.substring(10, 12));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, min, 0);
        return calendar.getTime();
    }

    public static Date parseDateYear4Month2Day2_Hour2Min2(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(4, 6));
        int day = Integer.valueOf(str.substring(6, 8));
        int hour = Integer.valueOf(str.substring(9, 11));
        int min = Integer.valueOf(str.substring(11, 13));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, min, 0);
        return calendar.getTime();
    }

    public static Date parseDateYear4Month2Day2Hour2Min2Sec2(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(4, 6));
        int day = Integer.valueOf(str.substring(6, 8));
        int hour = Integer.valueOf(str.substring(8, 10));
        int min = Integer.valueOf(str.substring(10, 12));
        int sec = Integer.valueOf(str.substring(12, 14));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, min, sec);
        return calendar.getTime();
    }

    public static Date parseDateYear4_Month2_Day2(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(5, 7));
        int day = Integer.valueOf(str.substring(8, 10));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date parseTimeHour2_Min2(String str) {
        int hour = Integer.valueOf(str.substring(0, 2));
        int min = Integer.valueOf(str.substring(3, 5));
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hour, min, 0);
        return calendar.getTime();
    }

    public static Date parseDateYear4_Month2_Day2_Hour2_Min2(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(5, 7));
        int day = Integer.valueOf(str.substring(8, 10));
        int hour = Integer.valueOf(str.substring(11, 13));
        int min = Integer.valueOf(str.substring(14, 16));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, min, 0);
        return calendar.getTime();
    }

    public static Date parseDateYear4_Month2_Day2_Hour2_Min2_Sec2(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int month = Integer.valueOf(str.substring(5, 7));
        int day = Integer.valueOf(str.substring(8, 10));
        int hour = Integer.valueOf(str.substring(11, 13));
        int min = Integer.valueOf(str.substring(14, 16));
        int sec = Integer.valueOf(str.substring(17, 19));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, min, sec);
        return calendar.getTime();
    }
}
