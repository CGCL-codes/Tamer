package org.genxdm.processor.w3c.xs.exception.scc;

import java.util.Set;
import org.genxdm.xs.enums.DerivationMethod;
import org.genxdm.xs.exceptions.ComponentConstraintException;
import org.genxdm.xs.types.Type;

@SuppressWarnings("serial")
public final class SccComplexTypeBaseComplexDerivationException extends SccTypeDerivationOKComplexException {

    public SccComplexTypeBaseComplexDerivationException(final Type derivedType, final Type baseType, final Set<DerivationMethod> subset, final ComponentConstraintException cause) {
        super(PART_BASE_COMPLEX, derivedType, baseType, subset, cause);
    }

    @Override
    public String getMessage() {
        return "The {type definition} of " + getDerivedType().getName() + " must be validly derived from " + getBaseName() + ".";
    }
}
