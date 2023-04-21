package net.jmarias.uqueue.contact;

import net.jmarias.uqueue.resources.Channel;
import net.jmarias.uqueue.resources.Vector;
import java.util.Collection;

/**
 *
 * @author jose
 */
public interface Contact {

    Integer getId();

    ContactScopeType getScope();

    ContactStatus getStatus();

    void setStatus(ContactStatus status);

    void setChannel(Channel channel);

    Integer getPriority();

    Integer getNodesCount();

    void notifyNodeStateChanged(Node node);

    Integer getFirstQueued();

    Integer getLastQueued();

    Integer getFirstRang();

    Integer getLastRang();

    Integer getFirstConnected();

    Integer getLastConnected();

    void setVector(Vector vector);

    Node getInstanceOfNode(Integer deviceId, NodeDirectionType direction, String calledTag, String dialedTag, String callerTag, String callerName, Integer CallerDeviceId, String cause);

    Collection<Node> enumerateNodes();
}
