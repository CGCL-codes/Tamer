package com.google.api.ads.dfp.lib.utils.v201111;

import com.google.api.ads.dfp.v201111.DateTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A utility class for handling {@link DateTime} objects.
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public final class DateTimeUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
   * {@code DateTimeUtils} is meant to be used statically.
   */
    private DateTimeUtils() {
    }

    /**
   * Creates a {@link DateTime} object from a string representation in the form
   * of {@code yyyy-MM-ddTHH:mm:ss} (e.g. 2011-01-01T00:00:00).
   *
   * @param dateTimeString the string representation of the {@code DateTime}
   * @return a {@code DateTime} object created from the string
   * @throws ParseException if the string could not be parsed
   */
    public static DateTime fromString(String dateTimeString) throws ParseException {
        return fromDate(new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateTimeString));
    }

    /**
   * Creates a {@link DateTime} object from a Java {@code Date} object already
   * set to the publisher's timezone. The timezone will not be set on the
   * {@code DateTime} object as it is ignored as input for all use cases.
   *
   * @param date the {@code Date} object to transform into a {@code DateTime}
   * @return a {@code DateTime} representing {@code date}
   */
    public static DateTime fromDate(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateTime dfpDateTime = new DateTime();
        dfpDateTime.setDate(DateUtils.fromDate(date));
        dfpDateTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        dfpDateTime.setMinute(calendar.get(Calendar.MINUTE));
        dfpDateTime.setSecond(calendar.get(Calendar.SECOND));
        return dfpDateTime;
    }

    /**
   * Gets a {@link DateTime} object representing the present time in the
   * timezone of the publisher.
   *
   * @param timeZoneId the timezone of the publisher
   * @return a {@code DateTime} object representing the present time in the
   *     publisher's timezone
   */
    public static DateTime now(String timeZoneId) {
        return fromDate(Calendar.getInstance(TimeZone.getTimeZone(timeZoneId)).getTime());
    }

    /**
   * Gets a {@link DateTime} object representing midnight of the present date in
   * the timezone of the publisher (i.e. all time fields are nullified).
   *
   * @param timeZoneId the timezone of the publisher
   * @return a {@code DateTime} object representing the present date in the
   *     publisher's timezone
   */
    public static DateTime today(String timeZoneId) {
        DateTime dateTime = fromDate(Calendar.getInstance(TimeZone.getTimeZone(timeZoneId)).getTime());
        dateTime.setHour(0);
        dateTime.setMinute(0);
        dateTime.setSecond(0);
        return dateTime;
    }

    /**
   * Gets a Java {@code Date} object from a {@link DateTime} object. The
   * timezone of the {@code DateTime} object is used when creating the new
   * date object.
   *
   * @param dateTime the {@code DateTime} object to convert to a {@code Date}
   * @return the {@code DateTime} object as a {@code Date} in the timezone set
   *     within {@code dateTime}
   */
    public static java.util.Date toDate(DateTime dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(dateTime.getTimeZoneID()));
        calendar.set(Calendar.YEAR, dateTime.getDate().getYear());
        calendar.set(Calendar.MONTH, dateTime.getDate().getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dateTime.getDate().getDay());
        calendar.set(Calendar.HOUR_OF_DAY, dateTime.getHour());
        calendar.set(Calendar.MINUTE, dateTime.getMinute());
        calendar.set(Calendar.SECOND, dateTime.getSecond());
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
   * Gets a string representation of the {@link DateTime} object in the form
   * of {@code yyyy-mm-ddTHH:mm:ss} (e.g. 2011-01-01T000:00:00)
   *
   * @param dateTime the {@code DateTime} object used to create the string
   * @return a string representation of the {@link DateTime} object
   */
    public static String toString(DateTime dateTime) {
        DateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone(dateTime.getTimeZoneID()));
        return dateTimeFormat.format(toDate(dateTime));
    }
}
