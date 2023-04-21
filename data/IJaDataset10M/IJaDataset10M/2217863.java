package com.javable.dataview.analysis;

import com.javable.dataview.DataChannel;

/**
 * Class that performes a number of statistical/analysis routines on channel(s).
 * <p>
 * Descriptive statistics methods portions copyright (C) 1999 CERN - European
 * Organization for Nuclear Research.
 */
public class ChannelStats {

    /** Left (from the peak) slope switch for the find slope routine */
    public static final int LEFT_SLOPE = 0;

    /** Right (from the peak) slope switch for the find slope routine */
    public static final int RIGHT_SLOPE = 1;

    /**
     * Returns the index of x channel that corresponds to the given x value
     * 
     * @param x x value
     * @param xChannel x channel
     * @return returns the correspondent index in data array
     */
    public static final int getIndexAtX(double x, DataChannel xChannel) {
        double delta = Double.POSITIVE_INFINITY;
        int i = -1;
        if (xChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if (Math.abs(xval - x) < delta) {
                    i = k;
                    delta = Math.abs(xval - x);
                }
            }
        }
        return i;
    }

    /**
     * Returns the value of y channel that corresponds to the given x value
     * 
     * @param x x value
     * @param xChannel x channel
     * @param yChannel y channel
     * @return returns the correspondent y value
     */
    public static final double getValueAtX(double x, DataChannel xChannel, DataChannel yChannel) {
        double value = 0;
        if (yChannel.getAttribute().isNormal()) {
            int i = getIndexAtX(x, xChannel);
            if (i < yChannel.size()) value = yChannel.getData(i);
        }
        return value;
    }

    /**
     * Returns the number of data points between xMin and xMax for the given x
     * channel / y channel pair
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param xChannel x channel
     * @param yChannel y channel
     * @return number of data points between xMin and xMax
     */
    public static final double getN(double xMin, double xMax, DataChannel xChannel, DataChannel yChannel) {
        double n = 0;
        if (yChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                    n++;
                }
            }
        }
        return n;
    }

    /**
     * Returns the mean of a data sequence. That is <code>sum / size</code>
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param xChannel x channel
     * @param yChannel y channel
     * @return double
     */
    public static final double getMean(double xMin, double xMax, DataChannel xChannel, DataChannel yChannel) {
        double n = 0;
        double mean = 0;
        if (yChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                    mean += yChannel.getData(k);
                    n++;
                }
            }
        }
        return mean / n;
    }

    /**
     * Returns the variance of a data sequence. That is
     * <code>(sumOfSquares - mean*sum) / size</code> with
     * <code>mean = sum/size</code>
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param xChannel x channel
     * @param yChannel y channel
     * @return double
     */
    public static final double getVariance(double xMin, double xMax, DataChannel xChannel, DataChannel yChannel) {
        double n = 0;
        double mean = 0;
        double sum = 0;
        double sumofsq = 0;
        if (yChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                    sum += yChannel.getData(k);
                    sumofsq += (yChannel.getData(k) * yChannel.getData(k));
                    n++;
                }
            }
        }
        mean = sum / n;
        return (sumofsq - mean * sum) / n;
    }

    /**
     * Returns the negative p array index of a data sequence
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param xChannel x channel
     * @param yChannel y channel
     * @return int
     */
    public static final int getNegativePeakIndex(double xMin, double xMax, DataChannel xChannel, DataChannel yChannel) {
        double delta = Double.POSITIVE_INFINITY;
        int i = -1;
        if (yChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                    if (yChannel.getData(k) < delta) {
                        i = k;
                        delta = yChannel.getData(k);
                    }
                }
            }
        }
        return i;
    }

    /**
     * Returns the positive p array index of a data sequence
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param xChannel x channel
     * @param yChannel y channel
     * @return int
     */
    public static final int getPositivePeakIndex(double xMin, double xMax, DataChannel xChannel, DataChannel yChannel) {
        double delta = Double.NEGATIVE_INFINITY;
        int i = -1;
        if (yChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                    if (yChannel.getData(k) > delta) {
                        i = k;
                        delta = yChannel.getData(k);
                    }
                }
            }
        }
        return i;
    }

    /**
     * Returns the absolute p array index of a data sequence
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param xChannel x channel
     * @param yChannel y channel
     * @return int
     */
    public static final int getAbsolutePeakIndex(double xMin, double xMax, DataChannel xChannel, DataChannel yChannel) {
        double delta = Double.NEGATIVE_INFINITY;
        int i = -1;
        if (yChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                    if (Math.abs(yChannel.getData(k)) > delta) {
                        i = k;
                        delta = Math.abs(yChannel.getData(k));
                    }
                }
            }
        }
        return i;
    }

    /**
     * Returns the area under the curve of a data sequence
     * <p>
     * This method assumes that xchannel values are monotonically rising with
     * index. All values that do not satisfy <code>x[n]&lt;x[n+1]</code>
     * criterion are discarded.
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param xChannel x channel
     * @param yChannel y channel
     * @return double
     */
    public static final double getCurveArea(double xMin, double xMax, DataChannel xChannel, DataChannel yChannel) {
        double area = 0.0;
        double p_x = 0.0;
        double p_y = 0.0;
        int count = 0;
        if (yChannel.getAttribute().isNormal()) {
            for (int k = 0; k < xChannel.size(); k++) {
                double xval = xChannel.getData(k);
                if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                    double yval = yChannel.getData(k);
                    if ((count > 0) && (xval > p_x)) area += (yval + p_y) / 2.0 * (xval - p_x);
                    p_x = xval;
                    p_y = yval;
                    count++;
                }
            }
        }
        return area;
    }

    /**
     * Returns the maximum slope <code>abs(delta(data)/delta(time))</code>
     * index
     * <p>
     * This method assumes that xchannel values are monotonically rising with
     * index. All values that do not satisfy <code>x[n]&lt;x[n+1]</code>
     * criterion are discarded.
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param pi index of the peak value to start the search
     * @param direction search direction (to left or to right from the peak)
     * @param xChannel x channel
     * @param yChannel y channel
     * @return int
     */
    public static final int getMaxSlopeIndex(double xMin, double xMax, int pi, int direction, DataChannel xChannel, DataChannel yChannel) {
        double delta = 0;
        int i = -1;
        double slope = 0.0;
        double p_x = 0.0;
        double p_y = 0.0;
        int count = 0;
        if (yChannel.getAttribute().isNormal()) {
            if (direction == RIGHT_SLOPE) {
                for (int k = pi; k < xChannel.size(); k++) {
                    double xval = xChannel.getData(k);
                    if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                        double yval = yChannel.getData(k);
                        if ((count > 0) && (xval > p_x)) {
                            delta = Math.abs((yval - p_y) / (xval - p_x));
                            if (delta > slope) {
                                slope = delta;
                                i = k;
                            }
                        }
                        p_x = xval;
                        p_y = yval;
                        count++;
                    }
                }
            }
            if (direction == LEFT_SLOPE) {
                for (int k = pi; k >= 0; k--) {
                    double xval = xChannel.getData(k);
                    if ((xval >= xMin) && (xval <= xMax)) {
                        double yval = yChannel.getData(k);
                        if ((count > 0) && (xval < p_x)) {
                            delta = Math.abs((yval - p_y) / (xval - p_x));
                            if (delta > slope) {
                                slope = delta;
                                i = k;
                            }
                        }
                        p_x = xval;
                        p_y = yval;
                        count++;
                    }
                }
            }
        }
        return i;
    }

    /**
     * Returns the index of a slope level (in %) of a data sequence
     * <p>
     * This method assumes that xchannel values are monotonically rising with
     * index. All values that do not satisfy <code>x[n]&lt;x[n+1]</code>
     * criterion are discarded.
     * 
     * @param xMin minimal limit for x
     * @param xMax maximal limit for x
     * @param pi index of the peak value to start the search
     * @param direction search direction (to left or to right from the peak)
     * @param level level of the peak value to find (%)
     * @param xChannel x channel
     * @param yChannel y channel
     * @return double
     */
    public static final int getSlopeLevelIndex(double xMin, double xMax, int pi, int direction, double level, DataChannel xChannel, DataChannel yChannel) {
        double delta = 0.0;
        double peak = 0.0;
        double p_x = 0.0;
        if ((pi < yChannel.size()) && (pi >= 0)) {
            peak = yChannel.getData(pi);
            delta = Math.abs(peak - peak * (level / 100.0));
            p_x = xChannel.getData(pi);
        }
        if (yChannel.getAttribute().isNormal()) {
            if (direction == RIGHT_SLOPE) {
                for (int k = pi; k < xChannel.size(); k++) {
                    double xval = xChannel.getData(k);
                    if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                        double yval = yChannel.getData(k);
                        if (xval > p_x) {
                            if (Math.abs(peak - yval) >= delta) {
                                return k;
                            }
                            p_x = xval;
                        }
                    }
                }
            }
            if (direction == LEFT_SLOPE) {
                for (int k = pi; k >= 0; k--) {
                    double xval = xChannel.getData(k);
                    if ((xval >= xMin) && (xval <= xMax)) {
                        double yval = yChannel.getData(k);
                        if (xval < p_x) {
                            if (Math.abs(peak - yval) >= delta) {
                                return k;
                            }
                            p_x = xval;
                        }
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Returns the auto-correlation of a data sequence.
     */
    public static final double autoCorrelation(double[] data, int lag, double mean, double variance) {
        int n = data.length;
        if (lag >= n) throw new IllegalArgumentException("lag is too large");
        double run = 0;
        for (int i = lag; i < n; ++i) run += (data[i] - mean) * (data[i - lag] - mean);
        return (run / (n - lag)) / variance;
    }

    /**
     * Returns the correlation of two data sequences. That is
     * <code>covariance(data1,data2)/(standardDev1*standardDev2)</code>.
     */
    public static final double correlation(double[] data1, double standardDev1, double[] data2, double standardDev2) {
        return covariance(data1, data2) / (standardDev1 * standardDev2);
    }

    /**
     * Returns the covariance of two data sequences.
     */
    public static final double covariance(double[] data1, double[] data2) {
        int size = data1.length;
        if (size != data2.length || size == 0) throw new IllegalArgumentException();
        double sumx = data1[0], sumy = data2[0], Sxy = 0;
        for (int i = 1; i < size; ++i) {
            double x = data1[i];
            double y = data2[i];
            sumx += x;
            Sxy += (x - sumx / (i + 1)) * (y - sumy / i);
            sumy += y;
        }
        return Sxy / size;
    }

    /**
     * Durbin-Watson computation.
     */
    public static final double durbinWatson(double[] data) {
        int size = data.length;
        if (size < 2) throw new IllegalArgumentException("data sequence must contain at least two values.");
        double run = 0;
        double run_sq = 0;
        run_sq = data[0] * data[0];
        for (int i = 1; i < size; ++i) {
            double x = data[i] - data[i - 1];
            run += x * x;
            run_sq += data[i] * data[i];
        }
        return run / run_sq;
    }

    /**
     * Returns the mean deviation of a dataset. That is
     * <code>Sum (Math.abs(data[i]-mean)) / data.size())</code>.
     */
    public static final double meanDeviation(double[] data, double mean) {
        int size = data.length;
        double sum = 0;
        for (int i = size; --i >= 0; ) sum += Math.abs(data[i] - mean);
        return sum / size;
    }

    /**
     * Returns the RMS (Root-Mean-Square) of a data sequence. That is
     * <code>Math.sqrt(Sum( data[i]*data[i] ) / data.size())</code>. The RMS
     * of data sequence is the square-root of the mean of the squares of the
     * elements in the data sequence. It is a measure of the average "size" of
     * the elements of a data sequence.
     * 
     * @param sumOfSquares
     *        <code>sumOfSquares(data) == Sum( data[i]*data[i] )</code> of the
     *        data sequence.
     * @param size the number of elements in the data sequence.
     */
    public static final double rms(int size, double sumOfSquares) {
        return Math.sqrt(sumOfSquares / size);
    }

    /**
     * Returns the sample variance of a data sequence. That is
     * <code>Sum ( (data[i]-mean)^2 ) / (data.size()-1)</code>.
     */
    public static final double sampleVariance(double[] data, double mean) {
        int size = data.length;
        double sum = 0;
        for (int i = size; --i >= 0; ) {
            double delta = data[i] - mean;
            sum += delta * delta;
        }
        return sum / (size - 1);
    }

    /**
     * Modifies a data sequence to be standardized. Changes each element
     * <code>data[i]</code> as follows:
     * <code>data[i] = (data[i]-mean)/standardDeviation</code>.
     */
    public static final void standardize(double[] data, double mean, double standardDeviation) {
        for (int i = data.length; --i >= 0; ) data[i] = (data[i] - mean) / standardDeviation;
    }

    /**
     * Returns <code>Sum( (data[i]-c)<sup>k</sup> )</code>; optimized for
     * common parameters like <code>c == 0.0</code> and/or
     * <code>k == -2 .. 4</code>.
     */
    public static double sumOfPowerDeviations(double[] data, int k, double c) {
        return sumOfPowerDeviations(data, k, c, 0, data.length - 1);
    }

    /**
     * Returns <code>Sum( (data[i]-c)<sup>k</sup> )</code> for all
     * <code>i = from .. to</code>; optimized for common parameters like
     * <code>c == 0.0</code> and/or <code>k == -2 .. 5</code>.
     */
    public static double sumOfPowerDeviations(final double[] data, final int k, final double c, final int from, final int to) {
        double sum = 0;
        double v;
        int i;
        switch(k) {
            case -2:
                if (c == 0.0) for (i = from - 1; ++i <= to; ) {
                    v = data[i];
                    sum += 1 / (v * v);
                } else for (i = from - 1; ++i <= to; ) {
                    v = data[i] - c;
                    sum += 1 / (v * v);
                }
                break;
            case -1:
                if (c == 0.0) for (i = from - 1; ++i <= to; ) sum += 1 / (data[i]); else for (i = from - 1; ++i <= to; ) sum += 1 / (data[i] - c);
                break;
            case 0:
                sum += to - from + 1;
                break;
            case 1:
                if (c == 0.0) for (i = from - 1; ++i <= to; ) sum += data[i]; else for (i = from - 1; ++i <= to; ) sum += data[i] - c;
                break;
            case 2:
                if (c == 0.0) for (i = from - 1; ++i <= to; ) {
                    v = data[i];
                    sum += v * v;
                } else for (i = from - 1; ++i <= to; ) {
                    v = data[i] - c;
                    sum += v * v;
                }
                break;
            case 3:
                if (c == 0.0) for (i = from - 1; ++i <= to; ) {
                    v = data[i];
                    sum += v * v * v;
                } else for (i = from - 1; ++i <= to; ) {
                    v = data[i] - c;
                    sum += v * v * v;
                }
                break;
            case 4:
                if (c == 0.0) for (i = from - 1; ++i <= to; ) {
                    v = data[i];
                    sum += v * v * v * v;
                } else for (i = from - 1; ++i <= to; ) {
                    v = data[i] - c;
                    sum += v * v * v * v;
                }
                break;
            case 5:
                if (c == 0.0) for (i = from - 1; ++i <= to; ) {
                    v = data[i];
                    sum += v * v * v * v * v;
                } else for (i = from - 1; ++i <= to; ) {
                    v = data[i] - c;
                    sum += v * v * v * v * v;
                }
                break;
            default:
                for (i = from - 1; ++i <= to; ) sum += Math.pow(data[i] - c, k);
                break;
        }
        return sum;
    }

    /**
     * Returns the sum of squares of a data sequence. That is
     * <code>Sum ( data[i]*data[i] )</code>.
     */
    public static double sumOfSquares(double[] data) {
        return sumOfPowerDeviations(data, 2, 0.0);
    }
}
