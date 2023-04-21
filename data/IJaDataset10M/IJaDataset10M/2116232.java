package com.xavax.jsf.util;

/**
 * MessageQueueEntry stores the message and cause for an entry in the
 * queue of error messages.
 */
public class MessageQueueEntry {

    /**
   * Construct a MessageQueueEntry.
   */
    public MessageQueueEntry() {
        this.message = null;
        this.cause = null;
    }

    /**
   * Construct a MessageQueueEntry with the specified message and cause.
   *
   * @param message  a string containing the error message.
   * @param cause    the exception that caused the error.
   */
    public MessageQueueEntry(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    /**
   * Returns the message for this queue entry.
   *
   * @return the message for this queue entry.
   */
    public String getMessage() {
        return this.message;
    }

    /**
   * Sets the message for this queue entry.
   *
   * @param message the new error message for this queue entry.
   */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
   * Returns the cause for this queue entry.
   *
   * @return the cause for this queue entry.
   */
    public Throwable getCause() {
        return this.cause;
    }

    /**
   * Sets the cause for this queue entry.
   *
   * @param cause the new cause for this queue entry.
   */
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    private String message;

    private Throwable cause;
}
