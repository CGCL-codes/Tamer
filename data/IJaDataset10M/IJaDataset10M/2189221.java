package org.openscience.cdk.test.nonotify;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.test.protein.data.PDBMonomerTest;

/**
 * Checks the funcitonality of the AtomContainer.
 *
 * @cdk.module test-nonotify
 */
public class NNPDBMonomerTest extends PDBMonomerTest {

    public NNPDBMonomerTest(String name) {
        super(name);
    }

    public void setUp() {
        super.builder = NoNotificationChemObjectBuilder.getInstance();
    }

    public static Test suite() {
        return new TestSuite(NNPDBMonomerTest.class);
    }
}
