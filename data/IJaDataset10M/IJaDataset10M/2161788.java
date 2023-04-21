package com.sshtools.j2ssh.transport;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;

/**
 * <p>
 * This class implements the payload portion each message sent by the transport
 * protocol. Each message consists of an integer message id followed by a
 * variable byte array containing message data.
 * </p>
 *
 * @author Lee David Painter
 * @version $Revision: 1.21 $
 *
 * @since 0.2.0
 */
public abstract class SshMessage {

    private int messageId;

    /**
     * <p>
     * Contructs the message.
     * </p>
     *
     * @param messageId the id of the message
     *
     * @since 0.2.0
     */
    public SshMessage(int messageId) {
        this.messageId = messageId;
    }

    /**
     * <p>
     * Returns the id of the message
     * </p>
     *
     * @return an integer message id
     *
     * @since 0.2.0
     */
    public final int getMessageId() {
        return messageId;
    }

    /**
     * <p>
     * Returns the name of the message implementation for debugging purposes.
     * </p>
     *
     * @return the name of the message e.g. "SSH_MSG_DISCONNECT"
     *
     * @since 0.2.0
     */
    public abstract String getMessageName();

    /**
     * <p>
     * Format the message into the payload array for sending by the transport
     * protocol. This implementation creates a byte array, writes the  message
     * id and calls the abstract <code>constructByteArray</code>.
     * </p>
     *
     * @return the payload portion of a transport protocol message
     *
     * @throws InvalidMessageException if the message is invalid
     *
     * @since 0.2.0
     */
    public final byte[] toByteArray() throws InvalidMessageException {
        ByteArrayWriter baw = new ByteArrayWriter();
        baw.write(messageId);
        constructByteArray(baw);
        return baw.toByteArray();
    }

    /**
     * <p>
     * Initializes the message from a byte array.
     * </p>
     *
     * @param data the byte array being read.
     *
     * @throws InvalidMessageException if the message is invalid
     *
     * @since 0.2.0
     */
    protected final void fromByteArray(ByteArrayReader data) throws InvalidMessageException {
        data.skip(5);
        int id = data.read();
        if (id != messageId) {
            throw new InvalidMessageException("The message id " + String.valueOf(id) + " is not the same as the message implementation id " + String.valueOf(messageId));
        }
        constructMessage(data);
    }

    /**
     * <p>
     * Helper method to extract the message id from the complete message data
     * recieved by the transport protocol.
     * </p>
     *
     * @param msgdata the transport protocol message
     *
     * @return the id of the message
     *
     * @since 0.2.0
     */
    public static Integer getMessageId(byte[] msgdata) {
        return new Integer(msgdata[5]);
    }

    /**
     * <p>
     * Message implementations should implement this method, writing the data
     * as exected in the transport protocol message format.
     * </p>
     *
     * @param baw the byte array being written to
     *
     * @throws InvalidMessageException if the message is invalid
     *
     * @since 0.2.0
     */
    protected abstract void constructByteArray(ByteArrayWriter baw) throws InvalidMessageException;

    /**
     * <p>
     * Message implementation should implement this method, reading the data as
     * expected in the transport protocol message format.
     * </p>
     *
     * @param bar the byte array being read
     *
     * @throws InvalidMessageException if the message is invalid
     *
     * @since 0.2.0
     */
    protected abstract void constructMessage(ByteArrayReader bar) throws InvalidMessageException;
}
