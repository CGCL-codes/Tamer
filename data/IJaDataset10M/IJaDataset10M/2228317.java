package org.openscience.cdk.qsar.descriptors.molecular;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.HINReader;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import java.io.InputStream;
import java.util.List;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsarmolecular
 */
public class MomentOfInertiaDescriptorTest extends MolecularDescriptorTest {

    public MomentOfInertiaDescriptorTest() {
    }

    @Before
    public void setUp() throws Exception {
        setDescriptor(MomentOfInertiaDescriptor.class);
    }

    /**
     * @cdk.bug 1956139
     * @throws InvalidSmilesException
     */
    @Test
    public void testMOIFromSmiles() throws InvalidSmilesException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("CCCC");
        DescriptorValue value = descriptor.calculate(mol);
        Assert.assertNotNull("The Exception should be non-null since we don't have 3D coords", value.getException());
    }

    @Test
    public void testMomentOfInertia1() throws ClassNotFoundException, CDKException, java.lang.Exception {
        String filename = "data/hin/gravindex.hin";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        ISimpleChemObjectReader reader = new HINReader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        List cList = ChemFileManipulator.getAllAtomContainers(content);
        IAtomContainer ac = (IAtomContainer) cList.get(0);
        DoubleArrayResult retval = (DoubleArrayResult) descriptor.calculate(ac).getValue();
        Assert.assertEquals(1820.692519, retval.get(0), 0.00001);
        Assert.assertEquals(1274.532522, retval.get(1), 0.00001);
        Assert.assertEquals(979.210423, retval.get(2), 0.00001);
        Assert.assertEquals(1.428517, retval.get(3), 0.00001);
        Assert.assertEquals(1.859347, retval.get(4), 0.00001);
        Assert.assertEquals(1.301592, retval.get(5), 0.00001);
        Assert.assertEquals(5.411195, retval.get(6), 0.00001);
    }

    @Test
    public void testMomentOfInertia2() throws ClassNotFoundException, CDKException, java.lang.Exception {
        String filename = "data/hin/momi2.hin";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        ISimpleChemObjectReader reader = new HINReader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        List cList = ChemFileManipulator.getAllAtomContainers(content);
        IAtomContainer ac = (IAtomContainer) cList.get(0);
        DoubleArrayResult retval = (DoubleArrayResult) descriptor.calculate(ac).getValue();
        Assert.assertEquals(10068.419360, retval.get(0), 0.00001);
        Assert.assertEquals(9731.078356, retval.get(1), 0.00001);
        Assert.assertEquals(773.612799, retval.get(2), 0.00001);
        Assert.assertEquals(1.034666, retval.get(3), 0.00001);
        Assert.assertEquals(13.014804, retval.get(4), 0.00001);
        Assert.assertEquals(12.578745, retval.get(5), 0.00001);
        Assert.assertEquals(8.2966226, retval.get(6), 0.00001);
    }
}
