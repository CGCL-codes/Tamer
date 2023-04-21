package peakml.math.function;

import jaolho.data.lma.*;
import peakml.math.*;

/**
 * Implementation of a linear function f(x)=ax+b. This class provides tight integration
 * with the LMA fit package for function fitting.
 */
public class LinearFunction extends LMAFunction implements Function {

    /** The a-parameter from the array returned by {@link LinearFunction#getParameters()}. */
    public static final int PARAM_A = 0;

    /** The b-parameter from the array returned by {@link LinearFunction#getParameters()}. */
    public static final int PARAM_B = 1;

    protected LinearFunction() {
    }

    protected LinearFunction(LMA lma) {
        this.lma = lma;
        this.parameters = new double[lma.parameters.length];
        System.arraycopy(lma.parameters, 0, this.parameters, 0, lma.parameters.length);
    }

    /**
	 * Constructs a new linear function with the a and b parameters stored in the array. The
	 * size of the array must be 2 and the first element needs to contain the a-parameter and
	 * the second element the b-parameter.
	 * 
	 * @param parameters	The array with the a and b parameters.
	 * @throws RuntimeException
	 * 						Thrown when the parameters array has a different size than 2 elements.
	 */
    public LinearFunction(double... parameters) throws RuntimeException {
        if (parameters.length != 2) throw new RuntimeException("Size of parameters array is different from 2 (a and b).");
        this.parameters = new double[parameters.length];
        System.arraycopy(parameters, 0, this.parameters, 0, parameters.length);
    }

    /**
	 * Constructs a new linear function with the given a and b parameters.
	 * 
	 * @param a				The a parameter.
	 * @param b				The b parameter.
	 */
    public LinearFunction(double a, double b) {
        this.parameters = new double[2];
        this.parameters[PARAM_A] = a;
        this.parameters[PARAM_B] = b;
    }

    /**
	 * Returns the y-value for the given x-value with the parameters stored in the
	 * instance of the class.
	 * 
	 * @param x				The x-value.
	 * @return				The y-value for the given x-value.
	 * @throws NullPointerException
	 * 						When no parameters are stored in the class.
	 */
    public double getY(double x) throws NullPointerException {
        if (parameters == null) throw new NullPointerException("No parameters stored in function");
        return getY(x, parameters);
    }

    /**
	 * Returns the y-value for the given x-value with the given parameters
	 * 
	 * @param x				The x-value.
	 * @param a				The parameters for the function.
	 * @return				The y-value for the given x-value.
	 * @throws RuntimeException
	 * 						Thrown when the parameters array a has a different size than 2 elements.
	 * @throws NullPointerException
	 * 						When the a is null.
	 */
    public double getY(double x, double[] a) throws RuntimeException, NullPointerException {
        return a[0] * x + a[1];
    }

    @Override
    public double getPartialDerivate(double x, double[] a, int parameterIndex) {
        switch(parameterIndex) {
            case PARAM_A:
                return x;
            case PARAM_B:
                return 1;
        }
        throw new RuntimeException("No such parameter index: " + parameterIndex);
    }

    /**
	 * When a fit has been performed by calling {@link LinearFunction#fit(double[], double[])}
	 * the returned object contains data on the fit. This is provided for integration purposes
	 * with the LMA fit package. It's encouraged to use the access methods of this class to
	 * get to the information in this object.
	 * 
	 * @return				The result of the fitting procedure.
	 */
    public LMA getLMA() {
        return lma;
    }

    /**
	 * Returns the array with the a (first element) and b (second element) parameters stored
	 * in the instance of this class.
	 * 
	 * @return				The parameters of this function.
	 */
    public double[] getParameters() {
        return parameters;
    }

    /**
	 * When a fit has been performed by calling {@link LinearFunction#fit(double[], double[])}
	 * the returned array contains the x-values used for the fit.
	 * 
	 * @return				The x-values for this fit.
	 */
    public double[] getXVals() {
        if (lma == null) return null;
        double data[] = new double[lma.xDataPoints.length];
        for (int i = 0; i < lma.xDataPoints.length; ++i) data[i] = lma.xDataPoints[i][0];
        return data;
    }

    /**
	 * When a fit has been performed by calling {@link LinearFunction#fit(double[], double[])}
	 * the returned array contains the y-values used for the fit.
	 * 
	 * @return				The y-values for this fit.
	 */
    public double[] getYVals() {
        if (lma == null) return null;
        return lma.yDataPoints;
    }

    public double rSquare() {
        double observation[] = lma.yDataPoints;
        double predictions[] = new double[observation.length];
        for (int i = 0; i < predictions.length; ++i) predictions[i] = getY(lma.xDataPoints[i][0]);
        return Math.pow(Statistical.pearsonsCorrelationS(observation, predictions), 2);
    }

    /**
	 * Performs an estimation of the best starting parameters for the fitting procedure. This
	 * function is used by {@link LinearFunction#fit(double[], double[])} and has been made
	 * public for integration purposes.
	 * 
	 * @return				The best parameters for starting the fit on the provided x-values.
	 */
    public static double[] MLE(double xvals[]) {
        return new double[] { 0.1, 10 };
    }

    /**
	 * Fits a linear function through the given data. This method makes use of the LMA fit
	 * package for making the fit. The parameters are estimated and returned in the new
	 * LinearFunction object.
	 * 
	 * @param xvals			The x-values to fit the line to.
	 * @param yvals			The y-values to fit the line to.
	 * @return				A LinearFunction instance fitted to the data.
	 * @throws RuntimeException
	 * 						Thrown when an error is encountered in the fitting procedure.
	 */
    public static LinearFunction fit(double xvals[], double yvals[]) throws RuntimeException {
        LMA lma = new LMA(new LinearFunction(), MLE(xvals), new double[][] { xvals, yvals });
        lma.fit();
        return new LinearFunction(lma);
    }

    protected LMA lma = null;

    protected double parameters[] = null;
}
