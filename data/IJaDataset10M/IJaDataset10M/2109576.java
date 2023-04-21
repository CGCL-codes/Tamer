package org.jactr.eclipse.remote.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ecf.core.identity.ID;

public class ControlRequest extends AbstractMessage {

    /**
   * 
   */
    private static final long serialVersionUID = 6399299449692794025L;

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(ControlRequest.class);

    private final boolean _isAbortRequest;

    private final ID _runID;

    /**
   * if abort is true, request abort, otherwise request cancel
   */
    public ControlRequest(ID sender, ID runID, boolean abort) {
        super(sender);
        _runID = runID;
        _isAbortRequest = abort;
    }

    public ID getRunID() {
        return _runID;
    }

    public boolean shouldAbort() {
        return _isAbortRequest;
    }
}
