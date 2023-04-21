package org.jgrapht.ext;

import java.io.*;
import junit.framework.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

/**
 * .
 * 
 * @author Charles Fry
 */
public class MatrixExporterTest extends TestCase {

    private static final String V1 = "v1";

    private static final String V2 = "v2";

    private static final String V3 = "v3";

    private static final String NL = System.getProperty("line.separator");

    private static final String LAPLACIAN = "1 1 2" + NL + "1 2 -1" + NL + "1 3 -1" + NL + "2 2 1" + NL + "2 1 -1" + NL + "3 3 1" + NL + "3 1 -1" + NL;

    private static final String NORMALIZED_LAPLACIAN = "1 1 1" + NL + "1 2 -0.7071067811865475" + NL + "1 3 -0.7071067811865475" + NL + "2 2 1" + NL + "2 1 -0.7071067811865475" + NL + "3 3 1" + NL + "3 1 -0.7071067811865475" + NL;

    private static final String UNDIRECTED_ADJACENCY = "1 2 1" + NL + "1 3 1" + NL + "1 1 2" + NL + "2 1 1" + NL + "3 1 1" + NL;

    private static final String DIRECTED_ADJACENCY = "1 2 1" + NL + "3 1 2" + NL;

    private static final MatrixExporter<String, DefaultEdge> exporter = new MatrixExporter<String, DefaultEdge>();

    public void testLaplacian() {
        UndirectedGraph<String, DefaultEdge> g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);
        StringWriter w = new StringWriter();
        exporter.exportLaplacianMatrix(w, g);
        assertEquals(LAPLACIAN, w.toString());
        w = new StringWriter();
        exporter.exportNormalizedLaplacianMatrix(w, g);
        assertEquals(NORMALIZED_LAPLACIAN, w.toString());
    }

    public void testAdjacencyUndirected() {
        UndirectedGraph<String, DefaultEdge> g = new Pseudograph<String, DefaultEdge>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);
        g.addEdge(V1, V1);
        StringWriter w = new StringWriter();
        exporter.exportAdjacencyMatrix(w, g);
        assertEquals(UNDIRECTED_ADJACENCY, w.toString());
    }

    public void testAdjacencyDirected() {
        DirectedGraph<String, DefaultEdge> g = new DirectedMultigraph<String, DefaultEdge>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);
        g.addEdge(V3, V1);
        Writer w = new StringWriter();
        exporter.exportAdjacencyMatrix(w, g);
        assertEquals(DIRECTED_ADJACENCY, w.toString());
    }
}
