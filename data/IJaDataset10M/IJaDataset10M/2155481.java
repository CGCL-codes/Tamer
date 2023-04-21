package org.panopticode.java.rdf;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import org.panopticode.PanopticodeOntology;
import org.panopticode.java.JavaOntology;
import org.panopticode.rdf.PanopticodeRDFStore;
import org.panopticode.rdf.ResourceNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class DefaultMethodStorage implements MethodStorage {

    private PanopticodeRDFStore store;

    DefaultMethodStorage(PanopticodeRDFStore rdfStore) {
        store = rdfStore;
    }

    public List<Resource> fetchAll() {
        LinkedList<Resource> results;
        ResultSet rs;
        String sparqlQuery;
        sparqlQuery = "SELECT ?method " + "WHERE { " + "?method  <" + RDF.type + "> <" + JavaOntology.METHOD_TYPE + "> " + "}";
        rs = store.select(sparqlQuery);
        results = new LinkedList<Resource>();
        while (rs.hasNext()) {
            Resource methodResource = rs.nextSolution().getResource("method");
            results.add(methodResource);
        }
        return results;
    }

    public List<Resource> fetchByFullyQualifiedName(final String name) {
        LinkedList<Resource> results;
        ResultSet rs;
        String sparqlQuery;
        sparqlQuery = "SELECT ?method " + "WHERE { " + "?method  <" + JavaOntology.FULLY_QUALIFIED_METHOD_NAME + "> \"" + name + "\" " + "}";
        rs = store.select(sparqlQuery);
        results = new LinkedList<Resource>();
        while (rs.hasNext()) {
            Resource methodResource = rs.nextSolution().getResource("method");
            results.add(methodResource);
        }
        return results;
    }

    public Resource getByPathAndLine(final String path, final long line) throws ResourceNotFoundException {
        String sparqlQuery = "SELECT ?method " + "WHERE { " + "?file  <" + PanopticodeOntology.FILE_PATH + ">  \"" + path + "\" . " + "?file  <" + JavaOntology.HAS_TYPE + "> ?type . " + "{ " + "{ " + "?type  <" + JavaOntology.HAS_CONSTRUCTOR + "> ?method " + "} UNION { " + "?type  <" + JavaOntology.HAS_METHOD + "> ?method " + "} " + "} . " + "?method  <" + PanopticodeOntology.STARTS_AT + "> ?position . " + "?position  <" + PanopticodeOntology.LINE + "> " + line + " " + "}";
        return store.getResource(sparqlQuery, "method");
    }
}
