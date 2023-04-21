package org.kf.math;

/**
 * A class for computing Hypergeometric Probability of contingency tables, such as genotypic tables
 * for now only for 2x3 tables
 *
 * There are 3 ways to compute :
 *   - using computeProb()
 *   - using computeProbForTableWithSameMarginsAsPreviousOne() on a table with same margins
 *     as the last one you gave to the computeProb method. N.B : you have to init the constants
 *     via the computeProb() method
 *
 *   - using fastComputeProbForTableWithSameMarginsAsPreviousOne() : fast version that tries not
 *     to use the expensive Math.exp() function. Like the previous one, don't forget to init
 *     it via the computeProb() method
 *
 */
public final class HypergeometricProbability {

    static final int DEFAULT_MAX = 1000;

    /** The log facts. */
    FastLogFactorials logFacts;

    double num;

    int nbmults = 0;

    public static final int NB_MULT_MAX = 100;

    int last_a, last_b;

    double last_p = 0;

    private double[] T;

    private int c1, c2, c3, n1, n2, n;

    private int k;

    /**
	 * Instantiates a new hypergeometric probability.
	 *
	 * @param logFacts the log facts
	 */
    public HypergeometricProbability(FastLogFactorials logFacts) {
        this.logFacts = logFacts;
    }

    /**
	 * Instantiates a new hypergeometric probability, allocating a
	 * {@link FastLogFactorials}
	 */
    public HypergeometricProbability() {
        this(new FastLogFactorials(DEFAULT_MAX));
    }

    void computeMarginsConstant(int... t) {
        int a = t[0], b = t[1], c = t[2], d = t[3], e = t[4], f = t[5];
        c1 = a + d;
        c2 = b + e;
        c3 = c + f;
        n1 = a + b + c;
        n2 = d + e + f;
        n = n1 + n2;
        logFacts.ensureCapacity(n);
        T = logFacts.getLogFactorialTable();
        num = T[c1] + T[c2] + T[c3] + T[n1] + T[n2] - T[n];
    }

    /**
	 * Compute the probability of the table, and perform initialization stuff for subsequent
	 * calls to the computeProbForTableWithSameMarginsAsPreviousOne and
	 *
	 * @param t the t
	 *
	 * @return the double
	 */
    public final double computeProb(int... t) {
        computeMarginsConstant(t);
        nbmults = 0;
        last_a = t[0];
        last_b = t[1];
        k = n2 - c1 - c2 + 1 + last_a;
        last_p = Math.exp(num - T[last_a] - T[last_b] - T[t[2]] - T[t[3]] - T[t[4]] - T[t[5]]);
        return last_p;
    }

    /**
	 * Compute the probability for a table with same margins as the last one given
	 * as parameter of the {@link #computeProb(int...)} method
	 *
	 * useful to avoid redundant computations
	 *
	 * @see #computeProb(int...)
	 *
	 * @param t the t
	 *
	 * @return the double
	 */
    public final double computeProbForTableWithSameMarginsAsPreviousOne(int... t) {
        return Math.exp(num - T[t[0]] - T[t[1]] - T[t[2]] - T[t[3]] - T[t[4]] - T[t[5]]);
    }

    /**
	 * fast version of computeProbForTableWithSameMarginsAsPreviousOne
	 * that tries not to use the expensive Math.exp() function.
	 *
	 * @see  #computeProbForTableWithSameMarginsAsPreviousOne(int...)
	 * @see #computeProb(int...)
	 *
	 * @param a the a
	 * @param b the b
	 *
	 * @return the double
	 */
    public final double fastComputeProbForTableWithSameMarginsAsPreviousOne(int a, int b) {
        if (a == last_a && (b == last_b + 1) && nbmults < NB_MULT_MAX) {
            last_p *= (n1 - last_a - last_b) * (c2 - last_b) / ((double) ((b) * (k + last_b)));
            nbmults++;
            last_b++;
        } else {
            nbmults = 0;
            last_a = a;
            last_b = b;
            k = n2 - c1 - c2 + 1 + last_a;
            last_p = Math.exp(num - T[a] - T[b] - T[n1 - a - b] - T[c1 - a] - T[c2 - b] - T[n2 - c1 - c2 + a + b]);
        }
        return last_p;
    }

    public final FastLogFactorials getLogFacts() {
        return logFacts;
    }
}
