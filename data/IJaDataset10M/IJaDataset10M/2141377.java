package toxtree.plugins.proteinbinding.test;

import toxTree.core.IDecisionRule;
import toxtree.plugins.proteinbinding.rules.MichaelAcceptorRule;

public class MichaeAcceptorsRuleTest extends TestProteinBindingRules {

    @Override
    public String getHitsFile() {
        return null;
    }

    @Override
    public String getResultsFolder() {
        return null;
    }

    @Override
    protected IDecisionRule createRuleToTest() throws Exception {
        return new MichaelAcceptorRule();
    }
}
