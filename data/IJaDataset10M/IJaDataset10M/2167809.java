package ForceGraph;

import UADgraphEditor.LinkGroup;
import UADgraphEditor.NodeGroup;
import java.awt.Color;
import java.util.Vector;

public class Graph extends Cell {

    public Vector vertices = new Vector();

    public Vector edges = new Vector();

    public Graph() {
        this(3);
    }

    public Graph(int d) {
        setDimensions(d);
    }

    public Vertex insertVertex() {
        Vertex v = new Vertex(this);
        vertices.add(v);
        return v;
    }

    public Vertex insertVertex(String label) {
        Vertex v = new Vertex(this);
        vertices.add(v);
        return v;
    }

    public Edge insertEdge(Vertex from, Vertex to, double lm) {
        Edge e = new Edge(this, from, to, lm);
        from.insertNeighbor(e);
        to.insertNeighbor(e);
        edges.add(e);
        return e;
    }

    public void deleteVertex(Vertex v) {
        Edge e;
        try {
            for (int i = 0; i < v.inEdges.size(); i++) {
                e = (Edge) v.inEdges.get(i);
                ((Vertex) v.inNeighbors.get(i)).deleteNeighbor(e);
                edges.remove(e);
            }
            for (int i = 0; i < v.outEdges.size(); i++) {
                e = (Edge) v.outEdges.get(i);
                ((Vertex) v.outNeighbors.get(i)).deleteNeighbor(e);
                edges.remove(e);
            }
            vertices.remove(v);
        } catch (Exception ex) {
            throw new Error(v + ex.getMessage());
        }
    }

    public void deleteEdge(Edge e) {
        try {
            e.getFrom().deleteNeighbor(e);
            e.getTo().deleteNeighbor(e);
            edges.remove(e);
        } catch (Exception ex) {
            throw new Error(e + ex.getMessage());
        }
    }

    @Override
    void recomputeBoundaries() {
        int d = getDimensions();
        Vertex v;
        double lo[] = getMin(), hi[] = getMax();
        for (int i = 0; i < d; i++) {
            lo[i] = Double.MAX_VALUE;
            hi[i] = -Double.MAX_VALUE;
        }
        for (int i = 0; i < vertices.size(); i++) {
            v = (Vertex) vertices.get(i);
            double c[] = v.getCoords();
            for (int j = 0; j < d; j++) {
                lo[j] = Math.min(lo[j], c[j]);
                hi[j] = Math.max(hi[j], c[j]);
            }
        }
        recomputeSize();
    }

    private int numberOfMarkedVertices = 0;

    boolean isConnected() {
        if (vertices.size() == 0) {
            return false;
        }
        for (int i = 0; i < vertices.size(); i++) {
            ((Vertex) vertices.get(i)).booleanField = false;
        }
        numberOfMarkedVertices = 0;
        dft(((Vertex) vertices.get(0)));
        return (numberOfMarkedVertices == vertices.size());
    }

    private void dft(Vertex v) {
        Vertex neighbor;
        v.booleanField = true;
        ++numberOfMarkedVertices;
        for (int i = 0; i < v.inEdges.size(); i++) {
            neighbor = (Vertex) v.inNeighbors.get(i);
            if (!neighbor.booleanField) {
                dft(neighbor);
            }
        }
        for (int i = 0; i < v.outEdges.size(); i++) {
            neighbor = (Vertex) v.outNeighbors.get(i);
            if (!neighbor.booleanField) {
                dft(neighbor);
            }
        }
    }

    public int getNumberOfVertices() {
        return vertices.size();
    }

    public int getNumberOfEdges() {
        return edges.size();
    }

    public Vertex getVertex(int i) {
        return (Vertex) vertices.get(i);
    }

    public Edge getEdge(int i) {
        return (Edge) edges.get(i);
    }
}
