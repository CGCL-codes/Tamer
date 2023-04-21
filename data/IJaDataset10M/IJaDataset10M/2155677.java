package com.trollworks.ttk.conduit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** The message sent between between processes using a conduit. */
public class ConduitMessage implements Runnable {

    private static final byte[] ID = { '#', 'W', 'i', 'l', 'k', 'e', 's', '!' };

    private String mUser;

    private String mID;

    private String mMessage;

    private ConduitReceiver mReceiver;

    /**
	 * Creates a new conduit message.
	 * 
	 * @param id An ID that clients will use to filter reception of messages.
	 * @param message The message.
	 */
    public ConduitMessage(String id, String message) {
        mUser = System.getProperty("user.name");
        mID = id;
        mMessage = message;
    }

    /**
	 * Creates a new conduit message by reading it in from the specified stream.
	 * 
	 * @param stream The stream to read the message from.
	 * @throws IOException if the underlying data stream throws an exception.
	 */
    public ConduitMessage(DataInputStream stream) throws IOException {
        int i = 0;
        while (i < ID.length) {
            byte value = stream.readByte();
            if (value == ID[i]) {
                i++;
            } else if (value == ID[0]) {
                i = 1;
            } else {
                i = 0;
            }
        }
        mUser = stream.readUTF();
        mID = stream.readUTF();
        mMessage = stream.readUTF();
    }

    /**
	 * Writes the message to a data output stream.
	 * 
	 * @param stream The stream to write to.
	 * @throws IOException if the stream throws an exception.
	 */
    void send(DataOutputStream stream) throws IOException {
        stream.write(ID);
        stream.writeUTF(getUser());
        stream.writeUTF(getID());
        stream.writeUTF(getMessage());
        stream.flush();
    }

    /** @param receiver The message receiver. */
    void setReceiver(ConduitReceiver receiver) {
        mReceiver = receiver;
    }

    @Override
    public void run() {
        mReceiver.conduitMessageReceived(this);
    }

    /** @return The user. */
    public String getUser() {
        return mUser;
    }

    /** @return The message ID. */
    public String getID() {
        return mID;
    }

    /** @return The message. */
    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return "[" + getUser() + " : " + getID() + "] " + getMessage();
    }
}
