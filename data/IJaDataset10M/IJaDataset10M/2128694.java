package com.xpresso.utils.unitTests.comline;

import com.xpresso.utils.exceptions.XpressoException;
import com.xpresso.utils.system.CommandLine;
import com.xpresso.utils.system.CommandLineListener;
import com.xpresso.utils.system.CommandResult;
import junit.framework.TestCase;

public class TestCommandLine extends TestCase implements CommandLineListener {

    public void testCommnadLine() {
        try {
            System.out.println("Before executing commnad");
            CommandLine.execute("whoami");
            System.out.println("After executing command");
        } catch (XpressoException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCommandLineObject() {
        try {
            System.out.println("----- Before executing commnad");
            CommandLine cmd = new CommandLine();
            CommandResult result = cmd.exec("whoami", null);
            System.out.println("After executing command. Result: " + result.getFinalOutput());
            System.out.println("Command exit value: " + result.getExitValue());
        } catch (XpressoException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCommandLineListener() {
        try {
            System.out.println("----- Before executing commnad");
            CommandLine cmd = new CommandLine();
            CommandResult result = cmd.exec("ls -la", this);
            System.out.println("After executing command. Result: " + result.getFinalOutput());
        } catch (XpressoException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void handleErrorMessage(String error) {
        System.out.println("Error from command: " + error);
    }

    public void handleOutputMessage(String output) {
        System.out.println("Output from command: " + output);
    }

    public void testErrorCommand() {
        try {
            System.out.println("----- Before executing commnad");
            CommandLine cmd = new CommandLine();
            CommandResult result = cmd.exec("badcommand", null);
            System.out.println("After executing command. Output: " + result.getFinalOutput());
            System.out.println("After executing command. Error: " + result.getFinalError());
            System.out.println("Command exit value: " + result.getExitValue());
        } catch (XpressoException e) {
            e.printStackTrace();
            fail();
        }
    }
}
