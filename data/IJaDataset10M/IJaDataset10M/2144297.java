package com.ibm.tuningfork.core.math;

public class Utility {

    public static long choose(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    public static long factorial(int n) {
        if (n < 0) {
            throw new RuntimeException("factorial: negative argument" + n);
        }
        if (factorialCache != null && n < factorialCache.length) {
            return factorialCache[n];
        }
        return factorialRaw(n);
    }

    private static long factorialCache[];

    static {
        factorialCache = new long[12];
        for (int i = 0; i < factorialCache.length; i++) factorialCache[i] = factorialRaw(i);
    }

    private static long factorialRaw(int n) {
        if (n <= 1) {
            return 1;
        }
        long subResult = factorial(n - 1);
        long result = n * subResult;
        if (result < (((double) n) * subResult) - 1) {
            throw new RuntimeException("factorial: overflow");
        }
        return result;
    }
}
