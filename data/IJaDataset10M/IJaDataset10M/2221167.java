package mutant.test.rules;

import java.util.Iterator;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import toxTree.core.IDecisionRule;
import mutant.rules.SA27;
import mutant.test.TestMutantRules;

public class SA27Test extends TestMutantRules {

    @Override
    protected IDecisionRule createRuleToTest() throws Exception {
        return new SA27();
    }

    @Override
    public String getHitsFile() {
        return "NA27/sa2_l_iss2.sdf";
    }

    @Override
    public String getResultsFolder() {
        return "NA27";
    }

    public void testSO3H() throws Exception {
        assertFalse(verifyRule(ruleToTest, "CCC=1C=C(C(=CC=1[N+](=O)[O-])C(CCO)CCO)S(O)(=O)=O"));
    }

    public void testSO3HSmarts() throws Exception {
        assertTrue(applySmarts("[$(a(N(O)=O):a[#16X4](=[OX1])(=[OX1])[OX2H1]),$(a(N(O)=O):a:a[#16X4](=[OX1])(=[OX1])[OX2H1]),$(a(N(O)=O):a:a:a[#16X4](=[OX1])(=[OX1])[OX2H1]),$(a(N(O)=O):a:a:a:a[#16X4](=[OX1])(=[OX1])[OX2H1])]", "CCC=1C=C(C(=CC=1[N+](=O)[O-])C(CCO)CCO)S(O)(=O)=O"));
    }

    public void test_ortho_carboxylicacid() throws Exception {
        assertFalse(verifyRule(ruleToTest, "O=[N+]([O-])C=1C=CC=CC=1(C(O)=O)"));
    }

    /**
     * [a;!$(a(a[A;!#1])(a[A;!#1]))]([$([N+]([O-])=O),$([N](=O)=O)])
     * @throws Exception
     */
    public void test56() throws Exception {
        assertTrue(verifyRule(ruleToTest, "CNC=1C=CC(=CC=1[N+](=O)[O-])N(CCO)CCO"));
        assertTrue(verifyRule(ruleToTest, "[H]OC([H])([H])C([H])([H])N(C=1C([H])=C([H])C(=C(C=1([H]))[N+](=O)[O-])N([H])C([H])([H])[H])C([H])([H])C([H])([H])O[H]"));
    }

    public void test125() throws Exception {
        assertTrue(verifyRule(ruleToTest, "NC=1C=C(C=CC=1(C(=O)O))[N+](=O)[O-]"));
        assertTrue(verifyRule(ruleToTest, "[H]OC(=O)C=1C([H])=C([H])C(=C([H])C=1N([H])[H])[N+](=O)[O-]"));
    }

    public void test_ortho_disubstitution() throws Exception {
        String smarts = "[a;!$(a(a[A;!#1])(a[A;!#1]))]([$([N+]([O-])=O)])";
        Boolean yes = new Boolean(true);
        Boolean no = new Boolean(false);
        Object[][] smiles = { { "CC=1C=CC=CC=1[N+](=O)[O-]", yes }, { "[H]OC([H])([H])C([H])([H])N(C=1C([H])=C([H])C(=C(C=1([H]))[N+](=O)[O-])N([H])C([H])([H])[H])C([H])([H])C([H])([H])O[H]", yes }, { "[H]C1=C([H])C([H])=C(C(=C1([H]))[N+](=O)[O-])Cl", yes }, { "O=[N+]([O-])C1=CC=CC=C1Cl", yes }, { "O=[N+]([O-])C1=CC=CC=C1", yes }, { "[H]C1=C([H])C([H])=C(C([H])=C1([H]))[N+](=O)[O-]", yes }, { "O=[N+]([O-])C1=C(C)C=CC=C1(C)", no }, { "[H]C1=C([H])C(=C(C(=C1([H]))C([H])([H])[H])[N+](=O)[O-])C([H])([H])[H]", no }, { "O=[N+]([O-])C2=C3C=CC=CC3(=CC=1C=CC=CC=12)", yes }, { "[H]C=1C([H])=C([H])C=3C(C=1([H]))=C([H])C=2C([H])=C([H])C([H])=C([H])C=2C=3[N+](=O)[O-]", yes } };
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        for (int i = 0; i < smiles.length; i++) {
            IAtomContainer ac = sp.parseSmiles(smiles[i][0].toString());
            System.out.print(smiles[i][0]);
            System.out.print("\tExpected\t");
            System.out.print(smiles[i][1]);
            assertEquals(((Boolean) smiles[i][1]).booleanValue(), match(ac, smarts));
            System.out.println("\tOK");
        }
    }

    protected boolean match(IAtomContainer mol, String smarts) throws Exception {
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Iterator<IAtom> atoms = mol.atoms();
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            if ("H".equals(atom.getSymbol())) atom.setAtomicNumber(1);
        }
        CDKHydrogenAdder h = CDKHydrogenAdder.getInstance(mol.getBuilder());
        h.addImplicitHydrogens(mol);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
        CDKHueckelAromaticityDetector.detectAromaticity(mol);
        SMARTSQueryTool sqt = new SMARTSQueryTool(smarts, true);
        return sqt.matches(mol);
    }
}
