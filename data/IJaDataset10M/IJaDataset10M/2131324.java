package org.openscience.cdk.test.graph.rebond;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.graph.rebond.Bspt;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module test-standard
 */
public class BsptTest extends CDKTestCase {

    public BsptTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(BsptTest.class);
    }

    public void testToString() {
        Bspt bspt = new Bspt(3);
        assertNotNull(bspt.toString());
    }

    public void testBspt() {
        Bspt bspt = new Bspt(3);
        assertNotNull(bspt);
    }
}
