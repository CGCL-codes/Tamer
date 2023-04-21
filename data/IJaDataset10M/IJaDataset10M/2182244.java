package net.sourceforge.ondex.webservice2.Exceptions;

import net.sourceforge.ondex.webservice2.plugins.BufferedOndexListener;
import org.apache.log4j.Logger;

/**
 *
 * @author Christian Brenninkmeijer
 */
public class CaughtException extends WebserviceException {

    /**
     * Constructs an instance of <code>CaughtException</code>
     * based on the caught exception.
     * @param msg the detail message.
     * @param logger The logger of the calling class.
     */
    public CaughtException(Exception e, Logger logger) {
        super(e.getClass().getName() + " " + e.getMessage(), logger);
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement element : trace) {
            logger.error(element.toString());
        }
    }

    /**
     * Constructs an instance of <code>CaughtException</code>
     * based on the caught exception.
     * @param msg the detail message.
     * @param logger The logger of the calling class.
     */
    public CaughtException(String message, Exception e, Logger logger) {
        super(message + " caused: " + e.getClass().getName() + " " + e.getMessage(), logger);
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement element : trace) {
            logger.error(element.toString());
        }
    }

    /**
     * Constructs an instance of <code>CaughtException</code>
     * based on the caught exception.
     * @param msg the detail message.
     * @param listener The Ondex listener to add message to
     * @param logger The logger of the calling class.
     */
    public CaughtException(Exception e, BufferedOndexListener listener, Logger logger) {
        super(e.getClass().getName() + " " + e.getMessage() + " " + listener.getCompleteEventHistory(), logger);
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement element : trace) {
            logger.error(element.toString());
        }
        logger.error(listener.getCompleteEventHistory());
    }

    /**
     * Constructs an instance of <code>CaughtException</code>
     * based on the caught exception.
     * @param msg the detail message.
     * @param e The exception caught
     * @param listener The Ondex listener to add message to
     * @param logger The logger of the calling class.
     */
    public CaughtException(String message, Exception e, BufferedOndexListener listener, Logger logger) {
        super(message + " caused: " + e.getClass().getName() + " " + e.getMessage() + " " + listener.getCompleteEventHistory(), logger);
        StackTraceElement[] trace = e.getStackTrace();
        logger.error(listener.getCompleteEventHistory());
        for (StackTraceElement element : trace) {
            logger.error(element.toString());
        }
        logger.error(listener.getCompleteEventHistory());
    }
}
