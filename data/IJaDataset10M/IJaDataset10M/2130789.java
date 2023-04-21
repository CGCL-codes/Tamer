package rice.scribe.messaging;

import rice.pastry.*;
import rice.pastry.security.*;
import rice.pastry.routing.*;
import rice.pastry.messaging.*;
import rice.scribe.*;
import rice.scribe.security.*;
import rice.scribe.maintenance.*;
import java.io.*;
import java.util.*;

/**
 *
 * MessagePublish is used whenever a Scribe nodes wishes to send events 
 * to a particular topic. The PublishMessage takes care of forwarding itself
 * to all the nodes in the topic's multicast tree.
 * 
 * @version $Id: MessagePublish.java,v 1.1.1.1 2003/06/17 21:10:46 egs Exp $ 
 *
 * @author Romer Gil 
 * @author Eric Engineer
 * @author Atul Singh
 * @author Animesh Nandi
 */
public class MessagePublish extends ScribeMessage implements Serializable {

    /**
     * Constructor
     *
     * @param addr the address of the scribe receiver.
     * @param source the node generating the message.
     * @param topicId the topic to which this message refers to.
     * @param c the credentials associated with the mesasge.
     */
    public MessagePublish(Address addr, NodeHandle source, NodeId topicId, Credentials c) {
        super(addr, source, topicId, c);
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
        Credentials cred = scribe.getCredentials();
        SendOptions opt = scribe.getSendOptions();
        NodeId topicId;
        if (topic != null) {
            if (!topic.isTopicManager()) {
                if (topic.getParent() == null) {
                    topic.setParent(m_source);
                    topic.postponeParentHandler();
                    if (topic.isWaitingUnsubscribe()) {
                        scribe.leave(m_topicId, null, cred);
                        topic.waitUnsubscribe(false);
                    }
                } else {
                    if (!topic.getParent().equals(m_source)) {
                        ScribeMessage msg = scribe.makeUnsubscribeMessage(m_topicId, cred);
                        scribe.routeMsgDirect(m_source, msg, cred, opt);
                    } else topic.postponeParentHandler();
                }
            } else {
                if (!scribe.getSecurityManager().verifyCanPublish(m_source, m_topicId)) {
                    return;
                }
            }
            Vector childrenVector = topic.getChildren();
            int j = 0;
            ScribeMessage msg = scribe.makePublishMessage(m_topicId, cred);
            msg.setData((Serializable) this.getData());
            IScribeApp[] apps = topic.getApps();
            for (int i = 0; i < apps.length; i++) {
                apps[i].forwardHandler(this);
            }
            while (j < childrenVector.size()) {
                NodeHandle nhandle = (NodeHandle) childrenVector.elementAt(j);
                j++;
                if (!scribe.routeMsgDirect(nhandle, msg, cred, opt)) {
                    int k = 0;
                    Vector topicsForChild = (Vector) scribe.getTopicsForChild((NodeHandle) nhandle);
                    while (k < topicsForChild.size()) {
                        topicId = (NodeId) topicsForChild.elementAt(k);
                        Topic tp = (Topic) scribe.getTopic(topicId);
                        tp.removeChild(nhandle, null);
                        k++;
                    }
                } else {
                    scribe.addChildToAlreadySentHBNodes(nhandle.getNodeId());
                }
            }
            if (topic.hasSubscribers()) {
                for (int i = 0; i < apps.length; i++) {
                    apps[i].receiveMessage(this);
                }
            }
        } else {
            ScribeMessage msg = scribe.makeUnsubscribeMessage(m_topicId, cred);
            scribe.routeMsgDirect(m_source, msg, cred, opt);
        }
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
        return true;
    }

    public String toString() {
        return new String("PUBLISH MSG:" + m_source);
    }
}
