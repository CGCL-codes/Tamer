package de.eqc.srcds.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Mostly like the {@link SimpleFormatter} but outputs only one line per log record.
 * 
 * @author Holger Cremer
 */
public class CompactSimpleFormatter extends Formatter {

    private final Date dat = new Date();

    private static final String FORMAT_PATTERN = "{0,date} {0,time}";

    private MessageFormat formatter;

    private final String lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    private String getClassNameWithoutPackage(final String sourceClassName) {
        final int lastIndexOf = sourceClassName.lastIndexOf('.');
        String ret;
        if (lastIndexOf == -1) {
            ret = sourceClassName;
        } else {
            ret = sourceClassName.substring(lastIndexOf + 1);
        }
        return ret;
    }

    /**
     * Format the given LogRecord.
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    @Override
    public synchronized String format(final LogRecord record) {
        final StringBuilder sb = new StringBuilder();
        dat.setTime(record.getMillis());
        Object args[] = new Object[1];
        args[0] = dat;
        final StringBuffer text = new StringBuffer();
        if (formatter == null) {
            formatter = new MessageFormat(FORMAT_PATTERN);
        }
        formatter.format(args, text, null);
        sb.append(text);
        sb.append(" [");
        if (record.getSourceClassName() != null) {
            sb.append(getClassNameWithoutPackage(record.getSourceClassName()));
        } else {
            sb.append(record.getLoggerName());
        }
        sb.append("] ");
        final String message = formatMessage(record);
        sb.append(record.getLevel().getName());
        sb.append(": ");
        sb.append(message);
        sb.append(lineSeparator);
        if (record.getThrown() != null) {
            try {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }
}
