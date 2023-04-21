package fll.util;

import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Some utiities for working with logging.
 */
public final class LogUtils {

    private static final Logger LOGGER = getLogger();

    private LogUtils() {
    }

    private static boolean log4jConfigured = false;

    /**
   * Check if log4j is configured. This is done by checking a flag set by
   * {@link #initializeLogging()}.
   */
    public static boolean isLog4jConfigured() {
        return log4jConfigured;
    }

    private static boolean insideAddBufferedAppenderIfNeeded = false;

    /**
   * If the root logger doesn't have any appenders, then add a
   * {@link BufferedAppender}.
   */
    private static void addBufferedAppenderIfNeeded() {
        synchronized (LogUtils.class) {
            if (insideAddBufferedAppenderIfNeeded) {
                return;
            }
            insideAddBufferedAppenderIfNeeded = true;
            try {
                if (!isLog4jConfigured()) {
                    final Logger rootLogger = Logger.getRootLogger();
                    final List<Appender> toRemove = new LinkedList<Appender>();
                    boolean foundBufferedAppender = false;
                    final Enumeration<?> appenders = rootLogger.getAllAppenders();
                    while (appenders.hasMoreElements()) {
                        final Appender appender = (Appender) appenders.nextElement();
                        if (appender instanceof BufferedAppender) {
                            foundBufferedAppender = true;
                        } else {
                            toRemove.add(appender);
                        }
                    }
                    for (final Appender appender : toRemove) {
                        rootLogger.removeAppender(appender);
                    }
                    if (!foundBufferedAppender) {
                        setupBufferedAppender();
                    }
                }
            } finally {
                insideAddBufferedAppenderIfNeeded = false;
            }
        }
    }

    private static void setupBufferedAppender() {
        final Logger rootLogger = Logger.getRootLogger();
        rootLogger.addAppender(BufferedAppender.INSTANCE);
        rootLogger.setLevel(Level.ALL);
    }

    /**
   * Automatically gets logger for class calling this method. This method also
   * ensures that a {@link BufferedAppender} is added to the default appenders
   * until log4j is configured.
   */
    public static Logger getLogger() {
        addBufferedAppenderIfNeeded();
        final StackTraceElement[] elements = new RuntimeException().getStackTrace();
        boolean useNextElement = false;
        for (final StackTraceElement element : elements) {
            if (!useNextElement) {
                if (element.getClassName().equals(LogUtils.class.getName()) && element.getMethodName().equals("getLogger")) {
                    useNextElement = true;
                }
            } else {
                return Logger.getLogger(element.getClassName());
            }
        }
        LOGGER.warn("Cannot find logger for calling class with stack trace, returning root logger:" + Arrays.asList(elements));
        return Logger.getRootLogger();
    }

    /**
   * Find fll-log4j.xml at the root of the class path and use that to initialize
   * log4j.
   */
    public static void initializeLogging() {
        synchronized (LogUtils.class) {
            if (!log4jConfigured) {
                LogManager.resetConfiguration();
                final URL config = LogUtils.class.getResource("/fll-log4j.xml");
                DOMConfigurator.configure(config);
                log4jConfigured = true;
                BufferedAppender.INSTANCE.flushEvents();
            }
        }
    }
}
