package org.openscience.cdk.qsar.descriptors.molecular;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * TestSuite that runs XlogP tests.
 *
 * @cdk.module test-qsarmolecular
 */
public class XLogPDescriptorTest extends MolecularDescriptorTest {

    public XLogPDescriptorTest() {
    }

    @Before
    public void setUp() throws Exception {
        setDescriptor(XLogPDescriptor.class);
    }

    @Ignore
    @Test
    public void testno688() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("O=C(O)c1[nH0]cccc1");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(-1.69, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno1596() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("Nc2ccc(S(=O)(=O)c1ccc(N)cc1)cc2");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(0.86, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    @Test
    public void testno367() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("O=C(O)C(N)CCCN");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(-3.30, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno1837() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("O=P(N1CC1)(N2CC2)N3CC3");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(-1.19, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno87() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("c1cc2ccc3ccc4ccc5cccc6c(c1)c2c3c4c56");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(7.00, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno1782() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("S1C2N(C(=O)C2NC(=O)C(c2ccccc2)C(=O)O)C(C(=O)O)C1(C)C");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(1.84, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno30() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("C(#Cc1ccccc1)c1ccccc1");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(4.62, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Ignore
    @Test
    public void testno937() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("ClCC(O)C[nH0]1c([nH0]cc1[N+](=O)[O-])C");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(0.66, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno990() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("FC(F)(F)c1ccc(cc1)C(=O)N");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(1.834, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    @Test
    public void testno1000() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("Clc1cccc(c1)/C=C/[N+](=O)[O-]");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(2.809, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    @Test
    public void testApirinBug1296383() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("CC(=O)OC1=CC=CC=C1C(=O)O");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(1.422, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno1429() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("O=C(OC)CNC(=O)c1ccc(N)cc1");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(0.31, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    @Test
    public void testno1274() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("O=[N+]([O-])c1ccc(cc1)CC(N)C(=O)O");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(-1.487, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    @Test
    public void testno454() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("O=C1NC(=O)C=CN1C1OC(CO)C(O)C1O");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(-2.11, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testno498() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("O=C1N(C)C=CC(=O)N1C");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(-0.59, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }

    @Test
    public void testAprindine() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("CCN(CC)CCCN(C2Cc1ccccc1C2)c3ccccc3");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(5.03, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    @Test
    public void test1844() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("Brc1cc(Cl)c(O[P+]([S-])(OC)OC)cc1Cl");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(5.22, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    @Test
    public void test1810() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("Clc1ccc2Sc3ccccc3N(CCCN3CCN(C)CC3)c2c1");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(4.56, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 1.0);
    }

    /**
     * @cdk.inchi InChI=1/C23H20N2O3S/c26-22-21(16-17-29(28)20-14-8-3-9-15-20)23(27)25(19-12-6-2-7-13-19)24(22)18-10-4-1-5-11-18/h1-15,21H,16-17H2
     */
    @Test
    public void test1822() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true), new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("[S+]([O-])(CCC1C(=O)N(N(c2ccccc2)C1=O)c1ccccc1)c1ccccc1");
        assertAtomTypesPerceived(mol);
        addExplicitHydrogens(mol);
        Assert.assertEquals(2.36, ((DoubleResult) descriptor.calculate(mol).getValue()).doubleValue(), 0.1);
    }
}
