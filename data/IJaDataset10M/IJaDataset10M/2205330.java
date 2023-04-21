package org.openscience.cdk.test.qsar.descriptors.molecular;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.CarbonConnectivityOrderOneDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.test.CDKTestCase;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsar
 */
public class CarbonConnectivityOrderOneDescriptorTest extends CDKTestCase {

    public CarbonConnectivityOrderOneDescriptorTest() {
    }

    public static Test suite() {
        return new TestSuite(CarbonConnectivityOrderOneDescriptorTest.class);
    }

    public void testCarbonConnectivityOrderOneDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        double[] testResult = { 1.115355 };
        IMolecularDescriptor descriptor = new CarbonConnectivityOrderOneDescriptor();
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("O=C(O)CC");
        DoubleResult retval = (DoubleResult) descriptor.calculate(mol).getValue();
        assertEquals(testResult[0], retval.doubleValue(), 0.0001);
    }
}
