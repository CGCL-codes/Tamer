package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SessionManager;
import com.bluemarsh.jswat.SessionSetup;
import junit.extensions.*;
import junit.framework.*;

/**
 * Tests the interrupt command.
 */
public class interruptTest extends CommandTestCase {

    public interruptTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new SessionSetup(new TestSuite(interruptTest.class));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void test_interrupt() {
        Session session = SessionManager.beginSession();
        SessionManager.launchSimple("locals");
        runCommand(session, "clear all");
        runCommand(session, "interrupt main");
        try {
            runCommand(session, "interrupt no_thread");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        SessionManager.deactivate(true);
        SessionManager.endSession();
    }
}
