package org.matsim.core.router.util;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.Node;

public class RoutingNetworkFactory implements NetworkFactory {

    public RoutingNetwork createRoutingNetwork(Network network) {
        RoutingNetwork routingNetwork = new RoutingNetwork(network);
        return routingNetwork;
    }

    @Override
    public Link createLink(Id id, Id fromNodeId, Id toNodeId) {
        throw new RuntimeException("Not supported operation!");
    }

    @Override
    public Link createLink(Id id, Node fromNode, Node toNode) {
        throw new RuntimeException("Not supported operation!");
    }

    @Override
    public RoutingNetworkNode createNode(Id id, Coord coord) {
        throw new RuntimeException("Not supported operation!");
    }
}
