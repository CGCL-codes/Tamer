package com.c4j.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Implements matching algorithms.
 *
 * @author koethnig
 *
 */
public final class Matching {

    private Matching() {
    }

    /**
     * Returns a maximal matching for a graph.
     *
     * Running time is O(n+m).
     *
     * @param graph
     *     The graph for which a maximal matching will be calculated.
     * @return
     *     The maximal matching. Points from each covered vertex to the
     *     covered vertex that shares the same matching edge.
     */
    public static <E> Map<E, E> getMaximalMatching(final SimpleGraph<E> graph) {
        final Map<E, E> matching = new HashMap<E, E>();
        for (final E vertex : graph.getVertices()) {
            if (!matching.containsKey(vertex)) {
                for (final E neighbor : graph.getNeighbors(vertex)) {
                    if (!matching.containsKey(neighbor)) {
                        matching.put(vertex, neighbor);
                        matching.put(neighbor, vertex);
                    }
                }
            }
        }
        return matching;
    }

    /**
     * Runs from a vertex v to the root of the vertex and alternates
     * a matching along this path.
     *
     * The path must be alternating in respect to the matching, such
     * that v is covered by the matching and the root is not covered.
     * (This also implies that the length of the path must be even).
     *
     * The method alternates the matching along the path such that the
     * path is still alternating in respect to the new matching but v
     * is uncovered and the root of v is covered.
     *
     * @param v
     *     The vertex
     * @param matching
     *     The matching that will be alternated along the path.
     *     Points from each covered vertex to the covered vertex
     *     that shares the same matching edge.
     * @param rootpath
     *     A data structure that holds an alternating path in respect
     *     to the matching. Just points from a vertex to its
     *     predecessor.
     *
     */
    private static <E> void alternatePathToRoot(final E v, final Map<E, E> rootpath, final Map<E, E> matching) {
        E x = v;
        E y;
        matching.remove(x);
        while (rootpath.get(x) != null) {
            y = rootpath.get(x);
            x = rootpath.get(y);
            matching.put(x, y);
            matching.put(y, x);
        }
    }

    /**
     * Augments a matching of a bipartite graph and returns true if
     * possible and false otherwise.
     *
     * If the graph is not bipartite, the matching may will not be
     * augmented although an augmenting path exists.
     *
     * Running time is O(n+m).
     *
     * @param graph
     *     The bipartite graph for which the matching will be augmented
     *     if possible.
     * @param matching
     *     The matching of the bipartite graph that will be augmented if
     *     possible. Points from each covered vertex to the covered
     *     vertex that shares the same matching edge.
     * @throws GraphException
     * 	   if the graph matching could not be augmented, since the graph
     *     is bipartite.
     * @return
     */
    private static <E> boolean augmentMatching(final SimpleGraph<E> graph, final Map<E, E> matching) throws GraphException {
        final Set<E> even = graph.getVertices();
        even.removeAll(matching.keySet());
        final Set<E> odd = new HashSet<E>();
        final Map<E, E> root = new HashMap<E, E>();
        final Map<E, E> rootpath = new HashMap<E, E>();
        for (final E x : even) {
            root.put(x, x);
            rootpath.put(x, null);
        }
        final LinkedList<E> queue = new LinkedList<E>(even);
        while (!queue.isEmpty()) {
            final E x = queue.getFirst();
            for (final E y : graph.getNeighbors(x)) {
                if (!odd.contains(y)) {
                    if (even.contains(y)) {
                        if (root.get(x).equals(root.get(y))) {
                            throw new GraphException("The graph is not bipartite.");
                        }
                        alternatePathToRoot(x, rootpath, matching);
                        alternatePathToRoot(y, rootpath, matching);
                        matching.put(x, y);
                        matching.put(y, x);
                        return true;
                    }
                    final E z = matching.get(y);
                    odd.add(y);
                    even.add(z);
                    root.put(z, x);
                    rootpath.put(y, x);
                    rootpath.put(z, y);
                    queue.addLast(z);
                }
            }
            queue.remove(x);
        }
        return false;
    }

    /**
     * Returns a maximum matching for a bipartite graph. If the graph
     * is not bipartite returns a matching that is at least maximal.
     *
     * Running time is O(n*(n+m)).
     *
     * @param graph
     *     The bipartite graph for which a maximum matching will be
     *     calculated.
     * @return
     *     The maximum matching. Points from each covered vertex to the
     *     covered vertex that shares the same matching edge.
     * @throws GraphException
     * 	   if the graph could calculate a maximum matching, since the graph
     *     is bipartite.
     */
    public static <E> Map<E, E> getMaximumMatching(final SimpleGraph<E> graph) throws GraphException {
        final Map<E, E> matching = getMaximalMatching(graph);
        boolean b = true;
        while (b) b = augmentMatching(graph, matching);
        return matching;
    }
}
