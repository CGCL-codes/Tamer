package org.jiopi.ibean.loader.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * 
 * Loader的日志格式定义
 * 
 * @since 2010.4.12
 *
 */
public class LoaderLogFormatter extends Formatter {

    private final Date dat;

    private static final String format = "{0,date} {0,time}";

    private final MessageFormat formatter;

    private final Object args[] = new Object[1];

    private final String lineSeparator = "\n";

    public LoaderLogFormatter() {
        dat = new Date();
        formatter = new MessageFormat(format);
        args[0] = dat;
    }

    @Override
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        synchronized (formatter) {
            dat.setTime(record.getMillis());
            formatter.format(args, sb, null);
        }
        sb.append(" ");
        sb.append(record.getLoggerName());
        sb.append(" ");
        String message = formatMessage(record);
        sb.append(record.getLevel());
        sb.append(": ");
        sb.append(message);
        sb.append(lineSeparator);
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
        return sb.toString();
    }
}
