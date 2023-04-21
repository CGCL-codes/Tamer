package org.mili.jmibs.impl;

import static org.junit.Assert.*;
import org.easymock.*;
import org.junit.*;
import org.mili.jmibs.api.*;

/**
 * @author Michael Lieshoff
 * @version 1.0 04.06.2010
 * @since 1.1
 */
public class IntegerIntervalTest {

    private IntervalBenchmark<IntegerInterval> b = null;

    @Before
    public void setUp() throws Exception {
        b = (IntervalBenchmark<IntegerInterval>) EasyMock.createMock(IntervalBenchmark.class);
    }

    @Test
    public void testIntegerInterval() {
        new IntegerInterval();
    }

    @Test
    public void testIntegerIntervalIntegerInteger() {
        IntegerInterval i = new IntegerInterval(10, 40);
        assertEquals(10, (int) i.getMin());
        assertEquals(40, (int) i.getMax());
    }

    @Test
    public void testCreate() {
        IntegerInterval i = IntegerInterval.create(10, 40);
        assertNotNull(i);
        assertEquals(10, (int) i.getMin());
        assertEquals(40, (int) i.getMax());
    }

    @Test
    public void testTraverse() {
        this.b.execute();
        EasyMock.expectLastCall().times(30);
        EasyMock.replay(this.b);
        IntegerInterval i = IntegerInterval.create(10, 40);
        i.traverse(this.b);
        assertEquals(39, (int) i.getValue());
    }

    @Test
    public void testGetDifference() {
        IntegerInterval i = IntegerInterval.create(10, 40);
        assertEquals(30, (int) i.getDifference());
    }

    @Test
    public void testGetMinimumInterval() {
        IntegerInterval i = IntegerInterval.create(10, 40);
        IntegerInterval i0 = i.getMinimumInterval();
        assertEquals(0, (int) i0.getMin());
        assertEquals(1, (int) i0.getMax());
    }
}
