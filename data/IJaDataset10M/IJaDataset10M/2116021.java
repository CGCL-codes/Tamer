package org.genxdm.processor.w3c.xs.exception;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.exceptions.SchemaException;

/**
 * Used to indicate an implementation limitations
 */
@SuppressWarnings("serial")
public abstract class SicException extends SchemaException {

    public static final String PART_OVERSIZED_INTEGER = "1";

    public SicException(final String partNumber) {
        super(ValidationOutcome.SIC_Limitation, partNumber);
    }

    public SicException(final String partNumber, final SchemaException cause) {
        super(ValidationOutcome.SIC_Limitation, partNumber, PreCondition.assertArgumentNotNull(cause, "cause"));
    }
}
