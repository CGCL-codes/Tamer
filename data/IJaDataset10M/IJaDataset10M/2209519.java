package com.c4j.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Implements algorithms to calculate topological orders.
 *
 * @author koethnig
 *
 */
public final class TopologicalOrder {

    private TopologicalOrder() {
    }

    private static <E> void getTopologicalOrder(final DirectedGraph<E> graph, final Collection<E> vlist, final List<E> deppath, final List<E> result) throws Exception {
        for (final E v : vlist) {
            if (deppath.contains(v)) {
                deppath.add(v);
                throw new Exception("Can not create topological order!");
            }
            if (!result.contains(v)) {
                final Set<E> succ = graph.getSuccessors(v);
                if (!succ.isEmpty()) {
                    deppath.add(v);
                    getTopologicalOrder(graph, succ, deppath, result);
                    deppath.remove(v);
                }
                result.add(v);
            }
        }
    }

    /**
     * Returns a topological order of the vertices in vlist and
     * its successors if the graph is cycle free, otherwise returns
     * a cycle (i.e. the first element in the returned list equals
     * the last element in the list)
     */
    public static <E> List<E> getTopologicalOrder(final DirectedGraph<E> graph, final Collection<E> vlist) {
        final List<E> result = new Vector<E>();
        final List<E> deppath = new Vector<E>();
        try {
            getTopologicalOrder(graph, vlist, deppath, result);
        } catch (final Exception e) {
            return deppath.subList(deppath.indexOf(deppath.get(deppath.size() - 1)), deppath.size());
        }
        return result;
    }

    /**
     * Returns a topological order of the vertex and
     * its successors if the graph is cycle free, otherwise returns
     * a cycle (i.e. the first element in the returned list equals
     * the last element in the list)
     */
    public static <E> List<E> getTopologicalOrder(final DirectedGraph<E> graph, final E vertex) {
        final Set<E> c = new HashSet<E>();
        c.add(vertex);
        return getTopologicalOrder(graph, c);
    }

    /**
     * Returns a topological order of all vertices in the graph if the
     * graph is cycle free, otherwise returns a cycle (i.e. the first
     * element in the returned list equals the last element in the list)
     */
    public static <E> List<E> getTopologicalOrder(final DirectedGraph<E> graph) {
        return getTopologicalOrder(graph, graph.getVertices());
    }

    /**
     * Returns true if the list represents a cycle, found by one
     * of the getTopologicalOrder methods.
     */
    public static <E> boolean isCycle(final List<E> list) {
        return list.size() > 1 && list.get(0) == list.get(list.size() - 1);
    }
}
