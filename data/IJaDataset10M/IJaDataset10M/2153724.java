package org.skycastle.util;

import java.util.List;
import java.util.Random;

public class MathUtils {

    /**
     * Multiply with this to convert an angle from degrees to radians
     */
    public static final float DEGREES_TO_RADIANS = (float) (Math.PI / 180.0);

    /**
     * Multiply with this to convert an angle from radians to degrees
     */
    public static final float RADIANS_TO_DEGREES = (float) (180.0 / Math.PI);

    /**
     * The golden ratio.
     * <p/>
     * See e.g. <a href="http://en.wikipedia.org/wiki/Golden_ratio">Wikipedia on Golden Ratio</a>.
     */
    public static final float GOLDEN_RATIO = 1.618033989f;

    private static final Random theTempRandom = new Random();

    /**
     * @param value
     *
     * @return the input value, clamped to the 0..1 range.
     */
    public static float clampToZeroToOne(float value) {
        if (value < 0) {
            value = 0;
        }
        if (value > 1) {
            value = 1;
        }
        return value;
    }

    /**
     * @param value
     *
     * @return the input value, clamped to the 0..1 range.
     */
    public static double clampToZeroToOne(double value) {
        if (value < 0) {
            value = 0;
        }
        if (value > 1) {
            value = 1;
        }
        return value;
    }

    /**
     * @param value input value, if larger than one, then one is subtracted until it is in the 0..1 range, if
     *              smaller than 0, then one is added until it is in the 0..1 range.
     *
     * @return the input value, rolled to the 0..1 range.
     */
    public static double rollToZeroToOne(double value) {
        return value - Math.floor(value);
    }

    /**
     * @param value input value, if larger than one, then one is subtracted until it is in the 0..1 range, if
     *              smaller than 0, then one is added until it is in the 0..1 range.
     *
     * @return the input value, rolled to the 0..1 range.
     */
    public static float rollToZeroToOne(float value) {
        return (float) (value - Math.floor(value));
    }

    /**
     * Clamps the given value to the -1..1 range.
     */
    public static float clampToMinusOneToOne(float value) {
        if (value < -1) {
            value = -1;
        }
        if (value > 1) {
            value = 1;
        }
        return value;
    }

    /**
     * Does a linear interpolation.
     *
     * @param t when 0, the result is a, when 1, the result is b.
     * @param a value at start of range
     * @param b value at end of range
     *
     * @return an interpolated value between a and b (or beyond), with the relative position t.
     */
    public static float interpolate(float t, float a, float b) {
        return a + t * (b - a);
    }

    /**dis
     * Does a linear interpolation using doubles
     *
     * @param t when 0, the result is a, when 1, the result is b.
     * @param a value at start of range
     * @param b value at end of range
     *
     * @return an interpolated value between a and b (or beyond), with the relative position t.
     */
    public static double interpolate(double t, double a, double b) {
        return a + t * (b - a);
    }

    /**
     * Calculates a linearily interpolated value, given a start value and position, an end value and position,
     * and the position to get the value at.
     * <p/>
     * First calculates the relative position, then does a normal linear interpolation between the start and
     * end value, using the relative position as the interpolation factor.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     */
    public static float interpolate(final float position, final float startPosition, final float endPosition, final float startValue, final float endValue) {
        if (position == startPosition && position == endPosition) {
            return 0.5f * (startValue + endValue);
        }
        float relativePosition = calculateRelativePosition(position, startPosition, endPosition);
        return startValue + relativePosition * (endValue - startValue);
    }

    public static float calculateRelativePosition(final float position, final float startPosition, final float endPosition) {
        if (position == startPosition && position == endPosition) {
            return 0.5f;
        }
        if (startPosition == endPosition) {
            throw new IllegalArgumentException("The start position and end positions are the same ('" + startPosition + "' ), but the position is '" + position + "'.  Can not interpolate.");
        }
        return (position - startPosition) / (endPosition - startPosition);
    }

    /**
     * Gets an interpolated value from a list of floats based on a relative position.
     *
     * @param relativePosition the normalized position to get interpolated value for, 0.0 = start of list, 1.0
     *                         = end of list.
     * @param values           a list with the values to interpolate the result from.
     * @param wrap             if true, the list will be treated as a circular list, with the first value
     *                         coming after the last one.
     *
     * @return When position is equal to 0, the first value from the value list is used, when position is
     *         equal to 1, the last value from the value list is used, in other cases the values from the list
     *         are interpolated based on the relative position.
     *
     * @throws IllegalArgumentException if the relativePosition is less than zero or larger than one, or if
     *                                  the values list is null or empty.
     */
    public static float interpolateFromList(final float relativePosition, final List<? extends Number> values, boolean wrap) {
        ParameterChecker.checkZeroToOneInclusive(relativePosition, "relativePosition");
        ParameterChecker.checkNotEmpty(values, "values");
        final int maxPos = values.size() + (wrap ? 0 : -1);
        final float index = relativePosition * maxPos;
        float floor = (float) Math.floor(index);
        float ceil = (float) Math.ceil(index);
        float positionBetweenFloorAndCeil = index - floor;
        final int floorIndex = wrapToRange((int) floor, values.size());
        final int ceilIndex = wrapToRange((int) ceil, values.size());
        return interpolate(positionBetweenFloorAndCeil, values.get(floorIndex).floatValue(), values.get(ceilIndex).floatValue());
    }

    /**
     * Normalizes a value and gets an interpolated value from a list of floats based on the normalized
     * relative position.
     *
     * @param position      position to get interpolated value for
     * @param startPosition minimum position value
     * @param endPosition   maximum position value
     * @param values        a list with the values to interpolate the result from.
     * @param wrap          if true, the list will be treated as a circular list, with the first value coming
     *                      after the last one.
     *
     * @return When position is equal to the start position, the first value from the value list is used, when
     *         position is equal to the end position, the last value from the value list is used, in other
     *         cases the values from the list are interpolated based on the relative position.
     */
    public static float interpolateFromList(final float position, final float startPosition, final float endPosition, final List<? extends Number> values, boolean wrap) {
        float relativePosition = calculateRelativePosition(position, startPosition, endPosition);
        return interpolateFromList(relativePosition, values, wrap);
    }

    /**
     * @param relativeOuterListIndex
     * @param relativeInnerListIndex
     * @param values
     * @param defaultValue
     * @param wrapOuterList          if true, the outer list will be treated as a circular list, with the
     *                               first value coming after the last one.
     * @param wrapInnerList          if true, the inner list will be treated as a circular list, with the
     *                               first value coming after the last one.
     *
     * @return
     */
    public static float interpolateFromNestedLists(final float relativeOuterListIndex, final float relativeInnerListIndex, final List<List<? extends Number>> values, float defaultValue, boolean wrapOuterList, boolean wrapInnerList) {
        ParameterChecker.checkZeroToOneInclusive(relativeOuterListIndex, "relativeOuterListIndex");
        ParameterChecker.checkZeroToOneInclusive(relativeInnerListIndex, "relativeInnerListIndex");
        if (values == null || values.isEmpty()) {
            return defaultValue;
        }
        final int outerListSize = values.size() + (wrapOuterList ? 0 : -1);
        float outerIndex = relativeOuterListIndex * outerListSize;
        float floor = (float) Math.floor(outerIndex);
        float ceil = (float) Math.ceil(outerIndex);
        float positionBetweenFloorAndCeil = outerIndex - floor;
        int floorIndex = wrapToRange((int) floor, values.size());
        int ceilIndex = wrapToRange((int) ceil, values.size());
        final List<? extends Number> floorList = values.get(floorIndex);
        final List<? extends Number> ceilList = values.get(ceilIndex);
        float floorValue = floorList == null ? defaultValue : interpolateFromList(relativeInnerListIndex, floorList, wrapInnerList);
        float ceilValue = ceilList == null ? defaultValue : interpolateFromList(relativeInnerListIndex, ceilList, wrapInnerList);
        return interpolate(positionBetweenFloorAndCeil, floorValue, ceilValue);
    }

    /**
     * Calculates a linearily interpolated value, given a start value and position, an end value and position,
     * and the position to get the value at.
     * <p/>
     * First calculates the relative position, then does a normal linear interpolation between the start and
     * end value, using the relative position as the interpolation factor.
     * <p/>
     * NOTE: having an opposite negative and positive start and end value, there will be a division by zero,
     * and NaN values generated.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     */
    public static double interpolateDouble(final double position, final double startPosition, final double endPosition, final double startValue, final double endValue) {
        if (position == startPosition && position == endPosition) {
            return 0.5 * (startValue + endValue);
        }
        if (startPosition == endPosition) {
            throw new IllegalArgumentException("The start position and end positions are the same ('" + startPosition + "' ), but the position is '" + position + "'.  Can not interpolate.");
        }
        final double relativePosition = (position - startPosition) / (endPosition - startPosition);
        return startValue + relativePosition * (endValue - startValue);
    }

    /**
     * Calculates a linearily interpolated value, given a start position and value, an end position and value,
     * and the position to get the value at.
     * <p/>
     * If the position is outside start or end position, it is treated as if it was at the start or end value
     * respectively.
     * <p/>
     * First calculates the relative position, then does a normal linear interpolation between the start and
     * end value, using the relative position as the interpolation factor.
     * <p/>
     * <p/>
     * NOTE: having an opposite negative and positive start and end value, there will be a division by zero,
     * and NaN values generated.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     */
    public static float interpolateClamp(final float position, final float startPosition, final float endPosition, final float startValue, final float endValue) {
        float p = position;
        if (p < startPosition) {
            p = startPosition;
        } else if (p > endPosition) {
            p = endPosition;
        }
        return interpolate(p, startPosition, endPosition, startValue, endValue);
    }

    /**
     * Calculates a linearily interpolated value, given a start position and value, an end position and value,
     * and the position to get the value at.
     * <p/>
     * If the position is outside start or end position, it is treated as if it was at the start or end value
     * respectively.
     * <p/>
     * First calculates the relative position, then does a normal linear interpolation between the start and
     * end value, using the relative position as the interpolation factor.
     * <p/>
     * NOTE: having an opposite negative and positive start and end value, there will be a division by zero,
     * and NaN values generated.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     */
    public static double interpolateClampDouble(final double position, final double startPosition, final double endPosition, final double startValue, final double endValue) {
        double p = position;
        if (p < startPosition) {
            p = startPosition;
        } else if (p > endPosition) {
            p = endPosition;
        }
        return interpolateDouble(p, startPosition, endPosition, startValue, endValue);
    }

    /**
     * Calculates a smoothly (cosine) interpolated value, given a start value, an end value, and the position
     * to get the value at.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     */
    public static float interpolateSmoothly(final float position, final float startPosition, final float endPosition, final float startValue, final float endValue) {
        final float relativePosition = (position - startPosition) / (endPosition - startPosition);
        float result;
        if (relativePosition <= 0) {
            result = startValue;
        } else if (relativePosition >= 1) {
            result = endValue;
        } else {
            final double relativeSmoothPosition = (1.0 - Math.cos(relativePosition * Math.PI)) / 2.0;
            result = (float) (startValue * (1.0 - relativeSmoothPosition) + endValue * relativeSmoothPosition);
        }
        return result;
    }

    /**
     * Wraps the specified value, so that it is in the range 0 to rangeEnd - 1,
     *
     * @param value    the value to wrap
     * @param rangeEnd end of range, non inclusive
     *
     * @return the wrapped value.
     */
    public static int wrapToRange(final int value, final int rangeEnd) {
        int pos = value % rangeEnd;
        if (pos < 0) {
            pos += rangeEnd;
        }
        return pos;
    }

    /**
     * @return the smallest of the given numbers
     */
    public static int min(final int... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("at least one parameter is required");
        }
        int min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            int number = numbers[i];
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    /**
     * @return the largest of the given numbers
     */
    public static int max(final int... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("at least one parameter is required");
        }
        int max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            int number = numbers[i];
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    /**
     * Calculates an integer based on a floating point value, selecting one of the closest integer numbers
     * randomly, weighted by their relative closeness to the floating point number.
     *
     * @param random     a random number generator to use
     * @param realNumber the fractional value
     *
     * @return one of the two integers closest to the realNumber, weighted by their relative closeness.
     */
    public static int roundFloatToIntStatistically(final Random random, final float realNumber) {
        int result = roundDown(realNumber);
        if (random.nextFloat() < calculateProbabilityOfAddingOne(realNumber, result)) {
            result += 1;
        }
        return result;
    }

    /**
     * Round the number down to the closest integer
     */
    public static int roundDown(final float realNumber) {
        return (int) Math.round(Math.floor(realNumber));
    }

    /**
     * @param value   the value to clamp.
     * @param minimum lower boundary of the range, inclusive.
     * @param maximum upper boundary of the range, inclusive.
     *
     * @return the value clamped to the specified range.
     */
    @SuppressWarnings({ "AssignmentToMethodParameter" })
    public static int clamp(int value, final int minimum, final int maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("The minimum " + minimum + " is larger than the maximum " + maximum + ", possible bug?");
        }
        if (value < minimum) {
            value = minimum;
        } else if (value > maximum) {
            value = maximum;
        }
        return value;
    }

    /**
     * @param value   the value to clamp.
     * @param minimum lower boundary of the range, inclusive.
     * @param maximum upper boundary of the range, inclusive.
     *
     * @return the value clamped to the specified range.
     */
    @SuppressWarnings({ "AssignmentToMethodParameter" })
    public static double clampDouble(double value, final double minimum, final double maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("The minimum " + minimum + " is larger than the maximum " + maximum + ", possible bug?");
        }
        if (value < minimum) {
            value = minimum;
        } else if (value > maximum) {
            value = maximum;
        }
        return value;
    }

    /**
     * @param value          the value to test
     * @param lowerInclusive lower boundary value, inclusive.
     * @param upperExclusive upper boundary value, exclusive.
     *
     * @return true if the value is inside the bounds, false if not.
     */
    public static boolean isInsideBounds(final int value, final int lowerInclusive, final int upperExclusive) {
        if (lowerInclusive > upperExclusive) {
            throw new IllegalArgumentException("The lower boundary  " + lowerInclusive + " is larger than the upper boundary " + upperExclusive + ", possible bug?");
        }
        return value >= lowerInclusive && value < upperExclusive;
    }

    public static float getRandomNumberUsingTwoFloatSeeds(final float firstSeed, final float secondSeed) {
        seedRandomNumberGeneratorWithTwoFloats(theTempRandom, firstSeed, secondSeed);
        return theTempRandom.nextFloat();
    }

    public static long getRandomSeedUsingTwoIntSeeds(final int firstSeed, final int secondSeed) {
        seedRandomNumberGeneratorWithTwoFloats(theTempRandom, firstSeed, secondSeed);
        return theTempRandom.nextLong();
    }

    public static long getRandomSeedUsingTwoFloatSeeds(final float firstSeed, final float secondSeed) {
        seedRandomNumberGeneratorWithTwoFloats(theTempRandom, firstSeed, secondSeed);
        return theTempRandom.nextLong();
    }

    public static float getRandomNumberUsingTwoIntSeeds(final int firstSeed, final int secondSeed) {
        seedRandomNumberGeneratorWithTwoFloats(theTempRandom, firstSeed, secondSeed);
        return theTempRandom.nextFloat();
    }

    /**
     * Seeds the specified random number generator, and make the start position a bit more scrambled by
     * reading off a few random values.
     *
     * @param random
     * @param seed
     */
    public static void seedRandom(final Random random, final long seed) {
        random.setSeed(seed);
        random.nextLong();
        random.nextLong();
        random.setSeed(random.nextLong());
        random.nextLong();
        random.nextLong();
        random.nextLong();
    }

    /**
     * Seeds the specified random number generator with the combination of two different random seeds. Also
     * reads off a few random values, to get the random number generator into a bit more random state.
     *
     * @param random
     * @param firstSeed
     * @param secondSeed
     */
    public static void seedRandom(final Random random, final long firstSeed, final long secondSeed) {
        random.setSeed(firstSeed);
        random.nextLong();
        random.nextLong();
        random.setSeed(random.nextLong() ^ secondSeed);
        random.nextLong();
        random.nextLong();
        random.nextLong();
    }

    /**
     * Seeds the specified random number generator with the combination of three different random seeds. Also
     * reads off a few random values, to get the random number generator into a bit more random state.
     *
     * @param random
     * @param firstSeed
     * @param secondSeed
     */
    public static void seedRandom(final Random random, final long firstSeed, final long secondSeed, final long thirdSeed) {
        random.setSeed(firstSeed);
        random.nextLong();
        random.setSeed(random.nextLong() ^ secondSeed);
        random.nextLong();
        random.setSeed(random.nextLong() ^ thirdSeed);
        random.nextLong();
        random.nextLong();
    }

    /**
     * Rolls the specified integer number, by adding some offset to it, and wrapping the number around to
     * start from the beginning of the range if it grew past the end of the range.
     * <p/>
     * E.g. with a rangeEnd of 10, and a start value of 7, if the rollOffset is 5, the result will be 2.
     *
     * @param originalValue the value to start from when adding the rollOffset.
     * @param rangeEnd      maximum value, non inclusive.
     * @param rollOffset    an offset added to the originalValue.  Can be any positive or negative integer.
     *
     * @return the rolled value.  Will be in the range [0, rangeEnd - 1].
     */
    public static int rollInRange(final int originalValue, final int rangeEnd, final int rollOffset) {
        if (rangeEnd <= 0) {
            throw new IllegalArgumentException("The rangeEnd parameter (" + rangeEnd + ") should not be smaller than the range start (0)");
        }
        final int rolledValue = (originalValue + rangeEnd + (rollOffset % rangeEnd)) % rangeEnd;
        assert rolledValue >= 0 && rolledValue < rangeEnd : "Rolled value out of permitted range. " + rolledValue;
        return rolledValue;
    }

    public static void seedRandomNumberGeneratorWithTwoFloats(final Random random, final float firstSeed, final float secondSeed) {
        seedRandom(random, (long) (firstSeed * 117237), (long) (secondSeed * 135317));
    }

    /**
     * Clamps the value to the specified range, rolling over to the other side in case it is outside the
     * range.
     *
     * @param value   the value to clamp
     * @param minimum minimum value, inclusive.
     * @param maximum maximum value, inclusive.
     *
     * @return a value between minimum and maximum, achieved by rolling the value with the size of the range
     *         until it is in the range.
     */
    public static double clampWithRoll(double value, final double minimum, final double maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("The minimum " + minimum + " is larger than the maximum " + maximum + ", possible bug?");
        }
        final double range = maximum - minimum;
        while (value < minimum) {
            value += range;
        }
        while (value > maximum) {
            value -= range;
        }
        return value;
    }

    protected static float calculateProbabilityOfAddingOne(final float realNumber, final int result) {
        return realNumber - (float) result;
    }

    private MathUtils() {
    }
}
