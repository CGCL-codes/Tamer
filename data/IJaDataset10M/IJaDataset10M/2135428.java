package org.ofbiz.workflow;

/**
 * InvalidState Workflow Exception
 */
public final class InvalidState extends WfException {

    public InvalidState() {
    }

    public InvalidState(String msg) {
        super(msg);
    }

    public InvalidState(String msg, Throwable nested) {
        super(msg, nested);
    }
}
