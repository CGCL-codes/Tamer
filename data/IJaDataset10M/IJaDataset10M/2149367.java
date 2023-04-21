package appointments.domain;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for conversions Date -> String and String -> Date.
 * 
 * @author facundo.maldonado
 */
public class DateUtils {

    private static DateFormat timeFormatter = new SimpleDateFormat("HH:mm a");

    private static DateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

    private static DateFormat dateTimeFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");

    /**
	 * Parse the given string with the format dd-MMM-yyyy to a date.
	 * 
	 * @param dateString string dd-MMM-yyyy.
	 * @return date.
	 */
    public static Date parseStringtoDate(final String dateString) {
        try {
            return dateFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Unparseable String: " + dateString, e);
        }
    }

    /**
	 * Parse the given string with the format dd-MMM-yyyy HH:mm a to a date.
	 * 
	 * @param dateString string dd-MMM-yyyy HH:mm a.
	 * @return date.
	 */
    public static Date parseStringToDateTime(final String dateString) {
        try {
            return dateTimeFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Unparseable String: " + dateString, e);
        }
    }

    /**
	 * Parse the given string with the format HH:mm a to a date.
	 * 
	 * @param dateString string HH:mm a.
	 * @return date.
	 */
    public static Date parseStringToTime(final String dateString) {
        try {
            return dateTimeFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Unparseable String: " + dateString, e);
        }
    }

    /**
	 * Returns the time of the date as a string.
	 * 
	 * @param date the date to extract the time.
	 * @return time as string.
	 */
    public static String formatTimeFromDate(final Date date) {
        String time = timeFormatter.format(date);
        Date appointmentTime;
        try {
            appointmentTime = timeFormatter.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException("Unparseable String: " + time, e);
        }
        return timeFormatter.format(appointmentTime);
    }

    /**
	 * Returns a <code>Time</code> from a <code>Date</code>.
	 * 
	 * @param date to parse and extract a <code>Time</code>
	 * @return <code>Time</code>
	 */
    public static Time getTimeFromDate(final Date date) {
        String dateString = timeFormatter.format(date);
        Date timeDate;
        try {
            timeDate = timeFormatter.parse(dateString);
            return new Time(timeDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Unparseable String: " + dateString, e);
        }
    }
}
