package org.dago.wecommand.wiimote.messages;

/**
 * BT Message interface.
 */
public interface Message {

    /**
	 * Gives the message identifier
	 * @return the message identifier
	 */
    long getId();

    /**
	 * Gives the type of message
	 * @return the message type
	 */
    MessageType getType();

    /**
	 * Gives the message as bytes
	 * @return the message bytes
	 */
    byte[] toBytes();

    /**
	 * Gives the payload buffer
	 * @return the payload buffer
	 */
    byte[] getPayload();
}
