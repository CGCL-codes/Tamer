package ontorama.graph.controller;

import ontorama.controller.NodeSelectedEvent;
import ontorama.graph.view.GraphView;
import ontorama.model.GraphNode;
import ontorama.model.Graph;
import org.tockit.events.Event;
import org.tockit.events.EventBroker;
import org.tockit.events.EventListener;

public class GraphViewFocusEventHandler implements EventListener {

    private GraphView graphView;

    private EventBroker eventBroker;

    public GraphViewFocusEventHandler(EventBroker eventBroker, GraphView graphView) {
        this.graphView = graphView;
        this.eventBroker = eventBroker;
        eventBroker.subscribe(this, NodeSelectedEvent.class, GraphNode.class);
    }

    public void processEvent(Event e) {
        GraphNode node = (GraphNode) e.getSubject();
        this.graphView.focus(node);
    }
}
