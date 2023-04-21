package mutant.test.rules;

import mutant.rules.SA54_nogen;
import mutant.test.TestMutantRules;
import toxTree.core.IDecisionRule;

public class SA54_nogenTest extends TestMutantRules {

    @Override
    protected IDecisionRule createRuleToTest() throws Exception {
        return new SA54_nogen();
    }

    @Override
    public String getHitsFile() {
        return "NA54/NA54hits.sdf";
    }

    @Override
    public String getResultsFolder() {
        return "NA54";
    }
}
