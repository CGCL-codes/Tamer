package net.sourceforge.pergamon.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";

    public static String format(final Date time) {
        return DateUtil.format(time, DateUtil.DEFAULT_FORMAT);
    }

    public static String format(final Date time, final String pattern) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(time);
    }
}
