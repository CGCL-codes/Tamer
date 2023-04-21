package org.eclipse.core.internal.databinding.validation;

import org.eclipse.core.internal.databinding.conversion.StringToNumberParser;

/**
 * Validates that a string is of the appropriate format and is in the range of
 * an long.
 * 
 * @since 1.0
 */
public class StringToLongValidator extends AbstractStringToNumberValidator {

    private static final Long MIN = new Long(Long.MIN_VALUE);

    private static final Long MAX = new Long(Long.MAX_VALUE);

    /**
	 * @param converter
	 */
    public StringToLongValidator(NumberFormatConverter converter) {
        super(converter, MIN, MAX);
    }

    protected boolean isInRange(Number number) {
        return StringToNumberParser.inLongRange(number);
    }
}
