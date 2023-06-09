package org.hypergraphdb.viewer.event;

import org.hypergraphdb.viewer.FEdge;
import org.hypergraphdb.viewer.GraphView;

public final class GraphViewEdgesRemovedEvent extends GraphViewChangeEventAdapter {

    private final FEdge[] edges;

    public GraphViewEdgesRemovedEvent(GraphView source, FEdge[] hiddenEdges) {
        super(source);
        edges = hiddenEdges;
    }

    public final int getType() {
        return EDGES_REMOVED_TYPE;
    }

    public final FEdge[] getRemovedEdges() {
        final FEdge[] returnThis = new FEdge[edges.length];
        System.arraycopy(edges, 0, returnThis, 0, edges.length);
        return returnThis;
    }
}
