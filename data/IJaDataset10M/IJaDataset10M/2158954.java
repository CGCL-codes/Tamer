package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SessionManager;
import com.bluemarsh.jswat.SessionSetup;
import junit.extensions.*;
import junit.framework.*;

/**
 * Tests the clear command.
 */
public class clearTest extends CommandTestCase {

    public clearTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new SessionSetup(new TestSuite(clearTest.class));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void test_clear() {
        Session session = SessionManager.beginSession();
        runCommand(session, "clear all");
        try {
            runCommand(session, "clear a1");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        try {
            runCommand(session, "clear -1");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        try {
            runCommand(session, "clear 1");
            fail("expected CommandException");
        } catch (CommandException ce) {
        }
        runCommand(session, "stop clazz:123");
        runCommand(session, "clear 1");
        runCommand(session, "stop clazz:123");
        runCommand(session, "stop clazz:124");
        runCommand(session, "stop clazz:125");
        runCommand(session, "clear 1 2 3");
        runCommand(session, "stop clazz:123");
        runCommand(session, "clear all");
        SessionManager.endSession();
    }
}
