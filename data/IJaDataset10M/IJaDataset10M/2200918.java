package org.speakmon.coffeehouse;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * Created: Tue Jan  7 19:43:42 2003
 *
 * @author <a href="mailto:ben@speakmon.org">Ben Speakmon</a>
 */
class LogFormatter extends Formatter {

    public LogFormatter() {
        super();
    }

    /**
     * Describe <code>format</code> method here.
     *
     * @param logRecord a <code>LogRecord</code> value
     * @return a <code>String</code> value
     */
    public String format(LogRecord logRecord) {
        return logRecord.getMessage();
    }
}
