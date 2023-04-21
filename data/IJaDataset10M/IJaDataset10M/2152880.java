package org.easypeas.exceptions;

import org.easypeas.invocation.Invoker;

/**
 * Exception thrown when an Invoker cannot be found, that was defined by the
 * System property
 * 
 * @author S Owen
 */
public class InvokerNotFoundException extends Exception {

    private static final long serialVersionUID = -6146512073410538593L;

    private final Invoker defaultInvoker;

    /**
	 * Instantiates a new invoker not found exception, taking a message to
	 * explain the error and the default invoker to use as an alternative.
	 * 
	 * @param msg
	 *            the message
	 * @param invoker
	 *            the default invoker
	 */
    public InvokerNotFoundException(String msg, Invoker invoker) {
        super(msg);
        this.defaultInvoker = invoker;
    }

    /**
	 * @return the default invoker as an alternative to the one not found.
	 */
    public Invoker getDefaultInvoker() {
        return defaultInvoker;
    }
}
