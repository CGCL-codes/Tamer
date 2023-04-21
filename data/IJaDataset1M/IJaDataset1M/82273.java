package mutant.test.rules;

import mutant.rules.SA28;
import mutant.rules.SA31a;
import mutant.test.TestMutantRules;
import toxTree.core.IDecisionRule;
import toxTree.logging.TTLogger;
import toxTree.tree.rules.smarts.AbstractRuleSmartSubstructure;

public class SA28Test extends TestMutantRules {

    @Override
    protected IDecisionRule createRuleToTest() throws Exception {
        return new SA28();
    }

    @Override
    public String getHitsFile() {
        return "NA28/sa18_l_iss2_updated.sdf";
    }

    @Override
    public String getResultsFolder() {
        return "NA28";
    }

    public void testNnotinring() {
        try {
            TTLogger.configureLog4j(true);
            assertFalse(verifyRule(ruleToTest, "ON2CCC=1C=CC=CC=12"));
        } catch (Exception x) {
            fail(x.getMessage());
        }
    }

    public void testSO3H() {
        try {
            assertTrue(applySmarts(SA28.amine_and_SO3H, "[H]C(=C([H])C=1C([H])=C([H])C(=C([H])C=1S(=O)(=O)[O-])N([H])[H])C=2C([H])=C([H])C(=C([H])C=2S(=O)(=O)[O-])N([H])[H]"));
            assertFalse(applySmarts(SA28.amine_and_SO3H, "O=S(=O)([O-])C2=CC=CC=C2(CC=1C=CC=C(N)C=1)"));
            assertTrue(applySmarts(SA28.amine_and_SO3H, "O=S(=O)([O-])C1=CC=C(N)C=C1"));
            assertTrue(applySmarts(SA28.amine_and_SO3H, "O=S(=O)([O-])C1=CC=CC2=CC(N)=CC=C12"));
            assertTrue(applySmarts(SA28.amine_and_SO3H, "O=S(=O)([O-])C=1C=CC2=CC(N)=CC=C2(C=1)"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testOrthoSubstitution_ignoreAromatic() {
        try {
            assertTrue(verifyRule(ruleToTest, "CC=1C=CC=2C=CC=CC=2(C=1(N))"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void test669() {
        try {
            assertFalse(verifyRule(ruleToTest, "[H]C(=C([H])C=1C([H])=C([H])C(=C([H])C=1S(=O)(=O)[O-])N([H])[H])C=2C([H])=C([H])C(=C([H])C=2S(=O)(=O)[O-])N([H])[H]"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void test_ortho_carboxylicacid() throws Exception {
        exclusionRuleTest(SA28.index_ortho_carboxylicacid);
    }

    public void test_ortho_disubstituted() throws Exception {
        exclusionRuleTest(SA28.index_ortho_disubstitution);
    }

    public void test_SO3H_rule() throws Exception {
        exclusionRuleTest(SA28.index_so3h_1);
        exclusionRuleTest(SA28.index_so3h_2);
        exclusionRuleTest(SA28.index_so3h_3);
    }

    public void exclusionRuleTest(int index) throws Exception {
        assertTrue(verifySMARTS((AbstractRuleSmartSubstructure) ruleToTest, SA28.exclusion_rules[index][1].toString(), SA28.exclusion_rules[index][2].toString()) > 0);
        assertEquals(((Boolean) SA28.exclusion_rules[index][4]).booleanValue(), verifyRule(ruleToTest, SA28.exclusion_rules[index][2].toString()));
    }
}
