package org.databene.benerator.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.databene.commons.StringUtil;
import org.databene.script.DatabeneScriptParser;
import org.databene.script.WeightedSample;

/**
 * Provides utility functions for generating numbers in an interval.<br/>
 * <br/>
 * Created: 03.09.2006 13:23:02
 * @since 0.1
 * @author Volker Bergmann
 */
public class RandomUtil {

    /** The basic random provider */
    private static Random random = new Random();

    /** Generates a random long value in the range from min to max */
    public static long randomLong(long min, long max) {
        if (min > max) throw new IllegalArgumentException("min (" + min + ") > max (" + max + ")");
        long range = max - min + 1;
        long result;
        if (range != 0) result = min + (random.nextLong() % range); else result = random.nextLong();
        if (result < min) result += range;
        return result;
    }

    /** Generates a random int value in the range from min to max */
    public static int randomInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("min > max: " + min + " > " + max);
        int range = max - min + 1;
        int result;
        if (range != 0) result = min + (random.nextInt() % range); else result = random.nextInt();
        if (result < min) result += range;
        return result;
    }

    public static <T> T randomElement(T... values) {
        if (values.length == 0) throw new IllegalArgumentException("Cannot choose random value from an empty array");
        return values[random.nextInt(values.length)];
    }

    public static <T> T randomElement(List<T> values) {
        return values.get(randomIndex(values));
    }

    public static int randomIndex(List<?> values) {
        if (values.size() == 0) throw new IllegalArgumentException("Cannot create random index for an empty array");
        return random.nextInt(values.size());
    }

    public static char randomDigit(int min) {
        return (char) ('0' + min + random.nextInt(10 - min));
    }

    public static float randomProbability() {
        return random.nextFloat();
    }

    public static Date randomDate(Date min, Date max) {
        return new Date(randomLong(min.getTime(), max.getTime()));
    }

    public static Object randomFromWeightLiteral(String literal) {
        if (StringUtil.isEmpty(literal)) return null;
        WeightedSample<?>[] samples = DatabeneScriptParser.parseWeightedLiteralList(literal);
        int sampleCount = samples.length;
        if (sampleCount == 1) return samples[0];
        float[] probSum = new float[sampleCount];
        double sum = 0;
        for (int i = 0; i < sampleCount; i++) {
            double weight = samples[i].getWeight();
            if (weight < 0) throw new IllegalArgumentException("Negative weight in literal: " + literal);
            sum += weight;
            probSum[i] = (float) sum;
        }
        if (sum == 0) return samples[randomInt(0, sampleCount)];
        for (int i = 0; i < sampleCount; i++) probSum[i] /= (float) sum;
        float probability = randomProbability();
        int i = Arrays.binarySearch(probSum, probability);
        if (i < 0) i = -i - 1;
        if (i >= probSum.length) i = probSum.length - 1;
        return samples[i].getValue();
    }
}
