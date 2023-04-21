package com.bluemarsh.jswat.core.breakpoint;

import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.SessionHelper;
import com.sun.jdi.Location;
import java.io.File;
import java.net.MalformedURLException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HitCountTest {

    @Test
    public void countEqual() {
        Session session = SessionHelper.getSession();
        BreakpointFactory bf = BreakpointProvider.getBreakpointFactory();
        BreakpointManager bm = BreakpointProvider.getBreakpointManager(session);
        int line = 33;
        String srcpath = System.getProperty("test.src.dir");
        File srcfile = new File(srcpath, "HitCountTestCode.java");
        Breakpoint bp = null;
        try {
            String url = srcfile.toURI().toURL().toString();
            bp = bf.createLineBreakpoint(url, null, line);
            HitCountCondition cond = new HitCountCondition();
            cond.setCount(2);
            cond.setType(HitCountConditionType.EQUAL);
            bp.addCondition(cond);
            bm.addBreakpoint(bp);
            assertNotNull(cond.describe());
            assertEquals(2, cond.getCount());
            assertEquals(HitCountConditionType.EQUAL, cond.getType());
            assertTrue(cond.isVisible());
        } catch (MalformedClassNameException mcne) {
            fail(mcne.toString());
        } catch (MalformedURLException mue) {
            fail(mue.toString());
        }
        SessionHelper.launchDebuggee(session, "HitCountTestCode");
        SessionHelper.resumeAndWait(session);
        Location loc = BreakpointHelper.getLocation(session);
        assertNotNull("failed for hit 2", loc);
        assertEquals(line, loc.lineNumber());
        assertEquals(2, bp.getHitCount());
        assertTrue("loop variable has wrong value", BreakpointHelper.compareVariable(session, "ii", new Integer(1)));
        bm.removeBreakpoint(bp);
        SessionHelper.resumeAndWait(session);
        loc = BreakpointHelper.getLocation(session);
        assertNull("failed to terminate", loc);
        assertFalse("failed to disconnect", session.isConnected());
    }

    @Test
    public void countGreater() {
        Session session = SessionHelper.getSession();
        BreakpointFactory bf = BreakpointProvider.getBreakpointFactory();
        BreakpointManager bm = BreakpointProvider.getBreakpointManager(session);
        int line = 33;
        String srcpath = System.getProperty("test.src.dir");
        File srcfile = new File(srcpath, "HitCountTestCode.java");
        Breakpoint bp = null;
        try {
            String url = srcfile.toURI().toURL().toString();
            bp = bf.createLineBreakpoint(url, null, line);
            HitCountCondition cond = new HitCountCondition();
            cond.setCount(2);
            cond.setType(HitCountConditionType.GREATER);
            bp.addCondition(cond);
            bm.addBreakpoint(bp);
        } catch (MalformedClassNameException mcne) {
            fail(mcne.toString());
        } catch (MalformedURLException mue) {
            fail(mue.toString());
        }
        SessionHelper.launchDebuggee(session, "HitCountTestCode");
        for (int ii = 2; ii < 5; ii++) {
            SessionHelper.resumeAndWait(session);
            assertEquals(ii + 1, bp.getHitCount());
            Location loc = BreakpointHelper.getLocation(session);
            assertNotNull("failed for hit " + ii, loc);
            assertEquals(line, loc.lineNumber());
            assertTrue("loop variable has wrong value", BreakpointHelper.compareVariable(session, "ii", new Integer(ii)));
        }
        bm.removeBreakpoint(bp);
        SessionHelper.resumeAndWait(session);
        Location loc = BreakpointHelper.getLocation(session);
        assertNull("failed to terminate", loc);
        assertFalse("failed to disconnect", session.isConnected());
    }

    @Test
    public void countMultiple() {
        Session session = SessionHelper.getSession();
        BreakpointFactory bf = BreakpointProvider.getBreakpointFactory();
        BreakpointManager bm = BreakpointProvider.getBreakpointManager(session);
        int line = 33;
        String srcpath = System.getProperty("test.src.dir");
        File srcfile = new File(srcpath, "HitCountTestCode.java");
        Breakpoint bp = null;
        try {
            String url = srcfile.toURI().toURL().toString();
            bp = bf.createLineBreakpoint(url, null, line);
            HitCountCondition cond = new HitCountCondition();
            cond.setCount(2);
            cond.setType(HitCountConditionType.MULTIPLE);
            bp.addCondition(cond);
            bm.addBreakpoint(bp);
        } catch (MalformedClassNameException mcne) {
            fail(mcne.toString());
        } catch (MalformedURLException mue) {
            fail(mue.toString());
        }
        SessionHelper.launchDebuggee(session, "HitCountTestCode");
        for (int ii = 2; ii <= 10; ii += 2) {
            SessionHelper.resumeAndWait(session);
            assertEquals(ii, bp.getHitCount());
            Location loc = BreakpointHelper.getLocation(session);
            assertNotNull("failed for hit " + ii, loc);
            assertEquals(line, loc.lineNumber());
            assertTrue("loop variable has wrong value", BreakpointHelper.compareVariable(session, "ii", new Integer(ii - 1)));
        }
        bm.removeBreakpoint(bp);
        SessionHelper.resumeAndWait(session);
        Location loc = BreakpointHelper.getLocation(session);
        assertNull("failed to terminate", loc);
        assertFalse("failed to disconnect", session.isConnected());
    }
}
