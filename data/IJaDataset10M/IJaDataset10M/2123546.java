package com.ibm.wala.util.graph.labeled;

import java.util.Iterator;
import com.ibm.wala.util.graph.NumberedNodeManager;
import com.ibm.wala.util.graph.impl.SlowNumberedNodeManager;

/**
 * A labeled graph implementation suitable for sparse graphs.
 */
public class SlowSparseNumberedLabeledGraph<T, U> extends AbstractNumberedLabeledGraph<T, U> {

    /**
   * @return a graph with the same nodes and edges as g
   */
    public static <T, U> SlowSparseNumberedLabeledGraph<T, U> duplicate(LabeledGraph<T, U> g) {
        SlowSparseNumberedLabeledGraph<T, U> result = new SlowSparseNumberedLabeledGraph<T, U>(g.getDefaultLabel());
        copyInto(g, result);
        return result;
    }

    public static <T, U> void copyInto(LabeledGraph<T, U> g, LabeledGraph<T, U> into) {
        if (g == null) {
            throw new IllegalArgumentException("g is null");
        }
        for (Iterator<? extends T> it = g.iterator(); it.hasNext(); ) {
            into.addNode(it.next());
        }
        for (Iterator<? extends T> it = g.iterator(); it.hasNext(); ) {
            T n = it.next();
            for (Iterator<? extends T> it2 = g.getSuccNodes(n); it2.hasNext(); ) {
                T s = it2.next();
                for (U l : g.getEdgeLabels(n, s)) {
                    into.addEdge(n, s, l);
                }
            }
        }
    }

    private final SlowNumberedNodeManager<T> nodeManager;

    private final SparseNumberedLabeledEdgeManager<T, U> edgeManager;

    public SlowSparseNumberedLabeledGraph(U defaultLabel) {
        if (defaultLabel == null) {
            throw new IllegalArgumentException("null default label");
        }
        nodeManager = new SlowNumberedNodeManager<T>();
        edgeManager = new SparseNumberedLabeledEdgeManager<T, U>(nodeManager, defaultLabel);
    }

    @Override
    protected NumberedLabeledEdgeManager<T, U> getEdgeManager() {
        return edgeManager;
    }

    @Override
    protected NumberedNodeManager<T> getNodeManager() {
        return nodeManager;
    }
}
