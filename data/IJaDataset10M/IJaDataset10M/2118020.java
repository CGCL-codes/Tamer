package logahawk.formatters;

import net.jcip.annotations.*;
import logahawk.listeners.*;

/**
 * A very simple {@link MessageFormatter} that does not output the time of the log message. This may be useful for
 * outputting to the console (i.e. System.out).
 */
@Immutable
public class SimpleMessageFormatter implements MessageFormatter {

    public SimpleMessageFormatter() {
    }

    public String format(LogMeta meta, String text) {
        synchronized (buffer) {
            buffer.setLength(0);
            buffer.append("(");
            buffer.append(meta.getSeverity());
            buffer.append(") ");
            buffer.append(text != null && text.length() > 0 ? text : "(null)");
            return buffer.toString();
        }
    }

    /** A single StringBuilder is used to avoid having to re-allocate a string each time. */
    protected final StringBuilder buffer = new StringBuilder(1024);
}
