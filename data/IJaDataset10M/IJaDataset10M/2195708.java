package nl.utwente.ewi.stream.network.provenance;

import java.util.Collection;
import java.util.Vector;

public class TripleCollection {

    private Vector<Triple> collection = new Vector<Triple>();

    public TripleCollection() {
    }

    public TripleCollection(Collection<Triple> col) {
        collection.addAll(col);
    }

    public void addTriple(Triple triple) {
        collection.add(triple);
    }

    public Collection<Triple> getCollection() {
        return collection;
    }
}
