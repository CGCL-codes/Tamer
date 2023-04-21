package org.jgrapht.graph;

import java.io.*;
import org.jgrapht.*;

/**
 * An {@link EdgeFactory} for producing edges by using a class as a factory.
 *
 * @author Barak Naveh
 * @since Jul 14, 2003
 */
public class ClassBasedEdgeFactory<V, E> implements EdgeFactory<V, E>, Serializable {

    private static final long serialVersionUID = 3618135658586388792L;

    private final Class<? extends E> edgeClass;

    public ClassBasedEdgeFactory(Class<? extends E> edgeClass) {
        this.edgeClass = edgeClass;
    }

    /**
     * @see EdgeFactory#createEdge(Object, Object)
     */
    public E createEdge(V source, V target) {
        try {
            return edgeClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Edge factory failed", ex);
        }
    }
}
