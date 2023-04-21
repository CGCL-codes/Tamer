package org.openscience.cdk.test.modulesuites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.test.inchi.InChIGeneratorTest;

/**
 * TestSuite that runs all the sample tests for the CDK module inchi.
 *
 * @cdk.module test-inchi
 */
public class MinchiTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("The inchi module Tests");
        suite.addTest(new JUnit4TestAdapter(InChIGeneratorTest.class));
        return suite;
    }
}
