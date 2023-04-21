package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.MCSConcealedSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoClassSelector;

/**
 * Represents a pseudo class selector of the form :mcs-concealed
 */
public class DefaultMCSConcealedSelector extends DefaultPseudoClassSelector implements MCSConcealedSelector {

    /**
     * Initialize a new instance.
     */
    public DefaultMCSConcealedSelector() {
        super(PseudoClassTypeEnum.MCS_CONCEALED);
    }

    protected PseudoClassSelector copyImpl() {
        return new DefaultMCSConcealedSelector();
    }
}
