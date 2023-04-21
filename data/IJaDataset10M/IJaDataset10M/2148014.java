package net.taylor.log;

import java.text.MessageFormat;
import org.apache.commons.logging.Log;

/**
 * This class cleans up logging by checking if the level is enabled and then
 * formating the text.
 * 
 * @author jgilbert
 * 
 */
public class Logger {

    private static String format(String text, Object... value) {
        MessageFormat format = new MessageFormat(text);
        return format.format(value);
    }

    public static void trace(Log log, String text, Object... value) {
        if (log.isTraceEnabled()) {
            log.trace(format(text, value));
        }
    }

    public static void debug(Log log, String text, Object... value) {
        if (log.isDebugEnabled()) {
            log.debug(format(text, value));
        }
    }

    public static void info(Log log, String text, Object... value) {
        if (log.isInfoEnabled()) {
            log.info(format(text, value));
        }
    }

    public static void warn(Log log, String text, Object... value) {
        if (log.isWarnEnabled()) {
            log.warn(format(text, value));
        }
    }

    public static void error(Log log, String text, Object... value) {
        if (log.isErrorEnabled()) {
            log.error(format(text, value));
        }
    }

    public static void fatal(Log log, String text, Object... value) {
        if (log.isFatalEnabled()) {
            log.fatal(format(text, value));
        }
    }

    public static void stackTrace(Log log, String text, Object... value) {
        Exception e = new Exception();
        if (log.isDebugEnabled()) {
            log.debug(format(text, value), e);
        }
    }
}
