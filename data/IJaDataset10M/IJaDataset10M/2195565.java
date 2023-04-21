package rice.scribe.messaging;

import rice.pastry.*;
import rice.pastry.security.*;
import rice.pastry.routing.*;
import rice.pastry.messaging.*;
import rice.scribe.*;
import rice.scribe.security.*;
import rice.scribe.maintenance.*;
import java.io.*;

/**
 *
 * MessageSubscribe is used whenever a Scribe node wants to subscribe itself 
 * to a topic.
 * 
 * @version $Id: MessageSubscribe.java,v 1.1.1.1 2003/06/17 21:10:46 egs Exp $ 
 * 
 * @author Romer Gil 
 * @author Eric Engineer
 */
public class MessageSubscribe extends ScribeMessage implements Serializable {

    /**
     * Constructor
     *
     * @param addr the address of the scribe receiver.
     * @param source the node generating the message.
     * @param tid the topic to which this message refers to.
     * @param c the credentials associated with the mesasge.
     */
    public MessageSubscribe(Address addr, NodeHandle source, NodeId tid, Credentials c) {
        super(addr, source, tid, c);
    }

    /**
     * This method is called whenever the scribe node receives a message for 
     * itself and wants to process it. The processing is delegated by scribe 
     * to the message.
     * 
     * @param scribe the scribe application.
     * @param topic the topic within the scribe application.
     */
    public void handleDeliverMessage(Scribe scribe, Topic topic) {
    }

    /**
     * This method is called whenever the scribe node forwards a message in 
     * the scribe network. The processing is delegated by scribe to the 
     * message.
     * 
     * @param scribe the scribe application.
     * @param topic the topic within the scribe application.
     *
     * @return true if the message should be routed further, false otherwise.
     */
    public boolean handleForwardMessage(Scribe scribe, Topic topic) {
        NodeId topicId = m_topicId;
        NodeHandle nhandle = m_source;
        Credentials cred = scribe.getCredentials();
        SendOptions opt = scribe.getSendOptions();
        if (!scribe.getSecurityManager().verifyCanSubscribe(m_source, m_topicId)) {
            return false;
        }
        if (m_source.getNodeId().equals(scribe.getNodeId())) {
            return true;
        } else {
        }
        if (topic == null) {
            topic = new Topic(topicId, scribe);
            topic.setState(Topic.JOINING);
            topic.addToScribe();
            ScribeMessage msg = scribe.makeSubscribeMessage(m_topicId, cred);
            topic.postponeParentHandler();
            msg.setData((Serializable) this.getData());
            scribe.routeMsg(m_topicId, msg, cred, opt);
            scribe.notifyScribeObservers(m_topicId);
        }
        topic.addChild(nhandle, this);
        return false;
    }

    public String toString() {
        return new String("SUBSCRIBE MSG:" + m_source);
    }
}
