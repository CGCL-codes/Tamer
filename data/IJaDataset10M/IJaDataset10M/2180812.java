package com.moonpolysoft.evoTrader.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date stripTime(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date skipWeekendsForwards(Date day) {
        day = stripTime(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DAY_OF_WEEK, 2);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return cal.getTime();
    }

    public static Date skipWeekendsBackwards(Date day) {
        day = stripTime(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DAY_OF_WEEK, -1);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_WEEK, -2);
        }
        return cal.getTime();
    }

    public static Date addDay(Date day, int n) {
        boolean positive = n >= 0;
        if (!positive) n = -n;
        day = stripTime(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        for (int i = 0; i < n; i++) {
            cal.add(Calendar.DAY_OF_MONTH, positive ? 1 : -1);
            cal.setTime(positive ? skipWeekendsForwards(cal.getTime()) : skipWeekendsBackwards(cal.getTime()));
        }
        return cal.getTime();
    }

    public static int tradingDaysBetween(Date start, Date end) {
        Date cur = start;
        int count = 0;
        while (cur.compareTo(end) < 0) {
            cur = addDay(cur, 1);
            count++;
        }
        return count;
    }

    public static Date fixedDate(int month, int day, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.YEAR, year);
        return stripTime(cal.getTime());
    }
}
