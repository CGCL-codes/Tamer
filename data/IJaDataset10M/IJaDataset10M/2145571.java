package org.genxdm.processor.w3c.xs.exception.cvc;

import org.genxdm.xs.components.ElementDefinition;
import org.genxdm.xs.resolve.LocationInSchema;

@SuppressWarnings("serial")
public final class CvcElementNotNillableException extends CvcElementException {

    public CvcElementNotNillableException(final ElementDefinition decl, final LocationInSchema location) {
        super(PART_NOT_NILLABLE, decl, location);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CvcElementNotNillableException) {
            final CvcElementNotNillableException e = (CvcElementNotNillableException) obj;
            return getElementDeclaration().equals(e.getElementDeclaration());
        } else {
            return false;
        }
    }

    @Override
    public String getMessage() {
        return "The element declaration '" + getElementDeclaration() + "' is not nillable.";
    }
}
