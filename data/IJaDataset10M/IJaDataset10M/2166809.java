package com.controltier.ctl;

import com.controltier.ctl.tools.CtlTest;

public class TestConstants extends CtlTest {

    public TestConstants(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
    }

    public void testConstants() {
        assertNotNull(Constants.getSystemCtlHome());
        assertNotNull(Constants.getSystemCtlBase());
        assertNotNull(Constants.getSystemAntHome());
        assertNotNull(Constants.MAIN_CLASSNAME);
    }
}
