package org.jgrapht.graph;

import java.util.*;
import org.jgrapht.*;

/**
 * ParanoidGraph provides a way to verify that objects added to a graph obey the
 * standard equals/hashCode contract. It can be used to wrap an underlying graph
 * to be verified. Note that the verification is very expensive, so
 * ParanoidGraph should only be used during debugging.
 *
 * @author John Sichi
 * @version $Id: ParanoidGraph.java 586 2008-01-27 23:30:50Z perfecthash $
 */
public class ParanoidGraph<V, E> extends GraphDelegator<V, E> {

    /**
     */
    private static final long serialVersionUID = 5075284167422166539L;

    public ParanoidGraph(Graph<V, E> g) {
        super(g);
    }

    /**
     * @see Graph#addEdge(Object, Object, Object)
     */
    public boolean addEdge(V sourceVertex, V targetVertex, E e) {
        verifyAdd(edgeSet(), e);
        return super.addEdge(sourceVertex, targetVertex, e);
    }

    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex(V v) {
        verifyAdd(vertexSet(), v);
        return super.addVertex(v);
    }

    private static <T> void verifyAdd(Set<T> set, T t) {
        for (T o : set) {
            if (o == t) {
                continue;
            }
            if (o.equals(t) && (o.hashCode() != t.hashCode())) {
                throw new IllegalArgumentException("ParanoidGraph detected objects " + "o1 (hashCode=" + o.hashCode() + ") and o2 (hashCode=" + t.hashCode() + ") where o1.equals(o2) " + "but o1.hashCode() != o2.hashCode()");
            }
        }
    }
}
