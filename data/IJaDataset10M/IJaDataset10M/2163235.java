package org.openscience.cdk.test.debug;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.debug.DebugChemObjectBuilder;
import org.openscience.cdk.test.AtomTest;

/**
 * Checks the funcitonality of the AtomContainer.
 *
 * @cdk.module test-datadebug
 */
public class DebugAtomTest extends AtomTest {

    public DebugAtomTest(String name) {
        super(name);
    }

    public void setUp() {
        super.builder = DebugChemObjectBuilder.getInstance();
    }

    public static Test suite() {
        return new TestSuite(DebugAtomTest.class);
    }
}
