package net.infian.framework.logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.infian.framework.FrameworkConstants;
import net.infian.framework.logging.logger.ConsoleLogger;
import net.infian.framework.logging.logger.Logger;
import net.infian.framework.util.PropertyUtil;

/**
 * This manages the logging.<br />
 * <br />
 * The log.properties file must be in the following form:<br />
 * <br />
 * {@code LOGGER_NAME.logger = PATH.TO.LOGGER.CLASS}<br />
 * {@code LOGGER_NAME.level = LEVEL_OF_LOGGING #optional, INFO is default}<br />
 * {@code LOGGER_NAME.ADDITIONAL_SETTINGS_WHERE_APPLICABLE = VALUE}
 */
public final class LogManager {

    private static final Map<String, Logger> loggers;

    private static final Logger defaultLogger;

    static {
        Properties logProps = PropertyUtil.getProps(FrameworkConstants.logProps);
        List<String> logList = PropertyUtil.getNamesWithAttribute(logProps, FrameworkConstants.loggerAtt);
        Logger logger;
        Thread t;
        if (logList.contains(FrameworkConstants.defaultString)) {
            loggers = new HashMap<String, Logger>(logList.size(), 1);
        } else {
            loggers = new HashMap<String, Logger>(logList.size() + 1, 1);
            loggers.put(FrameworkConstants.defaultString, new ConsoleLogger(FrameworkConstants.defaultString, logProps));
        }
        for (String name : logList) {
            try {
                logger = (Logger) Class.forName(logProps.getProperty(name + FrameworkConstants.loggerAtt)).getConstructor(String.class, Properties.class).newInstance(name, logProps);
                loggers.put(name, logger);
                t = new Thread(new LogThread(logger));
                t.setDaemon(true);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        defaultLogger = loggers.get(FrameworkConstants.defaultString);
    }

    /**
	 * This gets a Logger by name.
	 * @param name name of the Logger
	 * @return specified Logger or the "default" logger if not found
	 */
    public static final Logger getLogger(final String name) {
        Logger logger = loggers.get(name);
        if (logger == null) {
            logger = defaultLogger;
        }
        return logger;
    }

    /**
	 * This gets the "default" Logger.
	 * @return "default" Logger
	 */
    public static final Logger getLogger() {
        return defaultLogger;
    }

    /**
	 * This error logs an Exception using the specified Logger.
	 * @param name name of the Logger to use
	 * @param e Exception to be logged
	 */
    public static final void logException(final String name, final Exception e) {
        loggers.get(name).exception(e);
    }

    /**
	 * This error logs an Exception using the "default" Logger.
	 * @param e Exception to be logged
	 */
    public static final void logException(final Exception e) {
        defaultLogger.exception(e);
    }

    /**
	 * This error logs an Object using the specified Logger.
	 * @param name name of the Logger to use
	 * @param o Object to be logged
	 */
    public static final void logError(final String name, final Object o) {
        loggers.get(name).error(o);
    }

    /**
	 * This error logs an Object using the "default" Logger.
	 * @param o Object to be logged
	 */
    public static final void logError(final Object o) {
        defaultLogger.error(o);
    }

    /**
	 * This info logs an Object using the specified Logger.
	 * @param name name of the Logger to use
	 * @param o Object to be logged
	 */
    public static final void logInfo(final String name, final Object o) {
        loggers.get(name).info(o);
    }

    /**
	 * This info logs an Object using the "default" Logger.
	 * @param o Object to be logged
	 */
    public static final void logInfo(final Object o) {
        defaultLogger.info(o);
    }

    /**
	 * This debug logs an Object using the specified Logger.
	 * @param name name of the Logger to use
	 * @param o Object to be logged
	 */
    public static final void logDebug(final String name, final Object o) {
        loggers.get(name).debug(o);
    }

    /**
	 * This debug logs an Object using the "default" Logger.
	 * @param o Object to be logged
	 */
    public static final void logDebug(final Object o) {
        defaultLogger.debug(o);
    }
}
