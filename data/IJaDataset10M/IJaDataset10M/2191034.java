package org.axsl.fo.fo.prop;

import org.axsl.fo.FoContext;

/**
 * The XSL-FO border-separation property.
 */
public interface BorderSeparationPa {

    /**
     * Returns the IPD portion of the "border-separation" trait for this FO.
     * @param context An object that knows how to resolve FO Tree context
     * issues.
     * @return The length, in millipoints, of the IPD portion of the
     * "border-separation" trait for this FO.
     * @see "XSL-FO Recommendation 1.0, Section 7.26.5"
     * @see "XSL-FO Recommendation 1.1, Section 7.28.5"
     */
    int traitBorderSeparationIpd(FoContext context);

    /**
     * Returns the BPD portion of the "border-separation" trait for this FO.
     * @param context An object that knows how to resolve FO Tree context
     * issues.
     * @return The length, in millipoints, of the BPD portion of the
     * "border-separation" trait for this FO.
     * @see "XSL-FO Recommendation 1.0, Section 7.26.5"
     * @see "XSL-FO Recommendation 1.1, Section 7.28.5"
     */
    int traitBorderSeparationBpd(FoContext context);
}
