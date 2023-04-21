package com.amazonaws.mturk.cmd.test;

import junit.textui.TestRunner;
import com.amazonaws.mturk.cmd.GetBalance;

public class TestGetBalance extends TestBase {

    private static GetBalance cmd = null;

    static {
        cmd = new GetBalance();
        cmd.setSandBoxMode();
    }

    public static void main(String[] args) {
        TestRunner.run(TestGetBalance.class);
    }

    public TestGetBalance(String arg0) {
        super(arg0);
    }

    public void testHappyCase() throws Exception {
        cmd.getBalance();
    }
}
