package org.gzigzag.test;

import org.gzigzag.*;
import junit.framework.*;
import java.awt.*;
import java.awt.event.*;

/** A JUnit test for the EditCursor class.
 */
public class TestEditCursors extends TestCase {

    public static final String rcsid = "$Id: TestEditCursors.java,v 1.3 2001/06/16 09:52:05 tjl Exp $";

    public static boolean dbg = false;

    protected static void p(String s) {
        if (dbg) ZZLogger.log(s);
    }

    protected static void pa(String s) {
        ZZLogger.log(s);
    }

    public TestEditCursors(String s) {
        super(s);
    }

    ZZDimSpace sp = new ZZDimSpace();

    ZZCell home = sp.getHomeCell();

    public void testEditCursorStack() {
        ZZCell[] c = new ZZCell[] { home.N(), home.N(), home.N(), home.N() };
        ZZCursor[] cursors = new ZZCursor[] { new ZZCursorVirtual(c[0]), new ZZCursorVirtual(c[1]), new ZZCursorVirtual(c[2]), new ZZCursorVirtual(c[3]) };
        ZZCell cur = home.N();
        EditCursor.set(cur, c[0]);
        assertEquals(c[0], EditCursor.get(cur));
        assertEquals(c[0], EditCursor.get(cur));
        assertEquals(c[0], ZZCursorReal.get(cur));
        EditCursor.push(cur, c[1]);
        assertEquals(c[1], EditCursor.get(cur));
        assertEquals(c[1], EditCursor.get(cur));
        EditCursor.set(cur, c[2]);
        assertEquals(c[2], EditCursor.get(cur));
        assertEquals(c[2], EditCursor.get(cur));
        assertEquals(c[0], ZZCursorReal.get(cur));
        EditCursor.push(cur, c[3]);
        assertEquals(c[3], EditCursor.get(cur));
        assertEquals(c[3], EditCursor.get(cur));
        EditCursor.pop(cur);
        assertEquals(EditCursor.get(cur), c[2]);
        assertEquals(c[2], EditCursor.get(cur));
        assertEquals(c[2], EditCursor.get(cur));
        EditCursor.pop(cur);
        assertEquals(c[0], EditCursor.get(cur));
        EditCursor.set(cur, c[3]);
        EditCursor.push(cur, c[1]);
        assertEquals(c[1], EditCursor.get(cur));
        assertEquals(c[3], ZZCursorReal.get(cur));
        EditCursor.push(cur, c[2]);
        EditCursor.push(cur, c[2]);
        EditCursor.push(cur, c[2]);
        EditCursor.push(cur, c[2]);
        EditCursor.setStack(cur, cursors);
        assertEquals(c[3], EditCursor.get(cur));
        assertEquals(c[0], ZZCursorReal.get(cur));
        VirtualEditCursor vc = EditCursor.getVirtual(cur);
        assertEquals(c[3], vc.get());
        vc.push(c[1]);
        EditCursor.setStack(cur, vc);
        assertEquals(c[1], EditCursor.get(cur));
        EditCursor.pop(cur);
        assertEquals(c[3], EditCursor.get(cur));
        EditCursor.pop(cur);
        EditCursor.pop(cur);
        EditCursor.pop(cur);
        boolean raised = false;
        try {
            EditCursor.pop(cur);
        } catch (ZZError e) {
            raised = true;
        }
        assertTrue(raised);
    }
}
