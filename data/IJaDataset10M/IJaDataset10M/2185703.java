package com.hack23.cia.web.api.content;

import com.hack23.cia.model.api.application.events.ExternalUrlOperationType;
import com.hack23.cia.web.api.events.AbstractUserAction;

/**
 * The Class ExternalUrlAction.
 */
public class ExternalUrlAction extends AbstractUserAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The operation. */
    private final ExternalUrlOperationType operation;

    /**
	 * Instantiates a new external url action.
	 * 
	 * @param operation the operation
	 */
    public ExternalUrlAction(final ExternalUrlOperationType operation) {
        super();
        this.operation = operation;
    }

    /**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
    public final ExternalUrlOperationType getOperation() {
        return operation;
    }
}
