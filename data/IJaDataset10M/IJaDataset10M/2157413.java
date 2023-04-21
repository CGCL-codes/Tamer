package org.jreliability.function.common;

import org.jreliability.function.ReliabilityFunction;

/**
 * The {@code NMWDReliabilityFunction} represents the {@code
 * ReliabilityFunction} based on the {@code Weibull reliabilityFunction} with a
 * third parameter:
 * <p>
 * {@code R(x) = 1 - F(x) = e^-(a * x^b * e^(lambda * x))}<br />
 * with {@code lambda, a, b > 0}.
 * <p>
 * While the parameter {@code lambda} scales the reliabilityFunction, the
 * {@code a} and {@code b} parameters determines the {@code shape} of the
 * reliabilityFunction. This function allows to model bathtub-shaped failure
 * rates that can directly be derived from given Weibullplots, cf. [1].
 * <p>
 * [1] Lai, C.D. and Xie, Min and Murthy, D. N. P.: A Modified Weibull
 * Distribution. In IEEE Transactions on Reliability, Vol. 52, No. 1, 2003.
 * 
 * @author glass
 * 
 */
public class NMWDReliabilityFunction implements ReliabilityFunction {

    /**
	 * The {@code lambda} parameter somehow resembles the failure-rate {@code
	 * lambda}.
	 */
    protected final double lambda;

    /**
	 * The used shape of the {@code NMWDReliabilityFunction}.
	 */
    protected final double a;

    /**
	 * The used shape of the {@code NMWDReliabilityFunction}.
	 */
    protected final double b;

    /**
	 * Constructs a {@code NMWDReliabilityFunction} with a given {@code lambda},
	 * {@code a}, and {@code b}.
	 * 
	 * @param lambda
	 *            the scale value
	 * @param a
	 *            the first shape value
	 * @param b
	 *            the second shape value
	 */
    public NMWDReliabilityFunction(double lambda, double a, double b) {
        this.lambda = lambda;
        this.a = a;
        this.b = b;
        if (!(lambda > 0) || !(a > 0) || !(b > 0)) {
            throw new IllegalArgumentException("NMWDReliabilityFunction: Lambda, A, and B should be greater 0.");
        }
    }

    public double getY(double x) {
        double exponent = -a * Math.pow(x, b) * Math.exp(lambda * x);
        double y = Math.exp(exponent);
        return y;
    }
}
