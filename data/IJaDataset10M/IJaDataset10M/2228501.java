package net.sf.sanity4j.util;

/**
 * This exception can be thrown to indicate a failure in the QA process. 
 * 
 * @author Yiannis Paschalidis
 * @since Sanity4J 1.0
 */
public class QAException extends RuntimeException {

    /**
     * Creates a QAException with the specified message.
     * 
     * @param msg the message.
     */
    public QAException(final String msg) {
        super(msg);
    }

    /**
     * Creates a QAException with the specified message and cause.
     * 
     * @param msg the message.
     * @param throwable the cause of the exception.
     */
    public QAException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }

    /**
     * Creates a QAException with the specified cause.
     * 
     * @param throwable the cause of the exception.
     */
    public QAException(final Throwable throwable) {
        super(throwable);
    }
}
