package com.liferay.portal.kernel.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <a href="DateUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DateUtil {

    public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static int compareTo(Date date1, Date date2) {
        if ((date1 != null) && (date2 == null)) {
            return -1;
        } else if ((date1 == null) && (date2 != null)) {
            return 1;
        } else if ((date1 == null) && (date2 == null)) {
            return 0;
        }
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        if (time1 == time2) {
            return 0;
        } else if (time1 < time2) {
            return -1;
        } else {
            return 1;
        }
    }

    public static String getCurrentDate(String pattern, Locale locale) {
        return getDate(new Date(), pattern, locale);
    }

    public static String getDate(Date date, String pattern, Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        return dateFormat.format(date);
    }

    public static DateFormat getISOFormat() {
        return getISOFormat(StringPool.BLANK);
    }

    public static DateFormat getISOFormat(String text) {
        String pattern = StringPool.BLANK;
        if (text.length() == 8) {
            pattern = "yyyyMMdd";
        } else if (text.length() == 12) {
            pattern = "yyyyMMddHHmm";
        } else if (text.length() == 13) {
            pattern = "yyyyMMdd'T'HHmm";
        } else if (text.length() == 14) {
            pattern = "yyyyMMddHHmmss";
        } else if (text.length() == 15) {
            pattern = "yyyyMMdd'T'HHmmss";
        } else if ((text.length() > 8) && (text.charAt(8) == 'T')) {
            pattern = "yyyyMMdd'T'HHmmssz";
        } else {
            pattern = "yyyyMMddHHmmssz";
        }
        return new SimpleDateFormat(pattern);
    }

    public static DateFormat getISO8601Format() {
        return new SimpleDateFormat(ISO_8601_PATTERN);
    }

    public static DateFormat getUTCFormat() {
        return getUTCFormat(StringPool.BLANK);
    }

    public static DateFormat getUTCFormat(String text) {
        String pattern = StringPool.BLANK;
        if (text.length() == 8) {
            pattern = "yyyyMMdd";
        } else if (text.length() == 12) {
            pattern = "yyyyMMddHHmm";
        } else if (text.length() == 13) {
            pattern = "yyyyMMdd'T'HHmm";
        } else if (text.length() == 14) {
            pattern = "yyyyMMddHHmmss";
        } else if (text.length() == 15) {
            pattern = "yyyyMMdd'T'HHmmss";
        } else {
            pattern = "yyyyMMdd'T'HHmmssz";
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }
}
