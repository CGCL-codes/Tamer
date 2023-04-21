package org.openscience.cdk.test.ringsearch;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.ringsearch.RingPartitioner;
import org.openscience.cdk.ringsearch.SSSRFinder;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.test.CDKTestCase;

/**
 * This class tests the RingPartitioner class.
 *
 * @cdk.module test-standard
 *
 * @author         kaihartmann
 * @cdk.created    2005-05-24
 */
public class RingPartitionerTest extends CDKTestCase {

    static boolean standAlone = false;

    /**
	 *  Constructor for the RingPartitionerTest object
	 *
	 *@param  name  Description of the Parameter
	 */
    public RingPartitionerTest(String name) {
        super(name);
    }

    /**
	 *  The JUnit setup method
	 */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
	 *  A unit test suite for JUnit
	 *
	 *@return    The test suite
	 */
    public static Test suite() {
        return new TestSuite(RingPartitionerTest.class);
    }

    /**
	 *  A unit test for JUnit
	 */
    public void testConvertToAtomContainer_IRingSet() {
        IMolecule molecule = MoleculeFactory.makeAlphaPinene();
        SSSRFinder sssrf = new SSSRFinder(molecule);
        IRingSet ringSet = sssrf.findSSSR();
        IAtomContainer ac = RingPartitioner.convertToAtomContainer(ringSet);
        assertEquals(7, ac.getAtomCount());
        assertEquals(8, ac.getBondCount());
    }
}
