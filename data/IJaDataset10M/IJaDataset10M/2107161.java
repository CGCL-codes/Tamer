package org.jgrapht.graph;

/**
 * A functor interface for masking out vertices and edges of a graph.
 *
 * @author Guillaume Boulmier
 * @since July 5, 2007
 */
public interface MaskFunctor<V, E> {

    /**
     * Returns <code>true</code> if the edge is masked, <code>false</code>
     * otherwise.
     *
     * @param edge edge.
     *
     * @return .
     */
    public boolean isEdgeMasked(E edge);

    /**
     * Returns <code>true</code> if the vertex is masked, <code>false</code>
     * otherwise.
     *
     * @param vertex vertex.
     *
     * @return .
     */
    public boolean isVertexMasked(V vertex);
}
