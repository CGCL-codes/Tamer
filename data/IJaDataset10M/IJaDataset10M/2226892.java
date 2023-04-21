package org.openscience.cdk.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.test.structgen.RandomStructureGeneratorTest;
import org.openscience.cdk.test.structgen.VicinitySamplerTest;
import org.openscience.cdk.test.structgen.stochastic.PartialFilledStructureMergerTest;
import org.openscience.cdk.test.structgen.stochastic.operator.ChemGraphTest;
import org.openscience.cdk.test.structgen.stochastic.operator.CrossoverMachineTest;

/**
 * TestSuite that runs all the sample tests for the structgen module.
 *
 * @cdk.module  test-structgen
 */
public class MstructgenTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("JUnit tests for the structgen module");
        suite.addTest(StructgenCoverageTest.suite());
        suite.addTest(RandomStructureGeneratorTest.suite());
        suite.addTest(VicinitySamplerTest.suite());
        suite.addTest(PartialFilledStructureMergerTest.suite());
        suite.addTest(ChemGraphTest.suite());
        suite.addTest(CrossoverMachineTest.suite());
        return suite;
    }
}
