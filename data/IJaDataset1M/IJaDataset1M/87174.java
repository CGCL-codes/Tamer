package com.c4j.graph;

import static java.lang.String.format;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a simple graph.
 *
 * @author koethnig
 *
 * @param <E>
 */
public class SimpleGraph<E> {

    private final Map<E, Set<E>> adjacencyList;

    /**
     * Creates a new empty simple graph.
     */
    public SimpleGraph() {
        adjacencyList = new HashMap<E, Set<E>>();
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex
     * 		The vertex to add.
     *
     * @throws GraphIndexOutOfBoundsException
     * 		if the vertex is null or is already contained in the graph.
     */
    public void addVertex(final E vertex) {
        if (vertex == null) throw new GraphIndexOutOfBoundsException(format("Invalid argument: Vertex is ‘null’."));
        if (adjacencyList.containsKey(vertex)) throw new GraphIndexOutOfBoundsException(format("Graph already contain vertex ‘%s’.", vertex.toString()));
        adjacencyList.put(vertex, new HashSet<E>());
    }

    /**
     * Remove a vertex and all incident edges from the graph.
     *
     * @param vertex
     * 		The vertex to remove.
     *
     * @throws GraphIndexOutOfBoundsException
     * 		if the vertex is null or is not contained in the graph.
     */
    public void removeVertex(final E vertex) {
        if (vertex == null) throw new GraphIndexOutOfBoundsException(format("Invalid argument: Vertex is ‘null’."));
        if (!adjacencyList.containsKey(vertex)) throw new GraphIndexOutOfBoundsException(format("Graph does not contain vertex ‘%s’.", vertex.toString()));
        final Iterator<E> i = getNeighbors(vertex).iterator();
        while (i.hasNext()) removeEdge(vertex, i.next());
        adjacencyList.remove(vertex);
    }

    /**
     * Adds an edge between to vertices to the graph.
     *
     * @param v
     * 		One of the vertices to connect.
     * @param w
     * 		The other vertex to connect.
     *
     * @throws GraphIndexOutOfBoundsException
     * 		if one of the vertices is null or not contained in the graph or if
     * 		the vertices are not different or if the graph already contains the edge.
     */
    public void addEdge(final E v, final E w) {
        if (v == null || w == null) throw new GraphIndexOutOfBoundsException(format("Invalid argument: Vertex is ‘null’."));
        if (!adjacencyList.containsKey(v)) throw new GraphIndexOutOfBoundsException(format("Graph does not contain vertex ‘%s’.", v.toString()));
        if (!adjacencyList.containsKey(w)) throw new GraphIndexOutOfBoundsException(format("Graph does not contain vertex ‘%s’.", w.toString()));
        if (v.equals(w)) throw new GraphIndexOutOfBoundsException(format("Connecting a vertex with itself is not allowed."));
        if (getNeighbors(v).contains(w)) throw new GraphIndexOutOfBoundsException(format("Vertices ‘%s’ and ‘%s’ are already connected.", v.toString(), w.toString()));
        getNeighbors(v).add(w);
        getNeighbors(w).add(v);
    }

    /**
     * Removes an edge between to vertices to the graph.
     *
     * @param v
     * 		One of the vertices to disconnect.
     * @param w
     * 		The other vertex to disconnect.
     *
     * @throws GraphIndexOutOfBoundsException
     * 		if one of the vertices is null or not contained in the graph or if
     * 		the vertices are not different or if the graph does not contain the edge.
     */
    public void removeEdge(final E v, final E w) {
        if (v == null || w == null) throw new GraphIndexOutOfBoundsException(format("Invalid argument: Vertex is ‘null’."));
        if (!adjacencyList.containsKey(v)) throw new GraphIndexOutOfBoundsException(format("Graph does not contain vertex ‘%s’.", v.toString()));
        if (!adjacencyList.containsKey(w)) throw new GraphIndexOutOfBoundsException(format("Graph does not contain vertex ‘%s’.", w.toString()));
        if (v.equals(w)) throw new GraphIndexOutOfBoundsException(format("Disconnecting a vertex with itself is not allowed."));
        if (!getNeighbors(v).contains(w)) throw new GraphIndexOutOfBoundsException(format("Vertices ‘%s’ and ‘%s’ are not connected.", v.toString(), w.toString()));
        getNeighbors(v).remove(w);
        getNeighbors(w).remove(v);
    }

    /**
     * Returns a set of all vertices.
     *
     * @return
     * 		A set of all vertices.
     */
    public Set<E> getVertices() {
        return adjacencyList.keySet();
    }

    /**
     * Returns a list of all neighbors of a vertex.
     *
     * @param vertex
     * 		The vertex used to give the result.
     *
     * @return
     * 		The list of all neighbors of the vertex.
     *
     * @throws GraphIndexOutOfBoundsException
     * 		if the vertex is null or not contained in the graph.
     */
    public Set<E> getNeighbors(final E vertex) {
        if (vertex == null) throw new GraphIndexOutOfBoundsException(format("Invalid argument: Vertex is ‘null’."));
        if (!adjacencyList.containsKey(vertex)) throw new GraphIndexOutOfBoundsException(format("Graph does not contain vertex ‘%s’.", vertex.toString()));
        return adjacencyList.get(vertex);
    }

    /**
     * Returns true if the vertex is a leaf, i.e. it is connected to at most
     * one edge.
     *
     * @param vertex
     * 		the vertex that will be tested do be a leaf.
     */
    public boolean isLeaf(final E vertex) {
        return adjacencyList.get(vertex).size() < 2;
    }
}
