package org.judo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date getDateFromStr(String s) throws Exception {
        Date date = null;
        date = getLongFormatDate(s);
        if (date == null) date = getShortFormatDate(s);
        if (date == null) date = getRegularLongFormatDate(s);
        if (date == null) date = getRegularFormatDate(s);
        if (date == null) date = getLongestFormatDate(s);
        return date;
    }

    public static boolean isValidDate(String s) {
        Date date = null;
        date = getShortFormatDate(s);
        if (date == null) date = getLongFormatDate(s);
        if (date == null) date = getRegularLongFormatDate(s);
        if (date == null) date = getRegularFormatDate(s);
        if (date == null) return false;
        return true;
    }

    public static Date getShortFormatDate(String s) {
        Date date = null;
        try {
            SimpleDateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
            date = shortDate.parse(s);
        } catch (Exception e) {
        }
        return date;
    }

    public static Date getRegularFormatDate(String s) {
        Date date = null;
        try {
            SimpleDateFormat shortDate = new SimpleDateFormat("MM/dd/yyyy");
            date = shortDate.parse(s);
        } catch (Exception e) {
        }
        return date;
    }

    public static Date getLongestFormatDate(String s) {
        Date date = null;
        try {
            SimpleDateFormat longestDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            date = longestDate.parse(s);
        } catch (Exception e) {
        }
        return date;
    }

    public static Date getRegularLongFormatDate(String s) {
        Date date = null;
        try {
            SimpleDateFormat shortDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            date = shortDate.parse(s);
        } catch (Exception e) {
        }
        return date;
    }

    public static Date getLongFormatDate(String s) {
        Date date = null;
        try {
            SimpleDateFormat longDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = longDate.parse(s);
        } catch (Exception e) {
        }
        return date;
    }

    public static String getStringValue(Date date, String pattern) {
        String result = null;
        try {
            SimpleDateFormat longDate = new SimpleDateFormat(pattern);
            result = longDate.format(date);
        } catch (Exception e) {
        }
        return result;
    }
}
