package org.apache.shindig.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date parsing and writing utilities.
 */
public class DateUtil {

    private static final DateTimeFormatter rfc1123DateFormat = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.US).withZone(DateTimeZone.UTC);

    private static final DateTimeFormatter iso8601DateFormat = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

    private DateUtil() {
    }

    /**
   * Parses an RFC1123 format date.  Returns null if the date fails to parse for
   * any reason.
   *
   * @param dateStr
   * @return the date
   */
    public static Date parseRfc1123Date(String dateStr) {
        try {
            return rfc1123DateFormat.parseDateTime(dateStr).toDate();
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * Parses an ISO8601 formatted datetime into a Date or null
   * is parsing fails.
   *  
   * @param dateStr A datetime string in ISO8601 format
   * @return the date
   */
    public static Date parseIso8601DateTime(String dateStr) {
        try {
            return new DateTime(dateStr).toDate();
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * Formats an ISO 8601 format date.
   */
    public static String formatIso8601Date(Date date) {
        return formatIso8601Date(date.getTime());
    }

    /**
   * Formats an ISO 8601 format date.
   */
    public static String formatIso8601Date(long time) {
        return iso8601DateFormat.print(time);
    }

    /**
   * Formats an RFC 1123 format date.
   */
    public static String formatRfc1123Date(Date date) {
        return formatRfc1123Date(date.getTime());
    }

    /**
   * Formats an RFC 1123 format date.
   */
    public static String formatRfc1123Date(long timeStamp) {
        return rfc1123DateFormat.print(timeStamp);
    }
}
