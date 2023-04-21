package org.nakedobjects.viewer.skylark.drawing;

import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SizeTest extends TestCase {

    private Size s;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SizeTest.class);
    }

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        s = new Size(10, 20);
    }

    public void testCopy() {
        Size m = new Size(s);
        assertTrue(s != m);
        assertEquals(s, m);
    }

    public void testEnsure() {
        s.ensureWidth(18);
        assertEquals(new Size(18, 20), s);
        s.ensureWidth(12);
        assertEquals(new Size(18, 20), s);
        s.ensureHeight(16);
        assertEquals(new Size(18, 20), s);
        s.ensureHeight(26);
        assertEquals(new Size(18, 26), s);
    }

    public void addPadding() {
        s.extend(new Padding(1, 2, 3, 4));
        assertEquals(new Size(14, 26), s);
    }

    public void testExtend() {
        s.extendWidth(8);
        assertEquals(new Size(18, 20), s);
        s.extendHeight(6);
        assertEquals(new Size(18, 26), s);
        s.extend(new Size(3, 5));
        assertEquals(new Size(21, 31), s);
        s.extend(5, 3);
        assertEquals(new Size(26, 34), s);
    }
}
