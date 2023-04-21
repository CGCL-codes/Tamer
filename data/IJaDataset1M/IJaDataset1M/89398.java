package org.smallmind.scribe.pen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatTimestamp implements Timestamp {

    private static final DateFormatTimestamp STANDARD_TIMESTAMP = new DateFormatTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

    private DateFormat dateFormat;

    public static DateFormatTimestamp getDefaultInstance() {
        return STANDARD_TIMESTAMP;
    }

    public DateFormatTimestamp(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public synchronized String getTimestamp(Date date) {
        return dateFormat.format(date);
    }
}
