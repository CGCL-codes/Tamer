package com.w20e.socrates.rendering;

import junit.framework.TestCase;

public class TestGoto extends TestCase {

    private Goto gt;

    protected void setUp() throws Exception {
        super.setUp();
        this.gt = new Goto("xxx");
    }

    public void testGetTargetStateId() {
        assertEquals("xxx", this.gt.getTargetStateId());
    }
}
