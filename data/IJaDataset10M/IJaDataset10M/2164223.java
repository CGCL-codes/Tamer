package de.dermoba.srcp.devices;

import java.util.UUID;

/**
 * Interface for Listeners of CRCF Messages.
 * 
 * @author Michael Oppenauer 03.02.2009
 * 
 */
public interface CRCFInfoListener {

    /**
     * If the Listener is registered at the InfoChannel, then this Method is
     * called if an Message with the Method GET is received. The answer must be
     * an CRCF INFO Message, if you can deliver an attribute_value for this
     * attribute.
     * 
     * @param timestamp
     *            Message time stamp
     * @param bus
     *            SRCP Bus for GM, should be 0
     * @param sendTo
     *            INFO Channel the message was send to
     * @param replyTo
     *            INFO Channel to which you should reply
     * @param actor
     *            name of the CRCF actor
     * @param actor_id
     *            id of the CRCF actor
     * @param attribute
     *            name of the CRCF attribute which belongs to the actor
     */
    public void CRCFget(double timestamp, int bus, int sendTo, int replyTo, String actor, UUID actor_id, String attribute);

    /**
     * If the Listener is registered at the InfoChannel, then this Method is
     * called if an Message with the Method SET is received. The method has to
     * set the attribute to the new attribute_value.
     * 
     * @param timestamp
     *            Message time stamp
     * @param bus
     *            SRCP Bus for GM, should be 0
     * @param sendTo
     *            INFO Channel the message was send to
     * @param replyTo
     *            INFO Channel to which you should reply
     * @param actor
     *            name of the CRCF actor
     * @param actor_id
     *            id of the CRCF actor
     * @param attribute
     *            name of the CRCF attribute which belongs to the actor
     * @param attribute_value
     *            new value for the attribute
     */
    public void CRCFset(double timestamp, int bus, int sendTo, int replyTo, String actor, UUID actor_id, String attribute, String attribute_value);

    /**
     * If the Listener is registered at the InfoChannel, then this Method is
     * called if an Message with the Method INFO is received. It informs about
     * the value the attribute has.
     * 
     * @param timestamp
     *            Message time stamp
     * @param bus
     *            SRCP Bus for GM, should be 0
     * @param sendTo
     *            INFO Channel the message was send to
     * @param replyTo
     *            INFO Channel to which you should reply
     * @param actor
     *            name of the CRCF actor
     * @param actor_id
     *            id of the CRCF actor
     * @param attribute
     *            name of the CRCF attribute which belongs to the actor
     * @param attribute_value
     *            new value for the attribute
     */
    public void CRCFinfo(double timestamp, int bus, int sendTo, int replyTo, String actor, UUID actor_id, String attribute, String attribute_value);

    /**
     * If the Listener is registered at the InfoChannel, then this Method is
     * called if an Message with the Method LIST is received. The first reply
     * must be the count of the in <attribute> specified items. The reply is
     * INFO <attribute>COUNT <count>. Then method has to deliver all attributes
     * each as separate INFO messages and if they have an id then also their id
     * as attribute_value of the INFO message.
     * 
     * @param timestamp
     *            Message time stamp
     * @param bus
     *            SRCP Bus for GM, should be 0
     * @param sendTo
     *            INFO Channel the message was send to
     * @param replyTo
     *            INFO Channel to which you should reply
     * @param actor
     *            name of the CRCF actor
     * @param actor_id
     *            id of the CRCF actor
     * @param attribute
     *            name of the CRCF attribute which belongs to the actor
     */
    public void CRCFlist(double timestamp, int bus, int sendTo, int replyTo, String actor, UUID actor_id, String attribute);
}
