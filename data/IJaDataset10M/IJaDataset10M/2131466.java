package org.jgrapht.graph;

import java.util.*;
import org.jgrapht.*;

/**
 * A directed weighted graph that is a subgraph on other graph.
 *
 * @see Subgraph
 */
public class DirectedWeightedSubgraph<V, E> extends DirectedSubgraph<V, E> implements WeightedGraph<V, E> {

    private static final long serialVersionUID = 3905799799168250680L;

    /**
     * Creates a new weighted directed subgraph.
     *
     * @param base the base (backing) graph on which the subgraph will be based.
     * @param vertexSubset vertices to include in the subgraph. If <code>
     * null</code> then all vertices are included.
     * @param edgeSubset edges to in include in the subgraph. If <code>
     * null</code> then all the edges whose vertices found in the graph are
     * included.
     */
    public DirectedWeightedSubgraph(WeightedGraph<V, E> base, Set<V> vertexSubset, Set<E> edgeSubset) {
        super((DirectedGraph<V, E>) base, vertexSubset, edgeSubset);
    }
}
