package com.algorithmdb.algorithms.math.test;

import static org.junit.Assert.*;
import com.openjdk.sourcecode.ArrayList;
import org.junit.Test;
import com.algorithmdb.algorithms.math.Statistics;

/**
 * 
 * @author Cody Long
 * @date Dec 16 2010
 * 
 * A class which tests the abstract class Statistics.
 *
 */
public class StatisticsTest {

    @Test
    public void testStandardDeviationIntArray() {
        double expected = Math.sqrt(42);
        double actual = Statistics.standardDeviation(new int[] { 1, 3, 4, 6, 9, 19 });
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testStandardDeviationArrayListOfInteger() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(1);
        a.add(3);
        a.add(4);
        a.add(6);
        a.add(9);
        a.add(19);
        double expected = Math.sqrt(42);
        double actual = Statistics.standardDeviation(a);
        assertEquals(expected, actual, 0);
    }
}
