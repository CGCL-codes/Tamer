package net.sf.paperclips;

import junit.framework.TestCase;

public class ColumnPrintTest extends TestCase {

    public void testConstructor_invalidArguments() {
        try {
            new ColumnPrint(null, 2, 0, true);
            fail();
        } catch (IllegalArgumentException expected) {
        }
        try {
            new ColumnPrint(new PrintStub(), 1, 0, true);
            fail("bad columns argument was not caught");
        } catch (IllegalArgumentException expected) {
        }
        try {
            new ColumnPrint(new PrintStub(), 2, -1, true);
            fail("bad columnSpacing argument was not caught");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testEquals() {
        ColumnPrint columnPrint = new ColumnPrint(new PrintStub(0), 2, 0, true);
        assertEquals(columnPrint, new ColumnPrint(new PrintStub(0), 2, 0, true));
        assertFalse(columnPrint.equals(new ColumnPrint(new PrintStub(1), 2, 0, true)));
        assertFalse(columnPrint.equals(new ColumnPrint(new PrintStub(0), 3, 0, true)));
        assertFalse(columnPrint.equals(new ColumnPrint(new PrintStub(0), 2, 1, true)));
        assertFalse(columnPrint.equals(new ColumnPrint(new PrintStub(0), 2, 0, false)));
    }
}
