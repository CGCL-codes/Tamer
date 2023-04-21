package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SessionManager;
import com.bluemarsh.jswat.SessionSetup;
import junit.extensions.*;
import junit.framework.*;

/**
 * Tests the locals command.
 */
public class localsTest extends CommandTestCase {

    public localsTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new SessionSetup(new TestSuite(localsTest.class));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void test_locals() {
        Session session = SessionManager.beginSession();
        SimpleSessionListener ssl = new SimpleSessionListener();
        session.addListener(ssl);
        SessionManager.launchSimple("locals");
        runCommand(session, "clear all");
        runCommand(session, "thread main");
        try {
            runCommand(session, "locals");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        runCommand(session, "runto locals:189");
        waitForSuspend(ssl);
        try {
            runCommand(session, "locals a1");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        try {
            runCommand(session, "locals -1");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        try {
            runCommand(session, "locals 12345");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        runCommand(session, "locals");
        SessionManager.deactivate(true);
        SessionManager.launchSimple("tutorial");
        runCommand(session, "clear all");
        runCommand(session, "threadbrk main death");
        resumeAndWait(session, ssl);
        runCommand(session, "clear all");
        runCommand(session, "resume");
        runCommand(session, "thread Finalizer");
        try {
            runCommand(session, "locals");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        SessionManager.deactivate(true);
        session.removeListener(ssl);
        SessionManager.endSession();
    }
}
