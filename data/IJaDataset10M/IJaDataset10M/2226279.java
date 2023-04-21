package com.hack23.cia.web.action.user;

import com.hack23.cia.model.application.impl.user.AbstractTopListActionEvent.Operation;

/**
 * The Class AbstractTopListAction.
 */
public abstract class AbstractTopListAction extends AbstractParliamentAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The operation. */
    private final Operation operation;

    /**
	 * Instantiates a new abstract top list action.
	 * 
	 * @param operation
	 *            the operation
	 */
    public AbstractTopListAction(final Operation operation) {
        super();
        this.operation = operation;
    }

    /**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
    public final Operation getOperation() {
        return operation;
    }
}
