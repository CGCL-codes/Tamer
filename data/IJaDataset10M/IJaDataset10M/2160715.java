package net.java.dev.joode.util;

/**
 * This class helps to aleive the problems of lost accuracy in
 * floating point representations, when adding lots of floating number of
 * different scales together
 * http://en.wikipedia.org/wiki/Kahan_summation_algorithm
 */
public class KahanSum {

    private float sum = 0;

    private float c = 0;

    public KahanSum(float initialValue) {
        sum = initialValue;
    }

    public void add(float val) {
        float y = val - c;
        float t = sum + y;
        c = (t - sum) - y;
        sum = t;
    }

    public void reset() {
        sum = 0;
        c = 0;
    }

    public float val() {
        return sum;
    }
}
