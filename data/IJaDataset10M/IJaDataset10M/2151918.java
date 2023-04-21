package org.monet.modelling.ide.builders.stages.semantic;

import java.util.ArrayList;
import java.util.Collection;

public class SemanticRuleFactory {

    private ArrayList<SemanticRule> ruleSet = new ArrayList<SemanticRule>();

    public Collection<SemanticRule> getRuleSet() {
        return ruleSet;
    }

    public SemanticRuleFactory() {
    }
}
