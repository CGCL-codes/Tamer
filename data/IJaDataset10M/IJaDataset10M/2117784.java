package org.freelords.network.update;

import java.util.UUID;
import org.freelords.network.client.Session;

/**
 * This implements the server response after that a chat command is sent from a client.
 * The update is sent to the relevant clients and owns the chat message and the
 * id of the sender client.
 */
public class ChatUpdate implements Update {

    private String senderId;

    private String message;

    /** No-args constructor required by kryonet. */
    private ChatUpdate() {
    }

    /**
	 * Creates a ChatUpdate.
	 * @param message The chat message.
	 * @param senderId The id of the sender client.
	 */
    public ChatUpdate(String message, UUID senderId) {
        this.message = message;
        this.senderId = senderId.toString();
    }

    public String getMessage() {
        return message;
    }

    public UUID getSenderId() {
        return UUID.fromString(senderId);
    }

    /**
	 * This would update the Session object
	 *
	 * @param session The session to be updated with new data.
	 */
    @Override
    public void updateSession(Session session) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " senderId:" + senderId + "\n" + "message:" + message;
    }
}
