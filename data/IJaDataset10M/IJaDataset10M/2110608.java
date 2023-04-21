package cern.jet.random;

import cern.jet.random.engine.RandomEngine;

/**
 * Discrete Empirical distribution (pdf's can be specified).
 * <p>
 * The probability distribution function (pdf) must be provided by the user as an array of positive real numbers. 
 * The pdf does not need to be provided in the form of relative probabilities, absolute probabilities are also accepted.
 * <p>
 * <p>
 * Instance methods operate on a user supplied uniform random number generator; they are unsynchronized.
 * <dt>
 * Static methods operate on a default uniform random number generator; they are synchronized.
 * <p>
 * <b>Implementation:</b>
 * Walker's algorithm. 
 * Generating a random number takes <tt>O(1)</tt>, i.e. constant time, as opposed to commonly used algorithms with logarithmic time complexity.
 * Preprocessing time (on object construction) is <tt>O(k)</tt> where <tt>k</tt> is the number of elements of the provided empirical pdf.
 * Space complexity is <tt>O(k)</tt>.
 * <p>
 * This is a port of <A HREF="http://sourceware.cygnus.com/cgi-bin/cvsweb.cgi/gsl/randist/discrete.c?cvsroot=gsl">discrete.c</A> which was written by James Theiler and is distributed with <A HREF="http://sourceware.cygnus.com/gsl/">GSL 0.4.1</A>.
 * Theiler's implementation in turn is based upon
 * <p>
 * Alastair J. Walker, An efficient method for generating
 * discrete random variables with general distributions, ACM Trans
 * Math Soft 3, 253-256 (1977).
 * <p>
 * See also: D. E. Knuth, The Art of
 * Computer Programming, Volume 2 (Seminumerical algorithms), 3rd
 * edition, Addison-Wesley (1997), p120.
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 */
public class EmpiricalWalker extends AbstractDiscreteDistribution {

    protected int K;

    protected int[] A;

    protected double[] F;

    protected double[] cdf;

    /**
 * Constructs an Empirical distribution.
 * The probability distribution function (pdf) is an array of positive real numbers. 
 * It need not be provided in the form of relative probabilities, absolute probabilities are also accepted.
 * The <tt>pdf</tt> must satisfy both of the following conditions
 * <ul>
 * <li><tt>0.0 &lt;= pdf[i] : 0&lt;=i&lt;=pdf.length-1</tt>
 * <li><tt>0.0 &lt; Sum(pdf[i]) : 0&lt;=i&lt;=pdf.length-1</tt>
 * </ul>
 * @param pdf the probability distribution function.
 * @param interpolationType can be either <tt>Empirical.NO_INTERPOLATION</tt> or <tt>Empirical.LINEAR_INTERPOLATION</tt>.
 * @param randomGenerator a uniform random number generator.
 * @throws IllegalArgumentException if at least one of the three conditions above is violated.
 */
    public EmpiricalWalker(double[] pdf, int interpolationType, RandomEngine randomGenerator) {
        setRandomGenerator(randomGenerator);
        setState(pdf, interpolationType);
        setState2(pdf);
    }

    /**
 * Returns the cumulative distribution function.
 */
    public double cdf(int k) {
        if (k < 0) return 0.0;
        if (k >= cdf.length - 1) return 1.0;
        return cdf[k];
    }

    /**
 * Returns a deep copy of the receiver; the copy will produce identical sequences.
 * After this call has returned, the copy and the receiver have equal but separate state.
 *
 * @return a copy of the receiver.
 */
    public Object clone() {
        EmpiricalWalker copy = (EmpiricalWalker) super.clone();
        if (this.cdf != null) copy.cdf = (double[]) this.cdf.clone();
        if (this.A != null) copy.A = (int[]) this.A.clone();
        if (this.F != null) copy.F = (double[]) this.F.clone();
        return copy;
    }

    /**
 * Returns a random integer <tt>k</tt> with probability <tt>pdf(k)</tt>.
 */
    public int nextInt() {
        int c = 0;
        double u, f;
        u = this.randomGenerator.raw();
        u *= this.K;
        c = (int) u;
        u -= c;
        f = this.F[c];
        if (f == 1.0) return c;
        if (u < f) {
            return c;
        } else {
            return this.A[c];
        }
    }

    /**
 * Returns the probability distribution function.
 */
    public double pdf(int k) {
        if (k < 0 || k >= cdf.length - 1) return 0.0;
        return cdf[k - 1] - cdf[k];
    }

    /**
 * Sets the distribution parameters.
 * The <tt>pdf</tt> must satisfy all of the following conditions
 * <ul>
 * <li><tt>pdf != null && pdf.length &gt; 0</tt>
 * <li><tt>0.0 &lt;= pdf[i] : 0 &lt; =i &lt;= pdf.length-1</tt>
 * <li><tt>0.0 &lt; Sum(pdf[i]) : 0 &lt;=i &lt;= pdf.length-1</tt>
 * </ul>
 * @param pdf probability distribution function.
 * @throws IllegalArgumentException if at least one of the three conditions above is violated.
 */
    public void setState(double[] pdf, int interpolationType) {
        if (pdf == null || pdf.length == 0) {
            throw new IllegalArgumentException("Non-existing pdf");
        }
        int nBins = pdf.length;
        this.cdf = new double[nBins + 1];
        cdf[0] = 0;
        for (int i = 0; i < nBins; i++) {
            if (pdf[i] < 0.0) throw new IllegalArgumentException("Negative probability");
            cdf[i + 1] = cdf[i] + pdf[i];
        }
        if (cdf[nBins] <= 0.0) throw new IllegalArgumentException("At leat one probability must be > 0.0");
        for (int i = 0; i < nBins + 1; i++) {
            cdf[i] /= cdf[nBins];
        }
    }

    /**
 * Sets the distribution parameters.
 * The <tt>pdf</tt> must satisfy both of the following conditions
 * <ul>
 * <li><tt>0.0 &lt;= pdf[i] : 0 &lt; =i &lt;= pdf.length-1</tt>
 * <li><tt>0.0 &lt; Sum(pdf[i]) : 0 &lt;=i &lt;= pdf.length-1</tt>
 * </ul>
 * @param pdf probability distribution function.
 * @throws IllegalArgumentException if at least one of the three conditions above is violated.
 */
    public void setState2(double[] pdf) {
        int size = pdf.length;
        int k, s, b;
        int nBigs, nSmalls;
        Stack Bigs;
        Stack Smalls;
        double[] E;
        double pTotal = 0;
        double mean, d;
        for (k = 0; k < size; ++k) {
            pTotal += pdf[k];
        }
        this.K = size;
        this.F = new double[size];
        this.A = new int[size];
        E = new double[size];
        for (k = 0; k < size; ++k) {
            E[k] = pdf[k] / pTotal;
        }
        mean = 1.0 / size;
        nSmalls = 0;
        nBigs = 0;
        for (k = 0; k < size; ++k) {
            if (E[k] < mean) ++nSmalls; else ++nBigs;
        }
        Bigs = new Stack(nBigs);
        Smalls = new Stack(nSmalls);
        for (k = 0; k < size; ++k) {
            if (E[k] < mean) {
                Smalls.push(k);
            } else {
                Bigs.push(k);
            }
        }
        while (Smalls.size() > 0) {
            s = Smalls.pop();
            if (Bigs.size() == 0) {
                this.A[s] = s;
                this.F[s] = 1.0;
                break;
            }
            b = Bigs.pop();
            this.A[s] = b;
            this.F[s] = size * E[s];
            d = mean - E[s];
            E[s] += d;
            E[b] -= d;
            if (E[b] < mean) {
                Smalls.push(b);
            } else if (E[b] > mean) {
                Bigs.push(b);
            } else {
                this.A[b] = b;
                this.F[b] = 1.0;
            }
        }
        while (Bigs.size() > 0) {
            b = Bigs.pop();
            this.A[b] = b;
            this.F[b] = 1.0;
        }
    }

    /**
 * Returns a String representation of the receiver.
 */
    public String toString() {
        String interpolation = null;
        return this.getClass().getName() + "(" + ((cdf != null) ? cdf.length : 0) + ")";
    }
}
