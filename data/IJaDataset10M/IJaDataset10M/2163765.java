package mutant.test.rules;

import mutant.rules.SA20;
import mutant.test.TestMutantRules;
import toxTree.core.IDecisionRule;

public class SA20Test extends TestMutantRules {

    @Override
    protected IDecisionRule createRuleToTest() throws Exception {
        return new SA20();
    }

    @Override
    public String getHitsFile() {
        return "NA20/onc_23_fixed.sdf";
    }

    @Override
    public String getResultsFolder() {
        return "NA20";
    }
}
