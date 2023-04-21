package org.apache.harmony.luni.tests.java.io;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Listing of all the tests that are to be run.
 */
public class AllTests {

    public static void run() {
        TestRunner.main(new String[] { AllTests.class.getName() });
    }

    public static final Test suite() {
        TestSuite suite = tests.TestSuiteFactory.createTestSuite("Tests for java.io");
        suite.addTestSuite(BufferedReaderTest.class);
        suite.addTestSuite(FilePermissionTest.class);
        suite.addTestSuite(FileTest.class);
        suite.addTestSuite(InputStreamReaderTest.class);
        suite.addTestSuite(ObjectInputStreamTest.class);
        suite.addTestSuite(ObjectStreamConstantsTest.class);
        suite.addTestSuite(OutputStreamWriterTest.class);
        suite.addTestSuite(PushBackInputStreamTest.class);
        suite.addTestSuite(RandomAccessFileTest.class);
        suite.addTestSuite(ReaderTest.class);
        suite.addTestSuite(WriterTest.class);
        return suite;
    }
}
