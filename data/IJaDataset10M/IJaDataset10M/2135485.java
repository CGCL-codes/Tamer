package com.bluemarsh.jswat;

import junit.extensions.*;
import junit.framework.*;

/**
 * Tests the VMConnection class.
 */
public class VMConnectionTest extends TestCase {

    private Session session;

    public VMConnectionTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new SessionSetup(new TestSuite(VMConnectionTest.class));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        session = SessionManager.beginSession();
    }

    protected void tearDown() {
        SessionManager.endSession();
    }

    public void testVMCBuildConnection() {
        try {
            VMConnection.buildConnection("/place/that/does/not/exist", null, null, "blah blah blah");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
        }
        VMConnection conn = VMConnection.buildConnection(null, null, null, "blah blah blah");
        assertNotNull(conn);
    }

    public void testVMCLaunching() {
        VMConnection conn = VMConnection.buildConnection(null, null, null, "locals arg1 arg2 arg3");
        assertTrue(!conn.isRemote());
        assertEquals("locals", conn.getMainClass());
        assertNull(conn.getVM());
        assertNotNull(conn.getConnector());
        assertNotNull(conn.getConnectArgs());
        assertTrue(!conn.equals(null));
        assertTrue(!conn.equals(""));
        assertTrue(conn.equals(conn));
        VMConnection conn2 = VMConnection.buildConnection(null, null, null, "blah blah blah");
        assertTrue(!conn.equals(conn2));
        boolean okay = conn.launchDebuggee(session, false);
        assertTrue("debuggee failed to launch", okay);
        assertNotNull(conn.getVM());
        session.deactivate(true, this);
    }

    public void testVMCRunning() {
        VMConnection conn = VMConnection.buildConnection(null, null, null, "locals");
        boolean okay = conn.launchDebuggee(session, false);
        assertTrue("debuggee failed to launch", okay);
        session.resumeVM(this, false, true);
        session.deactivate(true, this);
    }
}
