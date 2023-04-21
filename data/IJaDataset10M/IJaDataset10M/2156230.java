package toxTree.cramer2;

import toxTree.core.IDecisionRule;
import cramer2.rules.RuleAcyclicAcetalEsterOfQ30;

/**
 * Test for {@link RuleAcyclicAcetalEsterOfQ30}
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-8-24
 */
public class RuleAcyclicAcetalEsterOfQ30Test extends AbstractRuleTest {

    @Override
    protected IDecisionRule createRule() {
        return new RuleAcyclicAcetalEsterOfQ30();
    }

    public void test() throws Exception {
        Object[][] answer = { { "COC(OC)C1=CC=CC=C1", new Boolean(true) }, { "c1ccc(cc1)N", new Boolean(false) }, { "O=C(OC)c1ccccc1(NC)", new Boolean(false) }, { "[H]OC([H])([H])[H]", new Boolean(false) } };
        ruleTest(answer);
    }
}
