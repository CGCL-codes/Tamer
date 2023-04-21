package it.unisannio.rcost.callgraphanalyzer.util;

import it.unisannio.rcost.callgraphanalyzer.Graph;
import it.unisannio.rcost.callgraphanalyzer.Node;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.gmf.runtime.notation.Diagram;

public final class CallGraphDiagramRegistry {

    private static CallGraphDiagramRegistry registry = new CallGraphDiagramRegistry();

    private Hashtable<String, Graph> graphs = new Hashtable<String, Graph>();

    protected CallGraphDiagramRegistry() {
    }

    public static CallGraphDiagramRegistry getInstance() {
        return registry;
    }

    public void addGraph(Diagram diagram, Graph graph) {
        graphs.put(diagram.getName(), graph);
    }

    public void removeGraph(Diagram diagram) {
        graphs.remove(diagram.getName());
    }

    public Graph getGraph(String diagramName) {
        return graphs.get(diagramName);
    }

    public Hashtable<String, Graph> getAllGraphs() {
        return graphs;
    }

    public void updateGraph(String name, Graph graph) {
        this.graphs.remove(name);
        this.graphs.put(name, graph);
    }
}
