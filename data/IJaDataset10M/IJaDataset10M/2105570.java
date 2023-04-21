package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SessionManager;
import com.bluemarsh.jswat.SessionSetup;
import junit.extensions.*;
import junit.framework.*;

/**
 * Tests performed while the Session is inactive which are expected to
 * succeed.
 */
public class InactiveCases extends CommandTestCase {

    public InactiveCases(String name) {
        super(name);
    }

    public static Test suite() {
        return new SessionSetup(new TestSuite(InactiveCases.class));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void test_commands_inactive_simple() {
        Session session = SessionManager.beginSession();
        String[] commands = new String[] { "about", "alias", "brkinfo", "capture", "classpath", "copysession", "exclude", "help help", "history", "loadsession", "logging", "monitor", "options", "props", "rmsession", "sourcepath", "stderr ignore this", "stderr", "stdout ignore this", "stdout", "version" };
        for (int ii = 0; ii < commands.length; ii++) {
            try {
                runCommand(session, commands[ii]);
            } catch (CommandException ce) {
                fail(commands[ii] + ": " + ce.getMessage());
            } catch (Exception e) {
                fail(commands[ii] + ": " + e.toString());
            }
        }
        SessionManager.endSession();
    }

    public void test_commands_inactive_multi() {
        Session session = SessionManager.beginSession();
        String[] commands = new String[] { "", ";;;", "dummy;dummy;dummy", "dummy ; dummy ; dummy", "dummy \"string\"", "dummy \\\"string\\\"", "dummy \\\\\"string\\\\\"", "dummy \\\\ \\ blah", "dummy 'string'", "dummy \'string\'", "dummy \\\'string\\\'", "dummy \";\" ; dummy \"a\" ; dummy \"\\\"\"", "dummy \"a\" ; dummy \"b\" ; dummy \"\\\"c\"", "dummy \"a;b\" ; dummy 'c\"' ; dummy '\\'d'", "dummy \"a;b\"" };
        for (int ii = 0; ii < commands.length; ii++) {
            try {
                runCommandMgr(session, commands[ii]);
            } catch (CommandException ce) {
                fail(commands[ii] + ": " + ce.getMessage());
            } catch (Exception e) {
                fail(commands[ii] + ": " + e.toString());
            }
        }
        SessionManager.endSession();
    }
}
