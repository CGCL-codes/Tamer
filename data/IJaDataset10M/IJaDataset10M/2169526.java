package org.jostraca.util.test;

import org.jostraca.util.CommandRunner;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.io.StringWriter;

/** <b>Description:</b><br>
 *  Test cases for CommandRunner
 */
public class CommandRunnerTest extends TestCase {

    public CommandRunnerTest(String pName) {
        super(pName);
    }

    public static TestSuite suite() {
        return new TestSuite(CommandRunnerTest.class);
    }

    public static void main(String[] pArgs) {
        TestRunner.run(suite());
    }

    public void testRun() throws Exception {
    }
}
