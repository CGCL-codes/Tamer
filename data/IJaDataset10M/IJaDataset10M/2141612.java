package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.ActiveSelector;
import com.volantis.mcs.themes.PseudoClassSelector;

/**
 * Represents a pseudo class selector of the form :active
 */
public class DefaultActiveSelector extends DefaultPseudoClassSelector implements ActiveSelector {

    /**
     * Initialize a new instance.
     */
    public DefaultActiveSelector() {
        super(PseudoClassTypeEnum.ACTIVE);
    }

    protected PseudoClassSelector copyImpl() {
        return new DefaultActiveSelector();
    }
}
