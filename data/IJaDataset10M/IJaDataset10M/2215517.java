package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SessionManager;
import com.bluemarsh.jswat.SessionSetup;
import junit.extensions.*;
import junit.framework.*;

/**
 * Tests the threads command.
 */
public class threadsTest extends CommandTestCase {

    public threadsTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new SessionSetup(new TestSuite(threadsTest.class));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void test_threads() {
        Session session = SessionManager.beginSession();
        SessionManager.launchSimple("locals");
        runCommand(session, "clear all");
        runCommand(session, "threads system");
        try {
            runCommand(session, "threads no_group");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        runCommand(session, "threads");
        SessionManager.deactivate(true);
        SessionManager.endSession();
    }
}
