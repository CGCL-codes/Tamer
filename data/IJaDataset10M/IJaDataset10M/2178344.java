package teach.matsim08.sheet6;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.matsim.basic.v01.Id;
import org.matsim.interfaces.networks.basicNet.BasicLink;
import org.matsim.interfaces.networks.basicNet.BasicNet;
import org.matsim.interfaces.networks.basicNet.BasicNode;

public class CANetwork implements BasicNet {

    private Map caLinks = new HashMap();

    private Map caNodes = new HashMap();

    public CANetwork(BasicNet basicNet) {
        Map links = basicNet.getLinks();
        Map nodes = basicNet.getNodes();
        for (Iterator it = nodes.values().iterator(); it.hasNext(); ) {
            BasicNode n = (BasicNode) it.next();
            CANode caNode = new CANode(n);
            this.caNodes.put(n.getId(), caNode);
        }
        for (Iterator it = links.values().iterator(); it.hasNext(); ) {
            BasicLink basicLink = (BasicLink) it.next();
            CALink caLink = new CALink(basicLink);
            this.caLinks.put(basicLink.getId(), caLink);
            caLink.build();
        }
        this.connect();
    }

    public Map<Id, ? extends BasicLink> getLinks() {
        return caLinks;
    }

    public Map<Id, ? extends BasicNode> getNodes() {
        return caNodes;
    }

    public void connect() {
        for (Iterator it = caLinks.values().iterator(); it.hasNext(); ) {
            CALink calink = (CALink) it.next();
            Id toNodeId = calink.getToNode().getId();
            Id fromNodeId = calink.getFromNode().getId();
            CANode toNode = (CANode) caNodes.get(toNodeId);
            CANode fromNode = (CANode) caNodes.get(fromNodeId);
            toNode.addCaInLink(calink);
            fromNode.addCaOutLink(calink);
        }
    }

    public void move() {
        for (Iterator it = caNodes.values().iterator(); it.hasNext(); ) {
            CANode caNode = (CANode) it.next();
            caNode.move();
        }
        for (Iterator it = caLinks.values().iterator(); it.hasNext(); ) {
            CALink caLink = (CALink) it.next();
            caLink.move();
        }
    }
}
