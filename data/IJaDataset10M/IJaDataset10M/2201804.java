package org.parser.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.parser.graph.Edge;
import org.parser.graph.Vertex;

/**
 * Implementation of {@link Vertex} interface.
 *
 * @author Alexander Ilyin
 * 
 * @param <D> data contained in vertices of a graph
 */
public class VertexImpl<D> implements Vertex<D> {

    private final D data;

    private final Set<Edge<D>> outgoingEdges = new HashSet<Edge<D>>();

    private final Set<Edge<D>> unmodifiableEdges = Collections.unmodifiableSet(outgoingEdges);

    /**
	 * Creates new instance of <code>VertexImpl</code> class.
	 * 
	 * @param data vertex data
	 */
    public VertexImpl(D data) {
        this.data = data;
    }

    public Set<Edge<D>> getOutgoingEdges() {
        return unmodifiableEdges;
    }

    public D getData() {
        return data;
    }

    boolean addOutgoingEdge(Edge<D> edge) {
        return outgoingEdges.add(edge);
    }
}
