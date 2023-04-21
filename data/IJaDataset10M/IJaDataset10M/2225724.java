package org.axsl.fo.fo.prop;

import org.axsl.common.value.AbsolutePosition;
import org.axsl.fo.FoContext;

/**
 * The XSL-FO Common Absolute Position properties.
 * @see "XSL-FO Recommendation 1.1, Section 7.6."
 */
public interface CommonAbsolutePositionPa extends CommonPositionPa {

    /**
     * Returns the "absolute-position" trait for this FO.
     * @param context An object that knows how to resolve FO Tree context
     * issues.
     * @return The "absolute-position" trait.
     * @see "XSL-FO Recommendation 1.0, Section 7.5.1"
     * @see "XSL-FO Recommendation 1.1, Section 7.6.1"
     */
    AbsolutePosition traitAbsolutePosition(FoContext context);
}
