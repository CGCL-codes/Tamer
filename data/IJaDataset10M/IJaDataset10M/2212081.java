package slojj.dotsbox.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Taken from Rssowl
 * 
 * Utility class providing convenience methods to (XML) parsing mechanisms.
 * 
 * @author Niko Schmuck (niko@nava.de) and Benjamin Pasero (bpasero@rssowl.org)
 * @version 1.2
 */
public final class DateUtils {

    /** Formatter for Date based on OS Locale */
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

    /** Formatter for Date (long) based on OS Locale */
    private static final DateFormat DATE_LONG_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);

    /** Formatter for Date and Time based on OS Locale */
    private static final DateFormat DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    /** Formatter for Date and Time (long) based on OS Locale */
    private static final DateFormat DATE_TIME_LONG_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT);

    /** An array of common date formats */
    private static SimpleDateFormat[] dateFormats = null;

    /** This utility class constructor is hidden */
    private DateUtils() {
    }

    /**
	 * Remove special chars of a date to use it into a filename
	 * 
	 * @param date
	 *            The date as String
	 * @return String The given date with replaced special chars
	 */
    public static String dateToFileName(String date) {
        String separator = "_";
        date = date.replaceAll(" ", separator);
        date = date.replaceAll("\\.", separator);
        date = date.replaceAll(":", separator);
        date = date.replaceAll("-", separator);
        date = date.replaceAll("/", separator);
        return date;
    }

    /**
	 * Format the current date to a String using the selected Locale and do not
	 * add the time to the String
	 * 
	 * @return String Formatted Date
	 */
    public static String formatDate() {
        return formatDate(new Date(), false, false);
    }

    /**
	 * Format the current date to a String using the selected Locale.
	 * 
	 * @param withTime
	 *            If TRUE set time to date String
	 * @return String Formatted Date
	 */
    public static String formatDate(boolean withTime) {
        return formatDate(new Date(), false, withTime);
    }

    /**
	 * Format the date to a String using the selected Locale.
	 * 
	 * @param date
	 *            The date to format
	 * @param withTime
	 *            If TRUE set time to date String
	 * @return String Formatted Date
	 */
    public static String formatDate(Date date, boolean withTime) {
        return formatDate(date, false, withTime);
    }

    /**
	 * Format the date to a String using the selected Locale.
	 * 
	 * @param date
	 *            The date to format
	 * @param longDateFormat
	 *            If TRUE use the Long Date Format
	 * @param withTime
	 *            If TRUE set time to date String
	 * @return String Formatted Date
	 */
    public static String formatDate(Date date, boolean longDateFormat, boolean withTime) {
        if (longDateFormat && withTime) return DATE_TIME_LONG_FORMAT.format(date); else if (withTime) return DATE_TIME_FORMAT.format(date); else if (longDateFormat) return DATE_LONG_FORMAT.format(date);
        return DATE_FORMAT.format(date);
    }

    /**
	 * Get the localized long date format for the given language.
	 * 
	 * @param aDate
	 *            The date to format
	 * @return String The date format
	 */
    public static String formatLongDate(Date aDate) {
        return DATE_LONG_FORMAT.format(aDate);
    }

    /**
	 * Tries different date formats to parse against the given string
	 * representation to retrieve a valid Date object.
	 * 
	 * @param strdate
	 *            Date as String
	 * @return Date The parsed Date
	 */
    public static Date getDate(String strdate) {
        if (strdate == null || strdate.length() == 0) return null;
        Date result = null;
        strdate = strdate.trim();
        if (strdate.length() > 10) {
            if ((strdate.substring(strdate.length() - 5).indexOf("+") == 0 || strdate.substring(strdate.length() - 5).indexOf("-") == 0) && strdate.substring(strdate.length() - 5).indexOf(":") == 2) {
                String sign = strdate.substring(strdate.length() - 5, strdate.length() - 4);
                strdate = strdate.substring(0, strdate.length() - 5) + sign + "0" + strdate.substring(strdate.length() - 4);
            }
            String dateEnd = strdate.substring(strdate.length() - 6);
            if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0) && dateEnd.indexOf(":") == 3) {
                if (!"GMT".equals(strdate.substring(strdate.length() - 9, strdate.length() - 6))) {
                    String oldDate = strdate;
                    String newEnd = dateEnd.substring(0, 3) + dateEnd.substring(4);
                    strdate = oldDate.substring(0, oldDate.length() - 6) + newEnd;
                }
            }
        }
        int i = 0;
        while (i < dateFormats.length) {
            try {
                synchronized (dateFormats[i]) {
                    return dateFormats[i].parse(strdate);
                }
            } catch (ParseException e) {
                i++;
            } catch (NumberFormatException e) {
                i++;
            }
        }
        return result;
    }

    /** Initialize the array of common date formats */
    static {
        final String[] possibleDateFormats = { "EEE, dd MMM yy HH:mm:ss z", "EEE, dd MMM yyyy HH:mm:ss z", "EEE, dd MMM yy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:sszzzz", "yyyy-MM-dd'T'HH:mm:ss z", "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HHmmss.SSSz", "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy HH:mm:ss z", "dd MMM yyyy HH:mm z", "yyyy-MM-dd'T'HH:mmZ", "yyyy-MM-dd" };
        dateFormats = new SimpleDateFormat[possibleDateFormats.length];
        TimeZone gmtTZ = TimeZone.getTimeZone("GMT");
        for (int i = 0; i < possibleDateFormats.length; i++) {
            dateFormats[i] = new SimpleDateFormat(possibleDateFormats[i], Locale.ENGLISH);
            dateFormats[i].setTimeZone(gmtTZ);
        }
    }
}
