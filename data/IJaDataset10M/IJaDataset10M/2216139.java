package org.openscience.cdk.smiles;

import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.isomorphism.IsomorphismTester;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.BondManipulator;

/**
 * Please see the test.gui package for visual feedback on tests.
 * 
 * @author         steinbeck
 * @cdk.module     test-smiles
 * @cdk.created    2003-09-19
 * 
 * @see org.openscience.cdk.gui.smiles.SmilesParserTest
 */
public class SmilesParserTest extends CDKTestCase {

    private static SmilesParser sp = new SmilesParser(NoNotificationChemObjectBuilder.getInstance());

    /** @cdk.bug 1363882 */
    @Test(timeout = 1000)
    public void testBug1363882() throws Exception {
        String smiles = "[H]c2c([H])c(c1c(nc(n1([H]))C(F)(F)F)c2Cl)Cl";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(18, mol.getAtomCount());
        Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(mol));
    }

    /** @cdk.bug 1535587 */
    @Test(timeout = 1000)
    public void testBug1535587() throws Exception {
        String smiles = "COC(=O)c2ccc3n([H])c1ccccc1c3(c2)";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(18, mol.getAtomCount());
        Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(mol));
        Assert.assertEquals("N", mol.getAtom(8).getSymbol());
        Assert.assertTrue(mol.getAtom(8).getFlag(CDKConstants.ISAROMATIC));
    }

    /** @cdk.bug 1579235 */
    @Test(timeout = 1000)
    public void testBug1579235() throws Exception {
        String smiles = "c2cc1cccn1cc2";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(9, mol.getAtomCount());
        Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(mol));
        Assert.assertEquals("N", mol.getAtom(6).getSymbol());
        for (IAtom atom : mol.atoms()) {
            if (atom.getSymbol().equals("C")) {
                Assert.assertEquals(IAtomType.Hybridization.SP2, atom.getHybridization());
            } else {
                Assert.assertEquals(IAtomType.Hybridization.PLANAR3, atom.getHybridization());
            }
        }
    }

    @Test(timeout = 1000)
    public void testBug1579229() throws Exception {
        String smiles = "c1c(c23)ccc(c34)ccc4ccc2c1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(14, mol.getAtomCount());
        Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(mol));
        for (IAtom atom : mol.atoms()) {
            Assert.assertEquals(IAtomType.Hybridization.SP2, atom.getHybridization());
        }
    }

    /** @cdk.bug 1579230 */
    @Test(timeout = 1000)
    public void testBug1579230() throws Exception {
        String smiles = "Cc1cccc2sc3nncn3c12";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(13, mol.getAtomCount());
        Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(mol));
        for (int i = 1; i < 13; i++) {
            IAtom atom = mol.getAtom(i);
            if (atom.getSymbol().equals("C")) Assert.assertEquals(IAtomType.Hybridization.SP2, atom.getHybridization());
            if (atom.getSymbol().equals("N") || atom.getSymbol().equals("S")) {
                Assert.assertTrue(IAtomType.Hybridization.SP2 == atom.getHybridization() || IAtomType.Hybridization.PLANAR3 == atom.getHybridization());
            }
        }
    }

    @org.junit.Test(timeout = 1000)
    public void testPyridine_N_oxideUncharged() throws Exception {
        String smiles = "O=n1ccccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(7, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testPyridine_N_oxideCharged() throws Exception {
        String smiles = "[O-][n+]1ccccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(7, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testPositivePhosphor() throws Exception {
        String smiles = "[Cl+3]([O-])([O-])([O-])[O-].[P+]([O-])(c1ccccc1)(c1ccccc1)c1cc([nH0+](C)c(c1)c1ccccc1)c1ccccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(0, mol.getAtom(22).getHydrogenCount().intValue());
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(38, mol.getAtomCount());
        Assert.assertEquals("P", mol.getAtom(5).getSymbol());
        Assert.assertEquals(+1, mol.getAtom(5).getFormalCharge().intValue());
        Assert.assertEquals("Cl", mol.getAtom(0).getSymbol());
        Assert.assertEquals(+3, mol.getAtom(0).getFormalCharge().intValue());
    }

    @org.junit.Test(timeout = 1000)
    public void testUnusualConjugatedRings() throws Exception {
        String smiles = "c1(Cl)cc2c3cc(Cl)c(Cl)cc3c2cc1Cl";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(16, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testUnusualConjugatedRings_2() throws Exception {
        String smiles = "c(c(ccc1)ccc2)(c1c(c3ccc4)c4)c23";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(16, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testUnusualConjugatedRings_3() throws Exception {
        Assume.assumeTrue(runSlowTests());
        String smiles = "c2ccc1cc3c(cc1c2)c4cccc5cccc3c45";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(20, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testUnusualConjugatedRings_4() throws Exception {
        String smiles = "Nc1c(c23)cccc3c4ccccc4c2cc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(17, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testUnusualConjugatedRings_5() throws Exception {
        String smiles = "c12ccccc1cc3c4ccccc4c5c3c2ccc5";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(20, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void test187_78_0() throws Exception {
        String smiles = "c1c(c23)ccc(c34)ccc4ccc2c1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(14, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void test187_78_0_PubChem() throws Exception {
        String smiles = "C1=CC2=C3C(=CC=C4C3=C1C=C4)C=C2";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(14, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void test41814_78_2() throws Exception {
        String smiles = "Cc1cccc2sc3nncn3c12";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(13, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void test239_64_5() throws Exception {
        String smiles = "c1ccc4c(c1)ccc5c3ccc2ccccc2c3nc45";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(21, mol.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testIndolizine() throws Exception {
        String smiles = "c2cc1cccn1cc2";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(9, mol.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSmiles1() throws Exception {
        String smiles = "C1c2c(c3c(c(O)cnc3)cc2)CC(=O)C1";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertEquals(16, molecule.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSmiles2() throws Exception {
        String smiles = "O=C(O3)C1=COC(OC4OC(CO)C(O)C(O)C4O)C2C1C3C=C2COC(C)=O";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertEquals(29, molecule.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSmiles3() throws Exception {
        String smiles = "CN1C=NC2=C1C(N(C)C(N2C)=O)=O";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertEquals(14, molecule.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSmiles4() throws Exception {
        String smiles = "CN(C)CCC2=CNC1=CC=CC(OP(O)(O)=O)=C12";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertEquals(19, molecule.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSmiles5() throws Exception {
        String smiles = "O=C(O)C1C(OC(C3=CC=CC=C3)=O)CC2N(C)C1CC2";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertEquals(21, molecule.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSmiles6() throws Exception {
        String smiles = "C1(C2(C)(C))C(C)=CCC2C1";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertEquals(10, molecule.getAtomCount());
    }

    @org.junit.Test(timeout = 1000)
    public void testSmiles7() throws Exception {
        String smiles = "C1(C=C(C=C(C=C(C=C(C=CC%35=C%36)C%31=C%35C%32=C%33C%36=C%34)C%22=C%31C%23=C%32C%24=C%25C%33=C%26C%34=CC%27=CC%28=CC=C%29)C%14=C%22C%15=C%23C%16=C%24C%17=C%18C%25=C%19C%26=C%27C%20=C%28C%29=C%21)C6=C%14C7=C%15C8=C%16C9=C%17C%12=C%11C%18=C%10C%19=C%20C%21=CC%10=CC%11=CC(C=C%30)=C%12%13)=C(C6=C(C7=C(C8=C(C9=C%13C%30=C5)C5=C4)C4=C3)C3=C2)C2=CC=C1";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertNotNull(molecule);
    }

    @org.junit.Test(timeout = 1000)
    public void testSmiles8() throws Exception {
        String smiles = "CC1(C(=C(CC(C1)O)C)C=CC(=CC=CC(=CC=CC=C(C=CC=C(C=CC1=C(CC(CC1(C)C)O)C)C)C)C)C)C";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertNotNull(molecule);
    }

    @org.junit.Test(timeout = 1000)
    public void testSmiles9() throws Exception {
        String smiles = "NC(C(C)C)C(NC(C(C)O)C(NC(C(C)C)C(NC(CCC(N)=O)C(NC(CC([O-])[O-])C(NCC(NC(CC(N)=O)C(NC(Cc1ccccc1)C(NC(CO)C(NC(Cc2ccccc2)C(NC(CO)C(NC(CC(C)C)C(NC(CCC([O-])[O-])C(NC(CO)C(NC(C(C)C)C(NC(CCCC[N+])C(NC(CCCC[N+])C(NC(CC(C)C)C(NC(CCCC[N+])C(NC(CC([O-])[O-])C(NC(CC(C)C)C(NC(CCC(N)=O)C(NC(CCC([O-])[O-])C(N3CCCC3C(NC(CCC(N)=O)C(NC(CCC([O-])[O-])C(N4CCCC4C(NC(CCCNC([N+])[N+])C(NC(C(C)C)C(NCC(NC(CCCC[N+])C(NC(CC(C)C)C(NC(CCCNC([N+])[N+])C(NC(CC(N)=O)C(NC(Cc5ccccc5)C(NC(C)C(N6CCCC6C(NC(C(C)CC)C(N7CCCC7C(NCC(NC(CCC([O-])[O-])C(N8CCCC8C(NC(C(C)C)C(NC(C(C)C)C(N9CCCC9C(NC(C(C)CC)C(NC(CC(C)C)C(NC%19C[S][S]CC(C(NC(CCCC[N+])C(NC(CCC([O-])[O-])C(N%10CCCC%10C(NC(CC(N)=O)C(NC(C)C(NC(CCC(N)=O)C(NC(CCC([O-])[O-])C(NC(C(C)CC)C(NC(CC(C)C)C(NC(CCC(N)=O)C(NC(CCCNC([N+])[N+])C(NC(CC(C)C)C(NC(CCC([O-])[O-])C(NC(CCC([O-])[O-])C(NC(C(C)CC)C(NC(C)C(NC(CCC([O-])[O-])C(NC(CC([O-])[O-])C(N%11CCCC%11C(NCC(NC(C(C)O)C(NC%14C[S][S]CC%13C(NC(C(C)O)C(NCC(NC(C[S][S]CC(C(NC(C)C(NC(Cc%12ccc(O)cc%12)C(NC(C)C(NC(C)C(N%13)=O)=O)=O)=O)=O)NC(=O)C(C(C)CC)NC(=O)C(CCC([O-])[O-])NC%14=O)C(O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)NC(=O)C(CC(C)C)NC(=O)C%15CCCN%15C(=O)C(CCCC[N+])NC(=O)C(CC(C)C)NC(=O)C(CCC([O-])[O-])NC(=O)C(CCC([O-])[O-])NC(=O)C%16CCCN%16C(=O)C(Cc%17ccccc%17)NC(=O)C(CC(N)=O)NC(=O)C%18CCCN%18C(=O)C(CC(N)=O)NC(=O)C(CO)NC%19=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O)=O";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertNotNull(molecule);
    }

    /**
	 * @cdk.bug 1296113
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug1296113() throws Exception {
        String smiles = "S(=O)(=O)(-O)-c1c2c(c(ccc2-N-c2ccccc2)-N=N-c2c3c(c(cc2)-N=N-c2c4c(c(ccc4)-S(=O)(=O)-O)ccc2)cccc3)ccc1";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertNotNull(molecule);
    }

    /**
     * @cdk.bug 1324105
     */
    @org.junit.Test(timeout = 1000)
    public void testAromaticSmiles2() throws Exception {
        String smiles = "n12:n:n:n:c:2:c:c:c:c:1";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Iterator bonds = molecule.bonds().iterator();
        while (bonds.hasNext()) Assert.assertTrue(((IBond) bonds.next()).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
	 * A unit test for JUnit. It is currently ignored because the SMILES
	 * given is invalid: the negative has an implied zero hydrogen count,
	 * making it have an unfilled valency.
	 */
    @Ignore
    @Test(timeout = 1000)
    public void testAromaticSmilesWithCharge() throws Exception {
        String smiles = "c1cc[c-]c1";
        IMolecule molecule = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(molecule);
        Assert.assertTrue(molecule.getAtom(0).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(0).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testAromaticSmiles() throws Exception {
        String smiles = "c1ccccc1";
        IMolecule molecule = sp.parseSmiles(smiles);
        Assert.assertTrue(molecule.getAtom(0).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(0).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
	 * @cdk.bug 630475
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug630475() throws Exception {
        String smiles = "CC1(C(=C(CC(C1)O)C)C=CC(=CC=CC(=CC=CC=C(C=CC=C(C=CC1=C(CC(CC1(C)C)O)C)C)C)C)C)C";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertTrue(mol.getAtomCount() > 0);
    }

    /**
	 * @cdk.bug 585811
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug585811() throws Exception {
        String smiles = "CC(C(C8CCC(CC8)=O)C3C4C(CC5(CCC(C9=CC(C=CN%10)=C%10C=C9)CCCC5)C4)C2CCC1CCC7(CCC7)C6(CC6)C1C2C3)=O";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertTrue(mol.getAtomCount() > 0);
    }

    /**
	 * @cdk.bug 593648
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug593648() throws Exception {
        String smiles = "CC1=CCC2CC1C(C)2C";
        IMolecule mol = sp.parseSmiles(smiles);
        IMolecule apinene = mol.getBuilder().newMolecule();
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addAtom(mol.getBuilder().newAtom("C"));
        apinene.addBond(0, 1, IBond.Order.DOUBLE);
        apinene.addBond(1, 2, IBond.Order.SINGLE);
        apinene.addBond(2, 3, IBond.Order.SINGLE);
        apinene.addBond(3, 4, IBond.Order.SINGLE);
        apinene.addBond(4, 5, IBond.Order.SINGLE);
        apinene.addBond(5, 0, IBond.Order.SINGLE);
        apinene.addBond(0, 6, IBond.Order.SINGLE);
        apinene.addBond(3, 7, IBond.Order.SINGLE);
        apinene.addBond(5, 7, IBond.Order.SINGLE);
        apinene.addBond(7, 8, IBond.Order.SINGLE);
        apinene.addBond(7, 9, IBond.Order.SINGLE);
        IsomorphismTester it = new IsomorphismTester(apinene);
        Assert.assertTrue(it.isIsomorphic(mol));
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testReadingOfTwoCharElements() throws Exception {
        String smiles = "[Na+]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals("Na", mol.getAtom(0).getSymbol());
    }

    @org.junit.Test(timeout = 1000)
    public void testReadingOfOneCharElements() throws Exception {
        String smiles = "[K+]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals("K", mol.getAtom(0).getSymbol());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testOrganicSubsetUnderstanding() throws Exception {
        String smiles = "[Ni+2]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals("Ni", mol.getAtom(0).getSymbol());
        smiles = "Ni";
        mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals("N", mol.getAtom(0).getSymbol());
        Assert.assertEquals("I", mol.getAtom(1).getSymbol());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testMassNumberReading() throws Exception {
        String smiles = "[13C]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals("C", mol.getAtom(0).getSymbol());
        Assert.assertEquals(13, mol.getAtom(0).getMassNumber().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testFormalChargeReading() throws Exception {
        String smiles = "[OH-]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals("O", mol.getAtom(0).getSymbol());
        Assert.assertEquals(-1, mol.getAtom(0).getFormalCharge().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testReadingPartionedMolecules() throws Exception {
        String smiles = "[Na+].[OH-]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(0, mol.getBondCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testExplicitSingleBond() throws Exception {
        String smiles = "C-C";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(1, mol.getBondCount());
        Assert.assertEquals(IBond.Order.SINGLE, mol.getBond(0).getOrder());
    }

    /**
	 * @cdk.bug 1175478
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug1175478() throws Exception {
        String smiles = "c1cc-2c(cc1)C(c3c4c2onc4c(cc3N5CCCC5)N6CCCC6)=O";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(27, mol.getAtomCount());
        Assert.assertEquals(32, mol.getBondCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testUnkownAtomType() throws Exception {
        String smiles = "*C";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(1, mol.getBondCount());
        Assert.assertTrue(mol.getAtom(0) instanceof IPseudoAtom);
        Assert.assertFalse(mol.getAtom(1) instanceof IPseudoAtom);
        smiles = "[*]C";
        mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(1, mol.getBondCount());
        Assert.assertTrue(mol.getAtom(0) instanceof IPseudoAtom);
        Assert.assertFalse(mol.getAtom(1) instanceof IPseudoAtom);
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testBondCreation() throws Exception {
        String smiles = "CC";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(1, mol.getBondCount());
        smiles = "cc";
        mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(1, mol.getBondCount());
    }

    /**
	 * @cdk.bug 784433
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug784433() throws Exception {
        String smiles = "c1cScc1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(5, mol.getAtomCount());
        Assert.assertEquals(5, mol.getBondCount());
    }

    /**
	 * @cdk.bug 873783.
	 */
    @org.junit.Test(timeout = 1000)
    public void testProton() throws Exception {
        String smiles = "[H+]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals(1, mol.getAtom(0).getFormalCharge().intValue());
    }

    /**
	 * @cdk.bug 881330.
	 */
    @org.junit.Test(timeout = 1000)
    public void testSMILESFromXYZ() throws Exception {
        String smiles = "C.C.N.[Co].C.C.C.[H].[He].[H].[H].[H].[H].C.C.[H].[H].[H].[H].[H]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(20, mol.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSingleBracketH() throws Exception {
        String smiles = "[H]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testSingleH() {
        try {
            String smiles = "H";
            sp.parseSmiles(smiles);
            Assert.fail("The SMILES string 'H' is not valid: H is not in the organic element subset");
        } catch (Exception e) {
        }
    }

    /**
	 * @cdk.bug 862930.
	 */
    @org.junit.Test(timeout = 1000)
    public void testHydroxonium() throws Exception {
        String smiles = "[H][O+]([H])[H]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(4, mol.getAtomCount());
    }

    /**
	 * @cdk.bug 809412
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug809412() throws Exception {
        String smiles = "Nc4cc3[n+](c2c(c1c(cccc1)cc2)nc3c5c4cccc5)c6c7c(ccc6)cccc7";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(33, mol.getAtomCount());
    }

    /**
	 * A bug found with JCP.
	 *  
	 * @cdk.bug 956926
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug956926() throws Exception {
        String smiles = "[c+]1ccccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(1, mol.getAtom(0).getFormalCharge().intValue());
        for (int i = 0; i < mol.getAtomCount(); i++) {
            Assert.assertEquals(2, mol.getConnectedAtomsCount(mol.getAtom(i)));
        }
        int hCount = 0;
        for (int i = 0; i < mol.getAtomCount(); i++) {
            hCount += mol.getAtom(i).getHydrogenCount();
        }
        Assert.assertEquals(5, hCount);
    }

    /**
	 * A bug found with JCP.
	 * 
	 * @cdk.bug   956929
	 * @cdk.inchi InChI=1/C4H5N/c1-2-4-5-3-1/h1-5H 
	 */
    @org.junit.Test(timeout = 1000)
    public void testPyrole() throws Exception {
        String smiles = "c1ccc[NH]1";
        IMolecule mol = sp.parseSmiles(smiles);
        StructureDiagramGenerator sdg = new StructureDiagramGenerator(mol);
        sdg.generateCoordinates();
        for (int i = 0; i < mol.getAtomCount(); i++) {
            if (mol.getAtom(i).getSymbol().equals("N")) {
                Assert.assertEquals(IBond.Order.SINGLE, ((IBond) mol.getConnectedBondsList(mol.getAtom(i)).get(0)).getOrder());
                Assert.assertEquals(IBond.Order.SINGLE, ((IBond) mol.getConnectedBondsList(mol.getAtom(i)).get(1)).getOrder());
            }
        }
    }

    @org.junit.Test(timeout = 1000)
    public void testHardCodedHydrogenCount() throws Exception {
        String smiles = "c1ccc[NH]1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtom(4).getHydrogenCount().intValue());
        smiles = "[n]1cc[nH]c1";
        mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtom(4).getHydrogenCount().intValue());
        Assert.assertEquals(0, mol.getAtom(0).getHydrogenCount().intValue());
        smiles = "[nH]1cc[n]c1";
        mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtom(0).getHydrogenCount().intValue());
        Assert.assertEquals(0, mol.getAtom(3).getHydrogenCount().intValue());
    }

    /**
	 * A bug found with JCP.
	 * 
	 * @cdk.bug 956929 
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug956929() throws Exception {
        String smiles = "Cn1cccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        StructureDiagramGenerator sdg = new StructureDiagramGenerator(mol);
        sdg.generateCoordinates();
        Assert.assertEquals(6, mol.getAtomCount());
        org.openscience.cdk.interfaces.IAtom nitrogen = mol.getAtom(1);
        Assert.assertEquals("N", nitrogen.getSymbol());
        List<IBond> bondsList = mol.getConnectedBondsList(nitrogen);
        Assert.assertEquals(3, bondsList.size());
        int totalBondOrder = BondManipulator.getSingleBondEquivalentSum(bondsList);
        Assert.assertEquals(3.0, totalBondOrder, 0.001);
    }

    /**
	 * A bug found with JCP.
	 * 
	 * @cdk.bug 956921
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug956921() throws Exception {
        String smiles = "[cH-]1cccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(5, mol.getAtomCount());
        java.util.Iterator atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atomi = (IAtom) atoms.next();
            Assert.assertEquals(1, atomi.getHydrogenCount().intValue());
            Assert.assertEquals(2, mol.getConnectedAtomsCount(atomi));
        }
        Assert.assertEquals(-1, mol.getAtom(0).getFormalCharge().intValue());
    }

    /**
	 * @cdk.bug 1274464
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug1274464() throws Exception {
        IAtomContainer fromSmiles = new SmilesParser(DefaultChemObjectBuilder.getInstance()).parseSmiles("C1=CC=CC=C1");
        AtomContainer fromFactory = MoleculeFactory.makeBenzene();
        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(fromFactory.getBuilder());
        Iterator<IAtom> atoms = fromFactory.atoms().iterator();
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(fromFactory.getBuilder());
        while (atoms.hasNext()) {
            IAtom nextAtom = atoms.next();
            IAtomType type = matcher.findMatchingAtomType(fromFactory, nextAtom);
            AtomTypeManipulator.configure(nextAtom, type);
            hAdder.addImplicitHydrogens(fromFactory, nextAtom);
        }
        CDKHueckelAromaticityDetector.detectAromaticity(fromFactory);
        boolean result = UniversalIsomorphismTester.isIsomorph(fromFactory, fromSmiles);
        Assert.assertTrue(result);
    }

    /**
	 * @cdk.bug 1095696
	 */
    @org.junit.Test(timeout = 1000)
    public void testSFBug1095696() throws Exception {
        String smiles = "Nc1ncnc2[nH]cnc12";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(10, mol.getAtomCount());
        Assert.assertEquals("N", mol.getAtom(6).getSymbol());
        Assert.assertEquals(1, mol.getAtom(6).getHydrogenCount().intValue());
    }

    /**
	 *  Example taken from 'Handbook of Chemoinformatics', Gasteiger, 2003, page 89
	 *  (Part I).
	 */
    @org.junit.Test(timeout = 1000)
    public void testNonBond() throws Exception {
        String sodiumPhenoxide = "c1cc([O-].[Na+])ccc1";
        IMolecule mol = sp.parseSmiles(sodiumPhenoxide);
        Assert.assertEquals(8, mol.getAtomCount());
        Assert.assertEquals(7, mol.getBondCount());
        IMoleculeSet fragments = ConnectivityChecker.partitionIntoMolecules(mol);
        int fragmentCount = fragments.getMoleculeCount();
        Assert.assertEquals(2, fragmentCount);
        org.openscience.cdk.interfaces.IMolecule mol1 = fragments.getMolecule(0);
        org.openscience.cdk.interfaces.IMolecule mol2 = fragments.getMolecule(1);
        Assert.assertEquals(6, Math.abs(mol1.getAtomCount() - mol2.getAtomCount()));
    }

    /**
	 *  Example taken from 'Handbook of Chemoinformatics', Gasteiger, 2003, page 89
	 *  (Part I).
	 */
    @org.junit.Test(timeout = 1000)
    public void testConnectedByRingClosure() throws Exception {
        String sodiumPhenoxide = "C1.O2.C12";
        IMolecule mol = sp.parseSmiles(sodiumPhenoxide);
        Assert.assertEquals(3, mol.getAtomCount());
        Assert.assertEquals(2, mol.getBondCount());
        IMoleculeSet fragments = ConnectivityChecker.partitionIntoMolecules(mol);
        int fragmentCount = fragments.getMoleculeCount();
        Assert.assertEquals(1, fragmentCount);
        org.openscience.cdk.interfaces.IMolecule mol1 = fragments.getMolecule(0);
        Assert.assertEquals(3, mol1.getAtomCount());
    }

    /**
	 *  Example taken from 'Handbook of Chemoinformatics', Gasteiger, 2003, page 89
	 *  (Part I).
	 */
    @org.junit.Test(timeout = 1000)
    public void testReaction() throws Exception {
        String reactionSmiles = "O>>[H+].[OH-]";
        IReaction reaction = sp.parseReactionSmiles(reactionSmiles);
        Assert.assertEquals(1, reaction.getReactantCount());
        Assert.assertEquals(2, reaction.getProductCount());
    }

    /**
	 *  Example taken from 'Handbook of Chemoinformatics', Gasteiger, 2003, page 90
	 *  (Part I).
	 */
    @org.junit.Test(timeout = 1000)
    public void testReactionWithAgents() throws Exception {
        String reactionSmiles = "CCO.CC(=O)O>[H+]>CC(=O)OCC.O";
        IReaction reaction = sp.parseReactionSmiles(reactionSmiles);
        Assert.assertEquals(2, reaction.getReactantCount());
        Assert.assertEquals(2, reaction.getProductCount());
        Assert.assertEquals(1, reaction.getAgents().getMoleculeCount());
        Assert.assertEquals(1, reaction.getAgents().getMolecule(0).getAtomCount());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount() throws Exception {
        String smiles = "C";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals(4, mol.getAtom(0).getHydrogenCount().intValue());
    }

    /**
	 * @cdk.bug 2028780
	 */
    @Test(timeout = 1000)
    public void testTungsten() throws Exception {
        String smiles = "[W]";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(1, mol.getAtomCount());
        Assert.assertEquals("W", mol.getAtom(0).getSymbol());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount2() throws Exception {
        String smiles = "CC";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(3, mol.getAtom(0).getHydrogenCount().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount2b() throws Exception {
        String smiles = "C=C";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(2, mol.getAtom(0).getHydrogenCount().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount2c() throws Exception {
        String smiles = "C#C";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(1, mol.getAtom(0).getHydrogenCount().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount3() throws Exception {
        String smiles = "CCC";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(3, mol.getAtomCount());
        Assert.assertEquals(2, mol.getAtom(1).getHydrogenCount().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount4() throws Exception {
        String smiles = "C1CCCCC1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(2, mol.getAtom(0).getHydrogenCount().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount4a() throws Exception {
        String smiles = "c1=cc=cc=c1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(1, mol.getAtom(0).getHydrogenCount().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testImplicitHydrogenCount4b() throws Exception {
        String smiles = "c1ccccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(1, mol.getAtom(0).getHydrogenCount().intValue());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testHOSECodeProblem() throws Exception {
        String smiles = "CC=CBr";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(4, mol.getAtomCount());
        Assert.assertEquals("Br", mol.getAtom(3).getSymbol());
    }

    /**
	 *  A unit test for JUnit
	 */
    @org.junit.Test(timeout = 1000)
    public void testPyridine() throws Exception {
        IMolecule mol = sp.parseSmiles("c1ccncc1");
        Assert.assertEquals(6, mol.getAtomCount());
        IAtom nitrogen = mol.getAtom(3);
        Assert.assertEquals("N", nitrogen.getSymbol());
        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
            Assert.assertEquals(IAtomType.Hybridization.SP2, atoms.next().getHybridization());
        }
    }

    /**
	 * @cdk.bug 1306780
	 */
    @org.junit.Test(timeout = 1000)
    public void testParseK() throws Exception {
        IMolecule mol = sp.parseSmiles("C=CCC(=NOS(=O)(=O)[O-])SC1OC(CO)C(O)C(O)C1(O).[Na+]");
        Assert.assertNotNull(mol);
        Assert.assertEquals(23, mol.getAtomCount());
        mol = sp.parseSmiles("C=CCC(=NOS(=O)(=O)[O-])SC1OC(CO)C(O)C(O)C1(O).[K]");
        Assert.assertNotNull(mol);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(23, mol.getAtomCount());
        mol = sp.parseSmiles("C=CCC(=NOS(=O)(=O)[O-])SC1OC(CO)C(O)C(O)C1(O).[K+]");
        Assert.assertNotNull(mol);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(23, mol.getAtomCount());
    }

    /**
	 * @cdk.bug 1459299
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1459299() throws Exception {
        IMolecule mol = sp.parseSmiles("Cc1nn(C)cc1[C@H]2[C@H](C(=O)N)C(=O)C[C@@](C)(O)[C@@H]2C(=O)N");
        Assert.assertNotNull(mol);
        Assert.assertEquals(22, mol.getAtomCount());
    }

    /**
	 * @cdk.bug 1365547
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1365547() throws Exception {
        IMolecule mol = sp.parseSmiles("c2ccc1[nH]ccc1c2");
        Assert.assertNotNull(mol);
        Assert.assertEquals(9, mol.getAtomCount());
        Assert.assertTrue(mol.getBond(0).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
	 * @cdk.bug 1365547
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1365547_2() throws Exception {
        IMolecule mol = sp.parseSmiles("[H]c1c([H])c(c([H])c2c([H])c([H])n([H])c12)Br");
        Assert.assertNotNull(mol);
        Assert.assertEquals(16, mol.getAtomCount());
        Assert.assertEquals(17, mol.getBondCount());
        for (int i = 0; i < 17; i++) {
            IBond bond = mol.getBond(i);
            if (bond.getAtom(0).getSymbol().equals("H") || bond.getAtom(0).getSymbol().equals("Br") || bond.getAtom(1).getSymbol().equals("H") || bond.getAtom(1).getSymbol().equals("Br")) {
                Assert.assertFalse(bond.getFlag(CDKConstants.ISAROMATIC));
            } else {
                Assert.assertTrue(bond.getFlag(CDKConstants.ISAROMATIC));
            }
        }
    }

    /**
	 * @cdk.bug 1235852
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1235852() throws Exception {
        IMolecule mol = sp.parseSmiles("O=C(CCS)CC(C)CCC2Cc1ccsc1CC2");
        Assert.assertNotNull(mol);
        Assert.assertEquals(19, mol.getAtomCount());
        Assert.assertEquals(20, mol.getBondCount());
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(12)), 0.001);
        Assert.assertEquals(2.0, mol.getBondOrderSum(mol.getAtom(13)), 0.001);
        Assert.assertEquals(2.0, mol.getBondOrderSum(mol.getAtom(14)), 0.001);
        Assert.assertEquals(2.0, mol.getBondOrderSum(mol.getAtom(15)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(16)), 0.001);
    }

    /**
	 * @cdk.bug 1519183
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1519183() throws Exception {
        IMolecule mol = sp.parseSmiles("c%101ccccc1.O%10");
        Assert.assertNotNull(mol);
        Assert.assertEquals(7, mol.getAtomCount());
        Assert.assertEquals(7, mol.getBondCount());
    }

    /**
	 * @cdk.bug 1530926
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1530926() throws Exception {
        IMolecule mol = sp.parseSmiles("[n+]%101ccccc1.[O-]%10");
        Assert.assertNotNull(mol);
        Assert.assertEquals(7, mol.getAtomCount());
        Assert.assertEquals(7, mol.getBondCount());
        for (int i = 0; i < 7; i++) {
            IBond bond = mol.getBond(i);
            if (bond.getAtom(0).getSymbol().equals("O") || bond.getAtom(1).getSymbol().equals("O")) {
                Assert.assertFalse(bond.getFlag(CDKConstants.ISAROMATIC));
            } else {
                Assert.assertTrue(bond.getFlag(CDKConstants.ISAROMATIC));
            }
        }
    }

    /**
	 * @cdk.bug 1541333
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1541333() throws Exception {
        IMolecule mol1 = sp.parseSmiles("OC(=O)CSC1=NC=2C=C(C=CC2N1C=3C=CC=CC3)N(=O)O");
        Assert.assertNotNull(mol1);
        Assert.assertEquals(23, mol1.getAtomCount());
        Assert.assertEquals(25, mol1.getBondCount());
        IMolecule mol2 = sp.parseSmiles("OC(=O)CSc1nc2cc(ccc2n1c3ccccc3)N(=O)O");
        Assert.assertNotNull(mol2);
        Assert.assertEquals(23, mol2.getAtomCount());
        Assert.assertEquals(25, mol2.getBondCount());
        Assert.assertEquals(IBond.Order.DOUBLE, mol1.getBond(1).getOrder());
        Assert.assertEquals(IBond.Order.DOUBLE, mol2.getBond(1).getOrder());
        Assert.assertTrue(mol1.getBond(7).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(mol2.getBond(7).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
	 * @cdk.bug 1719287
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1719287() throws Exception {
        IMolecule mol = sp.parseSmiles("OC(=O)[C@@H](N)CC[S+1](C)C[C@@H](O1)[C@@H](O)[C@@H](O)[C@@H]1n(c3)c(n2)c(n3)c(N)nc2");
        Assert.assertNotNull(mol);
        Assert.assertEquals(27, mol.getAtomCount());
        Assert.assertEquals(29, mol.getBondCount());
        Assert.assertEquals(1, mol.getAtom(7).getFormalCharge().intValue());
    }

    /**
	 * Test for bug #1503541 "Problem with SMILES parsing". All SMILES in the test
	 * should result in a benzene molecule. Sometimes only a Cyclohexa-dien was
	 * created.
	 * @cdk.bug 1503541
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1503541() throws Exception {
        IMolecule mol = sp.parseSmiles("C=1C=CC=CC=1");
        Assert.assertNotNull(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(6, mol.getBondCount());
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(0)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(1)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(2)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(3)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(4)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(5)), 0.001);
        mol = sp.parseSmiles("C1C=CC=CC=1");
        Assert.assertNotNull(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(6, mol.getBondCount());
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(0)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(1)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(2)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(3)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(4)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(5)), 0.001);
        mol = sp.parseSmiles("C=1C=CC=CC1");
        Assert.assertNotNull(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(6, mol.getBondCount());
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(0)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(1)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(2)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(3)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(4)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(5)), 0.001);
        mol = sp.parseSmiles("C1=CC=CC=C1");
        Assert.assertNotNull(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        Assert.assertEquals(6, mol.getBondCount());
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(0)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(1)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(2)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(3)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(4)), 0.001);
        Assert.assertEquals(3.0, mol.getBondOrderSum(mol.getAtom(5)), 0.001);
    }

    /**
	 * Test case for bug #1783367 "SmilesParser incorrectly assigns double bonds".
	 * "C=%10C=CC=C%02C=%10N(C)CCC%02" was parsed incorrectly whereas "C=1C=CC=C%02C=1N(C)CCC%02"
	 * was parsed correctly. There was a bug with parsing "C=%10".
	 * Author: Andreas Schueller <a.schueller@chemie.uni-frankfurt.de>
	 *
	 * @cdk.bug 1783367
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1783367() throws Exception {
        String smiles = "C=%10C=CC=C%02C=%10N(C)CCC%02";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(IBond.Order.SINGLE, mol.getBond(0).getOrder());
    }

    /**
	 * @cdk.bug 1783547
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1783547() throws Exception {
        String smiles = "c1ccccc1C1=CC=CC=C1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertTrue(mol.getBond(0).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(mol.getBond(1).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(mol.getBond(2).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(mol.getBond(3).getFlag(CDKConstants.ISAROMATIC));
        String smiles2 = "C%21=%01C=CC=C%02C=%01N(C)CCC%02.C%21c%02ccccc%02";
        IMolecule mol2 = sp.parseSmiles(smiles2);
        Assert.assertTrue(mol2.getBond(16).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(mol2.getBond(17).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(mol2.getBond(18).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(mol2.getBond(19).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
	 * Test case for bug #1783546 "Lost aromaticity in SmilesParser with Benzene".
	 * SMILES like "C=1C=CC=CC=1" which end in "=1" were incorrectly parsed, the ring
	 * closure double bond got lost.
	 * @cdk.bug 1783546
	 */
    @org.junit.Test(timeout = 1000)
    public void testBug1783546() throws Exception {
        String smiles = "C=1C=CC=CC=1";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(IBond.Order.SINGLE, mol.getBond(0).getOrder());
        Assert.assertEquals(IBond.Order.DOUBLE, mol.getBond(1).getOrder());
        Assert.assertEquals(IBond.Order.SINGLE, mol.getBond(2).getOrder());
        Assert.assertEquals(IBond.Order.DOUBLE, mol.getBond(3).getOrder());
        Assert.assertEquals(IBond.Order.SINGLE, mol.getBond(4).getOrder());
        Assert.assertEquals(IBond.Order.DOUBLE, mol.getBond(5).getOrder());
    }

    @org.junit.Test
    public void testChargedAtoms() throws Exception {
        String smiles = "[C-]#[O+]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(2, mol.getAtomCount());
        Assert.assertEquals(IBond.Order.TRIPLE, mol.getBond(0).getOrder());
        Assert.assertEquals(-1, mol.getAtom(0).getFormalCharge().intValue());
        Assert.assertEquals(1, mol.getAtom(1).getFormalCharge().intValue());
    }

    /**
	 * @cdk.bug 1872969
	 */
    @org.junit.Test
    public void bug1872969() throws Exception {
        String smiles = "CS(=O)(=O)[O-].[Na+]";
        IMolecule mol = sp.parseSmiles(smiles);
        for (int i = 0; i < 6; i++) {
            Assert.assertNotNull(mol.getAtom(i).getAtomTypeName());
        }
    }

    /**
	 * @cdk.bug 1875949
	 */
    @org.junit.Test
    public void testResonanceStructure() throws Exception {
        String smiles = "[F+]=C-[C-]";
        IMolecule mol = sp.parseSmiles(smiles);
        Assert.assertEquals(3, mol.getAtomCount());
        Assert.assertEquals(IBond.Order.DOUBLE, mol.getBond(0).getOrder());
        Assert.assertEquals(+1, mol.getAtom(0).getFormalCharge().intValue());
        Assert.assertEquals(-1, mol.getAtom(2).getFormalCharge().intValue());
    }

    /**
	 * @cdk.bug 1879589
	 */
    @org.junit.Test
    public void testSP2HybridizedSulphur() throws Exception {
        String smiles = "[s+]1c2c(nc3c1cccc3)cccc2";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            Assert.assertEquals(IAtomType.Hybridization.SP2, atom.getHybridization());
            Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test
    public void testMercaptan() throws Exception {
        IMolecule mol = sp.parseSmiles("C=CCS");
        assertAtomTypesPerceived(mol);
    }

    /**
     * @cdk.bug 1957958
     */
    @Test
    public void test3amino4methylpyridine() throws Exception {
        IMolecule mol = sp.parseSmiles("c1c(C)c(N)cnc1");
        assertAtomTypesPerceived(mol);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        boolean isaromatic = CDKHueckelAromaticityDetector.detectAromaticity(mol);
        Assert.assertTrue(isaromatic);
    }

    /**
	 * @cdk.bug 1959516
	 */
    @org.junit.Test
    public void testPyrrole1() throws Exception {
        String smiles = "[nH]1cccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(5, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "C", "C", "C", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.PLANAR3, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 1, 1, 1, 1, 1 }, mol);
    }

    /**
	 * @cdk.bug 1959516
	 */
    @org.junit.Test
    public void testPyrrole2() throws Exception {
        String smiles = "n1([H])cccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "H", "C", "C", "C", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.PLANAR3, IAtomType.Hybridization.S, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 0, 0, 1, 1, 1, 1 }, mol);
    }

    /**
	 * @cdk.bug 1962419
	 */
    @org.junit.Test
    public void testPyrrole3() throws Exception {
        String smiles = "n1cccc1";
        try {
            sp.parseSmiles(smiles);
            Assert.fail("The SMILES string 'n1cccc1' is invalid but no exception was thrown.");
        } catch (Exception e) {
        }
    }

    /**
	 * @cdk.bug 1962398
	 */
    @org.junit.Test
    public void testPyrroleAnion1() throws Exception {
        String smiles = "[n-]1cccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(5, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "C", "C", "C", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.PLANAR3, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 0, 1, 1, 1, 1 }, mol);
    }

    /**
	 * @cdk.bug 1960990
	 */
    @org.junit.Test
    public void testImidazole1() throws Exception {
        String smiles = "[nH]1cncc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(5, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "C", "N", "C", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.PLANAR3, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 1, 1, 0, 1, 1 }, mol);
    }

    /**
	 * @cdk.bug 1960990
	 */
    @org.junit.Test
    public void testImidazole2() throws Exception {
        String smiles = "n1([H])cncc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "H", "C", "N", "C", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.PLANAR3, IAtomType.Hybridization.S, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 0, 0, 1, 0, 1, 1 }, mol);
    }

    /**
	 * @cdk.bug 1962419
	 */
    @org.junit.Test
    public void testImidazole3() throws Exception {
        String smiles = "n1cncc1";
        try {
            sp.parseSmiles(smiles);
            Assert.fail("The SMILES string 'n1cncc1' is invalid but no exception was thrown.");
        } catch (Exception e) {
        }
    }

    /**
	 * @cdk.bug 1960990
	 */
    @org.junit.Test
    public void testImidazole4() throws Exception {
        String smiles = "n1cc[nH]c1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(5, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "C", "C", "N", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.PLANAR3, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 0, 1, 1, 1, 1 }, mol);
    }

    /**
	 * @cdk.bug 1959516
	 */
    @org.junit.Test
    public void testPyridine1() throws Exception {
        String smiles = "n1ccccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "C", "C", "C", "C", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 0, 1, 1, 1, 1, 1 }, mol);
    }

    /**
	 * @cdk.bug 1959516
	 */
    @org.junit.Test
    public void testPyrimidine1() throws Exception {
        String smiles = "n1cnccc1";
        IMolecule mol = sp.parseSmiles(smiles);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(6, mol.getAtomCount());
        assertAllSingleAndAromatic(mol);
        assertAtomSymbols(new String[] { "N", "C", "N", "C", "C", "C" }, mol);
        assertHybridizations(new IAtomType.Hybridization[] { IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2, IAtomType.Hybridization.SP2 }, mol);
        assertHydrogenCounts(new int[] { 0, 1, 0, 1, 1, 1 }, mol);
    }

    /**
     * @throws Exception
     * @cdk.bug 1967468
     */
    @Test
    public void testIndole1() throws Exception {
        String smiles1 = "c1ccc2cc[nH]c2(c1)";
        IMolecule mol = sp.parseSmiles(smiles1);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(9, mol.getAtomCount());
        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * @throws Exception
     * @cdk.bug 1967468
     */
    @Test
    public void testIndole2() throws Exception {
        String smiles1 = "C1(NC=C2)=C2C=CC=C1";
        IMolecule mol = sp.parseSmiles(smiles1);
        CDKHueckelAromaticityDetector.detectAromaticity(mol);
        assertAtomTypesPerceived(mol);
        Assert.assertEquals(9, mol.getAtomCount());
        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * @throws Exception
     * @cdk.bug 1963731
     */
    @Test
    public void testBug1963731() throws CDKException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule molecule = sp.parseSmiles("C(C1C(C(C(C(O1)O)N)O)O)O");
        int hcount = 0;
        for (int i = 0; i < molecule.getBondCount(); i++) {
            hcount += molecule.getAtom(i).getHydrogenCount();
        }
        Assert.assertEquals(13, hcount);
    }

    @Test
    public void testONSSolubility1() throws CDKException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule molecule = sp.parseSmiles("Oc1ccc(cc1OC)C=O");
        Assert.assertEquals(11, molecule.getAtomCount());
        Assert.assertEquals(11, molecule.getBondCount());
    }

    @Test
    public void test1456139() throws Exception {
        SmilesParser p = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = p.parseSmiles("Cc1nn(C)cc1[C@H]2[C@H](C(=O)N)C(=O)C[C@@](C)(O)[C@@H]2C(=O)N");
        IMolecule mol2 = DefaultChemObjectBuilder.getInstance().newMolecule(mol);
        Assert.assertNotNull(mol2);
        Assert.assertEquals(22, mol2.getAtomCount());
    }

    @Test
    public void testExplicitH() throws Exception {
        SmilesParser p = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol;
        mol = p.parseSmiles("CO[H]");
        Assert.assertEquals(3, mol.getAtomCount());
        mol = p.parseSmiles("[CH3][OH]");
        Assert.assertEquals(2, mol.getAtomCount());
        mol = p.parseSmiles("C([H])([H])([H])O([H])");
        Assert.assertEquals(6, mol.getAtomCount());
    }

    /**
     * @cdk.bug 2514200
     */
    @Test
    public void testno937() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("C[nH0]1c([nH0]cc1");
        Assert.assertNotNull(mol.getAtom(1).getHydrogenCount());
        Assert.assertEquals(0, mol.getAtom(1).getHydrogenCount().intValue());
        Assert.assertNotNull(mol.getAtom(3).getHydrogenCount());
        Assert.assertEquals(0, mol.getAtom(3).getHydrogenCount().intValue());
    }
}
