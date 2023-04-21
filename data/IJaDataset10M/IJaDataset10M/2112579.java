package edu.washington.mysms.server.sample.starbus;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        int[] values = { 5, 5, 5, 10, 0, 0 };
        int sum = 15;
        System.out.println(Arrays.toString(values));
        System.out.println(numCombinations(values, sum, 0) + " combination(s) that sum to " + sum);
    }

    public static int numCombinations(int[] values, int sum, int acc) {
        int comb = 0;
        if (values.length == 0) {
            return 0;
        }
        if (values[0] + acc == sum) {
            comb++;
        }
        comb += numCombinations(subarray(values, 1, values.length - 1), sum, acc);
        comb += numCombinations(subarray(values, 1, values.length - 1), sum, acc + values[0]);
        return comb;
    }

    private static int[] subarray(int[] original, int start, int end) {
        int[] copy = new int[end - start + 1];
        int j = 0;
        for (int i = start; i <= end; i++) {
            copy[j] = original[i];
            j++;
        }
        return copy;
    }
}
