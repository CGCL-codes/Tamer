package test;

import salvo.jesus.graph.*;
import junit.framework.*;
import java.util.*;
import java.io.*;

/**
 * White-box test cases for GraphImpl.
 *
 * @author John V. Sichi
 */
public class GraphImplTest extends TestCase {

    public GraphImplTest(String name) {
        super(name);
    }

    public void testVertexSet() throws Exception {
        Graph g = new GraphImpl();
        Vertex v1 = new VertexImpl();
        Vertex v2 = new VertexImpl();
        Vertex v3 = new VertexImpl();
        Vertex v4 = new VertexImpl();
        Vertex v5 = new VertexImpl();
        g.add(v1);
        g.add(v1);
        g.add(v2);
        g.add(v3);
        g.add(v3);
        g.addEdge(v4, v2);
        g.addEdge(v1, v5);
        g.remove(v3);
        Set set = g.getVertexSet();
        Assert.assertEquals(4, set.size());
        Assert.assertEquals(4, g.getVerticesCount());
        Assert.assertEquals(true, set.contains(v1));
        Assert.assertEquals(true, set.contains(v2));
        Assert.assertEquals(false, set.contains(v3));
        Assert.assertEquals(true, set.contains(v4));
        Assert.assertEquals(true, set.contains(v5));
        try {
            set.clear();
            fail("Expected immutability exception");
        } catch (Exception ex) {
        }
    }

    public void testEdgeSet() throws Exception {
        Graph g = new GraphImpl();
        Vertex v1 = new VertexImpl();
        Vertex v2 = new VertexImpl();
        Vertex v3 = new VertexImpl();
        g.add(v1);
        g.add(v2);
        g.add(v3);
        Edge e1 = g.addEdge(v1, v2);
        Edge e2 = new EdgeImpl(v2, v3);
        g.addEdge(e2);
        g.addEdge(e2);
        Edge e3 = new EdgeImpl(v1, v3);
        Edge e4 = new EdgeImpl(v1, v2);
        Edge e5 = g.addEdge(v3, v3);
        g.addEdge(e3);
        g.addEdge(e3);
        g.removeEdge(e3);
        Set set = g.getEdgeSet();
        Assert.assertEquals(3, set.size());
        Assert.assertEquals(3, g.getEdgesCount());
        Assert.assertEquals(true, set.contains(e1));
        Assert.assertEquals(true, set.contains(e2));
        Assert.assertEquals(false, set.contains(e3));
        Assert.assertEquals(false, set.contains(e4));
        Assert.assertEquals(true, set.contains(e5));
        List v1edges = g.getEdges(v1);
        List v2edges = g.getEdges(v2);
        List v3edges = g.getEdges(v3);
        Assert.assertEquals(1, v1edges.size());
        Assert.assertEquals(1, g.getDegree(v1));
        Assert.assertEquals(true, v1edges.contains(e1));
        Assert.assertEquals(2, v2edges.size());
        Assert.assertEquals(2, g.getDegree(v2));
        Assert.assertEquals(true, v2edges.contains(e1));
        Assert.assertEquals(true, v2edges.contains(e2));
        Assert.assertEquals(2, v3edges.size());
        Assert.assertEquals(2, g.getDegree(v3));
        Assert.assertEquals(true, v3edges.contains(e2));
        Assert.assertEquals(true, v3edges.contains(e5));
        List v1adj = g.getAdjacentVertices(v1);
        List v2adj = g.getAdjacentVertices(v2);
        List v3adj = g.getAdjacentVertices(v3);
        Assert.assertEquals(1, v1adj.size());
        Assert.assertEquals(true, v1adj.contains(v2));
        Assert.assertEquals(2, v2adj.size());
        Assert.assertEquals(true, v2adj.contains(v1));
        Assert.assertEquals(true, v2adj.contains(v3));
        Assert.assertEquals(2, v3adj.size());
        Assert.assertEquals(true, v3adj.contains(v2));
        Assert.assertEquals(true, v3adj.contains(v3));
        try {
            set.clear();
            fail("Expected immutability exception");
        } catch (Exception ex) {
        }
        try {
            v1edges.clear();
            fail("Expected immutability exception");
        } catch (Exception ex) {
        }
    }

    public void testRemoveEdges() throws Exception {
        Graph g = new GraphImpl();
        Vertex v1 = new VertexImpl();
        Vertex v2 = new VertexImpl();
        Vertex v3 = new VertexImpl();
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        Edge e = g.addEdge(v1, v3);
        g.removeEdges(v2);
        Set edgeSet = g.getEdgeSet();
        Assert.assertEquals(1, edgeSet.size());
        Assert.assertEquals(true, edgeSet.contains(e));
        Set vertexSet = g.getVertexSet();
        Assert.assertEquals(3, vertexSet.size());
    }

    public void testRemoveVertex() throws Exception {
        Graph g = new GraphImpl();
        Vertex v1 = new VertexImpl();
        Vertex v2 = new VertexImpl();
        Vertex v3 = new VertexImpl();
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        Edge e = g.addEdge(v1, v3);
        g.remove(v2);
        Set edgeSet = g.getEdgeSet();
        Assert.assertEquals(1, edgeSet.size());
        Assert.assertEquals(true, edgeSet.contains(e));
        Set vertexSet = g.getVertexSet();
        Assert.assertEquals(2, vertexSet.size());
    }

    public static Object cloneViaSerialization(Object o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    public void testSerialization() throws Exception {
        Graph g = new SmallGraph();
        g = (Graph) cloneViaSerialization(g);
    }
}
