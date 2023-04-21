package org.jgrapht.alg;

import java.util.*;
import org.jgrapht.*;
import org.jgrapht.traverse.*;

/**
 * An implementation of <a
 * href="http://mathworld.wolfram.com/DijkstrasAlgorithm.html">Dijkstra's
 * shortest path algorithm</a> using <code>ClosestFirstIterator</code>.
 *
 * @author John V. Sichi
 * @since Sep 2, 2003
 */
public final class DijkstraShortestPath<V, E> {

    private List<E> edgeList;

    private double pathLength;

    /**
     * Creates and executes a new DijkstraShortestPath algorithm instance. An
     * instance is only good for a single search; after construction, it can be
     * accessed to retrieve information about the path found.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     */
    public DijkstraShortestPath(Graph<V, E> graph, V startVertex, V endVertex) {
        this(graph, startVertex, endVertex, Double.POSITIVE_INFINITY);
    }

    /**
     * Creates and executes a new DijkstraShortestPath algorithm instance. An
     * instance is only good for a single search; after construction, it can be
     * accessed to retrieve information about the path found.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     * @param radius limit on path length, or Double.POSITIVE_INFINITY for
     * unbounded search
     */
    public DijkstraShortestPath(Graph<V, E> graph, V startVertex, V endVertex, double radius) {
        if (!graph.containsVertex(endVertex)) {
            throw new IllegalArgumentException("graph must contain the end vertex");
        }
        ClosestFirstIterator<V, E> iter = new ClosestFirstIterator<V, E>(graph, startVertex, radius);
        while (iter.hasNext()) {
            V vertex = iter.next();
            if (vertex.equals(endVertex)) {
                createEdgeList(graph, iter, endVertex);
                pathLength = iter.getShortestPathLength(endVertex);
                return;
            }
        }
        edgeList = null;
        pathLength = Double.POSITIVE_INFINITY;
    }

    /**
     * Return the edges making up the path found.
     *
     * @return List of Edges, or null if no path exists
     */
    public List<E> getPathEdgeList() {
        return edgeList;
    }

    /**
     * Return the length of the path found.
     *
     * @return path length, or Double.POSITIVE_INFINITY if no path exists
     */
    public double getPathLength() {
        return pathLength;
    }

    /**
     * Convenience method to find the shortest path via a single static method
     * call. If you need a more advanced search (e.g. limited by radius, or
     * computation of the path length), use the constructor instead.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     *
     * @return List of Edges, or null if no path exists
     */
    public static <V, E> List<E> findPathBetween(Graph<V, E> graph, V startVertex, V endVertex) {
        DijkstraShortestPath<V, E> alg = new DijkstraShortestPath<V, E>(graph, startVertex, endVertex);
        return alg.getPathEdgeList();
    }

    private void createEdgeList(Graph<V, E> graph, ClosestFirstIterator<V, E> iter, V endVertex) {
        edgeList = new ArrayList<E>();
        while (true) {
            E edge = iter.getSpanningTreeEdge(endVertex);
            if (edge == null) {
                break;
            }
            edgeList.add(edge);
            endVertex = Graphs.getOppositeVertex(graph, edge, endVertex);
        }
        Collections.reverse(edgeList);
    }
}
