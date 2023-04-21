package org.genxdm.processor.w3c.xs.exception.scc;

import java.util.Set;
import org.genxdm.xs.enums.DerivationMethod;
import org.genxdm.xs.exceptions.ComponentConstraintException;
import org.genxdm.xs.types.Type;

@SuppressWarnings("serial")
public final class SccComplexTypeBaseSimpleDerivationException extends SccTypeDerivationOKComplexException {

    public SccComplexTypeBaseSimpleDerivationException(final Type typeName, final Type baseName, final Set<DerivationMethod> subset, final ComponentConstraintException cause) {
        super(PART_BASE_SIMPLE, typeName, baseName, subset, cause);
    }

    @Override
    public String getMessage() {
        return "The {type definition} of " + getDerivedType().getName() + " must be validly derived from " + getBaseName() + ".";
    }
}
