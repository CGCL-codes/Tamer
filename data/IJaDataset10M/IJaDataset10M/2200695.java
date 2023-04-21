package eye.test;

import toxTree.core.IDecisionRule;
import eye.rules.Rule26;

public class Rule26Test extends TestExamples {

    @Override
    protected IDecisionRule createRuleToTest() {
        return new Rule26();
    }
}
