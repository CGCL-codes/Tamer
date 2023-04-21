package jbnc.graphs;

import jbnc.util.Timer;
import java.util.*;

/**
 *  Description of the Class
 *
 * @author     Jarek Sacha
 * @since      June 1, 1999
 */
public class Graph {

    /** */
    protected HashMap vertices = new HashMap();

    /**
   *  Test class Graph.
   *
   * @param  arg  The command line arguments
   */
    public static void main(String[] arg) {
        try {
            Timer t = new Timer();
            System.out.println("\nTesting class Graph.\n");
            Graph g = new Graph();
            Vertex v1 = new Vertex("1", 0);
            Vertex v2 = new Vertex("2", 1);
            Vertex v3 = new Vertex("3", 2);
            Edge e32 = new Edge(v3, v2);
            g.addVertex(v1);
            g.addEdge(e32);
            g.addEdge(v1, v2);
            g.dump();
            t.println("\nTotal time = ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * @return    The Edges value
   */
    public Edge[] getEdges() {
        Vector e = new Vector();
        Set s = vertices.entrySet();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            LinkedList l = (LinkedList) entry.getValue();
            if (l == null) {
                continue;
            }
            Iterator ic = l.iterator();
            while (ic.hasNext()) {
                e.add(ic.next());
            }
        }
        int size = e.size();
        if (size == 0) {
            return null;
        }
        Edge[] v = new Edge[size];
        for (int vi = 0; vi < size; ++vi) {
            v[vi] = (Edge) e.get(vi);
        }
        return v;
    }

    /**
   *  Returns number of vertices in the graph.
   *
   * @return    The NumberOfVertices value
   */
    public int getNumberOfVertices() {
        return vertices.size();
    }

    /**
   *  Return vertex with given index.
   *
   * @param  index  Description of Parameter
   * @return        The Vertex value
   */
    public Vertex getVertex(int index) {
        Set s = vertices.keySet();
        if (s == null || s.size() == 0) {
            return null;
        }
        Iterator i = s.iterator();
        while (i.hasNext()) {
            Vertex v = (Vertex) i.next();
            if (v.getIndex() == index) {
                return v;
            }
        }
        return null;
    }

    /**
   *  Return vertices as an array.
   *
   * @return    The Vertices value
   */
    public Vertex[] getVertices() {
        Set s = vertices.entrySet();
        int size = s.size();
        if (size == 0) {
            return null;
        }
        Vertex[] v = new Vertex[size];
        Iterator i = s.iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            Vertex vert = (Vertex) entry.getKey();
            v[vert.getIndex()] = vert;
        }
        return v;
    }

    /**
   *  Return all children nodes of the given parent node.
   *
   * @param  parent  Parent.
   * @return         Array of children if parent is present in the graph,
   *      otherwise null.
   */
    public Vertex[] getChildrenOf(Vertex parent) {
        LinkedList l = (LinkedList) vertices.get(parent);
        if (l == null) {
            return null;
        }
        Vertex[] children = new Vertex[l.size()];
        Iterator i = l.iterator();
        int count = -1;
        while (i.hasNext()) {
            Edge e = (Edge) i.next();
            children[++count] = e.getOutVertex();
        }
        return children;
    }

    /**
   *  Return all children nodes of the parent node with given index.
   *
   * @param  parentIndex  Description of Parameter
   * @return              Array of children if parent is present in the graph,
   *      otherwise null.
   */
    public int[] getChildrenOf(int parentIndex) {
        LinkedList l = null;
        Set s = vertices.entrySet();
        Iterator p = s.iterator();
        while (p.hasNext()) {
            Map.Entry e = (Map.Entry) p.next();
            Vertex v = (Vertex) e.getKey();
            if (parentIndex == v.getIndex()) {
                l = (LinkedList) e.getValue();
                break;
            }
        }
        if (l == null) {
            return null;
        }
        int[] children = new int[l.size()];
        Iterator i = l.iterator();
        int count = -1;
        while (i.hasNext()) {
            Edge e = (Edge) i.next();
            children[++count] = e.getOutVertex().getIndex();
        }
        return children;
    }

    /**  Remove all vertices and edges. */
    public void clear() {
        vertices.clear();
    }

    /**
   * @param  x              The feature to be added to the Vertex attribute
   * @exception  Exception  Vertex is null.
   */
    public void addVertex(Vertex x) throws Exception {
        if (x == null) {
            throw new Exception("Graph.addVertex:" + " vertex is null.");
        }
        if (vertices.containsKey(x)) {
            throw new Exception("Graph.addVertex:" + " vertex already present.");
        }
        vertices.put(x, null);
    }

    /**
   * @param  x              The feature to be added to the Edge attribute
   * @param  y              The feature to be added to the Edge attribute
   * @exception  Exception  Description of Exception
   */
    public void addEdge(Vertex x, Vertex y) throws Exception {
        Edge e = new Edge(x, y);
        addEdge(e);
    }

    /**
   * @param  e              The feature to be added to the Edge attribute
   * @exception  Exception  Description of Exception
   */
    public void addEdge(Edge e) throws Exception {
        Vertex x = e.getInVertex();
        Vertex y = e.getOutVertex();
        if (!vertices.containsKey(y)) {
            addVertex(y);
        }
        if (!vertices.containsKey(x)) {
            addVertex(x);
        }
        LinkedList l = (LinkedList) vertices.get(x);
        if (l == null) {
            l = new LinkedList();
        }
        l.add(e);
        vertices.put(x, l);
    }

    /**
   *  Returns true if vertex x is present in the graph.
   *
   * @param  x  Description of Parameter
   * @return    Description of the Returned Value
   */
    public boolean contains(Vertex x) {
        return vertices.containsKey(x);
    }

    /**
   *  Remove directed edge from the graph.
   *
   * @param  in   First vertex.
   * @param  out  Secon vertex.
   * @return      The edge that have been removed. null if the edge was not
   *      present.
   */
    public Edge removeEdge(Vertex in, Vertex out) {
        LinkedList l = (LinkedList) vertices.get(in);
        if (l == null) {
            return null;
        }
        Edge e = null;
        Iterator i = l.iterator();
        while (i.hasNext()) {
            e = (Edge) i.next();
            if (out == e.getOutVertex()) {
                l.remove(e);
                break;
            }
        }
        return e;
    }

    /**  Description of the Method */
    public void dump() {
        if (vertices == null) {
            System.out.println("Graph has no vertices.");
            return;
        }
        System.out.println("Graph has " + vertices.size() + " vertices.");
        Set s = vertices.entrySet();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            Vertex vertex = (Vertex) entry.getKey();
            System.out.print("Vertex " + vertex.getName() + ": ");
            LinkedList l = (LinkedList) entry.getValue();
            if (l != null) {
                Iterator ic = l.iterator();
                while (ic.hasNext()) {
                    System.out.print(ic.next().toString() + " ");
                }
            }
            System.out.println("");
        }
    }
}
