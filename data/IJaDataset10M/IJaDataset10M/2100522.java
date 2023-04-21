package test;

import salvo.jesus.graph.*;

/**
 * SmallDirectedGraph is a small directed graph definition useful for
 * manipulating from test cases (a fixture in JUnit parlance).  It contains
 * four vertices, one of which is isolated, and five edges, one of which is a
 * self-loop.
 *
 * @author John V. Sichi
 * @version $Id: SmallDirectedGraph.java,v 1.1 2002/02/27 07:04:46 perfecthash Exp $
 */
class SmallDirectedGraph extends DirectedGraphImpl {

    public final Vertex v1;

    public final Vertex v2;

    public final Vertex v3;

    public final Vertex v4;

    public final DirectedEdge e12;

    public final DirectedEdge e13;

    public final DirectedEdge e23;

    public final DirectedEdge e33;

    public final DirectedEdge e31;

    public SmallDirectedGraph() throws Exception {
        v1 = new VertexImpl("v1");
        v2 = new VertexImpl("v2");
        v3 = new VertexImpl("v3");
        v4 = new VertexImpl("v4");
        e12 = new DirectedEdgeImpl(v1, v2);
        e13 = new DirectedEdgeImpl(v1, v3);
        e23 = new DirectedEdgeImpl(v2, v3);
        e33 = new DirectedEdgeImpl(v3, v3);
        e31 = new DirectedEdgeImpl(v3, v1);
        add(v1);
        add(v2);
        add(v3);
        add(v4);
        addEdge(e12);
        addEdge(e13);
        addEdge(e23);
        addEdge(e33);
        addEdge(e31);
    }
}
