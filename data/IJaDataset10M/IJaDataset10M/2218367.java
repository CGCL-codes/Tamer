package com.ixora.rms.exception;

import com.ixora.rms.EntityId;
import com.ixora.rms.messages.Msg;

/**
 * @author Daniel Moraru
 */
public final class ProviderOverlappingAgentEntity extends RMSException {

    private static final long serialVersionUID = -3257585284546331361L;

    /**
     * Constructor.
     * @param eid
     */
    public ProviderOverlappingAgentEntity(EntityId eid) {
        super(Msg.RMS_ERROR_PROVIDER_OVERLAPPING_AGENT_ENTITY, new String[] { eid.toString() });
    }
}
