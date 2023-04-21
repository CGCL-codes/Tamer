package org.shapelogic.mathematics;

import junit.framework.TestCase;

/** Test NaturalNumberStream.
 * 
 * @author Sami Badawi
 *
 */
public class NaturalNumberStreamTest extends TestCase {

    /** Problem 1.
	 * Add all the natural numbers below 1000 that are multiples of 3 or 5.
	 * 
	 */
    public void testNaturalNumberStreamNext() {
        NaturalNumberStream naturalNumberStream = new NaturalNumberStream(2);
        assertNotNull(naturalNumberStream);
        assertTrue(naturalNumberStream.hasNext());
        assertEquals(new Integer(0), naturalNumberStream.next());
        assertEquals(new Integer(1), naturalNumberStream.next());
        assertTrue(naturalNumberStream.hasNext());
        assertEquals(new Integer(2), naturalNumberStream.next());
        assertFalse(naturalNumberStream.hasNext());
        assertNull(naturalNumberStream.next());
    }

    public void testNaturalNumberStreamGet() {
        NaturalNumberStream naturalNumberStream = new NaturalNumberStream(2);
        assertNotNull(naturalNumberStream);
        assertTrue(naturalNumberStream.hasNext());
        assertEquals(new Integer(0), naturalNumberStream.get(0));
        assertEquals(new Integer(1), naturalNumberStream.get(1));
        assertTrue(naturalNumberStream.hasNext());
        assertEquals(new Integer(2), naturalNumberStream.get(2));
        assertTrue(naturalNumberStream.hasNext());
        assertNull(naturalNumberStream.get(3));
        assertTrue(naturalNumberStream.hasNext());
    }

    public void testNaturalNumberStreamStartStop() {
        NaturalNumberStream naturalNumberStream = new NaturalNumberStream(1, 2);
        assertNotNull(naturalNumberStream);
        assertEquals(new Integer(1), naturalNumberStream.next());
        assertEquals(new Integer(2), naturalNumberStream.next());
        assertNull(naturalNumberStream.next());
    }
}
