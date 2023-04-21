package com.kyte.api.util;

import java.util.TimeZone;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

public class DateUtil {

    private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DateUtil.class);

    public static final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    public static final long MILLIS_PER_YEAR = 365 * DateUtil.MILLIS_PER_DAY;

    public static long getLocalEpoch(TimeZone timezone) {
        DateFormat dateFormat = DateFormat.getInstance();
        if (timezone != null) dateFormat.setTimeZone(timezone);
        long result = 0;
        try {
            result = DateFormat.getInstance().parse("01/02/70 12:00 AM").getTime();
        } catch (ParseException e) {
            LOGGER.error(e.toString(), e);
        }
        if (dateFormat.getTimeZone().inDaylightTime(new Date())) {
            result -= 60 * 60 * 1000;
        }
        return result;
    }

    public static int dateToDays(Date date, TimeZone timezone) {
        return DateUtil.timeToDays(date.getTime(), timezone);
    }

    public static int timeToDays(long timeMillis, TimeZone timezone) {
        return (int) ((timeMillis - DateUtil.getLocalEpoch(timezone)) / DateUtil.MILLIS_PER_DAY);
    }

    public static Date daysToDate(int days, TimeZone timezone) {
        return new Date(DateUtil.getLocalEpoch(timezone) + days * DateUtil.MILLIS_PER_DAY);
    }

    public static String getTimestamp() {
        SimpleDateFormat tsFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return tsFormat.format(new Date());
    }

    public static int getAge(Date birthDate) {
        if (birthDate == null) return 0;
        long ageMillis = System.currentTimeMillis() - birthDate.getTime();
        return (int) (ageMillis / DateUtil.MILLIS_PER_YEAR);
    }

    public static Date parse(String day, String month, String year) {
        int intMonth = Integer.valueOf(month);
        int intDay = Integer.valueOf(day);
        int intYear = Integer.valueOf(year);
        if (intYear < 100) intYear = 1900 + intYear;
        String dateStr = String.format("%1$02d/%2$02d/%3$04d", intMonth, intDay, intYear);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.parse(dateStr, new ParsePosition(0));
    }

    public static Date parseDateTime(String s) {
        if (StringUtil.isBlank(s)) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date result = dateFormat.parse(s, new ParsePosition(0));
        return result;
    }

    public static String getDateAsRFC822String(Date date) {
        if (date == null) return null;
        SimpleDateFormat RFC822DATEFORMAT = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
        return RFC822DATEFORMAT.format(date);
    }

    public static String ISO_8601BASIC_DATE_PATTERN = "yyyyMMdd'T'HHmmss'Z'";

    public static boolean isIsoTimestamp(String s) {
        if (StringUtil.isBlank(s)) {
            return false;
        }
        return s.matches("\\d{8}T\\d{6}Z");
    }

    public static Date parseIsoDateTime(String s) {
        if (StringUtil.isBlank(s)) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.ISO_8601BASIC_DATE_PATTERN);
        dateFormat.setLenient(false);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date result = dateFormat.parse(s, new ParsePosition(0));
        return result;
    }

    public static String getIsoTimestamp() {
        SimpleDateFormat tsFormat = new SimpleDateFormat(DateUtil.ISO_8601BASIC_DATE_PATTERN);
        tsFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return tsFormat.format(new Date());
    }

    public static String getIsoTimestampString(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.ISO_8601BASIC_DATE_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
}
