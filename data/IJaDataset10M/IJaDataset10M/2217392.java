package org.openscience.cdk.coverage;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TestSuite that uses tests whether all public methods in the core
 * module are tested. Unlike Emma, it does not test that all code is
 * tested, just all methods.
 *
 * @cdk.module test-isomorphism
 */
public class IsomorphismCoverageTest extends CoverageTest {

    private static final String CLASS_LIST = "isomorphism.javafiles";

    @BeforeClass
    public static void setUp() throws Exception {
        loadClassList(CLASS_LIST, IsomorphismCoverageTest.class.getClassLoader());
    }

    @Test
    public void testCoverage() {
        super.runCoverageTest();
    }
}
