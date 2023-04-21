package org.openfrag.OpenCDS.core.logging;

import org.openfrag.OpenCDS.core.exceptions.InvalidActionException;
import java.util.*;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * The Logger class, here you can listen for log messages, and log them
 *  yourself, or you can let the standard logger take over.
 *
 * @author  Lars 'Levia' Wesselius
*/
public class Logger {

    /** Severity used for information - nothings wrong, just info. */
    public static final String LOG_INFORMATION = "INFORMATION";

    /** Severity used for warnings - there was something wrong, but we can
         continue without hassle. */
    public static final String LOG_WARNING = "WARNING";

    /** Severity used for error - there was an error, and we needed to stop our
         current execution. */
    public static final String LOG_ERROR = "ERROR";

    private List<LogListener> m_LogListeners = new ArrayList<LogListener>();

    private List<LogListener> m_LogErrorListeners = new ArrayList<LogListener>();

    private static Logger m_Instance = new Logger();

    /**
     * The Logger constructor.
    */
    public Logger() {
    }

    /**
     * This functions logs a message.
     *
     * @param   severity    The severity of the event.
     * @param   message     The message.
    */
    public void log(String severity, String message) throws InvalidActionException {
        if (m_LogListeners.isEmpty() && m_LogErrorListeners.isEmpty()) {
            throw new InvalidActionException("There are no log handlers, " + "so it is useless to log anything.");
        }
        Date timeStamp = new Date();
        for (int i = 0; i < m_LogListeners.size(); i++) {
            LogListener listener = (LogListener) m_LogListeners.get(i);
            listener.logEvent(severity, message, timeStamp);
        }
        if (severity.equals(LOG_ERROR)) {
            for (int i = 0; i < m_LogErrorListeners.size(); i++) {
                LogListener listener = (LogListener) m_LogErrorListeners.get(i);
                listener.logEvent(severity, message, timeStamp);
            }
        }
    }

    /**
     * This function logs a message.
     *
     * @param   message     The message.
    */
    public void log(String message) throws InvalidActionException {
        if (m_LogListeners.isEmpty()) {
            throw new InvalidActionException("There are no log handlers, " + "so it is useless to log anything.");
        }
        Date timeStamp = new Date();
        for (int i = 0; i < m_LogListeners.size(); i++) {
            LogListener listener = (LogListener) m_LogListeners.get(i);
            listener.logEvent(LOG_INFORMATION, message, timeStamp);
        }
    }

    /**
     * Log an exception
     *
     * @param   exception     The exception to log.
    */
    public void logException(Exception exception) throws InvalidActionException {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            log(LOG_ERROR, sw.toString());
        } catch (InvalidActionException e) {
            throw e;
        }
    }

    /**
     * Method to register a listener for the log.
     *
     * @param   listener    The listener.
    */
    public void addListener(LogListener listener) {
        m_LogListeners.add(listener);
    }

    /**
     * Method to unregister a listener for the log.
     *
     * @param   listener    The listener to remove.
    */
    public void removeListener(LogListener listener) {
        m_LogListeners.remove(listener);
    }

    /**
     * Add a listener for errors only. The normal listener is also notified.
     *
     * @param   listener    The error log listener.
    */
    public void addErrorListener(LogListener listener) {
        m_LogErrorListeners.add(listener);
    }

    /**
     * Remove a listener for errors only.
     *
     * @param   listener    The error listener to remove.
    */
    public void removeErrorListener(LogListener listener) {
        m_LogErrorListeners.remove(listener);
    }

    /**
     * Get the instance (singleton)
     *
     * @return  This class.
    */
    public static Logger getInstance() {
        return m_Instance;
    }
}
