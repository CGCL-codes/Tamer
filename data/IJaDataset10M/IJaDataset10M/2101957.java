package net.sf.joafip;

import net.sf.joafip.btree.BtreeTests;
import net.sf.joafip.file.FileTests;
import net.sf.joafip.heapfile.HeapFileTests;
import net.sf.joafip.java.util.JavaUtilTests;
import net.sf.joafip.redblacktree.RedBlackTreeTests;
import net.sf.joafip.service.ServiceTests;
import net.sf.joafip.store.service.StoreServiceTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class PersistanceTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for persistence");
        suite.addTest(RedBlackTreeTests.suite());
        suite.addTest(BtreeTests.suite());
        suite.addTest(JavaUtilTests.suite());
        suite.addTest(HeapFileTests.suite());
        suite.addTest(FileTests.suite());
        suite.addTest(StoreServiceTests.suite());
        suite.addTest(ServiceTests.suite());
        return suite;
    }
}
