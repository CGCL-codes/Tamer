package ru.ksu.niimm.cll.mocassin.rdf.ontology;

public class OntologyBlankNode extends OntologyElement {

    public OntologyBlankNode() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OntologyBlankNode ? true : false;
    }
}
