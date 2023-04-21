package fr.cephb.lindenb.graph.nodes;

import org.w3c.dom.Element;
import fr.cephb.lindenb.bio.ncbi.Database;
import fr.cephb.lindenb.graph.Graph;
import fr.cephb.lindenb.graph.Node;

public class Omim extends Node {

    public Omim(Element e, Graph g, int id) {
        super(e, g, id);
    }

    @Override
    public Database getDatabase() {
        return Database.omim;
    }
}
