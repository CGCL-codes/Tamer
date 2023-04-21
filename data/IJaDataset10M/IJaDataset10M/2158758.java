package org.jlf.logging;

import java.util.*;
import java.util.logging.*;
import java.text.*;
import org.jlf.log.AppError;

/**
 * <code>JLFFormatter</code> implements the JDK logging framework
 * Formatter abstract class to look like a JLF log.  The
 * SimpleFormatter doesn't provide enough information and the
 * XMLFormatter is too verbose.<p>
 * 
 * To kick in this formatter, a simple way is to create/modify
 * a property file to override the defaults as follows:<p>
 *
 * <pre>
 * Step 1: Create a property file with the following logging overrides:
 *
 * ##########################################################################
 * # Kick in at least one handler (usually console or file handler)
 * 
 * handlers=java.util.logging.ConsoleHandler
 * #handlers=java.util.logging.FileHandler
 * 
 * ##########################################################################
 * # Override default logging properties
 * 
 * java.util.logging.ConsoleHandler.formatter=org.jlf.logging.JLFFormatter
 * java.util.logging.FileHandler.formatter=org.jlf.logging.JLFFormatter
 *
 * ##########################################################################
 * 
 * Step 2: Add a -Djava.util.logging.config.file=(file name) 
 *         option to your Java start command to kick in
 *         the property file overrides you just created
 *
 * @see java.util.logging.Formatter
 * 
 * @author Todd Lauinger
 * @version $Revision: 1.3 $
 *
 */
public class JLFFormatter extends java.util.logging.Formatter {

    /**
     * Default DateFormat for the date/time that is output to the log record.
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "MM-dd-yy HH:mm:ss:SSS";

    /**
     * Default log field separator.
     */
    public static final String DEFAULT_LOG_FIELD_SEPARATOR = "|";

    /**
     * Format a log record somewhat like the JLF native
     * logging framework.
     * 
	 * @see java.util.logging.Formatter#format(LogRecord)
	 */
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer(500);
        sb.append(record.getLoggerName());
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR);
        sb.append(record.getLevel().toString());
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR);
        Date recordDate = new Date(record.getMillis());
        String recordDateTime = (new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT)).format(recordDate);
        sb.append(recordDateTime);
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR);
        sb.append(record.getThreadID());
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR);
        sb.append(Thread.currentThread().getName());
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR);
        if (record.getMessage() != null) {
            sb.append(formatMessage(record));
        }
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            sb.append("\nException Thrown:\n");
            sb.append(AppError.getStackTraceAsString(thrown));
        }
        sb.append("\n");
        return sb.toString();
    }
}
