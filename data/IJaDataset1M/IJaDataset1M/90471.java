package javalens.maths;

import javalens.astronomy.Event;
import javalens.util.CalculationException;
import javalens.util.ComplexNumber;
import javalens.util.LinearModelResult;

/**
 *
 * @author vermaak
 */
public interface Maths {

    LinearModelResult fitCubic(double[] x, double[] y);

    LinearModelResult fitFithOrderPoly(double[] x, double[] y);

    LinearModelResult fitFourthOrderPoly(double[] x, double[] y);

    LinearModelResult fitLine(double[] x, double[] y);

    LinearModelResult fitParabola(double[] x, double[] y);

    /**
     * Fits a single lens light curve to a data set given by x,y and err data.
     * If err is null, an error of 0.1 mag per point is assumed. We will fit all
     * parameters except f, which we will assume to be 1.0 for the purposes of
     * feature selection. Well, the allowed range for f is 0.75 to 1.0
     *
     * @param x     Time in days
     * @param y     Magnitude curve
     * @param err   Error in magnitude or NULL
     * @param chisq A double vector of length one to return the chi-squared of the fit
     * @return a single lens Event that fits.
     */
    Event fitSingleLensCurve(double[] x, double[] y, double[] err, double[] chisq) throws CalculationException;

    /**
     * Root of polynomial, complex or other
     *
     * @return The real parts of the roots as an array
     */
    ComplexNumber[] polyroot(double[] coefficients);
}
