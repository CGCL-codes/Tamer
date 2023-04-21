package net.sf.japi.archstat.log;

import java.util.Formatter;
import org.jetbrains.annotations.NotNull;

/** The StreamLogger is a Logger for LogEntries that logs to the specified Stream.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 */
public class StreamLogger implements Logger {

    /** The Stream for logging. */
    @NotNull
    private final Formatter out;

    /** Creates a StreamLogger.
     * @param out Stream for logging.
     */
    public StreamLogger(@NotNull final Appendable out) {
        this.out = new Formatter(out);
    }

    /** {@inheritDoc} */
    public void log(@NotNull final LogEntry logEntry) {
        out.format("%s%n", logEntry);
    }
}
