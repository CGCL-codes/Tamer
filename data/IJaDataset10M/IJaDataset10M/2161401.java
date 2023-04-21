package org.openscience.cdk.qsar.descriptors.molecular;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * TestSuite that runs all QSAR tests.
 * @author      chhoppe from EUROSCREEN
 * @cdk.module test-qsarmolecular
 */
public class LargestPiSystemDescriptorTest extends MolecularDescriptorTest {

    public LargestPiSystemDescriptorTest() {
    }

    @Before
    public void setUp() throws Exception {
        setDescriptor(LargestPiSystemDescriptor.class);
    }

    @Test
    public void test1LargestPiSystemDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("c1ccccc1");
        System.out.println("test1>:" + ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }

    @Test
    public void test2LargestPiSystemDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("C=CC=Cc1ccccc1");
        Assert.assertEquals(10, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }

    @Test
    public void test3LargestPiSystemDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("C=CC=CCc2ccc(Cc1ccncc1C=C)cc2");
        Assert.assertEquals(8, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }

    @Test
    public void test4LargestPiSystemDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("CC=CNCC");
        Assert.assertEquals(3, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }

    @Test
    public void test5LargestPiSystemDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("C=C[NH2+]CC");
        Assert.assertEquals(3, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }

    @Test
    public void test6LargestPiSystemDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("CCNOC");
        Assert.assertEquals(2, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }

    @Test
    public void test7LargestPiSystemDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(false) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("CC=CC(C)=O");
        Assert.assertEquals(4, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }
}
