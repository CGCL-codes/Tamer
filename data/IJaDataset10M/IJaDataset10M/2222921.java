package org.middleheaven.graph;

import java.util.Collection;
import java.util.List;

public interface GraphPath<E, V> {

    public V getStartVertex();

    public V getEndVertex();

    /**
	 * A ordered collection of the edges that form the path.
	 * @return
	 */
    public Collection<E> getEdges();

    public void visit(GraphPathVisitor<E, V> visitor);

    public boolean isEmpty();

    public Graph<E, V> getGraph();
}
