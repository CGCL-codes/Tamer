package org.mandarax.util.logging;

/**
 * Logger interface. Useful to wrap similar logger concepts and keep mandarax independent from
 * the actual log mechanism in use. Implementation wraps log frameowrks like the JDK (1.4 and later)
 * log api or apache log4j.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 2.3.1
 */
public interface Logger {

    /**
	 * Log an error.
	 * @param message a message
	 * @param exception an exception or error
	 */
    public void error(String message, Throwable exception);

    /**
	 * Log a warning.
	 * @param message a message
	 * @param exception an exception or error
	 */
    public void warn(String message, Throwable exception);

    /**
	 * Log an info message.
	 * @param message a message
	 * @param exception an exception or error
	 */
    public void info(String message, Throwable exception);

    /**
	 * Log a debug message.
	 * @param message a message
	 * @param exception an exception or error
	 */
    public void debug(String message, Throwable exception);

    /**
	 * Log an error.
	 * @param message a message
	 */
    public void error(String message);

    /**
	 * Log a warn message.
	 * @param message a message
	 */
    public void warn(String message);

    /**
	 * Log an info message.
	 * @param message a message
	 */
    public void info(String message);

    /**
	 * Log a debug message.
	 * @param message a message
	 */
    public void debug(String message);

    /**
	 * Indicates whether logging on the DEBUG level is enabled.
	 * @return a boolean
	 */
    public boolean isDebugEnabled();

    /**
	 * Indicates whether logging on the INFO level is enabled.
	 * @return a boolean
	 */
    public boolean isInfoEnabled();
}
