package org.openscience.cdk.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.test.qsar.descriptors.bond.BondPartialPiChargeDescriptorTest;
import org.openscience.cdk.test.qsar.descriptors.bond.BondPartialSigmaChargeDescriptorTest;
import org.openscience.cdk.test.qsar.descriptors.bond.BondPartialTChargeDescriptorTest;
import org.openscience.cdk.test.qsar.descriptors.bond.BondSigmaElectronegativityDescriptorTest;
import org.openscience.cdk.test.qsar.descriptors.bond.IPBondDescriptorTest;
import org.openscience.cdk.test.qsar.descriptors.bond.ResonancePositiveChargeDescriptorTest;

/**
 * TestSuite that runs all the sample tests.
 *
 * @cdk.module test-qsarBond
 * @cdk.depends log4j.jar
 * @cdk.depends junit.jar
 */
public class MqsarBondTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("All QSAR Tests");
        suite.addTest(BondPartialPiChargeDescriptorTest.suite());
        suite.addTest(BondPartialSigmaChargeDescriptorTest.suite());
        suite.addTest(BondPartialTChargeDescriptorTest.suite());
        suite.addTest(BondSigmaElectronegativityDescriptorTest.suite());
        suite.addTest(IPBondDescriptorTest.suite());
        suite.addTest(ResonancePositiveChargeDescriptorTest.suite());
        return suite;
    }
}
