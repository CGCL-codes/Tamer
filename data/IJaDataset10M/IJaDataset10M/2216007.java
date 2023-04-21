package org.fgraph.blueprints.bp;

import java.util.*;
import com.google.common.collect.Iterables;
import org.fgraph.Direction;
import org.fgraph.Node;
import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

/**
 *  Adapts the Blueprints Vertex interface to the
 *  Filament Node interface.
 *
 *  @version   $Revision: 562 $
 *  @author    Paul Speed
 */
public class BpVertex extends BpElement<Node> implements Vertex {

    public BpVertex(Node node) {
        super(node);
    }

    public Iterable<Edge> getInEdges() {
        Collection<org.fgraph.Edge> edges = getObject().edges(null, Direction.IN);
        return Iterables.transform(edges, FGraphBpGraphAdapter.TO_EDGE);
    }

    public Iterable<Edge> getOutEdges() {
        Collection<org.fgraph.Edge> edges = getObject().edges(null, Direction.OUT);
        return Iterables.transform(edges, FGraphBpGraphAdapter.TO_EDGE);
    }
}
