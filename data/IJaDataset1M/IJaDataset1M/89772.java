package net.sf.staccatocommons.lang.tuple;

import static junit.framework.Assert.*;
import static net.sf.staccatocommons.lang.tuple.Tuples.*;
import java.util.Date;

/**
 * Test for {@link Quadruple}
 * 
 * @author flbulgarelli
 * 
 */
public class QuadrupleUnitTest extends TupleAbstractUnitTest {

    @Override
    public void testComponents() throws Exception {
        Quadruple<Integer, String, String, String> quad = _(9, "Hello", "World", "!");
        assertEquals((Integer) 9, quad.first());
        assertSame(quad._0(), quad.first());
        assertEquals("Hello", quad.second());
        assertSame(quad._1(), quad.second());
        assertEquals("World", quad.third());
        assertSame(quad._2(), quad.third());
        assertEquals("!", quad.fourth());
        assertSame(quad._3(), quad.fourth());
    }

    @Override
    public void testComparability() throws Exception {
        assertTrue(_(50, 2, 1, 5).compareTo(_(50, 2, 1, 5)) == 0);
        assertTrue(_(50, 2, 1, 5).compareTo(_(50, 2, 1, 4)) > 0);
        assertTrue(_(50, 2, 1, 5).compareTo(_(50, 2, 0, 5)) > 0);
        assertTrue(_(50, 2, 1, 5).compareTo(_(50, 1, 1, 5)) > 0);
        assertTrue(_(50, 2, 1, 5).compareTo(_(49, 2, 1, 5)) > 0);
    }

    @Override
    public void testEqualty() throws Exception {
        assertEquals(_(5, 90L, "blah", "bleh"), _(5, 90L, "blah", "bleh"));
        assertFalse(_("Hello", 5, 2, 3).equals(_("World", 5, 2, 4)));
        Date date = new Date();
        assertEquals(_(40, date, "hello", 'A').hashCode(), _(40, date.clone(), "hello", 'A').hashCode());
    }

    @Override
    public void testToString() throws Exception {
        assertEquals("(90,6,5,9)", _(90, 6, 5, 9).toString());
    }

    @Override
    public void testToArray() throws Exception {
        Quadruple<Integer, Integer, Integer, Integer> quad = _(90, 6, 9, 5);
        Object[] a = quad.toArray();
        assertEquals(quad, _(a[0], a[1], a[2], a[3]));
    }

    @Override
    protected Tuple sampleTuple() {
        return _(9, "", 4);
    }
}
