package org.matsim.core.router.util;

import org.matsim.core.api.network.Link;
import org.matsim.core.api.network.Network;
import org.matsim.core.api.network.Node;

/**
 * Proxy for the LeastCostPathCalculator to make it
 * work on an inverted network considering LinkToLinkTravelTimes
 * 
 * @author dgrether
 *
 */
public class LeastCostPathCalculatorInvertedNetProxy implements LeastCostPathCalculator {

    private Network invertedNetwork;

    private NetworkInverter netInverter;

    private LeastCostPathCalculator leastCostPathCalculator;

    public LeastCostPathCalculatorInvertedNetProxy(NetworkInverter networkInverter, LeastCostPathCalculator lcpc) {
        this.netInverter = networkInverter;
        this.invertedNetwork = this.netInverter.getInvertedNetwork();
        this.leastCostPathCalculator = lcpc;
    }

    /**
	 * @see org.matsim.core.router.util.LeastCostPathCalculator#calcLeastCostPath(org.matsim.core.api.network.Node, org.matsim.core.api.network.Node, double)
	 */
    public Path calcLeastCostPath(Node fromNode, Node toNode, double starttime) {
        Link startLink = this.invertedNetwork.getLink(fromNode.getId());
        Node startNode = startLink.getToNode();
        Link endLink = this.invertedNetwork.getLink(toNode.getId());
        Node endNode = endLink.getToNode();
        Path invertedPath = this.leastCostPathCalculator.calcLeastCostPath(startNode, endNode, starttime);
        Path path = new Path(this.netInverter.convertInvertedLinksToNodes(invertedPath.links), this.netInverter.convertInvertedNodesToLinks(invertedPath.nodes), invertedPath.travelTime, invertedPath.travelCost);
        return path;
    }
}
