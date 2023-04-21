package org.axsl.fo.fo.prop;

import org.axsl.fo.FoContext;

/**
 * The XSL-FO color-profile-name property.
 */
public interface ColorProfileNamePa {

    /**
     * Returns the "color-profile-name" trait for this FO.
     * @param context An object that knows how to resolve FO Tree context
     * issues.
     * @return The "color-profile-name" trait.
     * @see "XSL-FO Recommendation 1.0, Section 7.17.2"
     * @see "XSL-FO Recommendation 1.1, Section 7.18.2"
     */
    String traitColorProfileName(FoContext context);
}
