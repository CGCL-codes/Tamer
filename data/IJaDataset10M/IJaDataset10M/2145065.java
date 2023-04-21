package org.sdf4j.model;

/**
 * Interface to create vertex in the given model
 * @author jpiat
 *
 * @param <V> The model of vertex to create
 */
@SuppressWarnings("unchecked")
public interface ModelVertexFactory<V extends AbstractVertex> {

    /**
	 * Creates a vertex of the given kind
	 * @param kind The kind of the vertex to create
	 * @return The created vertex
	 */
    public V createVertex(String kind);
}
