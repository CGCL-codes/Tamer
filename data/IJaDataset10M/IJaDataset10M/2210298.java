package org.jrcaf.rule.registry;

import org.jrcaf.rule.IRuleDefinition;

/**
 *  Rules registry interface
 */
public interface IRulesRegistry {

    /**
    * Returns the rule definition for the passed id.
    * @param aId The id of the rule definition.
    * @return The rule definition for the passed id.
    * @category Getter
    */
    public abstract IRuleDefinition getRuleDefinitionFor(String aId);
}
