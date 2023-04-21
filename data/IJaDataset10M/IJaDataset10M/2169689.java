package es.optsicom.lib.util;

import java.util.Arrays;
import es.optsicom.lib.util.MathUtil;

public class MathUtilRandomTest {

    public static void main(String[] args) {
        double[] props = new double[] { 0.3, 0.3, 0.3, 0.1 };
        double[] generated = new double[props.length];
        int numGenerations = 10000000;
        for (int i = 0; i < numGenerations; i++) {
            int index = MathUtil.selectIndex(props);
            generated[index] += 1.0 / numGenerations;
        }
        System.out.println("Props: " + Arrays.toString(props));
        System.out.println("Generated: " + Arrays.toString(generated));
    }
}
