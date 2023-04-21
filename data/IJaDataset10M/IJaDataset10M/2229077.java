package quickfix;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for logging session-related events.
 *
 */
public class LogUtil {

    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);

    /**
     * Logs a throwable as a session event, including the stack trace.
     * 
     * @param log a Session log
     * @param message error message
     * @param t the exception to log
     */
    public static void logThrowable(Log log, String message, Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println(message);
        t.printStackTrace(printWriter);
        log.onErrorEvent(stringWriter.toString());
    }

    /**
     * Logs a throwable as a session event, including the stack trace.
     * 
     * @param sessionID the session ID
     * @param message the error message
     * @param t the exception to log
     */
    public static void logThrowable(SessionID sessionID, String message, Throwable t) {
        final Session session = Session.lookupSession(sessionID);
        if (session != null) {
            logThrowable(session.getLog(), message, t);
        } else {
            log.error(message, t);
        }
    }
}
