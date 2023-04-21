package org.openscience.cdk.test.qsar.descriptors.molecular;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondAcceptorCountDescriptor;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.test.CDKTestCase;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsar
 */
public class HBondAcceptorCountDescriptorTest extends CDKTestCase {

    public HBondAcceptorCountDescriptorTest() {
    }

    public static Test suite() {
        return new TestSuite(HBondAcceptorCountDescriptorTest.class);
    }

    public void testHBondAcceptorCountDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        IMolecularDescriptor descriptor = new HBondAcceptorCountDescriptor();
        Object[] params = { new Boolean(true) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("O=N(=O)c1cccc2cn[nH]c12");
        assertEquals(2, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }
}
