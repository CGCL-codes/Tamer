package org.robotframework.maven.acceptance;

import java.io.File;
import org.robotframework.maven.RobotMojo;
import org.robotframework.maven.testutils.RobotInstallation;
import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class TestInterpreterSelection extends TestCase {

    private RobotMojo mojo;

    protected void setUp() throws Exception {
        super.setUp();
        mojo = new RobotMojo();
        mojo.robotTestDirectory = new File("src/test/resources/robot-tests/interpreter_test.html");
    }

    public void testRobotIsExecutedSuccessfullyWithJython() throws Exception {
        mojo.robotScript = RobotInstallation.pathToJybot();
        mojo.robotArguments.add("--variable");
        mojo.robotArguments.add("INTERPRETER:jython");
        mojo.execute();
    }

    public void testRobotIsExecutedSuccessfullyWithPython() throws Exception {
        mojo.robotScript = RobotInstallation.pathToPybot();
        mojo.robotArguments.add("--variable");
        mojo.robotArguments.add("INTERPRETER:python");
        mojo.execute();
    }
}
