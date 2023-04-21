package org.jquantlib.testsuite.math.interpolations;

import static java.lang.Math.abs;
import static org.junit.Assert.assertFalse;
import org.jquantlib.math.integrals.SimpsonIntegral;
import org.jquantlib.math.interpolations.CubicSplineInterpolation;
import org.jquantlib.math.interpolations.factories.MonotonicCubicSpline;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Kong
 **/
public class MonotonicCubicSplineInterpolationTest extends InterpolationTestBase {

    private static final Logger logger = LoggerFactory.getLogger(MonotonicCubicSplineInterpolationTest.class);

    public MonotonicCubicSplineInterpolationTest() {
        logger.info("\n\n::::: " + this.getClass().getSimpleName() + " :::::");
    }

    @Ignore
    @Test
    public void testMCSplineErrorOnGaussianValues() {
        logger.info("Testing spline approximation on Gaussian data sets...");
        int[] points = { 5, 9, 17, 33 };
        double[] tabulatedMCErrors = { 1.7e-2, 2.0e-3, 4.0e-5, 1.8e-6 };
        double[] toleranceOnTabMCErr = { 0.1e-2, 0.1e-3, 0.1e-5, 0.1e-6 };
        SimpsonIntegral integral = new SimpsonIntegral(1e-12, 0);
        double scaleFactor = 1.9;
        for (int i = 0; i < points.length; i++) {
            int n = points[i];
            double[] x = xRange(-1.7, 1.9, n);
            double[] y = gaussian(x);
            CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0, CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0).interpolate(x, y);
        }
    }

    @Test
    public void testMCNotAKnotSplineOnGaussianValues() {
        logger.info("Testing spline interpolation on a Gaussian data set...");
        double interpolated, interpolated2;
        int n = 5;
        double[] x = new double[n];
        double[] y = new double[n];
        double x1_bad = -1.7, x2_bad = 1.7;
        for (double start = -1.9, j = 0; j < 2; start += 0.2, j++) {
            x = xRange(start, start + 3.6, n);
            y = gaussian(x);
            CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0, CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0).interpolate(x, y);
            checkValues("MC not-a-knot spline", interpolation, x, y);
            interpolated = interpolation.evaluate(x1_bad);
            assertFalse("MC not-a-knot spline interpolation good performance unverified" + "\n    at x = " + x1_bad + "\n    interpolated value: " + interpolated + "\n    expected value > 0.0", interpolated < 0.0);
            interpolated = interpolation.evaluate(x2_bad);
            assertFalse("MC not-a-knot spline interpolation good performance unverified" + "\n    at x = " + x2_bad + "\n    interpolated value: " + interpolated + "\n    expected value > 0.0", interpolated < 0.0);
        }
    }

    @Test
    public void testMCClampedSplineOnRPN15AValues() {
        logger.info("Testing spline interpolation on RPN15A data set...");
        final double RPN15A_x[] = { 7.99, 8.09, 8.19, 8.7, 9.2, 10.0, 12.0, 15.0, 20.0 };
        final double RPN15A_y[] = { 0.0, 2.76429e-5, 4.37498e-5, 0.169183, 0.469428, 0.943740, 0.998636, 0.999919, 0.999994 };
        double interpolated;
        double x_bad = 11.0;
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.FirstDerivative, 0.0, CubicSplineInterpolation.BoundaryCondition.FirstDerivative, 0.0).interpolate(RPN15A_x, RPN15A_y);
        checkValues("MC clamped spline", interpolation, RPN15A_x, RPN15A_y);
        check1stDerivativeValue("MC clamped spline", interpolation, RPN15A_x[0], 0.0);
        check1stDerivativeValue("MC clamped spline", interpolation, RPN15A_x[RPN15A_x.length - 1], 0.0);
        interpolated = interpolation.evaluate(x_bad);
        assertFalse("MC clamped spline interpolation good performance unverified" + "\n    at x = " + x_bad + "\n    interpolated value: " + interpolated + "\n    expected value < 1.0", interpolated > 1.0);
    }

    @Test
    public void testMCNotAKnotSplineOnRPN15AValues() {
        logger.info("Testing spline interpolation on RPN15A data set...");
        final double RPN15A_x[] = { 7.99, 8.09, 8.19, 8.7, 9.2, 10.0, 12.0, 15.0, 20.0 };
        final double RPN15A_y[] = { 0.0, 2.76429e-5, 4.37498e-5, 0.169183, 0.469428, 0.943740, 0.998636, 0.999919, 0.999994 };
        double interpolated;
        double x_bad = 11.0;
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0, CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0).interpolate(RPN15A_x, RPN15A_y);
        checkValues("MC not-a-knot spline", interpolation, RPN15A_x, RPN15A_y);
        interpolated = interpolation.evaluate(x_bad);
        assertFalse("MC not-a-knot spline interpolation good performance unverified" + "\n    at x = " + x_bad + "\n    interpolated value: " + interpolated + "\n    expected value < 1.0", interpolated > 1.0);
    }

    @Test
    public void testMCNotAKnotSplineOnSimmetricEndConditions() {
        int n = 9;
        double[] x, y;
        x = xRange(-1.8, 1.8, n);
        y = gaussian(x);
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0, CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0).interpolate(x, y);
        checkValues("Not-a-knot spline", interpolation, x, y);
        checkSymmetry("Not-a-knot spline", interpolation, x[0]);
    }

    @Test
    public void testMCNotAKnotSplineOnDerivativeEndConditions() {
        int n = 4;
        double[] x, y;
        x = xRange(-2.0, 2.0, n);
        y = parabolic(x);
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0, CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0).interpolate(x, y);
        checkValues("MC Not-a-knot spline", interpolation, x, y);
        check1stDerivativeValue("MC Not-a-knot spline", interpolation, x[0], 4.0);
        check1stDerivativeValue("MC Not-a-knot spline", interpolation, x[n - 1], -4.0);
        check2ndDerivativeValue("MC Not-a-knot spline", interpolation, x[0], -2.0);
    }

    @Test
    public void testMCClampedSplineOnDerivativeEndConditions() {
        int n = 4;
        double[] x, y;
        x = xRange(-2.0, 2.0, n);
        y = parabolic(x);
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.FirstDerivative, 4.0, CubicSplineInterpolation.BoundaryCondition.FirstDerivative, -4.0).interpolate(x, y);
        checkValues("MC Clamped spline", interpolation, x, y);
        check1stDerivativeValue("MC Clamped spline", interpolation, x[0], 4.0);
        check1stDerivativeValue("MC Clamped spline", interpolation, x[n - 1], -4.0);
        check2ndDerivativeValue("MC Clamped spline", interpolation, x[0], -2.0);
        check2ndDerivativeValue("MC Clamped spline", interpolation, x[n - 1], -2.0);
    }

    @Test
    public void testMCSecondDerivativeSplineOnDerivativeEndConditions() {
        int n = 4;
        double[] x, y;
        x = xRange(-2.0, 2.0, n);
        y = parabolic(x);
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.SecondDerivative, -2.0, CubicSplineInterpolation.BoundaryCondition.SecondDerivative, -2.0).interpolate(x, y);
        checkValues("MC SecondDerivative spline", interpolation, x, y);
        check1stDerivativeValue("MC SecondDerivative spline", interpolation, x[0], 4.0);
        check1stDerivativeValue("MC SecondDerivative spline", interpolation, x[n - 1], -4.0);
        check2ndDerivativeValue("MC SecondDerivative spline", interpolation, x[0], -2.0);
        check2ndDerivativeValue("MC SecondDerivative spline", interpolation, x[n - 1], -2.0);
    }

    @Test
    public void testMCNotAKnotSplineNonRestrictiveHymanFilter() {
        int n = 4;
        double[] x, y;
        x = xRange(-2.0, 2.0, n);
        y = parabolic(x);
        double zero = 0.0, interpolated, expected = 0.0;
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0, CubicSplineInterpolation.BoundaryCondition.NotAKnot, 0.0).interpolate(x, y);
        interpolated = interpolation.evaluate(zero);
        assertFalse("MC not-a-knot spline interpolation failed at x = " + zero + "\n    interpolated value: " + interpolated + "\n    expected value:     " + expected + "\n    error:              " + abs(interpolated - expected), abs(interpolated - expected) > 1e-15);
    }

    @Test
    public void testMCClampedSplineNonRestrictiveHymanFilter() {
        int n = 4;
        double[] x, y;
        x = xRange(-2.0, 2.0, n);
        y = parabolic(x);
        double zero = 0.0, interpolated, expected = 0.0;
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.FirstDerivative, 4.0, CubicSplineInterpolation.BoundaryCondition.FirstDerivative, -4.0).interpolate(x, y);
        interpolated = interpolation.evaluate(zero);
        assertFalse("MC clamped spline interpolation failed at x = " + zero + "\n    interpolated value: " + interpolated + "\n    expected value:     " + expected + "\n    error:              " + abs(interpolated - expected), abs(interpolated - expected) > 1e-15);
    }

    @Test
    public void testMCSecondDerivativeSplineNonRestrictiveHymanFilter() {
        int n = 4;
        double[] x, y;
        x = xRange(-2.0, 2.0, n);
        y = parabolic(x);
        double zero = 0.0, interpolated, expected = 0.0;
        CubicSplineInterpolation interpolation = new MonotonicCubicSpline(CubicSplineInterpolation.BoundaryCondition.SecondDerivative, -2.0, CubicSplineInterpolation.BoundaryCondition.SecondDerivative, -2.0).interpolate(x, y);
        interpolated = interpolation.evaluate(zero);
        assertFalse("MC SecondDerivative spline interpolation failed at x = " + zero + "\n    interpolated value: " + interpolated + "\n    expected value:     " + expected + "\n    error:              " + abs(interpolated - expected), abs(interpolated - expected) > 1e-15);
    }
}
