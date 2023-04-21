package org.eyrene.strmatch.core.alg;

import org.eyrene.strmatch.core.StrMatchAlgorithm;

/**
 * <p>Title: KarpRabin.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class KarpRabin extends StrMatchAlgorithm {

    protected short d;

    protected int q;

    protected int p;

    protected int t;

    private int h;

    /**
     * Costruttore di default
     */
    public KarpRabin() {
        super();
        this.d = 256;
        this.q = 512;
    }

    public String getName() {
        return "Karp-Rabin";
    }

    protected void preProcessing() {
        p = 0;
        t = 0;
        for (int i = 0; i <= m - 1; i++) {
            p = (p * d + P[i]) % q;
            t = (t * d + T[i]) % q;
        }
        h = 1;
        for (int i = 1; i <= m - 1; i++) {
            h = (h * d) % q;
        }
        log.info("Value of 'p': " + p);
        log.info("Initial value of 't' (0-" + (m - 1) + "): " + t);
        log.info("Value of 'h': " + h);
    }

    protected boolean compare(int i, int j) {
        if (p != t) {
            log.info("p != t ...");
            return false;
        }
        return P[j] == T[i + j];
    }

    protected boolean isLastCompare(int i, int j) {
        return j >= m;
    }

    protected boolean isLastStep(int i) {
        return i > n - m;
    }

    protected boolean isMatchFounded(int j) {
        return j == m;
    }

    protected int nextCompare(int j) {
        return j + 1;
    }

    protected int nextStep(int i) {
        if (!isLastStep(i + 1)) {
            log.info("Calculating new value of 't' as: t - '" + T[i] + "' + '" + T[i + m] + "'");
            t = ((t - T[i] * h) * d + T[i + m]) % q;
            if (t < 0) t += q;
            log.info("New value of 't' (" + (i + 1) + "-" + (i + m) + "): " + t);
        }
        return i + 1;
    }
}
