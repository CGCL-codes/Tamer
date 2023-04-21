package xxl.core.math.statistics.nonparametric.kernels;

import xxl.core.math.Statistics;
import xxl.core.math.functions.Differentiable;
import xxl.core.math.functions.Integrable;
import xxl.core.math.functions.RealFunction;

/**
 * This class models the <tt>CosineArch kernel function</tt>. Thus, it extends
 * {@link xxl.core.math.statistics.nonparametric.kernels.KernelFunction KernelFunction}.
 * Since the primitive as well as the first derivative are known, this class also 
 * implements {@link xxl.core.math.functions.Integrable Integrable} and
 * {@link xxl.core.math.functions.Differentiable Differentiable}.
 * 
 * @see xxl.core.math.statistics.nonparametric.kernels.KernelFunction
 * @see xxl.core.math.statistics.nonparametric.kernels.Kernels
 * @see xxl.core.math.statistics.nonparametric.kernels.KernelFunctionND
 * @see xxl.core.math.statistics.nonparametric.kernels.KernelBandwidths
 */
public class CosineArchKernel extends KernelFunction implements Integrable, Differentiable {

    /**
	 * Constructs a new CosineArchKernel and initializes the parameters.
	 *
	 */
    public CosineArchKernel() {
        AVG = 0.0;
        VAR = 1.0 - (8.0 / (Math.PI * Math.PI));
        R = Math.PI * Math.PI / 16.0;
    }

    /**
	 * Evaluates the CosineArch kernel at x.
	 * 
	 * @param x point to evaluate
	 * @return value of the CosineArch kernel at x
	 */
    public double eval(double x) {
        return Statistics.cosineArch(x);
    }

    /** Returns the primitive of the <tt>CosineArch</tt> kernel function
	 * as {@link xxl.core.math.functions.RealFunction real-valued function}
	 *
	 * @return primitive of the <tt>CosineArch</tt> kernel function
	 */
    public RealFunction primitive() {
        return new RealFunction() {

            public double eval(double x) {
                return Statistics.cosineArchPrimitive(x);
            }
        };
    }

    /** Returns the first derivative of the <tt>CosineArch</tt> kernel function
	 * as {@link xxl.core.math.functions.RealFunction real-valued function}
	 *
	 * @return first derivative of the <tt>CosineArch</tt> kernel function
	 */
    public RealFunction derivative() {
        return new RealFunction() {

            public double eval(double x) {
                return Statistics.cosineArchDerivative(x);
            }
        };
    }
}
