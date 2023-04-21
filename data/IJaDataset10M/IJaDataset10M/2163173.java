package de.mnl.util.logging;

import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Format the record for output to syslog. Note that level and time stamp
 * are part of the syslog protocol and therefore not included here.
 * 
 * @author Michael N. Lipp
 */
public class SyslogFormatter extends Formatter {

    @Override
    public String format(LogRecord rec) {
        return String.format(Locale.US, "[%s] %s", rec.getLoggerName(), rec.getMessage());
    }
}
