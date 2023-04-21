package org.openscience.cdk.qsar.descriptors.molecular;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.HINReader;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import java.io.InputStream;
import java.util.List;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsarmolecular
 */
public class CPSADescriptorTest extends MolecularDescriptorTest {

    public CPSADescriptorTest() {
    }

    @Before
    public void setUp() throws Exception {
        setDescriptor(CPSADescriptor.class);
    }

    @Test
    public void testCPSA() throws ClassNotFoundException, CDKException, java.lang.Exception {
        String filename = "data/hin/benzene.hin";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        ISimpleChemObjectReader reader = new HINReader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        List cList = ChemFileManipulator.getAllAtomContainers(content);
        IAtomContainer ac = (IAtomContainer) cList.get(0);
        DoubleArrayResult retval = (DoubleArrayResult) descriptor.calculate(ac).getValue();
        Assert.assertEquals(0, retval.get(28), 0.0001);
        Assert.assertEquals(1, retval.get(27), 0.0001);
        Assert.assertEquals(0, retval.get(26), 0.0001);
        Assert.assertEquals(356.8849, retval.get(25), 0.0001);
    }

    @Test
    public void testChargedMolecule() throws CDKException {
        String filename = "data/mdl/cpsa-charged.sdf";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        ISimpleChemObjectReader reader = new MDLV2000Reader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        List cList = ChemFileManipulator.getAllAtomContainers(content);
        IAtomContainer ac = (IAtomContainer) cList.get(0);
        DoubleArrayResult retval = (DoubleArrayResult) descriptor.calculate(ac).getValue();
        int ndesc = retval.length();
        for (int i = 0; i < ndesc; i++) Assert.assertTrue(retval.get(i) != Double.NaN);
    }

    @Test
    public void testUnChargedMolecule() throws CDKException {
        String filename = "data/mdl/cpsa-uncharged.sdf";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        ISimpleChemObjectReader reader = new MDLV2000Reader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        List cList = ChemFileManipulator.getAllAtomContainers(content);
        IAtomContainer ac = (IAtomContainer) cList.get(0);
        DoubleArrayResult retval = (DoubleArrayResult) descriptor.calculate(ac).getValue();
        int ndesc = retval.length();
        for (int i = 0; i < ndesc; i++) Assert.assertTrue(retval.get(i) != Double.NaN);
    }
}
