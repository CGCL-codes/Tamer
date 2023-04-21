package net.sf.callmesh.model;

import net.sf.callmesh.model.graph.CallGraphEdge;
import net.sf.callmesh.model.graph.CallGraphNode;

public class UnexpectedTypeException extends CallModelException {

    public UnexpectedTypeException(CallGraphNode node) {
        super("unexpected node type: " + node.getClass().getName());
    }

    public UnexpectedTypeException(CallGraphEdge edge) {
        super("unexpected edge type: " + edge.getClass().getName());
    }
}
