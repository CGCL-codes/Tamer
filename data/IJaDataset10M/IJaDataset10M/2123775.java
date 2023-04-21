package net.sf.alc.cfpj.algorithms;

import junit.framework.TestCase;

public class TestGreaterThan extends TestCase {

    private final BinaryFunctor<Integer, Integer, Boolean> predicate = BinaryPredicates.greaterThan();

    public void testSmaller() {
        assertFalse(predicate.eval(1, 3));
    }

    public void testGreater() {
        assertTrue(predicate.eval(3, 1));
    }

    public void testEqual() {
        assertFalse(predicate.eval(0, 0));
    }

    public void testNull() {
        assertFalse(predicate.eval(null, null));
    }

    public void testNullWithNonNull() {
        assertFalse(predicate.eval(null, 1));
    }

    public void testNonNullWithNull() {
        assertTrue(predicate.eval(1, null));
    }
}
