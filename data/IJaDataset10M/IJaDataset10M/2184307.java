package de.jakop.rugby.util;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.DebugGraphics;
import javax.swing.JPanel;
import junit.framework.TestCase;

/**
 * 
 * @author jakop
 */
public class EmptyIconTest extends TestCase {

    /** */
    public void testGetInstance() {
        assertEquals(EmptyIcon.get16Instance(), EmptyIcon.getInstance());
    }

    /** */
    public void testGet16Instance() {
        assertEquals(16, EmptyIcon.get16Instance().getIconHeight());
        assertEquals(16, EmptyIcon.get16Instance().getIconWidth());
    }

    /** */
    public void testGet20Instance() {
        assertEquals(20, EmptyIcon.get20Instance().getIconHeight());
        assertEquals(20, EmptyIcon.get20Instance().getIconWidth());
    }

    /** */
    public void testEmptyIcon() {
        EmptyIcon e = new EmptyIcon(67, 14);
        assertEquals(14, e.getIconWidth());
        assertEquals(67, e.getIconHeight());
    }

    /** */
    public void testPaintIcon() {
        EmptyIcon e = new EmptyIcon(12, 56);
        int x = 1;
        int y = 2;
        Component c = new JPanel();
        Graphics g = new DebugGraphics();
        e.paintIcon(c, g, x, y);
        assertEquals(1, x);
        assertEquals(2, y);
        assertEquals(c, c);
        assertEquals(g, g);
    }
}
