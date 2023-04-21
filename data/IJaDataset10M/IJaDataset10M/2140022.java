package com.tykhe.systems.snmpoller.logging;

import java.util.Date;
import java.text.DateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A customized single-log-item-per-line log formatter to replace Java's standard SimpleFormatter,
 * which places every log entry on two lines.  This formatter ensures a fixed width Date and facility
 * prefix followed by whatever text the user wants logged.
 * @author plubans
 * @see	java.util.logging.SimpleFormatter
 */
public class BasicLogFormatter extends Formatter {

    /**
	 * The DateFormat that we will use when printing Dates to the log.  
	 */
    private static DateFormat dateFormat = null;

    /**
     * Format a LogRecord into a String.  We get detailed date information,
     * followed by a space-padded log facility name, then a colon, a space,
     * and the message followed by a newline.
     */
    public String format(LogRecord record) {
        if (dateFormat == null) {
            try {
                dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
        return dateFormat.format(new Date()) + String.format("%8s", record.getLevel()) + ": " + record.getMessage() + "\n";
    }
}
