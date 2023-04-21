package mn.more.wits.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 * @author <a href="mailto:mike.liu@aptechmongolia.edu.mn">Mike Liu</a>
 * @version $Revision: 119 $
 */
public final class FormatUtil {

    public static final DateTimeFormat SHORT_DATE = DateTimeFormat.getFormat("yyyy.MM.dd");

    public static final DateTimeFormat LONG_DATE = DateTimeFormat.getFormat("yyyy.MM.dd hh:mm:ss");

    private static final String TIME_SEP = ":";

    private FormatUtil() {
    }

    public static String formatShortDate(Date d) {
        return SHORT_DATE.format(d);
    }

    public static String formatShortDate(long d) {
        return SHORT_DATE.format(new Date(d));
    }

    public static String formatLongDate(Date d) {
        return LONG_DATE.format(d);
    }

    public static String formatLongDate(long d) {
        return LONG_DATE.format(new Date(d));
    }

    public static String newShortDate() {
        return formatShortDate(new Date());
    }

    public static String newLongDate() {
        return formatLongDate(new Date());
    }

    public static String formatTime(long seconds) {
        int hour = (int) (seconds / 60 / 60);
        int minutes = (int) (seconds / 60 % 60);
        int sec = (int) (seconds % 60);
        return (hour < 10 ? "0" + hour : "" + hour) + TIME_SEP + (minutes < 10 ? "0" + minutes : "" + minutes) + TIME_SEP + (sec < 10 ? "0" + sec : "" + sec);
    }

    public static Date toDateTime(String longDate) {
        if (longDate == null || longDate.length() < 1) {
            return null;
        }
        return LONG_DATE.parse(longDate);
    }

    public static Date toDate(String shortDate) {
        if (shortDate == null || shortDate.length() < 1) {
            return null;
        }
        return SHORT_DATE.parse(shortDate);
    }
}
