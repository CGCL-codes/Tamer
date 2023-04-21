package net.sf.joafip.service.bug.primitive;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.joafip.NotStorableClass;

@NotStorableClass
public class ServiceBugPrimitiveTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for net.sf.joafip.service.bug.primitive");
        suite.addTestSuite(TestPrimitiveIO.class);
        suite.addTestSuite(TestHoldPrimitive.class);
        return suite;
    }
}
