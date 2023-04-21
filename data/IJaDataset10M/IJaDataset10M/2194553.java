package org.mobicents.ssf.flow.engine;

import org.mobicents.ssf.flow.SipFlowRuntimeException;

public class ActionException extends SipFlowRuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ActionException(Action action, Throwable t) {
        super(action.toString(), t);
    }
}
