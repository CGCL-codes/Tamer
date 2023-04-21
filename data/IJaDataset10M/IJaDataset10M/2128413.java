package org.qedeq.kernel.bo.logic.proof.common;

import org.qedeq.kernel.se.common.RuleKey;

/**
 * Gives rule information.
 *
 * @author  Michael Meyling
 */
public interface RuleChecker {

    /**
     * Check if a rule is already defined.
     *
     * @param   ruleName    Name of rule.
     * @return  Maximum rule version. Might be <code>null</code>.
     */
    public RuleKey getRule(String ruleName);
}
