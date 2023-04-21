package net.liveseeds.eye.selection;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class MasterTestCase extends TestCase {

    public MasterTestCase() {
    }

    public MasterTestCase(final String name) {
        super(name);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(CellSelectionEventTest.class);
        suite.addTestSuite(DefaultCellSelectionManagerTest.class);
        return suite;
    }
}
