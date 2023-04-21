package eye.test;

import toxTree.core.IDecisionRule;
import eye.rules.Rule25;

public class Rule25Test extends TestExamples {

    @Override
    protected IDecisionRule createRuleToTest() {
        return new Rule25();
    }
}
