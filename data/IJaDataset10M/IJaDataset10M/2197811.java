package jupiter.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import jupiter.message.*;
import org.json.*;

/**
 * Maintains a list of all other nodes for a particular domain
 * @author Justin
 *
 */
public class DomainNode {

    HashMap<String, Node> nodeMap = new HashMap<String, Node>();

    public Address address;

    public DomainNode(Address a) {
        address = a;
    }

    /**
	 * returns if valid sessions (typically users can create nodes), if
	 * false only server can createNodes
	 * @return
	 */
    public boolean sessionsCanCreateNodes() {
        return true;
    }

    /**
	 * A few cases
	 * 	- the node exists or doesn't
	 *  - sessions can create nodes on the fly, or can't
	 * Question
	 *  - if the nodes doesn't exist, we should probably save all messages to it
	 *  
	 * If sessions can create nodes, this will create a new node
	 */
    public JSONObject sendMessage(Message m) {
        Node n = nodeMap.get(m.to().toString());
        if (n == null) return null;
        return n.sendMessage(m);
    }

    void addNode(Node n) {
        nodeMap.put(n.address(), n);
    }

    void removeNode(Node n) {
        nodeMap.remove(n.address());
    }

    Node getNode(Address a) {
        return nodeMap.get(a.toString());
    }
}
