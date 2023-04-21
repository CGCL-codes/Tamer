package org.genxdm.processor.w3c.xs.impl.xmlrep;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.exceptions.SchemaException;

@SuppressWarnings("serial")
final class XMLEnumerationException extends Exception {

    public XMLEnumerationException(final SchemaException cause) {
        super(PreCondition.assertArgumentNotNull(cause, "cause"));
    }

    @Override
    public SchemaException getCause() {
        return (SchemaException) super.getCause();
    }
}
