package mutant.test.rules.qsar;

import java.util.ArrayList;
import java.util.List;
import mutant.rules.RuleAromaticDiazo;
import mutant.test.TestMutantRules;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesGenerator;
import toxTree.core.IDecisionRule;
import toxTree.query.MolFlags;

public class RuleAromaticDiazoTest extends TestMutantRules {

    @Override
    protected IDecisionRule createRuleToTest() throws Exception {
        return new RuleAromaticDiazo();
    }

    @Override
    public String getHitsFile() {
        return "AromaticDiazo/hits.sdf";
    }

    @Override
    public String getResultsFolder() {
        return "AromaticDiazo";
    }

    public void testProducts() throws Exception {
        List<String> expected = new ArrayList<String>();
        expected.add("[H]N([H])C1=C([H])C([H])=C(C([H])=C1N([H])[H])N([H])[H]");
        expected.add("[H]N([H])C=1C([H])=C([H])C(=C([H])C=1([H]))C=2C([H])=C([H])C(=C([H])C=2([H]))N([H])[H]");
        expected.add("[H]N([H])C1=C(O[H])C=2C(C([H])=C1S(=O)(=O)[O-])=C([H])C(=C(C=2N([H])[H])N([H])[H])S(=O)(=O)[O-]");
        expected.add("[H]N([H])C=1C([H])=C([H])C([H])=C([H])C=1([H])");
        String smiles = "NC6=CC=C(N=NC1=CC=C(C=C1)C2=CC=C(C=C2)N=NC=4C(N)=C5C(O)=C(N=NC3=CC=CC=C3)C(=CC5(=CC=4S(=O)(=O)[O-]))S(=O)(=O)[O-])C(N)=C6";
        IAtomContainer c = toxTree.query.FunctionalGroups.createAtomContainer(smiles);
        toxTree.query.MolAnalyser.analyse(c);
        assertTrue(ruleToTest.verifyRule(c));
        MolFlags mf = (MolFlags) c.getProperty(MolFlags.MOLFLAGS);
        assertNotNull(mf);
        IAtomContainerSet products = mf.getResidues();
        assertEquals(4, products.getAtomContainerCount());
        SmilesGenerator g = new SmilesGenerator(true);
        for (int i = 0; i < products.getAtomContainerCount(); i++) {
            String sm = g.createSMILES((IMolecule) products.getAtomContainer(i));
            assertTrue(expected.indexOf(sm) > -1);
        }
    }
}
