package org.openscience.cdk.test.qsar.descriptors.molecular;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.qsar.descriptors.molecular.CarbonTypesDescriptor;
import org.openscience.cdk.qsar.result.IntegerArrayResult;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsarmolecular
 */
public class CarbonTypesDescriptorTest extends MolecularDescriptorTest {

    public CarbonTypesDescriptorTest() {
    }

    public static Test suite() {
        return new TestSuite(CarbonTypesDescriptorTest.class);
    }

    public void setUp() throws Exception {
        setDescriptor(CarbonTypesDescriptor.class);
    }

    public void testButane() throws CDKException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("CCCC");
        IntegerArrayResult ret = (IntegerArrayResult) descriptor.calculate(mol).getValue();
        assertEquals(0, ret.get(0));
        assertEquals(0, ret.get(1));
        assertEquals(0, ret.get(2));
        assertEquals(0, ret.get(3));
        assertEquals(0, ret.get(4));
        assertEquals(2, ret.get(5));
        assertEquals(2, ret.get(6));
        assertEquals(0, ret.get(7));
        assertEquals(0, ret.get(8));
    }

    public void testComplex1() throws CDKException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("C(C)(C)C=C(C)C");
        IntegerArrayResult ret = (IntegerArrayResult) descriptor.calculate(mol).getValue();
        assertEquals(0, ret.get(0));
        assertEquals(0, ret.get(1));
        assertEquals(0, ret.get(2));
        assertEquals(1, ret.get(3));
        assertEquals(1, ret.get(4));
        assertEquals(4, ret.get(5));
        assertEquals(0, ret.get(6));
        assertEquals(1, ret.get(7));
        assertEquals(0, ret.get(8));
    }

    public void testComplex2() throws CDKException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = sp.parseSmiles("C#CC(C)=C");
        IntegerArrayResult ret = (IntegerArrayResult) descriptor.calculate(mol).getValue();
        assertEquals(1, ret.get(0));
        assertEquals(1, ret.get(1));
        assertEquals(1, ret.get(2));
        assertEquals(0, ret.get(3));
        assertEquals(1, ret.get(4));
        assertEquals(1, ret.get(5));
        assertEquals(0, ret.get(6));
        assertEquals(0, ret.get(7));
        assertEquals(0, ret.get(8));
    }
}
