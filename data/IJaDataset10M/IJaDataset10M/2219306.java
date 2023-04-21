package org.jnutella.jppp.routing;

import org.jnutella.jppp.core.*;
import org.jnutella.jppp.event.EventPerformer;

public interface Router extends EventPerformer {

    public void start(Place base);

    public void forward(Node target, Message msg, Forwarder forwarder) throws java.io.IOException;

    public void send(Session nextHop, Message message, Forwarder condition) throws java.io.IOException;

    public void sendto(Node target, Message message) throws java.io.IOException, NoRouteException;

    public Message[] pop(Node target) throws java.io.IOException;

    public void sendback(Message message) throws java.io.IOException;

    public Node[] findNextNodes(Message message, Node sender, Forwarder forwarder);

    public RoutingTable getRoutingTable();

    public RoutingTable createDefaultRoutingTable();

    public void refreshRoutingTable();

    public void addAdjacent(Node node);

    public void removeAdjacent(Node node);

    public void merge(RoutingTable table);
}
