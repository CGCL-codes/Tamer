package org.slf4j.profiler;

import java.text.DecimalFormat;

/**
 * 
 * A collection of utility methods.
 * 
 * @author Ceki G&uuml;lc&uuml;
 *  
 */
class Util {

    static final long NANOS_IN_ONE_MICROSECOND = 1000;

    static final long NANOS_IN_ONE_MILLISECOND = NANOS_IN_ONE_MICROSECOND * 1000;

    static final long NANOS_IN_ONE_SECOND = NANOS_IN_ONE_MILLISECOND * 1000;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.000");

    static DurationUnit selectDurationUnitForDisplay(StopWatch sw) {
        return selectDurationUnitForDisplay(sw.elapsedTime());
    }

    static DurationUnit selectDurationUnitForDisplay(long durationInNanos) {
        if (durationInNanos < 10 * NANOS_IN_ONE_MICROSECOND) {
            return DurationUnit.NANOSECOND;
        } else if (durationInNanos < 10 * NANOS_IN_ONE_MILLISECOND) {
            return DurationUnit.MICROSECOND;
        } else if (durationInNanos < 10 * NANOS_IN_ONE_SECOND) {
            return DurationUnit.MILLISSECOND;
        } else {
            return DurationUnit.SECOND;
        }
    }

    public static double convertToMicros(long nanos) {
        return (double) nanos / NANOS_IN_ONE_MICROSECOND;
    }

    public static double convertToMillis(long nanos) {
        return (double) nanos / NANOS_IN_ONE_MILLISECOND;
    }

    public static double convertToSeconds(long nanos) {
        return ((double) nanos / NANOS_IN_ONE_SECOND);
    }

    static String durationInDunrationUnitsAsStr(StringBuffer buf, StopWatch sw) {
        DurationUnit du = selectDurationUnitForDisplay(sw);
        return durationInDunrationUnitsAsStr(sw.elapsedTime(), du);
    }

    static String durationInDunrationUnitsAsStr(long nanos, DurationUnit durationUnit) {
        StringBuffer buf = new StringBuffer();
        switch(durationUnit) {
            case NANOSECOND:
                buf.append(nanos);
                break;
            case MICROSECOND:
                double micros = convertToMicros(nanos);
                buf.append(DECIMAL_FORMAT.format(micros));
                break;
            case MILLISSECOND:
                double millis = convertToMillis(nanos);
                buf.append(DECIMAL_FORMAT.format(millis));
                break;
            case SECOND:
                double seconds = convertToSeconds(nanos);
                buf.append(DECIMAL_FORMAT.format(seconds));
                break;
        }
        return buf.toString();
    }

    static void appendDurationUnitAsStr(StringBuffer buf, DurationUnit durationUnit) {
        switch(durationUnit) {
            case NANOSECOND:
                buf.append("nanoseconds.");
                break;
            case MICROSECOND:
                buf.append("microseconds.");
                break;
            case MILLISSECOND:
                buf.append("milliseconds.");
                break;
            case SECOND:
                buf.append(" seconds.");
                break;
        }
    }
}
