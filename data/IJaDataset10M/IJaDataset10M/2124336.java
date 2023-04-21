package net.derquinse.common.base;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import net.derquinse.common.test.EqualityTests;
import org.testng.annotations.Test;

/**
 * Tests for LongWaterMark
 * @author Andres Rodriguez
 */
public class LongWaterMarkTest {

    private LongWaterMark m;

    private void test(long current, long min, long max) {
        assertNotNull(m);
        assertEquals(m.get(), current);
        assertEquals(m.getMin(), min);
        assertEquals(m.getMax(), max);
        assertTrue(min <= current);
        assertTrue(max >= current);
    }

    private void equalTo(LongWaterMark other) {
        EqualityTests.two(m, other);
    }

    /**
	 * Empty.
	 */
    @Test
    public void empty() {
        m = LongWaterMark.of();
        test(0, 0, 0);
    }

    /**
	 * Initial value.
	 */
    @Test(dependsOnMethods = "empty")
    public void initial() {
        m = LongWaterMark.of(7);
        test(7, 7, 7);
    }

    /**
	 * Mutations.
	 */
    @Test(dependsOnMethods = "initial")
    public void mutation() {
        m = m.inc();
        test(8, 7, 8);
        m = m.dec();
        test(7, 7, 8);
        m = m.dec();
        test(6, 6, 8);
        m = m.inc();
        test(7, 6, 8);
        m = m.add(4);
        test(11, 6, 11);
        m = m.add(-7);
        test(4, 4, 11);
        m = m.set(9);
        test(9, 4, 11);
    }

    /**
	 * Equality.
	 */
    @Test(dependsOnMethods = "mutation")
    public void equals() {
        equalTo(m);
        equalTo(m.inc().dec());
        equalTo(LongWaterMark.of(m.getMin()).set(m.getMax()).set(m.get()));
    }

    private void notEquals(LongWaterMark other) {
        assertNotEquals(m, other);
        assertNotEquals(other, m);
    }

    /**
	 * Inequality.
	 */
    @Test(dependsOnMethods = "equals")
    public void notEquals() {
        notEquals(null);
        notEquals(m.inc());
        notEquals(m.dec());
        notEquals(m.set(m.getMin() - 10).set(m.get()));
        notEquals(m.set(m.getMax() + 10).set(m.get()));
    }
}
