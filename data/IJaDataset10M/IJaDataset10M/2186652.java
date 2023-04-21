package org.hanseltest.probes;

import junit.framework.TestCase;
import org.hansel.probes.EQComparator;
import org.hansel.probes.GEComparator;
import org.hansel.probes.GTComparator;
import org.hansel.probes.LEComparator;
import org.hansel.probes.LTComparator;
import org.hansel.probes.NEComparator;
import org.hansel.stack.HanselValue;

/**
 * Tests for the  org.hansel.probe.*Comparator classes.
 *
 * @author Niklas Mehner
 */
public class IntComparatorTest extends TestCase {

    private static HanselValue op1 = new HanselValue("op1", false, 1);

    private static HanselValue op2 = new HanselValue("op2", false, 1);

    /**
     * Create a new Test.
     * @param name Name of the test.
     */
    public IntComparatorTest(String name) {
        super(name);
    }

    /**
     * Test the EQComparator.
     */
    public void testEQComparator() {
        EQComparator eq = new EQComparator();
        assertEquals(6, eq.getPrecedence());
        assertEquals("==", eq.getSign());
        assertTrue(eq.compare(0, 0));
        assertTrue(!eq.compare(1, 0));
        assertEquals("op1 == op2", eq.createStackEntry(op1, op2).toString());
    }

    /**
     * Test the NEComparator.
     */
    public void testNEComparator() {
        NEComparator ne = new NEComparator();
        assertEquals(6, ne.getPrecedence());
        assertEquals("!=", ne.getSign());
        assertTrue(!ne.compare(0, 0));
        assertTrue(ne.compare(1, 0));
        assertEquals("op1 != op2", ne.createStackEntry(op1, op2).toString());
    }

    /**
     * Test the GTComparator.
     */
    public void testGTComparator() {
        GTComparator gt = new GTComparator();
        assertEquals(5, gt.getPrecedence());
        assertEquals(">", gt.getSign());
        assertTrue(!gt.compare(0, 0));
        assertTrue(!gt.compare(5, 7));
        assertTrue(gt.compare(8, 7));
        assertEquals("op1 > op2", gt.createStackEntry(op1, op2).toString());
    }

    /**
     * Test the GEComparator.
     */
    public void testGEComparator() {
        GEComparator ge = new GEComparator();
        assertEquals(5, ge.getPrecedence());
        assertEquals(">=", ge.getSign());
        assertTrue(ge.compare(0, 0));
        assertTrue(!ge.compare(5, 7));
        assertTrue(ge.compare(8, 7));
        assertEquals("op1 >= op2", ge.createStackEntry(op1, op2).toString());
    }

    /**
     * Test the LTComparator.
     */
    public void testLTComparator() {
        LTComparator lt = new LTComparator();
        assertEquals(5, lt.getPrecedence());
        assertEquals("<", lt.getSign());
        assertTrue(!lt.compare(0, 0));
        assertTrue(lt.compare(5, 7));
        assertTrue(!lt.compare(8, 7));
        assertEquals("op1 < op2", lt.createStackEntry(op1, op2).toString());
    }

    /**
     * Test the LEComparator.
     */
    public void testLEComparator() {
        LEComparator le = new LEComparator();
        assertEquals(5, le.getPrecedence());
        assertEquals("<=", le.getSign());
        assertTrue(le.compare(0, 0));
        assertTrue(le.compare(5, 7));
        assertTrue(!le.compare(8, 7));
        assertEquals("op1 <= op2", le.createStackEntry(op1, op2).toString());
    }
}
