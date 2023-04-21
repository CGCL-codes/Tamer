package dr.math;

/**
 * interface for a function of several variables with a gradient
 *
 * @version $Id: MFWithGradient.java,v 1.3 2005/05/24 20:26:01 rambaut Exp $
 *
 * @author Korbinian Strimmer
 */
public interface MFWithGradient extends MultivariateFunction {

    /**
	 * compute both function value and gradient at a point
	 *
	 * @param argument  function argument (vector)
	 * @param gradient  gradient (on return)
	 *
	 * @return function value
	 */
    double evaluate(double[] argument, double[] gradient);

    /**
	 * compute gradient at a point
	 *
	 * @param argument  function argument (vector)
	 * @param gradient  gradient (on return)
	 */
    void computeGradient(double[] argument, double[] gradient);
}
