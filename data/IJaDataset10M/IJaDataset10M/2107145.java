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

public class DefaultClassStorage implements ClassStorage {

    private PanopticodeRDFStore store;

    DefaultClassStorage(PanopticodeRDFStore rdfStore) {
        store = rdfStore;
    }

    public Resource getByFullyQualifiedName(final String name) throws ResourceNotFoundException {
        String sparqlQuery = "SELECT ?class " + "WHERE { " + "?class  <" + JavaOntology.FULLY_QUALIFIED_CLASS_NAME + "> \"" + name + "\" " + "}";
        return store.getResource(sparqlQuery, "class");
    }

    public List<Resource> fetchByPackage(Resource packageResource) {
        String packageName;
        packageName = packageResource.getProperty(PanopticodeOntology.NAME).getString();
        return fetchByPackageName(packageName);
    }

    public List<Resource> fetchByPackageName(String packageName) {
        LinkedList<Resource> results;
        ResultSet rs;
        String sparqlQuery;
        sparqlQuery = "SELECT ?class " + "WHERE { " + "?package  <" + RDF.type.getURI() + "> <" + JavaOntology.PACKAGE_TYPE + "> ." + "?package  <" + PanopticodeOntology.NAME + "> \"" + packageName + "\" ." + "?package  <" + JavaOntology.HAS_FILE + "> ?file ." + "?file  <" + JavaOntology.HAS_TYPE + "> ?class " + "}";
        rs = store.select(sparqlQuery);
        results = new LinkedList<Resource>();
        while (rs.hasNext()) {
            Resource classResource = rs.nextSolution().getResource("class");
            results.add(classResource);
        }
        return results;
    }

    public List<Resource> fetchAll() {
        LinkedList<Resource> results;
        ResultSet rs;
        String sparqlQuery;
        sparqlQuery = "SELECT ?class " + "WHERE { " + "?x  <" + JavaOntology.HAS_TYPE + "> ?class " + "}";
        rs = store.select(sparqlQuery);
        results = new LinkedList<Resource>();
        while (rs.hasNext()) {
            Resource classResource = rs.nextSolution().getResource("class");
            results.add(classResource);
        }
        return results;
    }
}
