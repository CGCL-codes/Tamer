package org.matsim.core.population.routes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.utils.misc.Time;

public class NodeNetworkRouteImpl extends AbstractRoute implements NetworkRouteWRefs, Cloneable {

    protected ArrayList<Node> route = new ArrayList<Node>();

    private double cost = Double.NaN;

    private Id vehicleId = null;

    /**
	 * This constructor is only needed for backwards compatibility reasons and thus is
	 * set to deprecated. New code should make use of the constructor which sets the
	 * start and the end link of a Route correctly.
	 */
    @Deprecated
    public NodeNetworkRouteImpl() {
    }

    public NodeNetworkRouteImpl(final Link startLink, final Link endLink) {
        super(startLink, endLink);
    }

    public NodeNetworkRouteImpl(final NetworkRouteWRefs route) {
        super(route.getStartLink(), route.getEndLink());
        super.setDistance(route.getDistance());
        super.setTravelTime(route.getTravelTime());
        this.route.addAll(route.getNodes());
        this.route.trimToSize();
    }

    @Override
    public NodeNetworkRouteImpl clone() {
        NodeNetworkRouteImpl cloned = (NodeNetworkRouteImpl) super.clone();
        ArrayList<Node> tmp = cloned.route;
        cloned.route = new ArrayList<Node>(tmp);
        return cloned;
    }

    @Override
    public void setLinkIds(final List<Id> linkids) {
        throw new UnsupportedOperationException("Setting only the link ids is not possible at this " + "level in the inheritance hierachy! If the Interfaces Link/Node/Route are used you " + "have to set the route by object references not by Ids.");
    }

    public List<Node> getNodes() {
        return this.route;
    }

    public void setLinks(final Link startLink, final List<Link> srcRoute, final Link endLink) {
        this.route.clear();
        setStartLink(startLink);
        setEndLink(endLink);
        if (srcRoute == null) {
            if (startLink != endLink) {
                this.route.add(startLink.getToNode());
            }
        } else {
            if (srcRoute.size() == 0) {
                if (startLink != endLink) {
                    this.route.add(startLink.getToNode());
                }
            } else {
                Link l = srcRoute.get(0);
                this.route.add(l.getFromNode());
                for (int i = 0; i < srcRoute.size(); i++) {
                    l = srcRoute.get(i);
                    this.route.add(l.getToNode());
                }
            }
        }
        this.route.trimToSize();
    }

    @Deprecated
    public void setNodes(final List<Node> srcRoute) {
        setNodes(null, srcRoute, null);
    }

    public void setNodes(final Link startLink, final List<Node> srcRoute, final Link endLink) {
        setStartLink(startLink);
        setEndLink(endLink);
        if (srcRoute == null) {
            this.route.clear();
        } else {
            this.route.clear();
            this.route.addAll(srcRoute);
        }
        this.route.trimToSize();
    }

    @Override
    public final double getDistance() {
        if (Double.isNaN(super.getDistance())) {
            super.setDistance(this.calcDistance());
        }
        return super.getDistance();
    }

    public final void setTravelCost(final double travelCost) {
        this.cost = travelCost;
    }

    public final double getTravelCost() {
        return this.cost;
    }

    public List<Id> getLinkIds() {
        List<Id> ret = new ArrayList<Id>(Math.max(0, this.route.size() - 1));
        for (Link l : getLinks()) {
            ret.add(l.getId());
        }
        return ret;
    }

    public final List<Link> getLinks() {
        if (this.route.size() == 0) {
            return new ArrayList<Link>(0);
        }
        Node prevNode = null;
        ArrayList<Link> links = new ArrayList<Link>(this.route.size() - 1);
        for (Node node : this.route) {
            if (prevNode != null) {
                boolean linkFound = false;
                for (Iterator<? extends Link> iter = prevNode.getOutLinks().values().iterator(); iter.hasNext() && !linkFound; ) {
                    Link link = iter.next();
                    if (link.getToNode() == node) {
                        links.add(link);
                        linkFound = true;
                    }
                }
                if (!linkFound) {
                    throw new RuntimeException("No link found from node " + prevNode.getId() + " to node " + node.getId());
                }
            }
            prevNode = node;
        }
        return links;
    }

    protected double calcDistance() {
        double distance = 0;
        for (Link link : getLinks()) {
            distance += link.getLength();
        }
        return distance;
    }

    /**
	 * This method returns a new Route object with the subroute of this beginning at fromNode and
	 * ending at toNode.
	 * @param fromNode
	 * @param toNode
	 * @return a route leading from <code>fromNode</code> to <code>toNode</code> along this route
	 * @throws IllegalArgumentException if <code>fromNode</code> or <code>toNode</code> are not part of this route
	 */
    public NetworkRouteWRefs getSubRoute(final Node fromNode, final Node toNode) {
        Link fromLink = getStartLink();
        Link toLink = getEndLink();
        int fromIndex = -1;
        int toIndex = -1;
        List<Link> links = getLinks();
        int max = links.size();
        if (fromNode == toNode) {
            if (this.route.size() > 1) {
                for (int i = 0; i < max; i++) {
                    Link link = links.get(i);
                    Node node = link.getFromNode();
                    if (node.equals(fromNode)) {
                        fromIndex = i;
                        toIndex = i;
                        toLink = link;
                        break;
                    }
                    fromLink = link;
                }
                if (fromIndex == -1) {
                    if (fromNode.equals(fromLink.getToNode())) {
                        fromIndex = max;
                        toIndex = max;
                    } else {
                        throw new IllegalArgumentException("Can't create subroute because fromNode is not in the original Route");
                    }
                }
            } else if (this.route.size() == 1) {
                if (this.route.get(0) == fromNode) {
                    fromIndex = 0;
                    toIndex = 0;
                } else {
                    throw new IllegalArgumentException("Can't create subroute because fromNode is not in the original Route");
                }
            } else {
                throw new IllegalArgumentException("Can't create subroute because route does not contain any nodes.");
            }
        } else {
            for (int i = 0; i < max; i++) {
                Link link = links.get(i);
                Node node = link.getFromNode();
                if (node.equals(fromNode)) {
                    fromIndex = i;
                    break;
                }
                fromLink = link;
            }
            if (fromIndex == -1) {
                throw new IllegalArgumentException("Can't create subroute because fromNode is not in the original Route");
            }
            for (int i = fromIndex; i < max; i++) {
                Link link = links.get(i);
                if (toIndex >= 0) {
                    toLink = link;
                    break;
                }
                Node node = link.getToNode();
                if (node.equals(toNode)) {
                    toIndex = i + 1;
                }
            }
            if (toIndex == -1) {
                throw new IllegalArgumentException("Can't create subroute because toNode is not in the original Route");
            }
        }
        NodeNetworkRouteImpl ret = new NodeNetworkRouteImpl();
        ret.setNodes(fromLink, this.route.subList(fromIndex, toIndex + 1), toLink);
        return ret;
    }

    @Override
    public final String toString() {
        StringBuilder b = new StringBuilder();
        b.append("NodeCarRoute: [dist=");
        b.append(this.getDistance());
        b.append("]");
        b.append("[trav_time=");
        b.append(Time.writeTime(this.getTravelTime()));
        b.append("]");
        b.append("[nof_nodes=");
        b.append(this.route.size());
        b.append("]");
        return b.toString();
    }

    public Id getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(final Id vehicleId) {
        this.vehicleId = vehicleId;
    }
}
