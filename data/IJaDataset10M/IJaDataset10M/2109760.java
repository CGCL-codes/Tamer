package de.searchworkorange.indexcrawler.remoteCommandServ.exception;

public class UnknownReceivedObjectFromQueueException extends Exception {

    /**
     *
     * @param cause
     */
    public UnknownReceivedObjectFromQueueException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public UnknownReceivedObjectFromQueueException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public UnknownReceivedObjectFromQueueException(String message) {
        super(message);
    }

    public UnknownReceivedObjectFromQueueException() {
    }
}
