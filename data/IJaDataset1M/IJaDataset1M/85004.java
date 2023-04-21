package wsattacker.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {

    private static final SimpleDateFormat TIMEONLY = new SimpleDateFormat("HH:mm:ss.S");

    private static final SimpleDateFormat FULL = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");

    public static String timeonly(Date date) {
        return TIMEONLY.format(date);
    }

    public static String full(Date date) {
        return FULL.format(date);
    }
}
