package org.apache.harmony.sound.tests.javax.sound.midi;

import junit.framework.TestCase;
import javax.sound.midi.Patch;

public class PatchTest extends TestCase {

    /**
     * test constructor of class Patch
     *
     */
    public void test_constructor() {
        Patch patch = new Patch(34, 68);
        assertEquals(34, patch.getBank());
        assertEquals(68, patch.getProgram());
        Patch patch2 = new Patch(-4, 567);
        assertEquals(-4, patch2.getBank());
        assertEquals(567, patch2.getProgram());
    }

    /**
     * test method getBank() of class Patch
     *
     */
    public void test_getBank() {
        Patch patch = new Patch(45, 78);
        assertEquals(45, patch.getBank());
        Patch patch1 = new Patch(-78, 78);
        assertEquals(-78, patch1.getBank());
        Patch patch2 = new Patch(16400, 78);
        assertEquals(16400, patch2.getBank());
    }

    /**
     * test method getProgram() of class Patch
     *
     */
    public void test_getProgram() {
        Patch patch = new Patch(45, 78);
        assertEquals(78, patch.getProgram());
        Patch patch1 = new Patch(-78, -5);
        assertEquals(-5, patch1.getProgram());
        Patch patch2 = new Patch(16400, 216);
        assertEquals(216, patch2.getProgram());
    }
}
