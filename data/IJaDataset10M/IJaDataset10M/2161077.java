package org.hswgt.teachingbox.core.rl.unittests;

import java.util.Arrays;
import org.hswgt.teachingbox.core.rl.tools.RangeParser;
import org.junit.Test;
import cern.jet.random.Uniform;

public class TestRangeParser {

    static final double EPSILON = 0.000000001;

    @Test
    public void test3Floats() {
        for (int i = 0; i < 100; i++) {
            double[] input = new double[] { Uniform.staticNextDoubleFromTo(-10, 10), Uniform.staticNextDoubleFromTo(-10, 10), Uniform.staticNextDoubleFromTo(-10, 10) };
            double[] parsed = RangeParser.parse("[" + input[0] + ":" + input[1] + ":" + input[2] + "]");
            assertArrayEquals(parsed, input);
        }
    }

    @Test
    public void test2Floats() {
        for (int i = 0; i < 100; i++) {
            double[] input = new double[] { Uniform.staticNextDoubleFromTo(-10, 10), Uniform.staticNextDoubleFromTo(-10, 10) };
            double[] result = { input[0], 1, input[1] };
            double[] parsed = RangeParser.parse("[" + input[0] + ":" + input[1] + "]");
            assertArrayEquals(parsed, result);
        }
    }

    public static void assertArrayEquals(double[] a, double[] b) {
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - b[i]) > EPSILON) {
                throw new AssertionError("expected " + Arrays.toString(b) + " was " + Arrays.toString(a));
            }
        }
    }
}
