package mipt.math.sys.num.over;

import mipt.math.Number;
import mipt.math.function.Function;
import mipt.math.function.impl.spec.HypergeometricSeriesCalculator;

/**
 * Class for calculating values of Jacobi polynomials <code>Pn(alpha, beta)</code>.
 * It uses the hypergeometric series for this. The Jacobi polynomials in the terms
 * of DependenceFunction's can be acquired only by JacobiPolynomialDependentBasis now.
 * @author Loginov Pavel
 */
public class JacobiPolynomial implements Function {

    protected HypergeometricSeriesCalculator pFq = new HypergeometricSeriesCalculator();

    protected Number alpha = null;

    protected Number beta = null;

    protected int degree = -1;

    protected Number[] interval = null;

    /**
	 * 
	 * @see mipt.math.function.Function#calc(mipt.math.Number)
	 */
    public Number calc(Number x) {
        Number xTransformed = intervalTransform(x);
        Number result = null;
        pFq.setAlpha(new Number[] { Number.createScalar(-getDegree()), Number.createScalar(getDegree() + 1).add(getAlpha()).add(getBeta()) });
        pFq.setBeta(new Number[] { Number.createScalar(1).add(getAlpha()) });
        result = HypergeometricSeriesCalculator.calcPochhammer(Number.createScalar(1).add(getAlpha()), getDegree()).divide(HypergeometricSeriesCalculator.calcPochhammer(Number.createScalar(1), getDegree())).mult(pFq.getFiniteSeriesValue(getDegree(), Number.createScalar(1).minus(xTransformed).divide(Number.createScalar(2))));
        return result;
    }

    /**
	 * Returns the alpha of Jacobi polynomial <code>Pn(alpha, beta)</code>.
	 * @return alpha
	 */
    public final Number getAlpha() {
        if (alpha == null) throw new IllegalStateException("Alpha is not set for Jacobi polynomial.");
        return alpha;
    }

    /**
	 * Sets the alpha to be used in Jacobi polynomial <code>Pn(alpha, beta)</code>.
	 * @param alpha - alpha in Jacobi polynomial <code>Pn(alpha, beta)</code>.
	 */
    public final void setAlpha(Number alpha) {
        if (alpha.doubleValue() <= -1) throw new IllegalArgumentException("Alpha must be > -1 for Jacobi polynomial.");
        this.alpha = alpha;
    }

    /**
	 * Returns the beta of Jacobi polynomial <code>Pn(alpha, beta)</code>.
	 * @return beta
	 */
    public final Number getBeta() {
        if (beta == null) throw new IllegalStateException("Beta is not set for Jacobi polynomial.");
        return beta;
    }

    /**
	 * Sets the beta to be used in Jacobi polynomial <code>Pn(alpha, beta)</code>.
	 * @param beta - beta in Jacobi polynomial <code>Pn(alpha, beta)</code>.
	 */
    public final void setBeta(Number beta) {
        if (beta.doubleValue() <= -1) throw new IllegalArgumentException("Beta must be > -1 for Jacobi polynomial.");
        this.beta = beta;
    }

    /**
	 * Returns the degree of Jacobi polynomial
	 * @return degree - the degree of Jacobi polynomial
	 */
    public final int getDegree() {
        if (degree < 0) throw new IllegalStateException("The degree is not set for Jacobi polynomial.");
        return degree;
    }

    /**
	 * Sets the degree of Jacobi polynomial
	 * @param degree - the degree to set
	 */
    public final void setDegree(int degree) {
        if (degree < 0) throw new IllegalArgumentException("Degree cannot be zero or below.");
        this.degree = degree;
    }

    /**
	 * Returns the interval of interpolation
	 * @return interval as Number[2]
	 */
    public final Number[] getInterval() {
        if (interval == null) throw new IllegalStateException("Interval is not set for Jacobi polynomial.");
        return interval;
    }

    /**
	 * Sets the interval of interpolation
	 * @param a - left edge of the interval
	 * @param b - right edge of the interval
	 */
    public final void setInterval(Number a, Number b) {
        this.interval = new Number[2];
        this.interval[0] = a;
        this.interval[1] = b;
    }

    /**
	 * Replaces "x" with "2/(b-a)*x - (b+a)/(b-a)" to match the interval where this 
	 * polynomial is defined. Here it is [-1, 1]
	 * @param x - the Number which is contained in interval [a, b] to be transformed
	 * @return xTransformed - Number contained within [-1, 1]
	 */
    public final Number intervalTransform(Number x) {
        Number xTransformed = Number.createScalar(2).divide((getInterval()[1].copy().minus(getInterval()[0]))).mult(x).minus((getInterval()[1].copy().add(getInterval()[0])).divide((getInterval()[1].copy().minus(getInterval()[0]))));
        return xTransformed;
    }
}
