package org.xmlcml.cml.converters.marker.regex;

import nu.xom.Element;

public class Quantifier extends AbstractRegexElement {

    public static final String QUANTIFIER_TAG = "quantifier";

    public Quantifier() {
        super(QUANTIFIER_TAG);
    }
}
