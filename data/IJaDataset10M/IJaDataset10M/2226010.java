package net.sf.joafip.store.service.bytecode.agent;

import net.sf.joafip.NotStorableClass;
import junit.framework.Test;
import junit.framework.TestSuite;

@NotStorableClass
public class StoreServiceByteCodeAgentTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for net.sf.joafip.store.service.bytecode.agent");
        suite.addTestSuite(TestPackageMgr.class);
        suite.addTestSuite(TestNoStorableAccess.class);
        return suite;
    }
}
