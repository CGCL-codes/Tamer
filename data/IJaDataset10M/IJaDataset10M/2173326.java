package org.xaware.api;

import org.xaware.shared.util.logging.XAwareLoggingLevels;
import org.xaware.testing.util.BaseTestCase;

/**
 * Test LoadAndExecute method of XABizView using a BizDoc that includes absolute path reference for BizDoc
 * 
 * @author abhatt
 */
public class TestCreate1 extends BaseTestCase {

    public TestCreate1(final String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testDummy() {
        System.out.println("XAware Debug level int: " + XAwareLoggingLevels.DEBUG.intValue());
        System.out.println("java Finest level int: " + java.util.logging.Level.FINEST.intValue());
        System.out.println("java Finer level int: " + java.util.logging.Level.FINER.intValue());
        System.out.println("java Fine level int: " + java.util.logging.Level.FINE.intValue());
        System.out.println("java All level int: " + java.util.logging.Level.ALL.intValue());
    }
}
