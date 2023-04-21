package org.jmlspecs.jir.openjml.option;

import ie.ucd.clops.runtime.rules.RuleStore;

/**
 * The rule store is used to handle the constraints and the validity checks
 * associated with the options.
 * 
 * @author The CLOPS team (kind@ucd.ie)
 */
public class JirEmbedderRuleStore extends RuleStore {

    public JirEmbedderRuleStore() {
    }

    @Override
    protected final boolean shouldApplyFlyRulesTransitively() {
        return false;
    }
}
