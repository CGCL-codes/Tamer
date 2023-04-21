package com.ixora.rms.exception;

import com.ixora.rms.messages.Msg;

/**
 * AgentNotStarted.
 * @author Daniel Moraru
 */
public final class InvalidAgentPackage extends InvalidAgentState {

    private static final long serialVersionUID = -7022205743877151179L;

    /**
     * Constructor.
     */
    public InvalidAgentPackage() {
        super(Msg.RMS_ERROR_INVALID_AGENT_PACKAGE);
    }
}
