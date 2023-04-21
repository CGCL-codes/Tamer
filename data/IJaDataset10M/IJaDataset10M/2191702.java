package net.sf.joafip.service.rel400;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.joafip.NotStorableClass;

@NotStorableClass
public class ServiceRel400LongDurationTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Long duration test for net.sf.joafip.service.rel400");
        suite.addTestSuite(TestHugeList.class);
        suite.addTestSuite(TestHugeListAutoSave.class);
        return suite;
    }
}
