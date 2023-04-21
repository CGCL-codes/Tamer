package net.sf.buildbox.devmodel.ui.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import java.util.Date;

/**
 * Utility class with miscelaneous methods.
 * They all will be deprecated and moved to more specialized classes in future.
 */
public final class Misc {

    public static final TimeZone NO_TIME_ZONE = TimeZone.createTimeZone(0);

    public static final DateTimeFormat ISO_DURATION_HMS_FORMAT = DateTimeFormat.getFormat("HH:mm:ss");

    public static final DateTimeFormat ISO_DATETIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static final DateTimeFormat HUMAN_DATETIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd' 'HH:mm:ss");

    public static final DateTimeFormat ATOM_DATETIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private Misc() {
    }

    public static String datePartStr(long time) {
        return ISO_DATETIME_FORMAT.format(new Date(time)).substring(0, 10);
    }

    public static String durationHMS(long duration) {
        return Misc.ISO_DURATION_HMS_FORMAT.format(new Date(duration), Misc.NO_TIME_ZONE);
    }

    public static boolean isTimestampInRange(long timestamp, long startTime, long endTime) {
        if (startTime != 0 && timestamp < startTime) return false;
        if (endTime != 0 && timestamp > endTime) return false;
        return true;
    }
}
