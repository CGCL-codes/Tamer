package net.sourceforge.entrainer.neurosky.util;

import static java.text.MessageFormat.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class LogUtils {

    public static void trace(Logger log, String pattern, Object... parms) {
        if (!log.isTraceEnabled()) return;
        log.trace(format(pattern, parms));
    }

    public static void debug(Logger log, String pattern, Object... parms) {
        if (!log.isDebugEnabled()) return;
        log.debug(format(pattern, parms));
    }

    public static void info(Logger log, String pattern, Object... parms) {
        if (!log.isInfoEnabled()) return;
        log.info(format(pattern, parms));
    }

    @SuppressWarnings("deprecation")
    public static void warn(Logger log, String pattern, Object... parms) {
        if (!log.isEnabledFor(Priority.WARN)) return;
        log.warn(format(pattern, parms));
    }

    @SuppressWarnings("deprecation")
    public static void warn(Logger log, String pattern, Throwable e, Object... parms) {
        if (!log.isEnabledFor(Priority.WARN)) return;
        log.warn(format(pattern, parms), e);
    }

    @SuppressWarnings("deprecation")
    public static void error(Logger log, String pattern, Object... parms) {
        if (!log.isEnabledFor(Priority.ERROR)) return;
        log.error(format(pattern, parms));
    }

    @SuppressWarnings("deprecation")
    public static void error(Logger log, String pattern, Throwable e, Object... parms) {
        if (!log.isEnabledFor(Priority.ERROR)) return;
        log.error(format(pattern, parms), e);
    }
}
