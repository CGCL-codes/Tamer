package org.axsl.fo.fo.prop;

import org.axsl.common.value.Span;
import org.axsl.fo.FoContext;

/**
 * The XSL-FO span property.
 */
public interface SpanPa {

    /**
     * Returns the "span" trait for this FO.
     * @param context An object that knows how to resolve FO Tree context
     * issues.
     * @return The effective span trait for this FO.
     * For FOs that are not block-level FOs, this will always return
     * {@link Span#ALL}.
     * @see "XSL-FO Recommendation 1.1, Section 7.20.4"
     * @see "XSL-FO Recommendation 1.1, Section 7.21.4"
     */
    Span traitSpan(FoContext context);
}
