package net.sf.paperclips;

import junit.framework.TestCase;

public class BreakPrintTest extends TestCase {

    public void testEquals_equivalent() {
        assertEquals(new BreakPrint(), new BreakPrint());
    }

    public void testEquals_different() {
        assertFalse(new BreakPrint().equals(new PrintStub()));
    }
}
