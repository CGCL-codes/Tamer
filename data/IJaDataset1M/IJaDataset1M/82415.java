package ontorama.controller;

import ontorama.model.GraphNode;

public class NodeSelectedEvent extends NodeEvent {

    public NodeSelectedEvent(GraphNode subject) {
        super(subject);
    }
}
