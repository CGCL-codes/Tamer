package playground.christoph.knowledge.utils;

import java.util.ArrayList;
import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkImpl;

public class GetAllIncludedLinks {

    /**
	 * Returns an ArrayList of Links.
	 * @param NetworkImpl network
	 * @param ArrayList< Node > includedNodes
	 *  
	 * @return A link from the network is included in the returned ArrayList, if its
	 * start- and end node are included in the includedNodes ArrayList.
	 */
    public ArrayList<Link> getAllLinks(Network network, ArrayList<Node> includedNodes) {
        ArrayList<Link> includedLinks = new ArrayList<Link>();
        getAllLinks(network, includedNodes, includedLinks);
        return includedLinks;
    }

    /**
	 * Returns an ArrayList of Links.
	 * @param NetworkImpl network
	 * @param Map< Id, Node > includedNodesMap
	 *  
	 * @return A link from the network is included in the returned ArrayList, if its
	 * start- and end node are included in the includedNodes ArrayList.
	 */
    public ArrayList<Link> getAllLinks(NetworkImpl network, Map<Id, Node> includedNodesMap) {
        ArrayList<Link> includedLinks = new ArrayList<Link>();
        getAllLinks(network, includedNodesMap, includedLinks);
        return includedLinks;
    }

    /**
	 * A link from the network is added to the includedLinks ArrayList, if its
	 * start- and end node are included in the includedNodes ArrayList.
	 *  
	 * @param NetworkImpl network
	 * @param ArrayList< Node > includedNodes
	 * @param ArrayList< Link > includedLinks
	 */
    public void getAllLinks(Network network, ArrayList<Node> includedNodes, ArrayList<Link> includedLinks) {
        Map<Id, ? extends Link> linkMap = network.getLinks();
        for (Link link : linkMap.values()) {
            Node fromNode = link.getFromNode();
            Node toNode = link.getToNode();
            if (includedNodes.contains(fromNode) && includedNodes.contains(toNode)) {
                includedLinks.add(link);
            }
        }
    }

    /**
	 * A link from the network is added to the includedLinks ArrayList, if its
	 * start- and end node are included in the includedNodes ArrayList.
	 *  
	 * @param NetworkImpl network
	 * @param Map< Id, Node > includedNodesMap
	 * @param ArrayList< Link > includedLinks
	 */
    public void getAllLinks(NetworkImpl network, Map<Id, Node> includedNodesMap, ArrayList<Link> includedLinks) {
        Map<Id, ? extends Link> linkMap = network.getLinks();
        for (Link link : linkMap.values()) {
            Node fromNode = link.getFromNode();
            Node toNode = link.getToNode();
            if (includedNodesMap.containsKey(fromNode.getId()) && includedNodesMap.containsKey(toNode.getId())) {
                includedLinks.add(link);
            }
        }
    }
}
