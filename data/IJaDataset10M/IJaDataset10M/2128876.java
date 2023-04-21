package ch.iserver.ace.collaboration.jupiter.server.serializer;

import ch.iserver.ace.collaboration.jupiter.server.Forwarder;
import ch.iserver.ace.collaboration.jupiter.server.ServerLogic;
import ch.iserver.ace.collaboration.jupiter.server.SessionParticipant;
import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 *
 */
public class JoinCommand implements SerializerCommand {

    private final SessionParticipant participant;

    private final ServerLogic logic;

    public JoinCommand(SessionParticipant participant, ServerLogic logic) {
        ParameterValidator.notNull("participant", participant);
        ParameterValidator.notNull("logic", logic);
        this.participant = participant;
        this.logic = logic;
    }

    protected RemoteUserProxy getUserProxy() {
        return participant.getUserProxy();
    }

    protected int getParticipantId() {
        return participant.getParticipantId();
    }

    protected ParticipantConnection getConnection() {
        return participant.getParticipantConnection();
    }

    /**
	 * @see ch.iserver.ace.collaboration.jupiter.server.serializer.SerializerCommand#execute(ch.iserver.ace.collaboration.jupiter.server.Forwarder)
	 */
    public void execute(Forwarder forwarder) {
        ParticipantConnection connection = getConnection();
        PortableDocument document = logic.getDocument();
        connection.sendDocument(document);
        logic.addParticipant(participant);
        forwarder.sendParticipantJoined(getParticipantId(), getUserProxy());
    }
}
