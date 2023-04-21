package org.nakedobjects.noa.facets.propparam.validate.mandatory;

import org.nakedobjects.noa.interactions.InteractionContext;
import org.nakedobjects.noa.interactions.InvalidException;

/**
 * The interaction is invalid because the property or
 * action parameter is mandatory (eg not annotated with <tt>@Optional</tt>).
 */
public class InvalidMandatoryException extends InvalidException {

    private static final long serialVersionUID = 1L;

    public InvalidMandatoryException(InteractionContext ic) {
        this(ic, "Mandatory");
    }

    public InvalidMandatoryException(InteractionContext ic, String message) {
        super(ic, message);
    }
}
