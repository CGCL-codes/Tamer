package org.eclipse.core.internal.databinding.conversion;

import com.ibm.icu.text.NumberFormat;

/**
 * Converts from a Number to a Short.
 * <p>
 * Class is thread safe.
 * </p>
 * @since 1.0
 */
public class NumberToShortConverter extends NumberToNumberConverter {

    /**
	 * @param numberFormat
	 * @param fromType
	 * @param primitive
	 */
    public NumberToShortConverter(NumberFormat numberFormat, Class fromType, boolean primitive) {
        super(numberFormat, fromType, (primitive) ? Short.TYPE : Short.class);
    }

    protected Number doConvert(Number number) {
        if (StringToNumberParser.inShortRange(number)) {
            return new Short(number.shortValue());
        }
        return null;
    }
}
