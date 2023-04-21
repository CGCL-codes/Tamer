package net.sf.metarbe;

import java.util.Map;

/**
 * Stateful session contains number of rules which activation depends on stateful activation strategy.
 * Normally, stateful session keeps state of rules beween several {@link #executeRules} invocations. 
 * Stateful session may cache processing data in order to help identify exact rule context.
 *     
 *
 */
public interface StatefulRuleSession extends RuleSession {

    /**
	 * Adds strategy for given context to cache data processed
	 * @param aRuleContext context to identify the data
	 * @param aValidation strategy object that is responsible for caching
	 */
    void addContextValidation(RuleContext aRuleContext, RuleContextValidation aValidation);

    /**
	 * Removes strategy for given context to cache data processed
	 * @param aRuleContext context to identify the data
	 */
    void removeContextValidation(RuleContext aRuleContext);

    /**
	 * Gets strategy per gievn context to cache data
	 * @param aRuleContext 
	 * @return strategy object 
	 */
    RuleContextValidation getContextValidation(RuleContext aRuleContext);

    /**
     * Gets all registered context validations 
     * @return unmodifiable map of validations
     */
    public Map<RuleContext, RuleContextValidation> getContextValidations();
}
