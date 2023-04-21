package com.myjavatools.lib.foundation;

import java.util.AbstractList;
import java.util.List;

/**
 * RangeList is a class that represents a list of values in a range.
 * It is immutable.
 *
 * @version 5.0, 11/30/04
 *
 * @see List
 * @since 5.0
 */
public class RangeList {

    /**
     * Creates a list of integers
     * @param from int
     * @param to int
     * @param step int
     * @return List that "contains" integers from
     * <code>from</code> to <code>to</to> with a step <code>step</step>
     */
    public static List<Integer> rangeList(final int from, final int to, final int step) {
        return new AbstractList<Integer>() {

            private final int sign = step < 0 ? -1 : 1;

            private final int size = step == 0 ? 0 : (to - from + step - sign) / step < 0 ? 0 : (to - from + step - sign) / step;

            @Override
            public Integer get(final int index) {
                if (index < size) {
                    return from + step * index;
                } else {
                    throw new IndexOutOfBoundsException("" + index + "/" + size);
                }
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    /**
   * Creates a list of integers
   * @param from integer
   * @param to int
   * @return List that "contains" integers from <code>from</code> to <code>to</to>
   */
    public static List<Integer> rangeList(final int from, final int to) {
        return rangeList(from, to, 1);
    }

    /**
   * Creates a list of doubles
   * @param from double
   * @param to double
   * @param step double
   * @return List that "contains" doubles
   * from <code>from</code> to <code>to</to> with a step <code>step</step>
   */
    public static List<Double> rangeList(final double from, final double to, final double step) {
        return new AbstractList<Double>() {

            final int size = step == 0 ? 0 : (int) ((to - from + step / 2) / step);

            @Override
            public Double get(final int index) {
                if (index < size) {
                    return from + step * index;
                } else {
                    throw new IndexOutOfBoundsException("" + index + "/" + size);
                }
            }

            @Override
            public int size() {
                return size;
            }
        };
    }
}
