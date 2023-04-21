package peakml.math.function;

import jaolho.data.lma.*;

/**
 * Implementation of a normal function, which can be used to get a fit to normally
 * distributed data. The normal has three degrees of freedom controlling the fit to
 * the given data: scale, mean and variance.
 */
public class NormalFunction extends LMAFunction {

    /** The index of the scale-parameter in the array returned by {@link NormalFunction#getParameters()} */
    public static final int PARAM_SCALE = 0;

    /** The index of the mean-parameter in the array returned by {@link NormalFunction#getParameters()} */
    public static final int PARAM_MEAN = 2;

    /** The index of the variance-parameter in the array returned by {@link NormalFunction#getParameters()} */
    public static final int PARAM_VARIANCE = 1;

    protected NormalFunction() {
    }

    protected NormalFunction(LMA lma) {
        this.lma = lma;
    }

    /**
	 * Constructs a new normal function with the given parameters. The parameters
	 * should be passed as an array of 3 elements, where: element 0 is the scale
	 * parameter, element 1 is the variance parameter and the element 2 is the
	 * mean parameter.
	 * 
	 * @param parameters		The array with all the parameters.
	 */
    public NormalFunction(double parameters[]) {
        this.parameters = new double[parameters.length];
        System.arraycopy(parameters, 0, this.parameters, 0, parameters.length);
    }

    /**
	 * Constructs a new normal function with the given parameters.
	 * 
	 * @param scale				The scale-parameter.
	 * @param mean				The mean parameter.
	 * @param variance			The variance parameter.
	 */
    public NormalFunction(double scale, double mean, double variance) {
        parameters = new double[3];
        parameters[PARAM_SCALE] = scale;
        parameters[PARAM_MEAN] = mean;
        parameters[PARAM_VARIANCE] = variance;
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
    public double getY(double x) throws RuntimeException, NullPointerException {
        if (lma != null) return getY(x, lma.parameters); else if (parameters != null) return getY(x, parameters); else throw new NullPointerException("No parameters stored in function");
    }

    @Override
    public double getY(double x, double[] a) {
        return a[PARAM_SCALE] * Math.exp(-Math.pow(x - a[PARAM_MEAN], 2) / (2 * Math.pow(a[PARAM_VARIANCE], 2))) / (sqrt2pi * a[PARAM_VARIANCE]);
    }

    @Override
    public double getPartialDerivate(double x, double[] a, int parameterIndex) {
        double y = getY(x, a);
        switch(parameterIndex) {
            case 0:
                return y / a[PARAM_SCALE];
            case 1:
                return -y / Math.pow(a[PARAM_VARIANCE], 4) * (a[PARAM_MEAN] + a[PARAM_VARIANCE] - x) * (-a[PARAM_MEAN] + a[PARAM_VARIANCE] + x);
            case 2:
                return y / Math.pow(a[PARAM_VARIANCE], 3) * (-a[PARAM_MEAN] + x);
        }
        throw new RuntimeException("No such parameter index: " + parameterIndex);
    }

    /**
	 * Returns the LMA-fit used for constructing this function. If no LMA-fit  was used
	 * for constructing this function null is returned.
	 * 
	 * @return			The LMA object used to make the fit.
	 */
    public LMA getLMA() {
        return lma;
    }

    public double[] getParameters() throws NullPointerException {
        if (this.parameters == null) throw new NullPointerException("No parameters stored in function.");
        return this.parameters;
    }

    /**
	 * Implementation of the maximum likelihood estimation for normal distributions,
	 * which can be used as an initial best guess for the parameters for the LMA fit
	 * algorithm. It takes an array containing the x-values to be used for the fit
	 * and calculates the mean and standard-deviation for these values. The resulting
	 * vector can be plugged into the LMA-algorithm.
	 * 
	 * @param xvals		The x-values to estimate the parameters to.
	 * @return			The estimated parameters.
	 */
    public static double[] MLE(double xvals[]) {
        double parameters[] = new double[3];
        parameters[PARAM_SCALE] = 1;
        parameters[PARAM_MEAN] = 0;
        for (int i = 0; i < xvals.length; ++i) parameters[PARAM_MEAN] += xvals[i];
        parameters[PARAM_MEAN] /= xvals.length;
        parameters[PARAM_VARIANCE] = 0;
        for (int i = 0; i < xvals.length; ++i) parameters[PARAM_VARIANCE] += Math.pow(xvals[i] - parameters[PARAM_MEAN], 2);
        parameters[PARAM_VARIANCE] = Math.sqrt(parameters[PARAM_VARIANCE] / xvals.length);
        return parameters;
    }

    /**
	 * Fits (i.e. estimates) the best parameters for the normal curve to the given data.
	 * The parameters are estimated and returned in the new NormalFunction object.
	 * 
	 * @param xvals			The x-values to fit the line to.
	 * @param yvals			The y-values to fit the line to.
	 * @return				A LinearFunction instance fitted to the data.
	 * @throws RuntimeException
	 * 						Thrown when an error is encountered in the fitting procedure.
	 */
    public static NormalFunction fit(double xvals[], double yvals[]) throws RuntimeException {
        LMA lma = new LMA(new NormalFunction(), NormalFunction.MLE(xvals), new double[][] { xvals, yvals });
        lma.fit();
        return new NormalFunction(lma);
    }

    protected LMA lma = null;

    protected double parameters[] = null;

    private static double sqrt2pi = Math.sqrt(2. * Math.PI);
}
