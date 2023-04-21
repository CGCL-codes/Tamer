package ontorama.backends.p2p.controller;

import ontorama.backends.p2p.P2PBackend;
import ontorama.backends.p2p.model.P2PNode;
import ontorama.backends.p2p.model.P2PNodeImpl;
import ontorama.model.graph.Graph;
import ontorama.model.graph.Node;
import ontorama.model.graph.events.NodeAddedEvent;
import ontorama.model.graph.GraphModificationException;
import org.tockit.events.Event;
import org.tockit.events.EventBroker;
import org.tockit.events.EventListener;

public class NodeAddedEventHandler implements EventListener {

    private EventBroker _eventBroker;

    private P2PBackend _p2pBackend;

    public NodeAddedEventHandler(EventBroker eventBroker, P2PBackend backend) {
        _eventBroker = eventBroker;
        _p2pBackend = backend;
        _eventBroker.subscribe(this, NodeAddedEvent.class, ontorama.model.graph.Graph.class);
    }

    public void processEvent(Event event) {
        NodeAddedEvent nodeAddedEvent = (NodeAddedEvent) event;
        ontorama.model.graph.Node node = nodeAddedEvent.getNode();
        P2PNode p2pNode = new P2PNodeImpl(node.getName(), node.getIdentifier());
        try {
            System.out.println("\n\n assertNode ");
            _p2pBackend.assertNode(p2pNode, node.getCreatorUri());
        } catch (GraphModificationException modExc) {
            modExc.printStackTrace();
            System.err.println("GraphModificationException: " + modExc.getMessage());
        }
    }
}
