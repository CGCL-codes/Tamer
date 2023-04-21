package fr.jgraph.model.graph;

/** Graphe non multiple : un seul arc peut connecter un ensemble donn� de noeuds */
public class SimpleGraph extends GraphDecorator {

    public SimpleGraph(Graph g) {
        super(g);
    }
}
