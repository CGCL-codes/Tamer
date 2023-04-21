package net.bcharris.photomosaic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private static final ThreadLocal<DateFormat> TIME_FORMAT = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("HHmmss");
        }
    };

    public static void log(String message) {
        System.out.println(TIME_FORMAT.get().format(new Date()) + ": " + message);
    }

    public static void log(String format, Object... args) {
        log(String.format(format, args));
    }
}
