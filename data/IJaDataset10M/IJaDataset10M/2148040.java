package org.openscience.cdk.test.qsar;

import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.interfaces.AtomContainer;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.io.ChemObjectReader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.qsar.Descriptor;
import org.openscience.cdk.qsar.descriptors.molecular.MomentOfInertiaDescriptor;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test
 */
public class MomentOfInertiaDescriptorTest extends CDKTestCase {

    public MomentOfInertiaDescriptorTest() {
    }

    public static Test suite() {
        return new TestSuite(MomentOfInertiaDescriptorTest.class);
    }

    public void testMomentOfInertia1() throws ClassNotFoundException, CDKException, java.lang.Exception {
        String filename = "data/hin/gravindex.hin";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        ChemObjectReader reader = new ReaderFactory().createReader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        AtomContainer[] c = ChemFileManipulator.getAllAtomContainers(content);
        AtomContainer ac = c[0];
        Descriptor descriptor = new MomentOfInertiaDescriptor();
        DoubleArrayResult retval = (DoubleArrayResult) descriptor.calculate(ac).getValue();
        assertEquals(1820.692519, retval.get(0), 0.00001);
        assertEquals(1274.532522, retval.get(1), 0.00001);
        assertEquals(979.210423, retval.get(2), 0.00001);
        assertEquals(1.428517, retval.get(3), 0.00001);
        assertEquals(1.859347, retval.get(4), 0.00001);
        assertEquals(1.301592, retval.get(5), 0.00001);
        assertEquals(5.411195, retval.get(6), 0.00001);
    }

    public void testMomentOfInertia2() throws ClassNotFoundException, CDKException, java.lang.Exception {
        String filename = "data/hin/momi2.hin";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        ChemObjectReader reader = new ReaderFactory().createReader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        AtomContainer[] c = ChemFileManipulator.getAllAtomContainers(content);
        AtomContainer ac = c[0];
        Descriptor descriptor = new MomentOfInertiaDescriptor();
        DoubleArrayResult retval = (DoubleArrayResult) descriptor.calculate(ac).getValue();
        assertEquals(10068.419360, retval.get(0), 0.00001);
        assertEquals(9731.078356, retval.get(1), 0.00001);
        assertEquals(773.612799, retval.get(2), 0.00001);
        assertEquals(1.034666, retval.get(3), 0.00001);
        assertEquals(13.014804, retval.get(4), 0.00001);
        assertEquals(12.578745, retval.get(5), 0.00001);
        assertEquals(8.2966226, retval.get(6), 0.00001);
    }
}
