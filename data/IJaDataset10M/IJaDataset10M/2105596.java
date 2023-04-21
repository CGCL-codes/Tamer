package net.sf.leechget.webapp;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class LeechgetFormatter {

    private static final NumberFormat numberFormater = NumberFormat.getNumberInstance();

    static {
        LeechgetFormatter.numberFormater.setMaximumFractionDigits(2);
        LeechgetFormatter.numberFormater.setRoundingMode(RoundingMode.CEILING);
    }

    public static String formatSpeed(final long speed) {
        if (speed <= 0) {
            return "Ilimitado";
        }
        return LeechgetFormatter.formatSize(speed) + "/s";
    }

    public static String formatSize(final long size) {
        if (size < 0) {
            return "Ilimitado";
        }
        final double newSize = size;
        if (size >= 1099511627776L) {
            return LeechgetFormatter.numberFormater.format(newSize / 1024.0D / 1024.0D / 1024.0D / 1024.0D) + " TB";
        }
        if (size >= 1073741824L) {
            return LeechgetFormatter.numberFormater.format(newSize / 1024.0D / 1024.0D / 1024.0D) + " GB";
        }
        if (size >= 1048576L) {
            return LeechgetFormatter.numberFormater.format(newSize / 1024.0D / 1024.0D) + " MB";
        }
        if (size >= 1024L) {
            return LeechgetFormatter.numberFormater.format(newSize / 1024.0D) + " KB";
        }
        return size + " B";
    }

    public static String formatSpeed(final double speed) {
        if (speed < 0) {
            return "-";
        }
        return LeechgetFormatter.formatSize((long) speed) + "/s";
    }

    public static String formatRemainTime(long seconds) {
        String formatted = "";
        if (seconds > 3600L) {
            formatted = formatted + (seconds / 3600L) + " h ";
            seconds -= seconds / 3600L * 3600L;
        }
        if (seconds > 60L) {
            formatted = formatted + (seconds / 60L) + " min ";
            seconds -= seconds / 60L * 60L;
        }
        if (seconds > 0L) {
            formatted = formatted + seconds + " s ";
        }
        return formatted.trim();
    }
}
