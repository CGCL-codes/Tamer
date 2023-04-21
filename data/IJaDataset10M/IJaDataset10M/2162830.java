package org.openscience.cdk.test.io;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.test.io.cml.CMLIOTests;
import org.openscience.cdk.test.io.iterator.IteratingMDLReaderTest;
import org.openscience.cdk.test.io.iterator.IteratingSMILESReaderTest;

/**
 * TestSuite that runs all the sample tests for the cdk.io package.
 *
 * @cdk.module test
 */
public class IOTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("The cdk.io Tests");
        suite.addTest(CMLIOTests.suite());
        suite.addTest(CrystClustReaderTest.suite());
        suite.addTest(GamessReaderTest.suite());
        suite.addTest(Gaussian98ReaderTest.suite());
        suite.addTest(GhemicalReaderTest.suite());
        suite.addTest(HINReaderTest.suite());
        suite.addTest(INChIReaderTest.suite());
        suite.addTest(INChIPlainTextReaderTest.suite());
        suite.addTest(MDLReaderTest.suite());
        suite.addTest(SDFReaderTest.suite());
        suite.addTest(MDLWriterTest.suite());
        suite.addTest(MDLRXNReaderTest.suite());
        suite.addTest(MDLRXNWriterTest.suite());
        suite.addTest(Mol2ReaderTest.suite());
        suite.addTest(PDBReaderTest.suite());
        suite.addTest(ShelXReaderTest.suite());
        suite.addTest(SMILESReaderTest.suite());
        suite.addTest(ReaderFactoryTest.suite());
        suite.addTest(IteratingMDLReaderTest.suite());
        suite.addTest(IteratingSMILESReaderTest.suite());
        return suite;
    }
}
