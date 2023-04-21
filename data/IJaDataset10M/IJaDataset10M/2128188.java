package edu.mit.lcs.haystack.rdf;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public interface IRDFEventSource {

    public void addRDFListener(Resource rdfListener, Resource subject, Resource predicate, RDFNode object, Resource cookie) throws RDFException;

    public void removeRDFListener(Resource cookie) throws RDFException;
}
