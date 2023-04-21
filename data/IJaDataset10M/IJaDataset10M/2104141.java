package com.android.settings.fuelgauge;

import android.content.Context;
import com.android.settings.R;

/**
 * Contains utility functions for formatting elapsed time and consumed bytes
 */
public class Utils {

    private static final int SECONDS_PER_MINUTE = 60;

    private static final int SECONDS_PER_HOUR = 60 * 60;

    private static final int SECONDS_PER_DAY = 24 * 60 * 60;

    /**
     * Returns elapsed time for the given millis, in the following format:
     * 2d 5h 40m 29s
     * @param context the application context
     * @param millis the elapsed time in milli seconds
     * @return the formatted elapsed time
     */
    public static String formatElapsedTime(Context context, double millis) {
        StringBuilder sb = new StringBuilder();
        int seconds = (int) Math.floor(millis / 1000);
        int days = 0, hours = 0, minutes = 0;
        if (seconds > SECONDS_PER_DAY) {
            days = seconds / SECONDS_PER_DAY;
            seconds -= days * SECONDS_PER_DAY;
        }
        if (seconds > SECONDS_PER_HOUR) {
            hours = seconds / SECONDS_PER_HOUR;
            seconds -= hours * SECONDS_PER_HOUR;
        }
        if (seconds > SECONDS_PER_MINUTE) {
            minutes = seconds / SECONDS_PER_MINUTE;
            seconds -= minutes * SECONDS_PER_MINUTE;
        }
        if (days > 0) {
            sb.append(context.getString(R.string.battery_history_days, days, hours, minutes, seconds));
        } else if (hours > 0) {
            sb.append(context.getString(R.string.battery_history_hours, hours, minutes, seconds));
        } else if (minutes > 0) {
            sb.append(context.getString(R.string.battery_history_minutes, minutes, seconds));
        } else {
            sb.append(context.getString(R.string.battery_history_seconds, seconds));
        }
        return sb.toString();
    }

    /**
     * Formats data size in KB, MB, from the given bytes.
     * @param context the application context
     * @param bytes data size in bytes
     * @return the formatted size such as 4.52 MB or 245 KB or 332 bytes
     */
    public static String formatBytes(Context context, double bytes) {
        if (bytes > 1000 * 1000) {
            return String.format("%.2f MB", ((int) (bytes / 1000)) / 1000f);
        } else if (bytes > 1024) {
            return String.format("%.2f KB", ((int) (bytes / 10)) / 100f);
        } else {
            return String.format("%d bytes", (int) bytes);
        }
    }
}
