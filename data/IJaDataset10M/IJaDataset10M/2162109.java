package com.amazonaws.mturk.cmd.test.scripts;

import com.amazonaws.mturk.cmd.test.util.ScriptRunner.ScriptResult;
import junit.textui.TestRunner;

public class TestResetAccount extends TestBase {

    public static void main(String[] args) {
        TestRunner.run(TestResetAccount.class);
    }

    public TestResetAccount(String arg0) {
        super(arg0);
    }

    public void testHappyCase() throws Exception {
        ScriptResult result = runScript("resetAccount", "--force");
        assertEquals(0, result.getExitValue());
    }
}
