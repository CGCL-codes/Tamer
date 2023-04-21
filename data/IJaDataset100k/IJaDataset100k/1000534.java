package cramer2.test;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import toxTree.core.IDecisionRule;
import toxTree.exceptions.DecisionMethodException;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolAnalyser;
import toxTree.query.MolFlags;
import toxTree.tree.AbstractRule;
import toxTree.tree.cramer.Rule3FuncGroups;
import toxTree.tree.cramer.RuleHasOnlySaltSulphonateSulphate;

/**
 * @author nina
 *
 */
public class Rule3FuncGroupsTest extends AbstractRuleTest {

    @Override
    protected IDecisionRule createRule() {
        return new Rule3FuncGroups();
    }

    @Override
    public void test() throws Exception {
        Object[][] answer = { { "CC(=O)CCC(O)=O", new Boolean(false) }, { "[H]OC(=O)C([H])([H])C([H])([H])C(=O)C([H])([H])[H]", new Boolean(false) } };
        ruleTest(answer);
    }

    public void testSalt() throws Exception {
        IMolecule acid_original = (IMolecule) FunctionalGroups.createAtomContainer("CC(=O)CCC(=O)O");
        assertFalse(verify(acid_original));
        IMolecule mol = (IMolecule) FunctionalGroups.createAtomContainer("CC(=O)CCC(=O)[O-].[Na+]");
        RuleHasOnlySaltSulphonateSulphate rule4 = new RuleHasOnlySaltSulphonateSulphate();
        MolAnalyser.analyse(mol);
        assertTrue(rule4.verifyRule(mol));
        MolFlags mf = (MolFlags) mol.getProperty(MolFlags.MOLFLAGS);
        if (mf == null) throw new DecisionMethodException(AbstractRule.ERR_STRUCTURENOTPREPROCESSED);
        IAtomContainerSet acid = mf.getResidues();
        assertNotNull(acid);
        assertEquals(1, acid.getAtomContainerCount());
        assertTrue(UniversalIsomorphismTester.isIsomorph(acid_original, acid.getAtomContainer(0)));
        assertFalse(verify(acid.getAtomContainer(0)));
    }

    protected boolean verify(IAtomContainer a) throws Exception {
        FunctionalGroups.clearMarks(a);
        MolAnalyser.analyse(a);
        return rule2test.verifyRule(a);
    }
}
