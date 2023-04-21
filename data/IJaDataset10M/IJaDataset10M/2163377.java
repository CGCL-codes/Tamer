package jmelib.logger;

/**
 * @author Dmitry Shyshkin
 *         Date: 6/5/2007 13:59:24
 */
public class Logger {

    /**
     * debug information
     */
    public static final int DEBUG = 0;

    /**
     * warnings
     */
    public static final int WARN = 1;

    /**
     * errors
     */
    public static final int ERROR = 2;

    private Appender appender;

    private String name;

    Logger(String name, Appender appender) {
        this.name = name;
        this.appender = appender;
    }

    /**
     * Log message at {@link #DEBUG} level
     * @param message message
     */
    public void log(String message) {
        log(DEBUG, message);
    }

    /**
     * Log error ad {@link #ERROR} level
     * @param message message
     * @param t error
     */
    public void log(String message, Throwable t) {
        log(ERROR, message, t);
    }

    /**
     * Log error at specified level
     * @param level logging level
     * @param message message
     * @param t error
     */
    public void log(int level, String message, Throwable t) {
        appender.append(new LogEntry(level, name, message, t));
    }

    /**
     * Log message at specified level
     * @param level logging level
     * @param message message
     */
    public void log(int level, String message) {
        appender.append(new LogEntry(level, name, message));
    }

    public void error(String message) {
        log(ERROR, message);
    }

    public void error(String message, Throwable t) {
        log(ERROR, message, t);
    }

    public void debug(String message) {
        log(DEBUG, message);
    }

    public void debug(String message, Throwable t) {
        log(DEBUG, message, t);
    }
}
