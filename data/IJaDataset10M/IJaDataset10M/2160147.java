package net.sf.joafip.store.service.classinfo;

import net.sf.joafip.NotStorableClass;
import junit.framework.Test;
import junit.framework.TestSuite;

@NotStorableClass
public class StoreServiceClassInfoTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for net.sf.joafip.store.service.collection");
        suite.addTestSuite(TestClassInfoFactory.class);
        suite.addTestSuite(TestClassReplacement.class);
        return suite;
    }
}
