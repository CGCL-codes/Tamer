package org.atlanalyzer.metaelements.atl;

import org.atlanalyzer.metaelements.ocl.VariableDeclaration;

/**
 * @author andreza
 *
 */
public class RuleVariableDeclaration extends VariableDeclaration {

    private Rule rule;

    /**
	 * @return the rule
	 */
    public Rule getRule() {
        return rule;
    }

    /**
	 * @param rule the rule to set
	 */
    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
