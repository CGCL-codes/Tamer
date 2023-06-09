package org.gzigzag.util;

import junit.framework.*;
import java.awt.*;
import java.awt.event.*;

/** A JUnit test for the Macintosh mouse hack.
 *  The Macintosh has only one mouse key, so we need to emulate at least the
 *  third (usually rightmost) mouse button; we can do that with using
 *  Meta (Apple) Key-Mouse event. Additionally, when dragging, Apple's/Sun's
 *  VM issues mouse events with no mouse button; this needs to be fixed, too.
 */
public class TestMacMouse extends TestCase {

    public static final String rcsid = "$Id: TestMacMouse.java,v 1.1 2001/07/04 15:11:53 uid31808 Exp $";

    public static boolean dbg = false;

    static final void p(String s) {
        if (dbg) System.out.println(s);
    }

    static final void pa(String s) {
        System.out.println(s);
    }

    public TestMacMouse(String s) {
        super(s);
    }

    /** The fake component we use to issue key events. */
    Component fake;

    public void setUp() {
        fake = new Button();
    }

    public void testMacMouse() {
        InputEventUtil.reset();
        long time = System.currentTimeMillis();
        MouseEvent e = new MouseEvent(fake, MouseEvent.MOUSE_DRAGGED, time, InputEvent.META_MASK, 0, 0, 0, false);
        MouseEvent f = InputEventUtil.mouseEventHack(e);
        assertTrue((f.getModifiers() & f.BUTTON1_MASK) == 0);
        assertTrue((f.getModifiers() & f.BUTTON3_MASK) != 0);
        e = new MouseEvent(fake, MouseEvent.MOUSE_DRAGGED, time, 0, 0, 0, 0, false);
        f = InputEventUtil.mouseEventHack(e);
        assertTrue((f.getModifiers() & f.BUTTON1_MASK) != 0);
        assertTrue((f.getModifiers() & f.BUTTON3_MASK) == 0);
        e = new MouseEvent(fake, MouseEvent.MOUSE_RELEASED, time, InputEvent.META_MASK | InputEvent.BUTTON1_MASK, 0, 0, 0, false);
        f = InputEventUtil.mouseEventHack(e);
        assertTrue((f.getModifiers() & f.BUTTON1_MASK) == 0);
        assertTrue((f.getModifiers() & f.BUTTON3_MASK) != 0);
        e = new MouseEvent(fake, MouseEvent.MOUSE_PRESSED, time, InputEvent.BUTTON1_MASK, 0, 0, 0, false);
        f = InputEventUtil.mouseEventHack(e);
        assertTrue((f.getModifiers() & f.BUTTON1_MASK) != 0);
        assertTrue((f.getModifiers() & f.BUTTON3_MASK) == 0);
    }
}
