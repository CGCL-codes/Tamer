package com.idna.gav.rules.international.impl;

import com.idna.common.utils.NlpHelper;
import com.idna.gav.rules.international.AbstractGavRule;

public class GavResponseSlovakiaRule extends AbstractGavRule {

    @Override
    protected boolean isValidCountryPostCode(String word) {
        return NlpHelper.isValidSlovakiaPostcode(word);
    }
}
