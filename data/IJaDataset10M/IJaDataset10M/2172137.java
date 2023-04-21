package org.ombu.participant;

import org.ombu.model.CoordinationContext;

public abstract class ParticipantManager {

    /**
	 * Registers a new participant with the coordinator.
	 * @return
	 */
    public long registerParticipant(CoordinationContext context) {
        return 0L;
    }
}
