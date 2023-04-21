package org.dita.dost.log;

/**
 * Common logging interface.
 */
public interface DITAOTLogger {

    /**
     * Log an information message.
     * 
     * @param msg message
     */
    public void logInfo(final String msg);

    /**
     * Log a warning message.
     * 
     * @param msg message
     */
    public void logWarn(final String msg);

    /**
     * Log an error message.
     * 
     * @param msg message
     */
    public void logError(final String msg);

    /**
     * Log a fatal error message.
     * 
     * @param msg message
     */
    public void logFatal(final String msg);

    /**
     * Log a debug message.
     * 
     * @param msg message
     */
    public void logDebug(final String msg);

    /**
     * Log an exception.
     * 
     * @param t exception
     */
    public void logException(final Throwable t);
}
