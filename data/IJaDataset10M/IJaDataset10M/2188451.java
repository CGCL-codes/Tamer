package playground.christoph.knowledge.nodeselection;

import java.util.Map;
import java.util.TreeMap;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.utils.geometry.CoordUtils;

public class SelectNodesCircular extends BasicSelectNodesImpl {

    double distance;

    Node centerNode;

    Link centerLink;

    public SelectNodesCircular(Network net) {
        this.network = net;
        distance = 0.0;
    }

    public Map<Id, Node> getNodes(Node center, double dist) {
        distance = dist;
        centerNode = center;
        centerLink = null;
        return getNodes();
    }

    public void getNodes(Node center, double dist, Map<Id, Node> nodesMap) {
        distance = dist;
        centerNode = center;
        centerLink = null;
        addNodesToMap(nodesMap);
    }

    public Map<Id, Node> getNodes(Link link, double dist) {
        distance = dist;
        centerLink = link;
        centerNode = null;
        return getNodes();
    }

    public void getNodes(Link link, double dist, Map<Id, Node> nodesMap) {
        distance = dist;
        centerLink = link;
        centerNode = null;
        addNodesToMap(nodesMap);
    }

    @Override
    public Map<Id, Node> getNodes() {
        Map<Id, Node> nodesMap = new TreeMap<Id, Node>();
        addNodesToMap(nodesMap);
        return nodesMap;
    }

    @Override
    public void addNodesToMap(Map<Id, Node> nodesMap) {
        if (centerNode != null || centerLink != null) {
            if (nodesMap == null) nodesMap = new TreeMap<Id, Node>();
            Map<Id, ? extends Node> networkNodesMap = network.getNodes();
            for (Node node : networkNodesMap.values()) {
                Coord coord = node.getCoord();
                double dist;
                if (centerNode != null) dist = CoordUtils.calcDistance(centerNode.getCoord(), coord); else {
                    if (centerLink instanceof LinkImpl) {
                        dist = ((LinkImpl) centerLink).calcDistance(coord);
                    } else dist = CoordUtils.calcDistance(centerLink.getCoord(), coord);
                }
                if (dist <= distance) {
                    if (!nodesMap.containsKey(node.getId())) nodesMap.put(node.getId(), node);
                }
            }
        }
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Node getNode() {
        return centerNode;
    }

    public void setNode(Node centerNode) {
        centerLink = null;
        this.centerNode = centerNode;
    }

    public Link getLink() {
        return centerLink;
    }

    public void setLink(Link centerLink) {
        centerNode = null;
        this.centerLink = centerLink;
    }

    @Override
    public SelectNodesCircular clone() {
        SelectNodesCircular clone = new SelectNodesCircular(this.network);
        return clone;
    }
}
