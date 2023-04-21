package certforge.util;

import java.util.Calendar;
import java.util.TimeZone;

public class Time {

    public static final int SECOND = 1;

    public static final int MINUTE = SECOND * 60;

    public static final int HOUR = MINUTE * 60;

    public static final int DAY = HOUR * 24;

    public static final int WEEK = DAY * 7;

    private static int timeZoneOffset = TimeZone.getDefault().getRawOffset() / 1000;

    private static int parseTime(String time) {
        if (time == null) {
            return -timeZoneOffset;
        }
        String[] hms = time.split(":");
        if (hms.length != 3) {
            return -timeZoneOffset;
        }
        return Integers.parse(hms[0]) * 3600 + Integers.parse(hms[1]) * 60 + Integers.parse(hms[2]) - timeZoneOffset;
    }

    private static int parseDate(String date) {
        if (date == null) {
            return -timeZoneOffset;
        }
        String[] ymd = date.split("-");
        if (ymd.length != 3) {
            return -timeZoneOffset;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Integers.parse(ymd[0]), Integers.parse(ymd[1]) - 1, Integers.parse(ymd[2]), 0, 0, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static int parse(String date, String time) {
        return parseDate(date) + parseTime(time) + timeZoneOffset;
    }

    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String toDateString(int time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) time * 1000);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
    }

    public static String toTimeString(int time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) time * 1000);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return String.format("%02d:%02d:%02d", Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second));
    }

    public static String toString(int time) {
        return toDateString(time) + " " + toTimeString(time);
    }

    public static int nextMidnightPlus(int now, int plus) {
        int next = (now + timeZoneOffset) / DAY * DAY - timeZoneOffset;
        next += plus;
        if (next < now) {
            next += DAY;
        }
        return next;
    }

    public static int lastMidnight(int now) {
        return nextMidnightPlus(now, 0) - DAY;
    }

    public static int nextThursdayMidnightPlus(int now, int plus) {
        int next = (now + timeZoneOffset) / WEEK * WEEK - timeZoneOffset;
        next += plus;
        if (next < now) {
            next += WEEK;
        }
        return next;
    }
}
