package org.tripcom.query.client.org.w3c.rdf.model;

/**
 * This exception indicates that the model cannot be modified
 */
public class ImmutableModelException extends ModelException {

    public ImmutableModelException(String msg) {
        super(msg);
    }
}
