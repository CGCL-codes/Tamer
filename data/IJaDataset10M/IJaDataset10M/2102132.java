package net.datao.jung.ontologiesItems;

import org.openrdf.elmo.annotations.rdf;

@rdf("http://workspace/type#ontVertex")
public interface OntVertex extends Vertex {

    @rdf("http://workspace/udc#hasDefault")
    public Ont getOnt();

    public void setOnt(Ont o);

    public boolean isImportFor(OntVertex possibleImporter);
}
