package org.ontoware.rdf2go.model.node.impl;

import org.ontoware.rdf2go.model.node.BlankNode;
import org.ontoware.rdf2go.model.node.Literal;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;

/**
 * Subclasses must have valid equals() and hashCode() implementations.
 * @author voelkel
 *
 */
public abstract class LiteralImpl implements Literal {

    /**
     * 
     */
    private static final long serialVersionUID = -4860142603655339470L;

    @Override
    public abstract String getValue();

    @Override
    public Resource asResource() throws ClassCastException {
        throw new ClassCastException("Literals are no resources");
    }

    @Override
    public Literal asLiteral() throws ClassCastException {
        return this;
    }

    @Override
    public URI asURI() throws ClassCastException {
        throw new ClassCastException("Literals are no URIs");
    }

    @Override
    public BlankNode asBlankNode() throws ClassCastException {
        throw new ClassCastException("Literals are no BlankNodes");
    }

    protected static String sparqlEncode(String raw) {
        String result = raw;
        result = result.replace("\\", "\\\\");
        result = result.replace("'", "\\'");
        result = result.replace("\"", "\\\"");
        return result;
    }
}
