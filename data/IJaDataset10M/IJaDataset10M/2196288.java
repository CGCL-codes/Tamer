package com.actionbazaar.buslogic;

/**
 * A WorkflowOrderViolationException
 */
public class WorkflowOrderViolationException extends RuntimeException {

    /**
     * Default constructor
     */
    public WorkflowOrderViolationException() {
    }

    /**
     * Creates a new WorkflowOrderViolationException with message
     * @param msg - message
     */
    public WorkflowOrderViolationException(String msg) {
        super(msg);
    }

    /**
     * Creates a new WorkflowOrderViolationException
     * @param msg - message
     * @param cause - cause
     */
    public WorkflowOrderViolationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates a new WorkflowOrderViolationException
     * @param cause - cause
     */
    public WorkflowOrderViolationException(Throwable cause) {
        super(cause);
    }
}
