package mutant.test.rules;

import mutant.rules.SA31c_nogen;
import mutant.test.TestMutantRules;
import toxTree.core.IDecisionRule;

public class SA31c_nogenTest extends TestMutantRules {

    @Override
    protected IDecisionRule createRuleToTest() throws Exception {
        return new SA31c_nogen();
    }

    @Override
    public String getHitsFile() {
        return "NA31c/halobenzodiox.sdf";
    }

    @Override
    public String getResultsFolder() {
        return "NA31c";
    }
}
