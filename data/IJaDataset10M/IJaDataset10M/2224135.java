package org.openjgraph.model.adjacencymatrix;

import java.util.Set;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openjgraph.model.Edge;
import org.openjgraph.model.Vertex;

/**
 *
 * @author Benjamin Ratti
 */
public class AdjacencyMatrixVertex extends Vertex {

    private AdjacencyMatrixGraph graph;

    private String index;

    /**
     * Ce contructeur permet de construire un AdjencyMatrixVertex en lui
     * précisant le graphe le contenant et l'index du sommet dans le graphe.
     * @param graph graphe contenant le sommet
     * @param index index du sommet dans le graphe
     */
    public AdjacencyMatrixVertex(AdjacencyMatrixGraph graph, String index) {
        this.graph = graph;
        this.index = index;
    }

    @Override
    public Set<Edge> getEdges() {
        return graph.getEdge(index);
    }

    @Override
    public String getId() {
        return graph.getVertexId(index);
    }

    @Override
    public String toString() {
        return graph.getVertexId(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdjacencyMatrixVertex other = (AdjacencyMatrixVertex) obj;
        if ((this.index == null) ? (other.index != null) : !this.index.equals(other.index)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(index).toHashCode();
    }

    @Override
    public int getDegree() {
        int sum = 0;
        for (Edge e : this.getEdges()) {
            sum += e.getValue();
        }
        for (Vertex v : graph.getVertexSet()) {
            sum -= graph.getEdgeValue(v.getId(), this.getId());
        }
        return sum;
    }
}
