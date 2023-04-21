package net.asfun.jvalog.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import static net.asfun.jvalog.common.log.logger;
import net.asfun.jvalog.common.InteractException;
import net.asfun.jvalog.vo.Setting;

public class ValueFormater {

    public static String date2string(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.setTimeZone(TimeZone.getTimeZone(Setting.TIMEZONE));
            return sdf.format(date);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
        }
        return date.toString();
    }

    public static Date string2date(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.setTimeZone(TimeZone.getTimeZone(Setting.TIMEZONE));
            return sdf.parse(date);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
            throw new InteractException(date + " doesn't match " + format);
        }
    }
}
