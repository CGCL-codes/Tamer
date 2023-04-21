package edu.au.mm.sampleset;

import junit.framework.TestCase;

/**
 * JUnit test for the SampleSet class.
 * @author Stefan Maetschke
 * @date   Oct 22, 2007
 */
public class SampleSetTest extends TestCase {

    /** Tests the constructor */
    public void testConstructor() {
        SampleSet set = new SampleSet();
        assertEquals(0, set.size());
        assertEquals(0, set.dimension());
    }

    /** Tests adding of a sample to the set */
    public void testAdd() {
        SampleSet set = new SampleSet();
        set.add(new Sample(1.0));
        assertEquals(1, set.size());
        assertEquals(1.0, set.sample(0).value(0));
        set.add(new Sample(2.0));
        assertEquals(2, set.size());
        assertEquals(2.0, set.sample(1).value(0));
        try {
            set.add(new Sample(1.0, 2.0));
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /** Tests getter for dimension */
    public void testDimension() {
        SampleSet set = new SampleSet();
        assertEquals(0, set.dimension());
        set.add(new Sample(1.0, 2.0));
        assertEquals(2, set.dimension());
    }
}
