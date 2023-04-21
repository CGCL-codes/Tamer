package toxtree.plugins.verhaar2.test.rules.class1;

import toxTree.core.IDecisionRule;
import toxtree.plugins.verhaar2.test.rules.AbstractRuleTest;
import verhaar.rules.Rule152;

public class Rule152Test extends AbstractRuleTest {

    @Override
    protected IDecisionRule createRule() {
        return new Rule152();
    }

    public void test() throws Exception {
        Object[][] answer = { { "CCCCCCCC(O)", Boolean.TRUE }, { "CO", Boolean.TRUE }, { "CCO", Boolean.TRUE }, { "C#CCO", Boolean.FALSE }, { "CCC=CCO", Boolean.FALSE }, { "c1ccccc1O", Boolean.FALSE }, { "CC(C)(O)C", Boolean.TRUE } };
        ruleTest(answer);
    }
}
