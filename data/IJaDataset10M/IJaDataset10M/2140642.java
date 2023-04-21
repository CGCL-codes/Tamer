package me.fnr.lcd_api.infrastructure;

/**
 * This singleton class represents the main configuration of the API. Its values
 * can be set by the utilizing classes through the setters. Generally any
 * program making use of the API can handle all these values in its own 
 * configuration, and set the instance of this class to match thus controlling
 * the API directly.
 * 
 * @author Robert Derelanko
 */
public class APIConfig {

    /**
	 * Keep reference to a singleton instance of this class.
	 */
    private static APIConfig instance = null;

    /**
	 * This field represents the level of logging output of the API. The higher
	 * the value means the more information is logged.
	 * 
	 * 0 = Logging Disabled
	 * 1 = Errors only
	 * 2 = Errors & Warnings
	 * 3 = Errors, Warnings & Info
	 * 4+ = All Levels
	 */
    private int logLevel = 4;

    /**
	 * When performing a solicited read on a socket, this represents the
	 * number of milliseconds to wait before timing out.
	 */
    private static long dataReadTimeoutInterval = 1000;

    /**
	 * When performing solicited reads on a socket, this represents the
	 * number of milliseconds the thread will sleep between queries on the
	 * read socket. If this value is too small, the CPU may be tied up for
	 * the full amount of time before dataReadTimeout occurs. If set too large,
	 * performance of the read may suffer.
	 */
    private static long dataReadSleepInterval = 10;

    /**
	 * Blocking Constructor - Should never be called directly, use getInstance
	 * instead.
	 */
    protected APIConfig() {
    }

    /**
	 * Returns an instance of the singleton configuration object.
	 * @return The one and only instance of the configuration singleton.
	 */
    public static APIConfig getInstance() {
        if (instance == null) {
            instance = new APIConfig();
        }
        return instance;
    }

    /**
	 * This method allows the main log level of the API to be set. The higher
	 * the value means the more information is logged.
	 * 
	 * 0 = Logging Disabled
	 * 1 = Errors only
	 * 2 = Errors & Warnings
	 * 3 = Errors, Warnings & Info
	 * 4+ = All Levels
	 * 
	 * @param theLogLevel The log level as an int.
	 */
    public void setLogLevel(int theLogLevel) {
        if (theLogLevel >= 0) {
            logLevel = theLogLevel;
        }
    }

    /**
	 * Method used to determine if ERROR level logging is enabled.
	 * @return True if ERROR logging is enabled, and False otherwise.
	 */
    public boolean isErrorLoggingEnabled() {
        return logLevel >= 1 ? true : false;
    }

    /**
	 * Method used to determine if WARN level logging is enabled.
	 * @return True if WARN logging is enabled, and False otherwise.
	 */
    public boolean isWarningLoggingEnabled() {
        return logLevel >= 2 ? true : false;
    }

    /**
	 * Method used to determine if INFO level logging is enabled.
	 * @return True if INFO logging is enabled, and False otherwise.
	 */
    public boolean isInfoLoggingEnabled() {
        return logLevel >= 3 ? true : false;
    }

    /**
	 * Method used to determine if DEBUG level logging is enabled.
	 * @return True if DEBUG logging is enabled, and False otherwise.
	 */
    public boolean isDebugLoggingEnabled() {
        return logLevel >= 4 ? true : false;
    }

    /**
	 * This method will indicate whether any level of logging is enabled.
	 * @return True if any level of logging is enabled, and False otherwise.
	 */
    public boolean isAnyLoggingEnabled() {
        return logLevel > 0 ? true : false;
    }

    /**
	 * When performing a solicited read on a socket, this represents the
	 * number of milliseconds to wait before timing out.
	 */
    public long getDataReadTimeoutInterval() {
        return dataReadTimeoutInterval;
    }

    /**
	 * This method allows the DataReadTimeoutInterval to be set.
	 * When performing a solicited read on a socket, this represents the
	 * number of milliseconds to wait before timing out.
	 */
    public void setDataReadTimeoutInterval(long theTimeoutInterval) {
        dataReadTimeoutInterval = theTimeoutInterval;
    }

    /**
	 * When performing solicited reads on a socket, this represents the
	 * number of milliseconds the thread will sleep between queries on the
	 * read socket. If this value is too small, the CPU may be tied up for
	 * the full amount of time before dataReadTimeout occurs. If set too large,
	 * performance of the read may suffer.
	 * @return The amount of time in milliseconds as a Long.
	 */
    public long getDataReadSleepInterval() {
        return dataReadSleepInterval;
    }

    /**
	 * This method allows the DataReadSleepInterval to be set.
	 * When performing solicited reads on a socket, this represents the
	 * number of milliseconds the thread will sleep between queries on the
	 * read socket. If this value is too small, the CPU may be tied up for
	 * the full amount of time before dataReadTimeout occurs. If set too large,
	 * performance of the read may suffer.
	 * @return The amount of time in milliseconds as a Long.
	 */
    public void setDataReadSleepInterval(long theDataReadSleepInterval) {
        dataReadSleepInterval = theDataReadSleepInterval;
    }
}
