package org.eclipse.core.internal.databinding.validation;

import org.eclipse.core.internal.databinding.conversion.NumberToDoubleConverter;
import org.eclipse.core.internal.databinding.conversion.StringToNumberParser;

/**
 * Validates if a Number can fit in a Double.
 * <p>
 * Class is thread safe.
 * </p>
 * @since 1.0
 */
public class NumberToDoubleValidator extends NumberToNumberValidator {

    private static final Double MIN = new Double(Double.MIN_VALUE);

    private static final Double MAX = new Double(Double.MAX_VALUE);

    /**
	 * @param converter
	 */
    public NumberToDoubleValidator(NumberToDoubleConverter converter) {
        super(converter, MIN, MAX);
    }

    protected boolean inRange(Number number) {
        return StringToNumberParser.inDoubleRange(number);
    }
}
