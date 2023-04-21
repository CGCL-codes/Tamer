package org.processmining.mining.fuzzymining.graph;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import org.processmining.framework.models.DotFileWriter;

public class Edges implements DotFileWriter {

    protected HashSet<Edge> edges;

    protected FuzzyGraph graph;

    protected double attenuationThreshold;

    public Edges(FuzzyGraph graph) {
        this.graph = graph;
        edges = new HashSet<Edge>();
        attenuationThreshold = 1.0;
    }

    public void setAttenuationThreshold(double attThreshold) {
        attenuationThreshold = attThreshold;
    }

    public void addEdge(Node source, Node target, double significance, double correlation) {
        Edge edge = new Edge(source, target, significance, correlation);
        if (edges.contains(edge)) {
            for (Edge oE : edges) {
                if (oE.equals(edge)) {
                    if (edge.significance > oE.significance) {
                        oE.significance = edge.significance;
                    }
                    if (edge.correlation > oE.correlation) {
                        oE.correlation = edge.correlation;
                    }
                }
                break;
            }
        } else {
            edges.add(edge);
        }
    }

    public Edge getEdge(Node source, Node target) {
        for (Edge edge : edges) {
            if (edge.source.equals(source) && edge.target.equals(target)) {
                return edge;
            }
        }
        return null;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void writeToDot(Writer bw) throws IOException {
        for (Edge edge : edges) {
            edge.setAttenuationThreshold(attenuationThreshold);
            edge.writeToDot(bw);
        }
    }
}
