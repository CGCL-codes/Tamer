package org.deri.iris.rules;

import java.util.List;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.facts.IFacts;

/**
 * This pre-processor throws an exception if the specified list of rules
 * contains a rule with rule head equality.
 * 
 * @author Adrian Marte
 */
public class DisallowRuleHeadEquality implements IRuleHeadEqualityPreProcessor {

    public List<IRule> process(List<IRule> rules, IFacts facts) throws EvaluationException {
        for (IRule rule : rules) {
            if (RuleHeadEquality.hasRuleHeadEquality(rule)) {
                throw new EvaluationException("Rules with rule head equality are not supported.");
            }
        }
        return rules;
    }
}
