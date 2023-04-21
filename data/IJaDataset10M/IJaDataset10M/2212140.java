package org.genxdm.xs.exceptions;

import org.genxdm.exceptions.PreCondition;

/**
 * This exception arises when a choice is made to abort the current schema processing. This choice always follows the
 * raising of an {@link SchemaException}. This exception is intentionally not-derived from {@link SchemaException}.
 */
@SuppressWarnings("serial")
public final class AbortException extends Exception {

    public AbortException(final SchemaException cause) {
        super(PreCondition.assertArgumentNotNull(cause, "cause"));
    }

    @Override
    public SchemaException getCause() {
        return (SchemaException) super.getCause();
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }
}
