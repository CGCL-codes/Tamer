package org.openscience.cdk.test.debug;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.debug.DebugChemObjectBuilder;
import org.openscience.cdk.test.BioPolymerTest;

/**
 * Checks the funcitonality of the AtomContainer.
 *
 * @cdk.module test-datadebug
 */
public class DebugBioPolymerTest extends BioPolymerTest {

    public DebugBioPolymerTest(String name) {
        super(name);
    }

    public void setUp() {
        super.builder = DebugChemObjectBuilder.getInstance();
    }

    public static Test suite() {
        return new TestSuite(DebugBioPolymerTest.class);
    }
}
