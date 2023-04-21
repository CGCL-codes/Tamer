package org.statcato.statistics.inferential;

import java.util.Vector;
import Jama.Matrix;
import org.statcato.statistics.FProbabilityDistribution;
import org.statcato.utils.HelperFunctions;

/**
 * Multiple regression for non-linear models.  Variations (explained, unexplained,
 * total) are calculated using matrix operations.
 * 
 * @author Margaret Yau
 * @version %I%, %G%
 * @since 1.0
 */
public class MultipleRegression2 {

    private int n;

    private int k;

    private Matrix A;

    private Matrix x;

    private Matrix b;

    private boolean hasConstant;

    /**
     * Constructor.
     * 
     * @param IndependentVars a vector of vectors of double, where each 
     *          vector is an independent variable with the same number of values
     * @param DependentVar a vector of double, which has the same number of 
     *          values as the independent variables
     */
    public MultipleRegression2(Vector<Vector<Double>> IndependentVars, Vector<Double> DependentVar, boolean hasConstant) {
        n = DependentVar.size();
        k = IndependentVars.size();
        this.hasConstant = hasConstant;
        int offset = hasConstant ? 1 : 0;
        double[][] matrix = new double[n][offset + k];
        for (int i = 0; i < n; ++i) {
            matrix[i][0] = 1;
        }
        for (int col = 0; col < k; ++col) {
            for (int row = 0; row < n; ++row) {
                double a = (Double) IndependentVars.elementAt(col).elementAt(row).doubleValue();
                matrix[row][col + offset] = a;
            }
        }
        A = new Matrix(matrix);
        double[][] array = new double[n][1];
        for (int i = 0; i < DependentVar.size(); ++i) array[i][0] = DependentVar.elementAt(i);
        b = new Matrix(array);
        try {
            x = A.solve(b);
        } catch (RuntimeException e) {
            throw new RuntimeException("Multiple regression cannot be " + "performed on a rank deficient matrix.");
        }
    }

    /**
     * Returns the coefficients of the regression equation 
     * y = b_0 + b_1 * x_1 + ... + b_k * x_k as a matrix of
     * dimension k+1 by 1: [b_0 b_1 ... b_k]'.
     * 
     * @return a k+1 by 1 matrix containing the coefficients of the 
     * regression equation
     */
    public Matrix RegressionEqCoefficients() {
        return x;
    }

    /**
     * Returns the predicted y value given a vector of values of the
     * independent variables using the regression equation.
     * 
     * @param var a vector of double that has the same number of values as
     * the number of independent variables.
     * 
     * @return predicted y value
     */
    public double YPredicted(Matrix var) {
        double y = hasConstant ? x.get(0, 0) : 0;
        int offset = hasConstant ? 1 : 0;
        for (int i = 0; i < k; ++i) {
            y += (Double) var.get(0, i) * x.get(i + offset, 0);
        }
        return y;
    }

    /**
     * Returns the average value of the dependent variable.
     * @return average y value
     */
    private double YAverage() {
        double sum = 0;
        for (int i = 0; i < n; ++i) {
            sum += b.get(i, 0);
        }
        return sum / n;
    }

    /**
     * Returns the total variation (SST, the sum of squared differences
     * between the y values and the average y value).
     * 
     * @return total variation
     */
    public double TotalVariation() {
        double yAverage = YAverage();
        double bSquared = b.transpose().times(b).get(0, 0);
        return Math.abs(bSquared - n * yAverage * yAverage);
    }

    /**
     * Returns the i th value of all the independent variables as a 1 by k matrix.
     * 
     * @param i index
     * @return matrix
     */
    public Matrix XVar(int i) {
        if (hasConstant) return A.getMatrix(i, i, 1, k); else return A.getMatrix(i, i, 0, k - 1);
    }

    /**
     * Returns the explained variation (SSR, the sum of squared differences
     * between the predicted y value and the average y value).
     * 
     * @return explained variation
     */
    public double ExplainedVariation() {
        double yAverage = YAverage();
        double yHatSquared = x.transpose().times(A.transpose()).times(b).get(0, 0);
        return Math.abs(yHatSquared - n * yAverage * yAverage);
    }

    /**
     * Returns the unexplained variation (the sum of squared differences 
     * between the predicted y value and the y value).
     * 
     * @return unexplained variation
     */
    public double UnexplainedVariation() {
        return Math.abs(b.transpose().times(b).get(0, 0) - x.transpose().times(A.transpose()).times(b).get(0, 0));
    }

    /**
     * Returns the coefficient of determination r^2, the amount of the variation 
     * in y that is explained by the regression line.
     * 
     * @return r^2
     */
    public double CoefficientOfDetermination() {
        return ExplainedVariation() / TotalVariation();
    }

    /**
     * Returns the adjusted coefficient of determination.
     * 
     * @return r^2
     */
    public double AdjustedCoefficientOfDetermination() {
        double r2 = CoefficientOfDetermination();
        return 1 - (n - 1) * (1 - r2) / (n - k - 1);
    }

    /**
     * Returns the standard error of estimate, 
     * sqrt(unexplained variation / (n-2)).
     * 
     * @return s
     */
    public double StandardError() {
        return Math.sqrt(UnexplainedVariation() / (n - k - 1));
    }

    /**
     * Returns the test statistics F.
     * F = (explained variation) / (unexplained variation) * (n - k - 1) / k
     * 
     * @return test statistics F
     */
    public double TestStatistics() {
        double unexplained = UnexplainedVariation();
        if (unexplained == 0) return Double.POSITIVE_INFINITY;
        return ExplainedVariation() / UnexplainedVariation() * (n - k - 1) / k;
    }

    /**
     * Returns the p-Value of test statistics.
     * 
     * @return p-Value
     */
    public double PValue() {
        double ts = TestStatistics();
        if (ts == Double.POSITIVE_INFINITY) return 0;
        FProbabilityDistribution dist = new FProbabilityDistribution(k, n - k - 1);
        double area = dist.cumulativeProbability(ts);
        return 1 - area;
    }

    /**
     * Returns the sample size.
     * 
     * @return n
     */
    public int SampleSize() {
        return n;
    }

    /** 
     * Returns the number of independent variables.
     * 
     * @return k
     */
    public int NumIndepVar() {
        return k;
    }

    @Override
    public String toString() {
        String text = "";
        text += "Sample size = " + SampleSize() + "<br>";
        text += "<br>";
        text += "<u>Variation</u>:<br>";
        text += "Explained variation = " + HelperFunctions.formatFloat(ExplainedVariation(), 4) + "<br>";
        text += "Unexplained variation = " + HelperFunctions.formatFloat(UnexplainedVariation(), 4) + "<br>";
        text += "Total variation = " + HelperFunctions.formatFloat(TotalVariation(), 4) + "<br>";
        text += "Coefficient of determination r<sup>2</sup> = " + HelperFunctions.formatFloat(CoefficientOfDetermination(), 4) + "<br>";
        text += "Standard error of estimate = " + HelperFunctions.formatFloat(StandardError(), 4) + "<br>";
        text += "Test statistics F = " + HelperFunctions.formatFloat(TestStatistics(), 4) + "<br>";
        text += "p-Value = " + HelperFunctions.formatFloat(PValue(), 4) + "<br>";
        return text;
    }
}
