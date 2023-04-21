package net.excel.report;

/**
 * Abstract wrapper class for the logging interface of choice. The methods
 * declared here are the same as those for the log4j
 */
public abstract class Logger {

    private static Logger logger = null;

    /**
     * Factory method to return the logger
     */
    public static final Logger getLogger(Class cl) {
        if (logger == null) {
            initializeLogger();
        }
        return logger.getLoggerImpl(cl);
    }

    /**
     * Initializes the logger in a thread safe manner
     */
    private static synchronized void initializeLogger() {
        if (logger != null) {
            return;
        }
        String loggerName = "";
        try {
            loggerName = System.getProperty("logger");
            Object log = null;
            if ((log = getLoggerInstances(loggerName)) == null) {
                loggerName = ReportEngine.getLogger();
                log = getLoggerInstances(loggerName);
            }
            if (null != log) {
                logger = (Logger) log;
            } else {
                loadDefaultLogger();
            }
        } finally {
        }
    }

    private static Object getLoggerInstances(String className) {
        Object log = null;
        if (null != className) {
            try {
                log = Class.forName(className).newInstance();
            } catch (InstantiationException e) {
                ;
            } catch (IllegalAccessException e) {
                ;
            } catch (ClassNotFoundException e) {
                ;
            }
            if (!(log instanceof Logger)) {
                log = null;
            }
        }
        return log;
    }

    private static void loadDefaultLogger() {
        logger = new net.excel.report.SimpleLogger();
        logger.warn("Could not instantiate logger, using default.(SimpleLogger)");
    }

    /**
     * Constructor
     */
    protected Logger() {
    }

    /**
     * Log a debug message
     */
    public abstract void debug(Object message);

    /**
     * Log a debug message and exception
     */
    public abstract void debug(Object message, Throwable t);

    /**
     * Log an error message
     */
    public abstract void error(Object message);

    /**
     * Log an error message object and exception
     */
    public abstract void error(Object message, Throwable t);

    /**
     * Log a fatal message
     */
    public abstract void fatal(Object message);

    /**
     * Log a fatal message and exception
     */
    public abstract void fatal(Object message, Throwable t);

    /**
     * Log an information message
     */
    public abstract void info(Object message);

    /**
     * Logs an information message and an exception
     */
    public abstract void info(Object message, Throwable t);

    /**
     * Log a warning message object
     */
    public abstract void warn(Object message);

    /**
     * Log a warning message with exception
     */
    public abstract void warn(Object message, Throwable t);

    /**
     * Accessor to the logger implementation
     */
    protected abstract Logger getLoggerImpl(Class cl);

    /**
     * Empty implementation of the suppressWarnings. Subclasses may or may not
     * override this method. This method is included primarily for backwards
     * support of the jxl.nowarnings property, and is used only by the
     * SimpleLogger
     * 
     * @param w
     *            suppression flag
     */
    public void setSuppressWarnings(boolean w) {
    }

    public boolean isDebugEnable() {
        return false;
    }

    public boolean isInfoEnable() {
        return false;
    }
}
