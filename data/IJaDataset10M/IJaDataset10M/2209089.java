package xal.tools;

import gov.sns.tools.statistics.*;

/**
 * Fit a set of x,y data pairs to a line where <code>y = slope * x + intercept</code>.
 */
public class LinearFit {

    protected final MutableUnivariateStatistics _xStats;

    protected final MutableUnivariateStatistics _yStats;

    protected final MutableUnivariateStatistics _xxStats;

    protected final MutableUnivariateStatistics _xyStats;

    protected final MutableUnivariateStatistics _yyStats;

    protected boolean _needsUpdate;

    protected double _slope;

    protected double _intercept;

    protected double _correlationCoefficient;

    /**
	 * Constructor
	 */
    public LinearFit() {
        _needsUpdate = false;
        _slope = Double.NaN;
        _intercept = Double.NaN;
        _correlationCoefficient = Double.NaN;
        _xStats = new MutableUnivariateStatistics();
        _yStats = new MutableUnivariateStatistics();
        _xxStats = new MutableUnivariateStatistics();
        _xyStats = new MutableUnivariateStatistics();
        _yyStats = new MutableUnivariateStatistics();
    }

    /**
	 * Add a new x,y pair.
	 */
    public synchronized void addSample(final double x, final double y) {
        _xStats.addSample(x);
        _yStats.addSample(y);
        _xxStats.addSample(x * x);
        _xyStats.addSample(x * y);
        _yyStats.addSample(y * y);
        _needsUpdate = true;
    }

    /**
	 * Get the slope performing a fit if needed.
	 * @return the fitted slope
	 */
    public synchronized double getSlope() {
        performFitIfNeeded();
        return _slope;
    }

    /**
	 * Get the intercept performing a fit if needed.
	 * @return the fitted intercept
	 */
    public synchronized double getIntercept() {
        performFitIfNeeded();
        return _intercept;
    }

    /**
	 * Get the correlation coefficient.
	 * @return the correlation coefficient.
	 */
    public synchronized double getCorrelationCoefficient() {
        performFitIfNeeded();
        return _correlationCoefficient;
    }

    /**
	 * Estimate the dependent variable (y) given the independent variable (x).
	 * @param x the independent variable
	 * @return the dependent variable
	 */
    public synchronized double estimateY(final double x) {
        performFitIfNeeded();
        return _slope * x + _intercept;
    }

    /**
	 * Get the mean square error of the y value with respect to the fitted line.  The square root of this number gives
	 * an indication of the uncertainty in y value estimates under certain assumptions about the error distribution.
	 * @return the mean square error of the y value with respect to the fitted line
	 */
    public synchronized double getMeanSquareOrdinateError() {
        performFitIfNeeded();
        final double slope = _slope;
        final double intercept = _intercept;
        final double xyMean = _xyStats.mean();
        final double xxMean = _xxStats.mean();
        final double yyMean = _yyStats.mean();
        return yyMean - 2 * slope * xyMean + slope * slope * xxMean - intercept * intercept;
    }

    /** Perform a linear fit if the the fit needs to be updated due to newly added data. */
    protected synchronized void performFitIfNeeded() {
        if (_needsUpdate) {
            performFit();
        }
    }

    /** Calculate the slope and intercept. */
    protected synchronized void performFit() {
        final double xMean = _xStats.mean();
        final double yMean = _yStats.mean();
        final double xyMean = _xyStats.mean();
        final double xxMean = _xxStats.mean();
        final double yyMean = _yyStats.mean();
        _slope = (xyMean - xMean * yMean) / (xxMean - xMean * xMean);
        _intercept = yMean - _slope * xMean;
        _correlationCoefficient = (xyMean - xMean * yMean) / Math.sqrt((xxMean - xMean * xMean) * (yyMean - yMean * yMean));
        _needsUpdate = false;
    }

    /**
	 * Generate a string representation of the linear equation.
	 * @return a string representation of the linear equation
	 */
    public synchronized String toString() {
        performFitIfNeeded();
        StringBuffer buffer = new StringBuffer();
        buffer.append("y = " + _slope + " * x + " + _intercept);
        buffer.append("\n" + "r = " + _correlationCoefficient);
        buffer.append("\n" + "<x> = " + _xStats.mean() + ", <y> = " + _yStats.mean());
        buffer.append("\n" + "<xx> = " + _xxStats.mean() + ", <xy> = " + _xyStats.mean() + ", <yy> = " + _yyStats.mean());
        return buffer.toString();
    }
}
