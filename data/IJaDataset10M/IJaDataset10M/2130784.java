package org.tigr.antware.shared.testing;

import org.tigr.antware.shared.command.events.CmdEventDispatcher;
import org.tigr.antware.shared.command.events.CommandAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * The <b>CmdEventDispatcherTest</b> class extends junit.framework.TestCase.
 * This class tests CmdEventDispatcher which is a simple value object so there isn't 
 * much to test. 
 * 
 */
public class CmdEventDispatcherTest extends TestCase {

    public class MyAdapter extends CommandAdapter {
    }

    /**
     * <code>ca</code> is a listener stub that we can use to test 
     * adding and removing listeners
     */
    MyAdapter ca = new MyAdapter();

    public CmdEventDispatcherTest(String p_name) {
        super(p_name);
    }

    /**
     * Sets up the test fixture. 
     * (Called before every test case method.)
     */
    protected void setUp() {
    }

    public static Test suite() {
        return new TestSuite(CmdEventDispatcherTest.class);
    }

    /**
     * <code>testCmdEventDispatcher</code> makes sure we can create a 
     * CmdEventDispatcher and add listeners.
     */
    public void testCmdEventDispatcher() {
        CmdEventDispatcher disp = new CmdEventDispatcher();
        disp.addRuntimeStatusListener(ca);
        disp.addStatusListener(ca);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
