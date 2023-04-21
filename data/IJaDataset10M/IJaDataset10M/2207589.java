package org.matsim.contrib.sna.graph;

import junit.framework.TestCase;

/**
 * @author illenberger
 *
 */
public class SparseGraphTest extends TestCase {

    public void test() {
        GraphBuilder<SparseGraph, SparseVertex, SparseEdge> builder = new SparseGraphBuilder();
        SparseGraph graph = builder.createGraph();
        SparseVertex v1 = builder.addVertex(graph);
        SparseVertex v2 = builder.addVertex(graph);
        SparseVertex v3 = builder.addVertex(graph);
        SparseVertex v4 = builder.addVertex(graph);
        SparseEdge e1 = builder.addEdge(graph, v1, v2);
        builder.addEdge(graph, v2, v3);
        builder.addEdge(graph, v3, v4);
        builder.addEdge(graph, v4, v1);
        assertEquals(4, graph.getVertices().size());
        assertEquals(4, graph.getEdges().size());
        Vertex current, prev, next;
        current = v1;
        prev = null;
        for (int i = 0; i < 4; i++) {
            next = current.getEdges().get(0).getOpposite(current);
            if (next == prev) next = current.getEdges().get(1).getOpposite(current);
            prev = current;
            current = next;
        }
        assertEquals(v1, current);
        builder.addEdge(graph, v1, v3);
        builder.addEdge(graph, v2, v4);
        assertEquals(3, v2.getNeighbours().size());
        assertEquals(3, v3.getNeighbours().size());
        boolean result = builder.removeVertex(graph, v1);
        assertEquals(false, result);
        builder.removeEdge(graph, graph.getEdge(v1, v2));
        builder.removeEdge(graph, graph.getEdge(v1, v3));
        builder.removeEdge(graph, graph.getEdge(v1, v4));
        assertEquals(0, v1.getNeighbours().size());
        result = builder.removeVertex(graph, v1);
        assertEquals(true, result);
        assertEquals(null, e1.getVertices());
        result = builder.removeEdge(graph, e1);
        assertEquals(false, result);
        current = v2;
        prev = null;
        for (int i = 0; i < 2; i++) {
            next = current.getEdges().get(0).getOpposite(current);
            if (next == prev) next = current.getEdges().get(1).getOpposite(current);
            prev = current;
            current = next;
        }
        assertEquals(v4, current);
    }
}
