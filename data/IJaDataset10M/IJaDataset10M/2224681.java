package com.volantis.xml.jdom;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the Visitor.Action typesafe enumeration.
 */
public abstract class VisitorTestAbstract extends TestCaseAbstract {

    public void testActionClassContinue() throws Exception {
        assertEquals("CONTINUE not identified as", "continue", Visitor.Action.CONTINUE.toString());
    }

    public void testActionClassSkip() throws Exception {
        assertEquals("SKIP not identified as", "skip", Visitor.Action.SKIP.toString());
    }

    public void testActionClassStop() throws Exception {
        assertEquals("STOP not identified as", "stop", Visitor.Action.STOP.toString());
    }
}
