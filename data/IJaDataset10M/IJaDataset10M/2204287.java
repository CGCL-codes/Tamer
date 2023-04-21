package org.genxdm.processor.w3c.xs.exception.cvc;

import org.genxdm.xs.components.ElementDefinition;
import org.genxdm.xs.resolve.LocationInSchema;

@SuppressWarnings("serial")
public final class CvcElementAbstractException extends CvcElementException {

    public CvcElementAbstractException(final ElementDefinition elementDeclaration, final LocationInSchema location) {
        super(PART_ABSTRACT, elementDeclaration, location);
    }

    @Override
    public String getMessage() {
        return "The {abstract} property for the element declaration '" + getElementDeclaration() + "' must be false.";
    }
}
