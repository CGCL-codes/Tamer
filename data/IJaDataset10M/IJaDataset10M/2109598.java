package org.isakiev.wic.polynomial;

import org.isakiev.wic.polynomial.DefaultFunction;
import org.isakiev.wic.polynomial.Function;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * Unit tests for default function
 * 
 * @author Ruslan Isakiev
 */
public class DefaultFunctionTest extends TestCase {

    private static Function f = new DefaultFunction();

    @Test
    public void test_0_0() {
        assertEquals(1.0, f.evaluate(0, 0));
    }

    @Test
    public void test_0_1() {
        assertEquals(0.5, f.evaluate(0, 1));
    }

    @Test
    public void test_1_0() {
        assertEquals(0.5, f.evaluate(1, 0));
    }

    @Test
    public void test_1_1() {
        assertEquals(0.0, f.evaluate(1, 1));
    }

    @Test
    public void test_1_0_05() {
        assertEquals(0.5, f.evaluate(1, 0, 0.5));
    }
}
