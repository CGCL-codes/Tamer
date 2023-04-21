package org.openscience.cdk.test.qsar.descriptors.molecular;

import java.io.InputStream;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.HINReader;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.EccentricConnectivityIndexDescriptor;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsar
 */
public class EccentricConnectivityIndexDescriptorTest extends CDKTestCase {

    public EccentricConnectivityIndexDescriptorTest() {
    }

    public static Test suite() {
        return new TestSuite(EccentricConnectivityIndexDescriptorTest.class);
    }

    public void testEccentricConnectivityIndex() throws ClassNotFoundException, CDKException, java.lang.Exception {
        String filename = "data/hin/gravindex.hin";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        IChemObjectReader reader = new HINReader(ins);
        ChemFile content = (ChemFile) reader.read((ChemObject) new ChemFile());
        List cList = ChemFileManipulator.getAllAtomContainers(content);
        IAtomContainer ac = (IAtomContainer) cList.get(0);
        IMolecularDescriptor descriptor = new EccentricConnectivityIndexDescriptor();
        IntegerResult retval = (IntegerResult) descriptor.calculate(ac).getValue();
        assertEquals(254, retval.intValue(), 0);
    }
}
