package de.jmda.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public abstract class TimeUtil {

    public static final long MSECS_SEC = 1000L;

    public static final long MSECS_MIN = MSECS_SEC * 60L;

    public static final long MSECS_HOUR = MSECS_MIN * 60L;

    public static final long MSECS_DAY = MSECS_HOUR * 24L;

    public static final long MSECS_WEEK = MSECS_DAY * 7L;

    public static final long MSECS_MONTH = MSECS_DAY * 30L;

    public static final long MSECS_YEAR = MSECS_DAY * 365L;

    public static final SimpleDateFormat getDateFormatSortableTimestamp() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS");
    }

    public static final SimpleDateFormat getDateFormatSortableTimestampPrecisionSeconds() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    }

    public static final SimpleDateFormat getDateFormatSortableTimestampPrecisionMinutes() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm");
    }

    public static final SimpleDateFormat getDateFormatSortableTimestampPrecisionHours() {
        return new SimpleDateFormat("yyyy.MM.dd HH");
    }

    public static final SimpleDateFormat getDateFormatSortableTimestampPrecisionDays() {
        return new SimpleDateFormat("yyyy.MM.dd");
    }

    public static final String getSortableTimestamp() {
        return TimeUtil.getDateFormatSortableTimestamp().format(new Date());
    }

    public static final String getSortableTimestampPrecisionSeconds() {
        return TimeUtil.getDateFormatSortableTimestampPrecisionSeconds().format(new Date());
    }

    public static final String getSortableTimestampPrecisionMinutes() {
        return TimeUtil.getDateFormatSortableTimestampPrecisionMinutes().format(new Date());
    }

    public static final String getSortableTimestampPrecisionMinutes(Date date) {
        return TimeUtil.getDateFormatSortableTimestampPrecisionMinutes().format(date);
    }
}
