package net.sourceforge.pmd.eclipse.dao;

import net.sourceforge.pmd.RuleSet;

/**
 * Transfer Object for a Rule Set
 * 
 * @author Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.1  2005/12/30 16:25:39  phherlin
 * Implement a new preferences model
 *
 *
 */
public class RuleSetTO {

    private boolean override;

    private String ruleSetUrl;

    private RuleSet ruleSet;

    /**
     * @return Returns the override.
     */
    public boolean isOverride() {
        return this.override;
    }

    /**
     * @param override The override to set.
     */
    public void setOverride(boolean override) {
        this.override = override;
    }

    /**
     * @return Returns the ruleSet.
     */
    public RuleSet getRuleSet() {
        return this.ruleSet;
    }

    /**
     * @param ruleSet The ruleSet to set.
     */
    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    /**
     * @return Returns the ruleSetUrl.
     */
    public String getRuleSetUrl() {
        return this.ruleSetUrl;
    }

    /**
     * @param ruleSetUrl The ruleSetUrl to set.
     */
    public void setRuleSetUrl(String ruleSetUrl) {
        this.ruleSetUrl = ruleSetUrl;
    }
}
