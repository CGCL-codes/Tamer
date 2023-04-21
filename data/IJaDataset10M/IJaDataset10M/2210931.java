package com.lgc.wsh.inv;

import com.lgc.wsh.util.Almost;
import com.lgc.wsh.util.LogMonitor;
import com.lgc.wsh.util.Monitor;
import com.lgc.wsh.util.IndexSorter;
import java.util.logging.*;

/** Search a single variable for a value that minimizes a function */
public class ScalarSolver {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("com.lgc.wsh.inv");

    private static final double GOLD = 0.5 * (Math.sqrt(5.) - 1.);

    private Function _function = null;

    /** Implement a function of one variable to be minimized */
    public interface Function {

        /** Return a single value as a function of the argument
        @param scalar Argument to be optimized within a known range.
        @return value to be minimized
    */
        public double function(double scalar);
    }

    /** Constructor
      @param function Objective function to be minimized.
   */
    public ScalarSolver(Function function) {
        _function = function;
    }

    /** Minimize a function of scalar and return the optimum value.
      @param scalarMin The minimum value allowed for the argument scalar.
      @param scalarMax The maximum value allowed for the argument scalar.
      @param okError The unknown error in scalar should be less than this
      fraction of the range: dscalar/(scalarMax-scalarMin) <= okError,
      where dscalar is the error bound on the returned value of scalar.
      @param okFraction The error in scalar should be less than
      this fraction of the minimum possible range for scalar:
      dscalar <= okFraction*(scalar-scalarMin),
      where dscalar is the error bound on the returned value of scalar.
      @param numberIterations The maximum number of iterations if greater
      than 6.  The optimization performs at least 6 iterations --
      the minimum necessary for a genuinely parabolic function.
      I recommend at least 20.
      @param monitor For reporting progress.  Ignored if null.
      @return The optimum value minimizing the function.
   */
    public double solve(double scalarMin, double scalarMax, double okError, double okFraction, int numberIterations, Monitor monitor) {
        if (monitor == null) monitor = new LogMonitor(null, null);
        monitor.report(0.);
        int iter = 0, nter = numberIterations;
        if (nter < 6) nter = 6;
        double[] xs = { 0., 0.25, 0.75, 1. };
        double[] fs = new double[4];
        for (int i = 0; i < fs.length; ++i) {
            fs[i] = function(xs[i], scalarMin, scalarMax);
        }
        iter = 4;
        double xmin = Float.MAX_VALUE;
        double error = 1.;
        double previousError = 1.;
        double previousPreviousError = 1.;
        double fraction = 1.;
        while (true) {
            monitor.report(((double) iter) / nter);
            int imin = sort(xs, fs);
            xmin = xs[imin];
            previousPreviousError = previousError;
            previousError = error;
            if (imin == 0) {
                error = xs[1] - xs[0];
            } else if (imin == 3) {
                error = xs[3] - xs[2];
            } else if (imin == 1 || imin == 2) {
                error = xs[imin + 1] - xs[imin - 1];
            } else {
                assert (false) : ("impossible imin=" + imin);
            }
            fraction = Almost.FLOAT.divide(error, xmin, 0.);
            if (iter >= nter || (error < okError && fraction < okFraction)) {
                break;
            }
            double xnew = Float.MAX_VALUE;
            if (imin == 0) {
                assert (xs[imin] == 0.) : ("Left endpoint should be zero");
                xnew = xs[1] * 0.1;
            } else if (imin == 3) {
                assert (xs[imin] == 1.) : ("Right endpoint should be one");
                xnew = 1. - 0.1 * (1. - xs[2]);
            } else if (imin == 1 || imin == 2) {
                boolean goodConvergence = false;
                if (error < previousPreviousError * 0.501) {
                    try {
                        xnew = minParabola(xs[imin - 1], fs[imin - 1], xs[imin], fs[imin], xs[imin + 1], fs[imin + 1]);
                        goodConvergence = true;
                    } catch (BadParabolaException e) {
                        goodConvergence = false;
                    }
                }
                if (!goodConvergence) {
                    if (xs[imin] - xs[imin - 1] >= xs[imin + 1] - xs[imin]) {
                        xnew = xs[imin - 1] + GOLD * (xs[imin] - xs[imin - 1]);
                    } else {
                        xnew = xs[imin + 1] - GOLD * (xs[imin + 1] - xs[imin]);
                    }
                }
            } else {
                assert (false) : ("Impossible imin=" + imin);
            }
            assert (xnew != Float.MAX_VALUE) : ("bad xnew");
            double fnew = Float.MAX_VALUE;
            for (int i = 0; i < xs.length; ++i) {
                if (Almost.FLOAT.equal(xnew, xs[i])) {
                    fnew = fs[i];
                }
            }
            if (fnew == Float.MAX_VALUE) {
                fnew = function(xnew, scalarMin, scalarMax);
            }
            if (imin < 2) {
                xs[3] = xnew;
                fs[3] = fnew;
            } else {
                xs[0] = xnew;
                fs[0] = fnew;
            }
            ++iter;
        }
        assert (xmin >= 0. && xmin <= 1.) : ("Impossible xmin=" + xmin);
        double result = scalarMin + xmin * (scalarMax - scalarMin);
        monitor.report(1.);
        return result;
    }

    private double[] _doubleTemp = new double[4];

    /** Reorganize samples
      @param xs Sorted by increasing values
      @param fs Sorted the same way as xs
      @return sample for which fs is minimized */
    private int sort(double[] xs, double[] fs) {
        assert xs.length == 4;
        int[] sortedX = (new IndexSorter(xs)).getSortedIndices();
        System.arraycopy(xs, 0, _doubleTemp, 0, 4);
        for (int i = 0; i < xs.length; ++i) {
            xs[i] = _doubleTemp[sortedX[i]];
        }
        System.arraycopy(fs, 0, _doubleTemp, 0, 4);
        for (int i = 0; i < xs.length; ++i) {
            fs[i] = _doubleTemp[sortedX[i]];
        }
        int imin = 0;
        for (int i = 1; i < fs.length; ++i) {
            if (fs[i] < fs[imin]) imin = i;
        }
        return imin;
    }

    /** @param x A fraction of the distance between scalarMin and scalarMax
 * @param scalarMin Minimum scalar
 * @param scalarMax Maximum scalar
 * @return optimum fraction of distance between minimum and maximum scalars
   */
    private double function(double x, double scalarMin, double scalarMax) {
        return function(scalarMin + x * (scalarMax - scalarMin));
    }

    /** Evaluate function
   * @param scalar Argument for embedded function
    * @return Value of function
   */
    private double function(double scalar) {
        return _function.function(scalar);
    }

    /**  Fit a parabola to three points and find the minimum point.
       where f(x1) = f1; f(x2) = f2; f(x3) = f3;
       and where x1 < x2 < x3; f(x2) < f(x1); f(x2) < f(x3).
       @param x1 A value of the argument to the parabolic function
       @param f1 The value of the function f(x1) at x1
       @param x2 A value of the argument to the parabolic function
       @param f2 The value of the function f(x2) at x2
       @param x3 A value of the argument to the parabolic function
       @param f3 The value of the function f(x3) at x3
       @return Value of x that minimizes function f(x)
       @exception BadParabolaException If the arguments
       describe a parabola that cannot be minimized in the range x1 < x <x3,
       or if the following strict inequalities
       are not true: x1 < x2 < x3; f(x2) < f(x1), f(x2) < f(x3),
       or if the x2 is too close to one of the endpoints
       to describe a parabola accurately.
 * @throws IllegalArgumentException If input values lead to degenerate
 * solutions.
   */
    private double minParabola(double x1, double f1, double x2, double f2, double x3, double f3) throws BadParabolaException, IllegalArgumentException {
        if (!Almost.FLOAT.le(x1, x2) || !Almost.FLOAT.le(x2, x3)) {
            throw new BadParabolaException("Violates x1 <= x2 <= x3: x1=" + x1 + " x2=" + x2 + " x3=" + x3);
        }
        if (Almost.FLOAT.equal(x1, x2)) {
            double result = x2 + 0.1 * (x3 - x2);
            return result;
        }
        if (Almost.FLOAT.equal(x2, x3)) {
            double result = x1 + 0.9 * (x2 - x1);
            return result;
        }
        if (!Almost.FLOAT.le(f2, f1) || !Almost.FLOAT.le(f2, f3)) {
            throw new BadParabolaException("Violates f(x2) <= f(x1), f(x2) <= f(x3)" + ": f1=" + f1 + " f2=" + f2 + " f3=" + f3);
        }
        double xm = Almost.FLOAT.divide((x2 - x1), (x3 - x1), 0.);
        if (xm < 0.001 || xm > 0.999) {
            throw new BadParabolaException("Parabola is badly sampled x1=" + x1 + " x2=" + x2 + " x3=" + x3);
        }
        double a = Almost.FLOAT.divide(((f3 - f1) * xm - (f2 - f1)), (xm - xm * xm), 0.);
        double b = f3 - f1 - a;
        if (Almost.FLOAT.ge(a * b, 0.) || 0.5 * Math.abs(b) > Math.abs(a)) {
            throw new BadParabolaException("Poor numerical conditioning a=" + a + " b=" + b);
        }
        xm = Almost.FLOAT.divide(-0.5 * b, a, -1.);
        if (xm < 0. || xm > 1.) {
            throw new BadParabolaException("Poor numerical conditioning a=" + a + " b=" + b + " xm=" + xm);
        }
        double result = xm * (x3 - x1) + x1;
        return result;
    }

    private static class BadParabolaException extends Exception {

        private static final long serialVersionUID = 1L;

        /** Available points do not describe a valid parabola
       @param message Error message
     */
        public BadParabolaException(String message) {
            super(message);
        }
    }

    /** Test code
 * @param args command line
 * @throws Exception all errors*/
    public static void main(String[] args) throws Exception {
        {
            final double answer = 1. / 3.;
            final int[] calls = new int[] { 0 };
            ScalarSolver solver = new ScalarSolver(new Function() {

                public double function(double scalar) {
                    ++calls[0];
                    return Math.abs(scalar - answer);
                }
            });
            double xmin = solver.solve(0., 1., 0.001, 0.001, 20, null);
            assert (xmin > answer - 0.001) : ("xmin > answer - 0.001");
            assert (xmin > answer * (1. - 0.001)) : ("xmin > answer*(1. - 0.001)");
            assert (xmin < answer + 0.001) : ("xmin < answer - 0.001");
            assert (xmin < answer * (1. + 0.001)) : ("xmin < answer*(1. + 0.001)");
            assert (calls[0] == 14) : ("calls[0] == 14 != " + calls[0]);
        }
        {
            final double answer = 1. / 3.;
            final int[] calls = new int[] { 0 };
            ScalarSolver solver = new ScalarSolver(new Function() {

                public double function(double scalar) {
                    ++calls[0];
                    return Math.abs(scalar - answer);
                }
            });
            double xmin = solver.solve(-1., 2., 0.001, 0.001, 20, null);
            assert (xmin > answer - 0.001) : ("xmin > answer - 0.001");
            assert (xmin > answer * (1. - 0.001)) : ("xmin > answer*(1. - 0.001)");
            assert (xmin < answer + 0.001) : ("xmin < answer - 0.001");
            assert (xmin < answer * (1. + 0.001)) : ("xmin < answer*(1. + 0.001)");
            assert (calls[0] == 15) : ("calls[0] == 15 != " + calls[0]);
        }
        {
            final double answer = 0.03;
            final int[] calls = new int[] { 0 };
            ScalarSolver solver = new ScalarSolver(new Function() {

                public double function(double scalar) {
                    ++calls[0];
                    return Math.abs(scalar - answer);
                }
            });
            double xmin = solver.solve(0., 1., 0.001, 0.001, 20, null);
            assert (xmin > answer - 0.001) : ("xmin > answer - 0.001");
            assert (xmin > answer * (1. - 0.001)) : ("xmin > answer*(1. - 0.001)");
            assert (xmin < answer + 0.001) : ("xmin < answer - 0.001");
            assert (xmin < answer * (1. + 0.001)) : ("xmin < answer*(1. + 0.001)");
            assert (calls[0] == 16) : ("calls[0] == 16 != " + calls[0]);
        }
        {
            final double answer = 0.98;
            final int[] calls = new int[] { 0 };
            ScalarSolver solver = new ScalarSolver(new Function() {

                public double function(double scalar) {
                    ++calls[0];
                    return Math.abs(scalar - answer);
                }
            });
            double xmin = solver.solve(0., 1., 0.001, 0.001, 20, null);
            assert (xmin > answer - 0.001) : ("xmin > answer - 0.001");
            assert (xmin > answer * (1. - 0.001)) : ("xmin > answer*(1. - 0.001)");
            assert (xmin < answer + 0.001) : ("xmin < answer - 0.001");
            assert (xmin < answer * (1. + 0.001)) : ("xmin < answer*(1. + 0.001)");
            assert (calls[0] == 12) : ("calls[0] == 12 != " + calls[0]);
        }
        {
            final double answer = 1. / 3.;
            final int[] calls = new int[] { 0 };
            ScalarSolver solver = new ScalarSolver(new Function() {

                public double function(double scalar) {
                    ++calls[0];
                    return (scalar - answer) * (scalar - answer);
                }
            });
            double xmin = solver.solve(0., 1., 0.001, 0.001, 7, null);
            assert (xmin > answer - 0.001) : ("xmin > answer - 0.001");
            assert (xmin > answer * (1. - 0.001)) : ("xmin > answer*(1. - 0.001)");
            assert (xmin < answer + 0.001) : ("xmin < answer - 0.001");
            assert (xmin < answer * (1. + 0.001)) : ("xmin < answer*(1. + 0.001)");
            assert (calls[0] == 6) : ("Number == 6 != " + calls[0]);
        }
        {
            final double answer = 1. / 3.;
            final int[] calls = new int[] { 0 };
            ScalarSolver solver = new ScalarSolver(new Function() {

                public double function(double scalar) {
                    ++calls[0];
                    return Math.sqrt(Math.abs(scalar - answer));
                }
            });
            double xmin = solver.solve(0., 1., 0.001, 0.001, 20, null);
            assert (xmin > answer - 0.001) : ("xmin > answer - 0.001");
            assert (xmin > answer * (1. - 0.001)) : ("xmin > answer*(1. - 0.001)");
            assert (xmin < answer + 0.001) : ("xmin < answer - 0.001");
            assert (xmin < answer * (1. + 0.001)) : ("xmin < answer*(1. + 0.001)");
            assert (calls[0] == 16) : ("Number == 16 != " + calls[0]);
        }
        {
            final double answer = 1. / 3.;
            final int[] calls = new int[] { 0 };
            ScalarSolver solver = new ScalarSolver(new Function() {

                public double function(double scalar) {
                    ++calls[0];
                    if (scalar < answer) return 3.;
                    return scalar - answer;
                }
            });
            double xmin = solver.solve(0., 1., 0.001, 0.001, 50, null);
            assert (xmin > answer - 0.001) : ("xmin > answer - 0.001");
            assert (xmin > answer * (1. - 0.001)) : ("xmin > answer*(1. - 0.001)");
            assert (xmin < answer + 0.001) : ("xmin < answer - 0.001");
            assert (xmin < answer * (1. + 0.001)) : ("xmin < answer*(1. + 0.001)");
            assert (calls[0] == 29) : ("Number == 29 != " + calls[0]);
        }
        assert null != new BadParabolaException("test constructor");
    }
}
