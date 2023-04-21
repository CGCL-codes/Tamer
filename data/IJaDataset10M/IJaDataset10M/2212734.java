package com.cosylab.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * An implementation of <code>java.util.logging.Formatter</code>. Produces
 * simple single line log reports meant to go to the console.
 *
 * @author Matej Sekoranja (matej.sekoranjaATcosylab.com)
 */
public class ConsoleLogFormatter extends Formatter {

    /** System property key to enable trace messages. */
    public static String KEY_TRACE = "TRACE";

    /** Line separator string. */
    private boolean showTrace;

    /** Line separator string. */
    private String lineSeparator;

    /** ISO 8601 date formatter. */
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /** Date object (used not to recreate it every time). */
    private Date date = new Date();

    public ConsoleLogFormatter() {
        try {
            lineSeparator = System.getProperty("line.separator");
            showTrace = System.getProperties().containsKey(KEY_TRACE);
        } catch (SecurityException e) {
            lineSeparator = "\n";
            showTrace = false;
        }
    }

    /**
	 * Format the given LogRecord.
	 *
	 * @param record the log record to be formatted.
	 *
	 * @return a formatted log record
	 */
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer(128);
        synchronized (date) {
            date.setTime(record.getMillis());
            sb.append(timeFormatter.format(date));
        }
        if (record.getLoggerName() != null || record.getSourceClassName() != null || record.getSourceMethodName() != null) {
            sb.append(' ');
            sb.append('[');
            if (record.getLoggerName() != null) {
                sb.append(record.getLoggerName());
                if (record.getSourceClassName() != null) {
                    sb.append(':');
                }
            }
            if (record.getSourceClassName() != null) {
                sb.append(record.getSourceClassName().substring(record.getSourceClassName().lastIndexOf('.') + 1));
            }
            sb.append('#');
            if (record.getSourceMethodName() != null) {
                sb.append(record.getSourceMethodName());
            }
            sb.append(']');
        }
        sb.append(' ');
        sb.append(record.getMessage());
        sb.append(lineSeparator);
        if (showTrace) {
            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                }
            }
        }
        return new String(sb);
    }
}
