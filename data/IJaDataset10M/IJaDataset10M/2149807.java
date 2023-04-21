package org.jaxup;

import org.jaxen.JaxenException;

/** A context node or object was not of the right type or in the right state
 *  to be used by the method to which it was provided. For example, the context
 *  node is not part of an XML document, or it wasn't of the right java type
 *  to be a node in an XML document for a particular Updater implementation.
 *
 *  @author Erwin Bolwidt
 */
public class InvalidContextException extends UpdateException {

    public InvalidContextException(String message) {
        super(message);
    }
}
