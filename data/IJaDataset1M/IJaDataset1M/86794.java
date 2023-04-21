package org.happy.commons.ver1x0.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.happy.commons.util.Arrays_1x0;
import org.junit.Test;

public class ArrayHelperTest {

    @Test
    public void testSum() {
        Integer[] a = new Integer[] { 3, 2, 4, 5 };
        assertEquals(14, ArraysHelper.sum(a));
        a = new Integer[10000];
        for (int i = 0; i < a.length; i++) a[i] = (int) java.lang.Math.random() * 1000;
        Integer[] b = a.clone();
        Arrays_1x0.shuffle(b, b.length * 2);
        assertEquals(ArraysHelper.sum(a), ArraysHelper.sum(b));
    }

    @Test
    public void testMult() {
        Integer[] numbers = new Integer[] { 2, 3, 5, 7, 11, 13, 17 };
        Long multResult = ArraysHelper.mult(numbers);
        assertEquals(new Long(2 * 3 * 5 * 7 * 11 * 13 * 17), multResult);
    }

    @Test
    public void testSortedOrder() {
        Integer[] a = new Integer[] { 3, 2, 4, 5 };
        assertTrue(2 == ArraysHelper.sortedOrder(a));
        a = new Integer[] { 1, 2, 3, 5 };
        assertTrue(4 == ArraysHelper.sortedOrder(a));
        a = new Integer[] { 5, 3, 2, 1 };
        assertTrue(-4 == ArraysHelper.sortedOrder(a));
    }
}
