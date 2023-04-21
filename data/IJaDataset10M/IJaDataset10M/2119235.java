package org.phramer.mert.math;

public class NumberManager {

    public static boolean lowerEqualM(double v1, double v2) {
        return (v1 - PRECISION_M <= v2);
    }

    public static boolean lowerT(double v1, double v2) {
        return (v1 + PRECISION_T < v2);
    }

    public static boolean lowerM(double v1, double v2) {
        return (v1 + PRECISION_M < v2);
    }

    public static double adjustT(double t) {
        return t;
    }

    public static double adjustM(double m) {
        return m;
    }

    public static double adjustP(double p) {
        return p;
    }

    public static double adjustLambda(double lambda) {
        return lambda;
    }

    public static boolean betterProbability(double previousP, double newP) {
        if (newP - previousP >= PRECISION_P) return true;
        return false;
    }

    public static boolean equalSuccesiveLambdas(double small, double big) {
        if (big - small <= PRECISION_LAMBDA) return true;
        return false;
    }

    public static double PRECISION_LAMBDA = 0.00001;

    public static double PRECISION_P = 0.0000001;

    public static double PRECISION_M = 0.00001;

    public static double PRECISION_T = 0.00001;
}
