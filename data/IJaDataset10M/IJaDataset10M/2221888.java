package org.yajul.util;

/**
 * Defines useful date / time formatting constants:
 * <ul><li> <a href="http://www.w3.org/TR/NOTE-datetime">
 * ISO 8601</a> format strings.</li>
 * <li>Commonly used date format strings.<li>
 * </ul>
 * User: josh
 * Date: Nov 24, 2003
 * Time: 9:58:56 PM
 */
public interface DateFormatConstants {

    /**
     * ISO 8601 date format yyyy-MM-dd.
     * A formatting string for java.text.SimpleDateFormat that
     * will allow parsing and formatting of ISO 8601 date strings
     * with year, month, and day specified.
     */
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Complete date plus hours, minutes, seconds and a decimal fraction of a
     * second.
     */
    public static final String ISO8601_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S";

    /**
     * A formatting string for java.text.SimpleDateFormat that
     * will allow parsing and formatting of ISO 8601 date strings
     * with all UTC fields specified.
     */
    public static final String ISO8601_UTC_FORMAT = "yyyy-MM-dd'T'hh:mm:ss,SSS'Z'";
}
