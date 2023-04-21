package org.axsl.fo.fo.prop;

import org.axsl.common.value.Break;
import org.axsl.fo.FoContext;

/**
 * The XSL-FO break-after property.
 */
public interface BreakAfterPa {

    /**
     * Returns the "break-after" trait for this FO.
     * @param context An object that knows how to resolve FO Tree context
     * issues.
     * @return The "break-after" trait for this FO..
     * @see "XSL-FO Recommendation 1.0, Section 7.19.1"
     * @see "XSL-FO Recommendation 1.1, Section 7.20.1"
     */
    Break traitBreakAfter(FoContext context);
}
