package mipt.math.fuzzy;

import mipt.math.Number;

public class GaussianNumber extends FuzzyNumber {

    protected double a;

    protected double sigma;

    /**
	 *          (x-a)^2
	 *        - -------
	 *          sigma^2
	 * G =  e
	 *
	 * @param value
	 * @param sigma
	 */
    public GaussianNumber(double value, double sigma) {
        this.a = value;
        this.sigma = sigma;
    }

    /**
	 * @param y
	 * @return
	 */
    public FuzzyNumber addFuzzy(double b) {
        a += b;
        return this;
    }

    /**
	 * @param y
	 * @return
	 */
    public final GaussianNumber add(GaussianNumber y) {
        return add(y.a, y.sigma);
    }

    /**
	 * @param a
	 * @return
	 */
    public final FuzzyNumber addFuzzy(FuzzyNumber b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                return add(b.doubleValue(), b.doubleError());
            case EXACT_NUMBER:
                return addFuzzy(b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    /**
	 * @param a
	 * @return
	 */
    public final Number add(Number b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                return add(b.doubleValue(), ((FuzzyNumber) b).doubleError());
            case EXACT_NUMBER:
                return add(b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    protected GaussianNumber add(double b, double bSigma) {
        a += b;
        sigma = Math.sqrt(sigma * sigma + bSigma * bSigma);
        return this;
    }

    /**
	 * ??
	 * @return int
	 * @param number mipt.math.Number
	 */
    public int compareTo(Number number) {
        return compare(a, number.doubleValue());
    }

    /**
	 * 
	 * @return mipt.math.fuzzy.FuzzyNumber
	 */
    public FuzzyNumber copyFuzzy() {
        return new GaussianNumber(a, sigma);
    }

    public FuzzyNumber createFuzzy(double value, double error) {
        return new GaussianNumber(value, error);
    }

    /**
	 *
	 * @param n
	 * @return
	 */
    public static Interval[] discretizate(int n, double value, double variance) {
        double intstep = 1.0 / n;
        Interval intervals[] = new Interval[n];
        double tmp = 0.0, prvs = 0.0;
        if (n != 1) {
            for (int i = 0; i < n - 1; i++) {
                double ln = Math.log(1.0 - intstep * (i + 1));
                prvs = tmp;
                tmp = Math.sqrt(ln * (-2.0 * variance));
                intervals[i] = new Interval(value - tmp, value + tmp);
            }
            tmp = 2 * tmp - prvs;
        } else {
            double ln = Math.log(0.5);
            tmp = 1.5 * Math.sqrt(ln * (-2.0 * variance));
        }
        intervals[n - 1] = new Interval(value - tmp, value + tmp);
        return intervals;
    }

    public Interval[] discretizate(int n) {
        return discretizate(n, a, sigma * sigma);
    }

    public final Number divide(Number b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                double c = b.doubleValue();
                return mult(1.0 / c, ((FuzzyNumber) b).doubleError() / (c * c));
            case EXACT_NUMBER:
                return mult(1. / b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    public final FuzzyNumber divideFuzzy(FuzzyNumber b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                double c = b.doubleValue();
                return mult(1.0 / c, b.doubleError() / (c * c));
            case EXACT_NUMBER:
                return multFuzzy(1. / b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    public final double doubleError() {
        return sigma;
    }

    public final double doubleValue() {
        return a;
    }

    /**
	 *
	 * @return
	 */
    public final double getSigma() {
        return sigma;
    }

    public int getType() {
        return GAUSSIAN_NUMBER;
    }

    public Number inverse() {
        return new GaussianNumber(1.0 / a, sigma / (a * a));
    }

    public Number minus() {
        return new GaussianNumber(-a, sigma);
    }

    public final Number minus(Number b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                return add(-b.doubleValue(), ((FuzzyNumber) b).doubleValue());
            case EXACT_NUMBER:
                return add(b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    public final FuzzyNumber minusFuzzy(FuzzyNumber b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                return add(-b.doubleValue(), b.doubleValue());
            case EXACT_NUMBER:
                return addFuzzy(b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    /**
	*
	*/
    public FuzzyNumber multFuzzy(double b) {
        a *= b;
        sigma *= Math.abs(b);
        return this;
    }

    /**
	 * @param y
	 * @return
	 */
    public final GaussianNumber mult(GaussianNumber y) {
        return mult(y.a, y.sigma);
    }

    protected GaussianNumber mult(double b, double bSigma) {
        double relError = a;
        a *= b;
        if (a == 0.) sigma = 0.; else {
            relError = sigma / relError;
            double relErrorB = bSigma / b;
            sigma = Math.abs(a) * Math.sqrt(relError * relError + relErrorB * relErrorB);
        }
        return this;
    }

    /**
	 * @param a
	 * @return
	 */
    public final FuzzyNumber multFuzzy(FuzzyNumber b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                return mult(b.doubleValue(), b.doubleError());
            case EXACT_NUMBER:
                return multFuzzy(b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    /**
	 * @param fn
	 * @return
	 */
    public final Number mult(Number b) {
        switch(b.getType()) {
            case GAUSSIAN_NUMBER:
                return mult(b.doubleValue(), ((FuzzyNumber) b).doubleError());
            case EXACT_NUMBER:
                return mult(b.doubleValue());
        }
        throwUnsupportedTypeException(b);
        return null;
    }

    /**
	 * 
	 * @return mipt.math.Number
	 */
    public Number setInverse() {
        a = 1.0 / a;
        sigma *= a;
        sigma *= a;
        return this;
    }

    /**
	 * 
	 * @return mipt.math.Number
	 */
    public Number setMinus() {
        a = -a;
        return this;
    }

    /**
	 * 
	 */
    public final double squareError() {
        return sigma * sigma;
    }

    /**
	 * 
	 */
    public final double squareValue() {
        return a * a;
    }

    protected void throwUnsupportedTypeException(Number b) {
        throw new IllegalArgumentException(b + " has type unsupported by GaussianNumber");
    }

    public String toString() {
        return toString(a) + "+/-" + toString(sigma);
    }
}
