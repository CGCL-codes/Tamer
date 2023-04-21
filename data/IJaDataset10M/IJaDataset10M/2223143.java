package org.jgrapht.graph;

import org.jgrapht.*;

/**
 * A simple directed graph. A simple directed graph is a directed graph in which
 * neither multiple edges between any two vertices nor loops are permitted.
 */
public class SimpleDirectedGraph<V, E> extends AbstractBaseGraph<V, E> implements DirectedGraph<V, E> {

    private static final long serialVersionUID = 4049358608472879671L;

    /**
     * Creates a new simple directed graph.
     *
     * @param edgeClass class on which to base factory for edges
     */
    public SimpleDirectedGraph(Class<? extends E> edgeClass) {
        this(new ClassBasedEdgeFactory<V, E>(edgeClass));
    }

    /**
     * Creates a new simple directed graph with the specified edge factory.
     *
     * @param ef the edge factory of the new graph.
     */
    public SimpleDirectedGraph(EdgeFactory<V, E> ef) {
        super(ef, false, false);
    }
}
