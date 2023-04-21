package org.jreliability.function.common;

import org.jreliability.function.ReliabilityFunction;

/**
 * The {@code LognormalReliabilityFunction} represents the {@code
 * ReliabilityFunction} of the {@code Lognormal reliabilityFunction}
 * <p>
 * {@code R(x) = 1 - F(x) = (1 / (rho * sqrt(2 * pi))) * e^(-0.5 * ((ln(T) - mu)
 * / rho)^2)}<br />
 * with {@code mu, rho > 0}.
 * <p>
 * The {@code rho} and {@code mu} parameter represent the standard deviation and
 * mean of the variable's natural logarithm.
 * 
 * @author glass
 * 
 */
public class LognormalReliabilityFunction implements ReliabilityFunction {

    /**
	 * The used mean of the natural logarithms of the times-to-failure.
	 */
    protected final double mu;

    /**
	 * The used standard deviation of the natural logarithms of the
	 * times-to-failure.
	 */
    protected final double rho;

    /**
	 * Constructs a {@code LognormalReliabilityFunction} with a given {@code mu}
	 * and {@code rho}.
	 * 
	 * @param mu
	 *            the mean of the variable's natural logarithm
	 * @param rho
	 *            the standard deviation of the variable's natural logarithm
	 */
    public LognormalReliabilityFunction(double mu, double rho) {
        this.mu = mu;
        this.rho = rho;
        if (!(mu > 0) || !(rho > 0)) {
            throw new IllegalArgumentException("LognormalReliabilityFunction: Mu and Rho should be greater 0.");
        }
    }

    public double getY(double x) {
        double preTerm = 1 / (rho * Math.sqrt(2 * Math.PI));
        double exponentTerm = -0.5 * Math.pow(((Math.log(x) - mu) / rho), 2);
        double y = preTerm * Math.exp(exponentTerm);
        return y;
    }
}
