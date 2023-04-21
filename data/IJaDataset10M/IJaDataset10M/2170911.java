package org.matsim.utils.misc;

import org.matsim.utils.StringUtils;

public class Time {

    public static final double UNDEFINED_TIME = Double.NEGATIVE_INFINITY;

    public static final String TIMEFORMAT_HHMM = "HH:mm";

    public static final String TIMEFORMAT_HHMMSS = "HH:mm:ss";

    public static final String TIMEFORMAT_SSSS = "ssss";

    private static String defaultTimeFormat = TIMEFORMAT_HHMMSS;

    /**
	 * Sets the default time format to be used for conversion of seconds to a string-representation
	 * ({@link #writeTime(double)}). If nothing is set, {@link #TIMEFORMAT_HHMMSS} is used as default.
	 *
	 * @param format
	 */
    public static final void setDefaultTimeFormat(final String format) {
        defaultTimeFormat = format;
    }

    /**
	 * @deprecated use Time.parseTime(String, '-') instead
	 * @param timeStr
	 * @return
	 */
    @Deprecated
    public static int secFromStr(final String timeStr) {
        return (int) parseTime(timeStr, '-');
    }

    /**
	 * @deprecated use Time.writeTime(double, '-') instead.
	 * @param time_s
	 * @return
	 */
    @Deprecated
    public static String strFromSec(final int time_s) {
        return writeTime(time_s, TIMEFORMAT_HHMMSS, '-');
    }

    public static final String writeTime(final double seconds, final String timeformat) {
        return writeTime(seconds, timeformat, ':');
    }

    public static final String writeTime(final double seconds, final char separator) {
        return writeTime(seconds, defaultTimeFormat, separator);
    }

    public static final String writeTime(final double seconds) {
        return writeTime(seconds, defaultTimeFormat, ':');
    }

    /**
	 * Converts the given time in seconds after midnight into a textual representation
	 *
	 * @param seconds The time to convert, measured in seconds after midnight.
	 * @param timeformat
	 * @param separator
	 * @return A textual representation of the passed time.
	 */
    public static final String writeTime(final double seconds, final String timeformat, final char separator) {
        if (TIMEFORMAT_SSSS.equals(timeformat)) {
            return Long.toString((long) seconds);
        } else if (seconds < 0) {
            if (seconds == UNDEFINED_TIME) return "undefined";
            return "-" + writeTime(Math.abs(seconds), timeformat, separator);
        }
        double s = seconds;
        long h = (long) (s / 3600);
        s = s % 3600;
        int m = (int) (s / 60);
        s = s % 60;
        String str_h;
        if (h < 10) {
            str_h = "0" + Long.toString(h);
        } else {
            str_h = Long.toString(h);
        }
        String str_m;
        if (m < 10) {
            str_m = "0" + Integer.toString(m);
        } else {
            str_m = Integer.toString(m);
        }
        if (TIMEFORMAT_HHMM.equals(timeformat)) {
            return str_h + separator + str_m;
        } else if (TIMEFORMAT_HHMMSS.equals(timeformat)) {
            String str_s;
            if (s < 10) {
                str_s = "0" + Integer.toString((int) s);
            } else {
                str_s = Integer.toString((int) s);
            }
            return str_h + separator + str_m + separator + str_s;
        }
        throw new IllegalArgumentException("The time format (" + timeformat + ") is not known.");
    }

    /**
	 * Parses the given string for a textual representation for time and returns
	 * the time value in seconds past midnight. It is the same as {@link #parseTime(String, char)}
	 * with the separator set to ':'.
	 *
	 * @param time the string describing a time to parse.
	 *
	 * @return the parsed time as seconds after midnight.
	 *
	 * @throws IllegalArgumentException when the string cannot be interpreted as a valid time.
	 */
    public static final double parseTime(final String time) {
        return parseTime(time, ':');
    }

    /**
	 * Parses the given string for a textual representation for time and returns
	 * the time value in seconds past midnight. The following formats are recognized:
	 * HH:mm:ss, HH:mm, ssss.
	 *
	 * @param time the string describing a time to parse.
	 * @param separator the character used between hours and minutes, and minutes and seconds.
	 *
	 * @return the parsed time as seconds after midnight.
	 *
	 * @throws IllegalArgumentException when the string cannot be interpreted as a valid time.
	 */
    public static final double parseTime(final String time, final char separator) {
        if (time == null || time.length() == 0 || time.equals("undefined")) {
            return Time.UNDEFINED_TIME;
        }
        boolean isNegative = (time.charAt(0) == '-');
        String[] strings = (isNegative ? StringUtils.explode(time.substring(1), separator) : StringUtils.explode(time, separator));
        double seconds = 0;
        if (strings.length == 1) {
            seconds = Math.abs(Double.parseDouble(strings[0]));
        } else if (strings.length == 2) {
            int h = Integer.parseInt(strings[0]);
            int m = Integer.parseInt(strings[1]);
            if ((m < 0) || (m > 59)) {
                throw new IllegalArgumentException("minutes are out of range in " + time);
            }
            seconds = Math.abs(h) * 3600 + m * 60;
        } else if (strings.length == 3) {
            int h = Integer.parseInt(strings[0]);
            int m = Integer.parseInt(strings[1]);
            double s = Double.parseDouble(strings[2]);
            if ((m < 0) || (m > 59)) {
                throw new IllegalArgumentException("minutes are out of range in " + time);
            }
            if ((s < 0) || (s > 59)) {
                throw new IllegalArgumentException("seconds are out of range in " + time);
            }
            seconds = Math.abs(h) * 3600 + m * 60 + s;
        } else {
            throw new IllegalArgumentException("time format is not valid in " + time);
        }
        if (isNegative) {
            seconds = -seconds;
        }
        return seconds;
    }
}
