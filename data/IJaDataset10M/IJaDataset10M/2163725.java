package edu.ucla.sspace.common;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.ucla.sspace.util.IntegerMap;

/**
 * A collection of static methods for statistical analysis.
 */
public class Statistics {

    /**
     * Uninstantiable
     */
    private Statistics() {
    }

    /**
     * Returns the entropy of the array.
     */
    public static double entropy(int[] a) {
        Map<Integer, Integer> symbolFreq = new IntegerMap<Integer>();
        for (int i : a) {
            Integer freq = symbolFreq.get(i);
            symbolFreq.put(i, (freq == null) ? 1 : 1 + freq);
        }
        double entropy = 0;
        double symbols = a.length;
        for (Integer freq : symbolFreq.values()) {
            double symbolProbability = freq / symbols;
            entropy -= symbolProbability * log2(symbolProbability);
        }
        return entropy;
    }

    /**
     * Returns the entropy of the array.
     */
    public static double entropy(double[] a) {
        Map<Double, Integer> symbolFreq = new HashMap<Double, Integer>();
        for (double d : a) {
            Integer freq = symbolFreq.get(d);
            symbolFreq.put(d, (freq == null) ? 1 : 1 + freq);
        }
        double entropy = 0;
        double symbols = a.length;
        for (Integer freq : symbolFreq.values()) {
            double symbolProbability = freq / symbols;
            entropy -= symbolProbability * log2(symbolProbability);
        }
        return entropy;
    }

    /**
     * Returns the base-2 logarithm of {@code d}.
     */
    public static double log2(double d) {
        return Math.log(d) / Math.log(2);
    }

    /**
     * Returns the base-2 logarithm of {@code d + 1}.
     * 
     * @see Math#log1p(double)
     */
    public static double log2_1p(double d) {
        return Math.log1p(d) / Math.log(2);
    }

    /**
     * Returns the mean value of the collection of numbers
     */
    public static double mean(Collection<? extends Number> values) {
        double sum = 0d;
        for (Number n : values) sum += n.doubleValue();
        return sum / values.size();
    }

    /**
     * Returns the mean value of the array of ints
     */
    public static double mean(int[] values) {
        double sum = 0d;
        for (int i : values) sum += i;
        return sum / values.length;
    }

    /**
     * Returns the mean value of the array of doubles
     */
    public static double mean(double[] values) {
        double sum = 0d;
        for (double d : values) sum += d;
        return sum / values.length;
    }

    /**
     * Returns the median value of the collection of numbers
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable> T median(Collection<T> values) {
        if (values.isEmpty()) throw new IllegalArgumentException("No median in an empty collection");
        List<T> sorted = new ArrayList<T>(values);
        Collections.sort(sorted);
        return sorted.get(sorted.size() / 2);
    }

    /**
     * Returns the median value of the array of ints
     */
    public static double median(int[] values) {
        if (values.length == 0) throw new IllegalArgumentException("No median in an empty array");
        int[] sorted = Arrays.copyOf(values, values.length);
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }

    /**
     * Returns the median value of the array of doubles
     */
    public static double median(double[] values) {
        if (values.length == 0) throw new IllegalArgumentException("No median in an empty array");
        double[] sorted = Arrays.copyOf(values, values.length);
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }

    /**
     * Randomly sets {@code valuesToSet} values to {@code true} for a sequence
     * from [0:{@code range}).
     *
     * @param valuesToSet the number of values that are to be set to {@code
     *        true} in the distribution
     * @param range the total number of values in the sequence.
     */
    public static BitSet randomDistribution(int valuesToSet, int range) {
        if (valuesToSet >= range) throw new IllegalArgumentException("too many values for range");
        BitSet values = new BitSet(range);
        if (valuesToSet < (range / 2)) {
            int set = 0;
            while (set < valuesToSet) {
                int i = (int) (Math.random() * range);
                if (!values.get(i)) {
                    values.set(i, true);
                    set++;
                }
            }
        } else {
            values.set(0, range, true);
            int set = range;
            while (set > valuesToSet) {
                int i = (int) (Math.random() * range);
                if (values.get(i)) {
                    values.set(i, false);
                    set--;
                }
            }
        }
        return values;
    }

    /**
     * Returns the standard deviation of the collection of numbers
     */
    public static double stddev(Collection<? extends Number> values) {
        double mean = mean(values);
        double sum = 0d;
        for (Number n : values) {
            double d = n.doubleValue() - mean;
            sum += d * d;
        }
        return Math.sqrt(sum / values.size());
    }

    /**
     * Returns the <a
     * href="http://en.wikipedia.org/wiki/Standard_error_(statistics)">standard
     * error</a> of the values in collection.
     */
    public static double stderr(Collection<? extends Number> values) {
        return sampleStddev(values) / Math.sqrt(values.size());
    }

    /**
     * Returns the <a
     * href="http://en.wikipedia.org/wiki/Standard_error_(statistics)">standard
     * error</a> of the values in the {@code int} array
     */
    public static double stderr(int[] values) {
        return sampleStddev(values) / Math.sqrt(values.length);
    }

    /**
     * Returns the <a
     * href="http://en.wikipedia.org/wiki/Standard_error_(statistics)">standard
     * error</a> of the values in the {@code double} array
     */
    public static double stderr(double[] values) {
        return sampleStddev(values) / Math.sqrt(values.length);
    }

    /**
     * Returns the standard deviation of the values in the int array
     */
    public static double stddev(int[] values) {
        double mean = mean(values);
        double sum = 0d;
        for (int i : values) {
            double d = i - mean;
            sum += d * d;
        }
        return Math.sqrt(sum / values.length);
    }

    /**
     * Returns the standard deviation of the values in the double array
     */
    public static double stddev(double[] values) {
        double mean = mean(values);
        double sum = 0d;
        for (double d : values) {
            double d2 = d - mean;
            sum += d2 * d2;
        }
        return Math.sqrt(sum / values.length);
    }

    /**
     * Returns the <a
     * href="http://en.wikipedia.org/wiki/Standard_deviation#With_sample_standard_deviation">sample
     * standard deviation</a> of the collection of numbers
     */
    public static double sampleStddev(Collection<? extends Number> values) {
        if (values.size() < 2) throw new IllegalArgumentException("Must have at least two values");
        double mean = mean(values);
        double sum = 0d;
        for (Number n : values) {
            double d = n.doubleValue() - mean;
            sum += d * d;
        }
        return Math.sqrt(sum / (values.size() - 1));
    }

    /**
     * Returns the <a
     * href="http://en.wikipedia.org/wiki/Standard_deviation#With_sample_standard_deviation">sample
     * standard deviation</a> of the values in the int array
     */
    public static double sampleStddev(int[] values) {
        if (values.length < 2) throw new IllegalArgumentException("Must have at least two values");
        double mean = mean(values);
        double sum = 0d;
        for (int i : values) {
            double d = i - mean;
            sum += d * d;
        }
        return Math.sqrt(sum / (values.length - 1));
    }

    /**
     * Returns the <a
     * href="http://en.wikipedia.org/wiki/Standard_deviation#With_sample_standard_deviation">sample
     * standard deviation</a> of the values in the double array
     */
    public static double sampleStddev(double[] values) {
        if (values.length < 2) throw new IllegalArgumentException("Must have at least two values");
        double mean = mean(values);
        double sum = 0d;
        for (double d : values) {
            double d2 = d - mean;
            sum += d2 * d2;
        }
        return Math.sqrt(sum / (values.length - 1));
    }
}
