package org.ontoware.rdf2go.model.node;

/**
 * marker interface
 * 
 * Implementations are expected to have valid implementations of equals( Object )
 * and hashCode()
 * 
 * @author voelkel
 * 
 */
public interface BlankNode extends Resource {

    String getInternalID();
}
