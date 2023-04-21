package edu.iastate.fgmine.graph;

import java.util.List;
import edu.iastate.fgmine.util.Cloneable;

/**
 * Undirected, No self-loops
 */
public interface Graph extends Cloneable<Graph> {

    Vertex getVertex(int index);

    List<Vertex> getVertices();

    int vertexSize();

    int edgeSize();

    Vertex addVertex(int label);

    void removeLastVertex();

    Edge addEdge(Vertex src, Vertex trg);

    Edge addEdge(int srcIndex, int trgIndex);

    void removeEdge(Edge e);

    void removeEdge(Vertex src, Vertex trg);

    void removeEdge(int srcIndex, int trgIndex);

    void removeEdgeWithLabels(int srcLabel, int trgLabel);
}
