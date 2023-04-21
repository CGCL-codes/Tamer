package phex.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * Proxy class for new logging.
 * @deprecated use slf4j directly
 */
@Deprecated
public class NLogger {

    /**
     * Returns a log instance.
     * @param clazz
     * @return a log instance.
     */
    private static Logger getLogInstance(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static boolean isDebugEnabled(Class<?> clazz) {
        return getLogInstance(clazz).isDebugEnabled();
    }

    public static boolean isWarnEnabled(Class<?> clazz) {
        return getLogInstance(clazz).isWarnEnabled();
    }

    public static void debug(Class<?> clazz, String message) {
        getLogInstance(clazz).debug(message);
    }

    public static void debug(Class<?> clazz, Throwable t) {
        getLogInstance(clazz).debug(t.toString(), t);
    }

    public static void debug(Class<?> clazz, Object message, Throwable t) {
        getLogInstance(clazz).debug(message.toString(), t);
    }

    public static void info(Class<?> clazz, String message) {
        getLogInstance(clazz).info(message);
    }

    public static void warn(Class<?> clazz, Object message, Throwable t) {
        getLogInstance(clazz).warn(message.toString(), t);
    }

    public static void warn(Class<?> clazz, String message) {
        getLogInstance(clazz).warn(message);
    }

    public static void warn(Class<?> clazz, Throwable t) {
        getLogInstance(clazz).warn(t.toString(), t);
    }

    public static void error(Class<?> clazz, String message) {
        getLogInstance(clazz).error(message);
    }

    public static void error(Class<?> clazz, Throwable t) {
        getLogInstance(clazz).error(t.toString(), t);
    }

    public static void error(Class<?> clazz, Object message, Throwable t) {
        getLogInstance(clazz).error(message.toString(), t);
    }
}
