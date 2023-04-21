package org.ofbiz.workflow;

/**
 * InvalidResource Workflow Exception
 */
public final class InvalidResource extends WfException {

    public InvalidResource() {
    }

    public InvalidResource(String msg) {
        super(msg);
    }

    public InvalidResource(String msg, Throwable nested) {
        super(msg, nested);
    }
}
