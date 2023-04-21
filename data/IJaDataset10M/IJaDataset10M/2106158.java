package org.imogene.web.gwt.client.util;

import java.util.Date;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtil {

    private static DateTimeFormat dateFormater = DateTimeFormat.getFormat(BaseNLS.constants().format_date());

    private static DateTimeFormat dateTimeFormater = DateTimeFormat.getFormat(BaseNLS.constants().format_date() + " " + BaseNLS.constants().format_time() + " (v)");

    private static DateTimeFormat timeFormater = DateTimeFormat.getFormat(BaseNLS.constants().format_time() + " v");

    /**
	 * Get Localized date
	 */
    public static String getFormatedDate(Date date) {
        return dateFormater.format(date);
    }

    /**
	 * Get Localized datetime
	 */
    public static String getFormatedDateTime(Date datetime) {
        return dateTimeFormater.format(datetime);
    }

    /**
	 * Get Localized time
	 */
    public static String getFormatedTime(Date time) {
        return timeFormater.format(time);
    }

    public static String getDate(Date date) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_date()).format(date);
    }

    public static String getDateTime(Date datetime) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_date() + " " + BaseNLS.constants().format_time()).format(datetime);
    }

    public static String getTime(Date time) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_time()).format(time);
    }

    /**
	 * Parse Localized datetime
	 */
    public static Date parseFormatedDateTime(String datetime) {
        return dateTimeFormater.parse(datetime);
    }

    /**
	 * Parse validation date
	 */
    public static Date parseValidationDate(String date) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_validation_date()).parse(date);
    }

    /**
	 * Parse User validation datetime
	 */
    public static Date parseValidationDateTime(String datetime) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_validation_date() + " " + BaseNLS.constants().format_validation_time()).parse(datetime);
    }

    /**
	 * Parse User validation time
	 */
    public static Date parseValidationTime(String time) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_validation_time()).parse(time);
    }

    /**
	 * Parse User entered date
	 */
    public static Date parseDate(String date) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_date()).parse(date);
    }

    /**
	 * Parse User entered datetime
	 */
    public static Date parseDateTime(String datetime) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_date() + " " + BaseNLS.constants().format_time()).parse(datetime);
    }

    /**
	 * Parse User entered time
	 */
    public static Date parseTime(String time) {
        return DateTimeFormat.getFormat(BaseNLS.constants().format_time()).parse(time);
    }

    /**
	 * Validation test for date
	 * @param pattern the validation pattern
	 * @param value the value to test
	 * @return true if the test matches
	 */
    public static boolean matchesDate(String pattern, Date value) {
        try {
            if (pattern.startsWith("<=")) {
                Date limit = parseValidationDate(pattern.substring(2));
                return value.before(limit) || value.compareTo(limit) == 0;
            }
            if (pattern.startsWith("<")) {
                Date limit = parseValidationDate(pattern.substring(1));
                return value.before(limit) && value.compareTo(limit) != 0;
            }
            if (pattern.startsWith(">=")) {
                Date limit = parseValidationDate(pattern.substring(2));
                return value.after(limit) || value.compareTo(limit) == 0;
            }
            if (pattern.startsWith(">")) {
                Date limit = parseValidationDate(pattern.substring(1));
                return value.after(limit) && value.compareTo(limit) != 0;
            }
            if (pattern.startsWith("!=")) {
                Date limit = parseValidationDate(pattern.substring(2));
                return value.compareTo(limit) != 0;
            }
            if (pattern.startsWith("==")) {
                Date limit = parseValidationDate(pattern.substring(2));
                return value.compareTo(limit) == 0;
            }
            if (pattern.contains(";")) {
                String[] integers = pattern.split(";");
                Date limitInf = parseValidationDate(integers[0]);
                Date limitSup = parseValidationDate(integers[1]);
                return value.after(limitInf) && value.before(limitSup);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
	 * Validation test for date time
	 * @param pattern the validation pattern
	 * @param value the value to test
	 * @return true if the test matches
	 */
    public static boolean matchesDateTime(String pattern, Date value) {
        try {
            if (pattern.startsWith("<=")) {
                Date limit = parseValidationDateTime(pattern.substring(2));
                return value.before(limit) || value.compareTo(limit) == 0;
            }
            if (pattern.startsWith("<")) {
                Date limit = parseValidationDateTime(pattern.substring(1));
                return value.before(limit) && value.compareTo(limit) != 0;
            }
            if (pattern.startsWith(">=")) {
                Date limit = parseValidationDateTime(pattern.substring(2));
                return value.after(limit) || value.compareTo(limit) == 0;
            }
            if (pattern.startsWith(">")) {
                Date limit = parseValidationDateTime(pattern.substring(1));
                return value.after(limit) && value.compareTo(limit) != 0;
            }
            if (pattern.startsWith("!=")) {
                Date limit = parseValidationDateTime(pattern.substring(2));
                return value.compareTo(limit) != 0;
            }
            if (pattern.startsWith("==")) {
                Date limit = parseValidationDateTime(pattern.substring(2));
                return value.compareTo(limit) == 0;
            }
            if (pattern.contains(";")) {
                String[] integers = pattern.split(";");
                Date limitInf = parseValidationDateTime(integers[0]);
                Date limitSup = parseValidationDateTime(integers[1]);
                return value.after(limitInf) && value.before(limitSup);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
	 * Validation test for time
	 * @param pattern the validation pattern
	 * @param value the value to test
	 * @return true if the test matches
	 */
    public static boolean matchesTime(String pattern, Date value) {
        try {
            if (pattern.startsWith("<=")) {
                Date limit = parseValidationTime(pattern.substring(2));
                return value.before(limit) || value.compareTo(limit) == 0;
            }
            if (pattern.startsWith("<")) {
                Date limit = parseValidationTime(pattern.substring(1));
                return value.before(limit) && value.compareTo(limit) != 0;
            }
            if (pattern.startsWith(">=")) {
                Date limit = parseValidationTime(pattern.substring(2));
                return value.after(limit) || value.compareTo(limit) == 0;
            }
            if (pattern.startsWith(">")) {
                Date limit = parseValidationTime(pattern.substring(1));
                return value.after(limit) && value.compareTo(limit) != 0;
            }
            if (pattern.startsWith("!=")) {
                Date limit = parseValidationTime(pattern.substring(2));
                return value.compareTo(limit) != 0;
            }
            if (pattern.startsWith("==")) {
                Date limit = parseValidationTime(pattern.substring(2));
                return value.compareTo(limit) == 0;
            }
            if (pattern.contains(";")) {
                String[] integers = pattern.split(";");
                Date limitInf = parseValidationTime(integers[0]);
                Date limitSup = parseValidationTime(integers[1]);
                return value.after(limitInf) && value.before(limitSup);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
	 * 
	 * @return
	 */
    public static DateTimeFormat getDateFormater() {
        return dateFormater;
    }
}
