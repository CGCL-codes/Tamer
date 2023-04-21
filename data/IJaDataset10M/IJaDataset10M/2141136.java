package com.inet.qlcbcc.internal.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * StringUtils.
 *
 * @author <a href="mailto:ntvy@inetcloud.vn">Vy Nguyen</a>
 * @version $Id: StringUtils.java 2011-04-09 15:23:27z nguyen_vt $
 *
 * @since 1.0
 */
public final class StringUtils {

    /** empty string. */
    public static final String EMPTY_STRING = "";

    private StringUtils() {
    }

    /**
   * Checks the given string is not null and must have at least one character.
   *
   * @param str the given string to check.
   * @return if the given string is not null and have at least one character.
   */
    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    /**
   * Get the given String is not null and default value must have at least one character.
   *
   * @param str the given string to check.
   * @param defaultValue the given default String
   * @return the given string value .
   */
    public static String getValue(String str, String defaultValue) {
        return (hasLength(str)) ? str : defaultValue;
    }

    /**
   * Convert to date 
   * @param str - the given string
   * @param defautValue - the given default value Date
   * @return Date
   * @throws ParseException when there is any error happen.
   */
    public static Date toDate(String str, Date defautValue) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if (str == null || str.isEmpty()) {
            return defautValue;
        }
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
        }
        return defautValue;
    }

    /**
   * Convert the given string to date with given format
   * 
   * @param date the given string represent for date
   * @param format the date format
   * @param defaultValue the given default value
   * @return the date
   */
    public static Date toDate(String date, String format, Date defaultValue) {
        if (!hasLength(date)) {
            return defaultValue;
        }
        try {
            if (!hasLength(format)) {
                format = "dd/MM/yyyy";
            }
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException ex) {
            return defaultValue;
        }
    }

    /**
   * Split string by given seperation
   * 
   * @param value the given value
   * @param seperation the given seperation
   * @return the list of value
   */
    public static String[] split(String value, String seperation) {
        if (!StringUtils.hasLength(value)) {
            return new String[0];
        }
        return value.split(seperation);
    }

    /**
   * check a string is number (int)
   * 
   * @param value is string to check 
   * @return true if is number 
   */
    public static boolean isNumeric(String value) {
        if (!StringUtils.hasLength(value)) {
            return false;
        }
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(value, pos);
        return value.length() == pos.getIndex();
    }
}
