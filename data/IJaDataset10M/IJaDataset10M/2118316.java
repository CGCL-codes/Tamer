package shu.cms.dc.estimate;

import java.text.*;
import shu.math.*;
import shu.math.*;
import shu.math.array.DoubleArray;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 * �w��į઺��i
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public class EstimatorReport {

    private static DecimalFormat df = new DecimalFormat("##.##");

    public static final void setDecimalFormat(DecimalFormat decimalFormat) {
        df = decimalFormat;
    }

    /**
   * �s�@�w��į઺��i
   * @param estimatedValues double[][]
   * @param realValues double[][]
   * @return EstimatorReport
   */
    public static EstimatorReport getInstance(double[][] estimatedValues, double[][] realValues) {
        EstimatorReport report = new EstimatorReport();
        report.statistics(estimatedValues, realValues);
        return report;
    }

    /**
   * �w��į઺�έp
   * @param estimatedValues double[][]
   * @param realValues double[][]
   */
    protected void statistics(double[][] estimatedValues, double[][] realValues) {
        double[][] errors = errors(estimatedValues, realValues);
        double[][] errorsT = DoubleArray.transpose(errors);
        double[][] relErrorsT = relativeValues(errors, realValues);
        absErrRGBMean = new double[] { Maths.mean(errorsT[0]), Maths.mean(errorsT[1]), Maths.mean(errorsT[2]) };
        absErrRGBMean = DoubleArray.times(absErrRGBMean, 100.);
        relErrRGBMean = new double[] { Maths.mean(relErrorsT[0]), Maths.mean(relErrorsT[1]), Maths.mean(relErrorsT[2]) };
        relErrRGBMean = DoubleArray.times(relErrRGBMean, 100.);
        absErrRGBMax = new double[] { Maths.max(errorsT[0]), Maths.max(errorsT[1]), Maths.max(errorsT[2]) };
        absErrRGBMax = DoubleArray.times(absErrRGBMax, 100.);
        relErrRGBMax = new double[] { Maths.max(relErrorsT[0]), Maths.max(relErrorsT[1]), Maths.max(relErrorsT[2]) };
        relErrRGBMax = DoubleArray.times(relErrRGBMax, 100.);
    }

    /**
   * ����������~�t
   * @return double
   */
    public double getAbsErrMeanDeltaRGB() {
        return Maths.mean(absErrRGBMean);
    }

    /**
   * �̤j������~�t
   * @return double
   */
    public double getAbsErrMaxDeltaRGB() {
        return Maths.mean(absErrRGBMax);
    }

    /**
   * �̤j�������~�t
   * @return double
   */
    public double getRelErrMeanDeltaRGB() {
        return Maths.mean(relErrRGBMean);
    }

    /**
   * �̤j���۹�~�t
   * @return double
   */
    public double getRelErrMaxDeltaRGB() {
        return Maths.mean(relErrRGBMax);
    }

    protected static final String toString(double[] doubleArray, DecimalFormat df) {
        StringBuilder buf = new StringBuilder();
        int size = doubleArray.length;
        for (int x = 0; x < size; x++) {
            buf.append(df.format(doubleArray[x]));
            buf.append(' ');
        }
        return buf.toString();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (df != null) {
            buf.append("absErrMean R G B: " + toString(absErrRGBMean, df) + "(% of max dynamic)\n");
            buf.append("absErrMean RGB: " + df.format(Maths.mean(absErrRGBMean)) + "(% of max dynamic)\n");
            buf.append("absErrMax R G B: " + toString(absErrRGBMax, df) + "(% of max dynamic)\n");
            buf.append("absErrMax RGB: " + df.format(Maths.mean(absErrRGBMax)) + "(% of max dynamic)\n");
            buf.append('\n');
            buf.append("relErrMean R G B: " + toString(relErrRGBMean, df) + "(% of pixel value)\n");
            buf.append("relErrMean RGB: " + df.format(Maths.mean(relErrRGBMean)) + "(% of pixel value)\n");
            buf.append("relErrMax R G B: " + toString(relErrRGBMax, df) + "(% of pixel value)\n");
            buf.append("relErrMax RGB: " + df.format(Maths.mean(relErrRGBMax)) + "(% of pixel value)");
        } else {
            buf.append("absErrMean R G B: " + DoubleArray.toString(absErrRGBMean) + "(% of max dynamic)\n");
            buf.append("absErrMean RGB: " + Maths.mean(absErrRGBMean) + "(% of max dynamic)\n");
            buf.append("absErrMax R G B: " + DoubleArray.toString(absErrRGBMax) + "(% of max dynamic)\n");
            buf.append("absErrMax RGB: " + Maths.mean(absErrRGBMax) + "(% of max dynamic)\n");
            buf.append('\n');
            buf.append("relErrMean R G B: " + DoubleArray.toString(relErrRGBMean) + "(% of pixel value)\n");
            buf.append("relErrMean RGB: " + Maths.mean(relErrRGBMean) + "(% of pixel value)\n");
            buf.append("relErrMax R G B: " + DoubleArray.toString(relErrRGBMax) + "(% of pixel value)\n");
            buf.append("relErrMax RGB: " + Maths.mean(relErrRGBMax) + "(% of pixel value)");
        }
        return buf.toString();
    }

    public double[] absErrRGBMean;

    public double[] absErrRGBMax;

    public double[] relErrRGBMean;

    public double[] relErrRGBMax;

    /**
   * �p��۹�ƭ�: (est-real)/real
   * @param errorsValues double[][]
   * @param realValues double[][]
   * @return double[][]
   */
    protected static final double[][] relativeValues(double[][] errorsValues, double[][] realValues) {
        int size = errorsValues.length;
        int width = errorsValues[0].length;
        double[][] relativeValues = new double[size][width];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < width; y++) {
                relativeValues[x][y] = errorsValues[x][y] / realValues[x][y];
            }
        }
        return relativeValues;
    }

    protected static final double[][] errors(double[][] estimatedValues, double[][] realValues) {
        int size = estimatedValues.length;
        int ch = estimatedValues[0].length;
        double[][] errors = new double[size][];
        for (int x = 0; x < size; x++) {
            double[] diff = DoubleArray.minus(estimatedValues[x], realValues[x]);
            for (int y = 0; y < ch; y++) {
                diff[y] = Math.abs(diff[y]);
            }
            errors[x] = diff;
        }
        return errors;
    }
}
