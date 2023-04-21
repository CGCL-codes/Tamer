package ie.omk.smpp.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new SequenceTest("Default sequence test") {

            public void runTest() {
                testSequence();
            }
        });
        suite.addTest(new SequenceTest("Sequence wrap test") {

            public void runTest() {
                testSequenceWrap();
            }
        });
        suite.addTest(new EncodingTest("ASCII encoding test") {

            public void runTest() {
                testASCIIEncoding();
            }
        });
        suite.addTest(new EncodingTest("Latin-1 encoding test") {

            public void runTest() {
                testLatinEncoding();
            }
        });
        suite.addTestSuite(ie.omk.smpp.util.TestSMPPDate.class);
        suite.addTestSuite(ie.omk.smpp.util.TestAPIConfig.class);
        return (suite);
    }
}
