package emulator.unittest.hardware.nmos6502;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import emulator.hardware.nmos6502.BreakPoints;

public class BreakPointsTest {

    private BreakPoints breakpoints;

    private int break1 = 13;

    private int break2 = 21;

    private int stop1 = 7;

    private int stop2 = 15;

    @Before
    public void setUp() throws Exception {
        breakpoints = new BreakPoints();
        breakpoints.addBreak(break1);
        breakpoints.addBreak(break2);
        breakpoints.addStop(stop1);
        breakpoints.addStop(stop2);
    }

    @Test
    public void testBreaks() {
        assertFalse(breakpoints.checkBreak(0));
        assertTrue(breakpoints.checkBreak(break1));
        assertTrue(breakpoints.checkBreak(break2));
    }

    @Test
    public void testStops() {
        assertTrue(breakpoints.checkBreak(stop1));
        assertTrue(breakpoints.checkBreak(stop2));
    }

    @Test
    public void testClearStops() {
        breakpoints.clearAllStops();
        assertFalse(breakpoints.checkBreak(stop1));
        assertFalse(breakpoints.checkBreak(stop2));
        assertTrue(breakpoints.checkBreak(break1));
        assertTrue(breakpoints.checkBreak(break2));
    }

    @Test
    public void testRemoveBreak() {
        breakpoints.removeBreak(break2);
        assertTrue(breakpoints.checkBreak(break1));
        assertFalse(breakpoints.checkBreak(break2));
        assertTrue(breakpoints.checkBreak(stop1));
        assertTrue(breakpoints.checkBreak(stop2));
    }
}
