package br.ufal.ic.joint.module.ontology.operations;

import java.util.List;

/**
 *  Class which implements the OntologyOperations interface
 *
 * @author Olavo Holanda
 * @version 1.0 - 15/01/2012
 */
public class OntologyOperationsImpl implements OntologyOperations {

    private OntologyRepositoryManager ontologyMgr;

    private OWLValidation validation;

    public OntologyOperationsImpl() {
        this.ontologyMgr = new OntologyRepositoryManager();
        this.validation = new OWLValidation();
    }

    /**
     * Add an ontology in the repository
     *
     * @param path
     *            the ontology path
     * @param uri
     *            the ontology uri
     */
    public void addOntology(String path, String ontologyURI) {
        this.ontologyMgr.addOntology(path, ontologyURI);
    }

    /**
     * Removes an ontology of the repository
     *
     * @param uri
     *            the ontology uri
     */
    public void deleteOntology(String ontologyURI) {
        this.ontologyMgr.deleteOntology(ontologyURI);
    }

    /**
     * Retrieves an ontology saving in the specified file path
     *
     * @param path
     *            the ontology file path
     * @param uri
     *            the ontology uri
     */
    public void retrieveOntology(String path, String ontologyURI) {
        this.ontologyMgr.retrieveOntology(path, ontologyURI);
    }

    /**
     * Updates an ontology in the repository
     *
     * @param path
     *            the ontology file path
     * @param uri
     *            the ontology uri
     */
    public void updateOntology(String path, String ontologyURI) {
        this.ontologyMgr.updateOntology(path, ontologyURI);
    }

    /**
     * Checks the ontology consistency
     *
     * @param path
     *            the ontology file path
     * @return boolean
     *            true if the consistency is ok, else false
     */
    public boolean checkOntologyConsistency(String path) {
        return this.validation.checkOntologyConsistency(path);
    }

    /**
     * Retrieves the OntologyCompiler to generate java code from ontologies
     *
     * @param path
     *            the jar file path wich the java code will be saved
     * @param urls
     *            a list with the ontologies URLs
     * @return compiler
     *            the compiler of ontologies
     */
    public OntologyCompiler getOntologyCompiler(String path, List<String> ontologiesURLs) {
        return new OntologyCompiler(path, ontologiesURLs);
    }
}
