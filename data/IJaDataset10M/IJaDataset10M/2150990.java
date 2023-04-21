package net.sf.alc.cfpj.algorithms;

import junit.framework.TestCase;

public class TestNotEqualTo extends TestCase {

    private static final BinaryFunctor<Object, Object, Boolean> predicate = BinaryPredicates.notEqualTo();

    public void test1() {
        assertTrue(predicate.eval(1, 3));
    }

    public void test2() {
        assertTrue(predicate.eval(3, 1));
    }

    public void test3() {
        assertFalse(predicate.eval(3, 3));
    }

    public void test4() {
        assertFalse(predicate.eval(null, null));
    }

    public void test5() {
        assertTrue(predicate.eval(null, 3));
    }

    public void test6() {
        assertTrue(predicate.eval(3, null));
    }
}
