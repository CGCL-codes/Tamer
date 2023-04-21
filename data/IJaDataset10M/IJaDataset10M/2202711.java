package de.denkselbst.util;

import java.text.DecimalFormat;

/**
 * Formats durations given in milliseconds as XXXXh YYm ZZs (hours-minutes-seconds). 
 * 
 * @author patrick
 */
public class Duration {

    public final int hours;

    public final int minutes;

    public final int seconds;

    public final String asString;

    private static final int SECOND = 1000;

    private static final int MINUTE = 60 * SECOND;

    private static final int HOUR = 60 * MINUTE;

    public static final DecimalFormat fmt = new DecimalFormat("00");

    public Duration(long duration) {
        hours = (int) (duration / HOUR);
        duration -= (hours * HOUR);
        minutes = (int) (duration / MINUTE);
        duration -= (minutes * MINUTE);
        seconds = (int) duration / SECOND;
        asString = fmt.format(hours) + "h " + fmt.format(minutes) + "m " + fmt.format(seconds) + "s";
    }

    @Override
    public String toString() {
        return asString;
    }
}
