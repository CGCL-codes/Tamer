package ch.iserver.ace.collaboration.jupiter.server.serializer;

import ch.iserver.ace.collaboration.jupiter.server.Forwarder;
import ch.iserver.ace.util.ParameterValidator;

/**
 *
 */
public class LeaveCommand implements SerializerCommand {

    /**
	 * 
	 */
    private final int participantId;

    /**
	 * 
	 */
    private final int reason;

    /**
	 * @param participantId
	 * @param reason
	 */
    public LeaveCommand(int participantId, int reason) {
        ParameterValidator.notNegative("participantId", participantId);
        this.participantId = participantId;
        this.reason = reason;
    }

    /**
	 * @see ch.iserver.ace.collaboration.jupiter.server.serializer.SerializerCommand#execute(ch.iserver.ace.collaboration.jupiter.server.Forwarder)
	 */
    public void execute(Forwarder forwarder) {
        forwarder.sendParticipantLeft(participantId, reason);
    }
}
