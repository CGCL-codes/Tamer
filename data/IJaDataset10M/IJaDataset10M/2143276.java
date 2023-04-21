package org.genxdm.processor.w3c.xs.exception.cvc;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.processor.w3c.xs.exception.sm.SmLocationException;
import org.genxdm.xs.components.ElementDefinition;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.resolve.LocationInSchema;

@SuppressWarnings("serial")
public final class CvcSubstitutionBlockedByHeadTypeException extends SmLocationException {

    private final ElementDefinition elementDeclaration;

    public CvcSubstitutionBlockedByHeadTypeException(final ElementDefinition elementDeclaration, final LocationInSchema location) {
        super(ValidationOutcome.SCC_Substitution_Group_OK_Transitive, "2.1", location);
        this.elementDeclaration = PreCondition.assertArgumentNotNull(elementDeclaration, "elementDeclaration");
    }

    public String getMessage() {
        return "Substitution using element " + elementDeclaration + " is blocked by the type of the substitution group.";
    }
}
