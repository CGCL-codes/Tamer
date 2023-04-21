package rice.pastry.messaging;

import rice.pastry.security.Credentials;
import java.io.*;
import java.util.*;
import rice.pastry.*;

/**
 * This is an abstract implementation of a message object.
 * 
 * @version $Id: Message.java,v 1.1.1.1 2003/06/17 21:10:41 egs Exp $
 *
 * @author Andrew Ladd
 * @author Sitaram Iyer
 */
public abstract class Message implements Serializable {

    private Address destination;

    private transient Credentials credentials;

    private transient Date theStamp;

    private NodeId senderId;

    private transient ObjectInputStream stream;

    /**
     * Gets the address of message receiver that the message is for.
     *
     * @return the destination id.
     */
    public Address getDestination() {
        return destination;
    }

    /**
     * Gets the credentials of the sender.
     *
     * @return credentials or null if the sender has no credentials.
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Gets the timestamp of the message, if it exists.
     *
     * @return a timestamp or null if the sender did not specify one.
     */
    public Date getDate() {
        return theStamp;
    }

    /**
     * Get sender Id.
     * 
     * @return the immediate sender's NodeId.
     */
    public NodeId getSenderId() {
        return senderId;
    }

    /**
     * Set sender Id. Called by NodeHandle just before dispatch, so that
     * this Id is guaranteed to belong to the immediate sender.
     * 
     * @param the immediate sender's NodeId.
     */
    public void setSenderId(NodeId id) {
        senderId = id;
    }

    /**
     * Get stream over which the object was deserialized. Used for indexing
     * into the LocalNode.pending hashmap. See README.handle_localnode.
     * 
     * @return the object input stream
     */
    public ObjectInputStream getStream() {
        return stream;
    }

    /**
     * If the message has no timestamp, this will stamp the message.
     *
     * @param time the timestamp.
     *
     * @return true if the message was stamped, false if the message already had 
     * a timestamp.
     */
    public boolean stamp(Date time) {
        if (theStamp.equals(null)) {
            theStamp = time;
            return true;
        } else return false;
    }

    /**
     * Constructor.
     *
     * @param dest the destination.
     */
    public Message(Address dest) {
        destination = dest;
        credentials = null;
        theStamp = null;
        senderId = null;
    }

    /**
     * Constructor.
     *
     * @param dest the destination.
     * @param cred the credentials.
     */
    public Message(Address dest, Credentials cred) {
        destination = dest;
        credentials = cred;
        theStamp = null;
        senderId = null;
    }

    /**
     * Constructor.
     *
     * @param dest the destination.
     * @param cred the credentials.
     * @param timestamp the timestamp
     */
    public Message(Address dest, Credentials cred, Date timestamp) {
        destination = dest;
        credentials = cred;
        this.theStamp = timestamp;
        senderId = null;
    }

    /**
     * Constructor.
     *
     * @param dest the destination.
     * @param timestamp the timestamp
     */
    public Message(Address dest, Date timestamp) {
        destination = dest;
        this.theStamp = timestamp;
        senderId = null;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        stream = in;
    }
}
