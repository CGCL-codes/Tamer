package org.nakedobjects.noa.facets.actions.validate;

import org.nakedobjects.noa.interactions.InteractionContext;
import org.nakedobjects.noa.interactions.InvalidException;

public class ActionArgumentsInvalidException extends InvalidException {

    private static final long serialVersionUID = 1L;

    public ActionArgumentsInvalidException(InteractionContext ic) {
        this(ic, "Invalid arguments");
    }

    public ActionArgumentsInvalidException(InteractionContext ic, String message) {
        super(ic, message);
    }
}
