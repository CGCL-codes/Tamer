package org.objectstyle.wolips.core.resources.internal.types.file;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run all file types regression tests
 */
public class FileTypesTestSuite extends TestCase {

    /**
	 * @param testName
	 */
    public FileTypesTestSuite(String testName) {
        super(testName);
    }

    /**
	 * @return
	 * @throws Exception
	 */
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(PBDotProjectAdapterTest.class);
        return suite;
    }
}
