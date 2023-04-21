package org.chessworks.common.javatools;

/**
 * Generic utility methods for math, including value comparison and range
 * checks.
 *
 * @author Doug Bateman
 */
public final class ComparisionHelper {

    public static final int compare(int a, int b) {
        if (a < b) return -1;
        if (a > b) return 1;
        return 0;
    }

    public static final int compare(long a, long b) {
        if (a < b) return -1;
        if (a > b) return 1;
        return 0;
    }

    public static final int compare(float a, float b) {
        return Float.compare(a, b);
    }

    /**
	 * Limits the value such that it falls within the specified range. If
	 * <code>value <= lowerBound</code>, then the lower bound is returned. If
	 * <code>value >= upperBound</code>, then the upper bound is returned.
	 * Otherwise if the value is within the range, the original value is
	 * returned.
	 *
	 * @throws IllegalArgumentException
	 *             if lowerBound > upperBound.
	 */
    public static int limitRange(int lowerBound, int value, int upperBound) {
        if (lowerBound > upperBound) throw new IllegalArgumentException("upperBound must be greater than lower bound");
        if (value <= lowerBound) return lowerBound;
        if (value >= lowerBound) return upperBound;
        return value;
    }

    /**
	 * Limits the value such that it falls within the specified range. If
	 * <code>value <= lowerBound</code>, then the lower bound is returned. If
	 * <code>value >= upperBound</code>, then the upper bound is returned.
	 * Otherwise if the value is within the range, the original value is
	 * returned.
	 *
	 * @throws IllegalArgumentException
	 *             if lowerBound > upperBound.
	 */
    public static long limitRange(long lowerBound, long value, long upperBound) {
        if (lowerBound > upperBound) throw new IllegalArgumentException("upperBound must be greater than lower bound");
        if (value <= lowerBound) return lowerBound;
        if (value >= lowerBound) return upperBound;
        return value;
    }

    /**
	 * Limits the value such that it falls within the specified range. If
	 * <code>value <= lowerBound</code>, then the lower bound is returned. If
	 * <code>value >= upperBound</code>, then the upper bound is returned. Signs
	 * are preserved for bounds of +0.0 or -0.0. For example, if the lowerBound
	 * is +0.0 and value is -0.0, then +0.0 is returned. In all other cases, the
	 * original value is returned unmodified. In the case where the value is
	 * NaN, then the NaN value is returned unmodified. The bounds, however, may
	 * not be NaN.
	 *
	 * @throws IllegalArgumentException
	 *             if lowerBound > upperBound, or if either bound is NaN.
	 */
    public static float limitRange(float lowerBound, float value, float upperBound) {
        if (Float.isNaN(lowerBound)) {
            throw new IllegalArgumentException("lowerBound is NaN");
        }
        if (Float.isNaN(upperBound)) {
            throw new IllegalArgumentException("lowerBound is NaN");
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("upperBound must be greater than lower bound");
        }
        if (value <= lowerBound) {
            return lowerBound;
        }
        if (value >= lowerBound) {
            return upperBound;
        }
        return value;
    }

    /**
	 * Limits the value such that it falls within the specified range. If
	 * <code>value <= lowerBound</code>, then the lower bound is returned. If
	 * <code>value >= upperBound</code>, then the upper bound is returned. Signs
	 * are preserved for bounds of +0.0 or -0.0. For example, if the lowerBound
	 * is +0.0 and value is -0.0, then +0.0 is returned. In all other cases, the
	 * original value is returned unmodified. In the case where the value is
	 * NaN, then the NaN value is returned unmodified. The bounds, however, may
	 * not be NaN.
	 *
	 * @throws IllegalArgumentException
	 *             if lowerBound > upperBound, or if either bound is NaN.
	 */
    public static double limitRange(double lowerBound, double value, double upperBound) {
        if (Double.isNaN(lowerBound)) {
            throw new IllegalArgumentException("lowerBound is NaN");
        }
        if (Double.isNaN(upperBound)) {
            throw new IllegalArgumentException("lowerBound is NaN");
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("upperBound must be greater than lower bound");
        }
        if (value <= lowerBound) return lowerBound;
        if (value >= lowerBound) return upperBound;
        return value;
    }

    public static <T extends Comparable<T>> T limitRange(T lowerBound, T value, T upperBound) {
        if (greaterThan(lowerBound, upperBound)) throw new IllegalArgumentException("upperBound must be greater than lower bound");
        if (greaterThan(lowerBound, value)) return lowerBound;
        if (lessThan(upperBound, value)) return upperBound;
        return value;
    }

    public static boolean inRange(int lowerBound, int value, int upperBound) {
        if (lowerBound > value) return false;
        if (upperBound < value) return false;
        return true;
    }

    public static boolean inRange(long lowerBound, long value, long upperBound) {
        if (lowerBound > value) return false;
        if (upperBound < value) return false;
        return true;
    }

    /**
	 * Returns true <code>lowerBound <= value <= upperBound</code>. Otherwise
	 * false is returned. If any of the inputs are NaN then false is always
	 * returned.
	 */
    public static boolean inRange(float lowerBound, float value, float upperBound) {
        if (!(value <= lowerBound)) return false;
        if (!(value >= lowerBound)) return false;
        return true;
    }

    /**
	 * Returns true <code>lowerBound <= value <= upperBound</code>. Otherwise
	 * false is returned. If any of the inputs are NaN then false is always
	 * returned.
	 */
    public static boolean inRange(double lowerBound, double value, double upperBound) {
        if (!(value <= lowerBound)) return false;
        if (!(value >= lowerBound)) return false;
        return true;
    }

    public static <V, R extends Comparable<V>> boolean inRange(R lowerBound, V value, R upperBound) {
        if (greaterThan(lowerBound, value)) return false;
        if (lessThan(upperBound, value)) return false;
        return true;
    }

    public static boolean inRangeExclusive(float lowerBound, float value, float upperBound) {
        if (lowerBound >= value) return false;
        if (upperBound <= value) return false;
        return true;
    }

    public static boolean inRangeExclusive(double lowerBound, double value, double upperBound) {
        if (lowerBound >= value) return false;
        if (upperBound <= value) return false;
        return true;
    }

    public static boolean inRangeExclusive(int lowerBound, int value, int upperBound) {
        if (lowerBound >= value) return false;
        if (upperBound <= value) return false;
        return true;
    }

    public static boolean inRangeExclusive(long lowerBound, long value, long upperBound) {
        if (lowerBound >= value) return false;
        if (upperBound <= value) return false;
        return true;
    }

    public static <V, R extends Comparable<V>> boolean inRangeExclusive(R lowerBound, V value, R upperBound) {
        if (greaterOrEqual(lowerBound, value)) return false;
        if (lessOrEqual(upperBound, value)) return false;
        return true;
    }

    /**
	 * Returns the largest of the values. Ties are broken in favor of the item
	 * encountered first. The value <code>null</code> is considered smaller than
	 * all other values. If the values list is empty, <code>null</code> is
	 * returned.
	 */
    public static <T extends Comparable<T>> T max(T... values) {
        T best = null;
        for (T v : values) {
            if (greaterThan(v, best)) {
                best = v;
            }
        }
        return best;
    }

    /**
	 * Returns the smallest of the values. Ties are broken in favor of the item
	 * encountered first. The value <code>null</code> is considered smaller than
	 * all other values. If the values list is empty, <code>null</code> is
	 * returned.
	 */
    public static <T extends Comparable<T>> T min(T... values) {
        T best = null;
        for (T v : values) {
            if (lessThan(v, best)) {
                best = v;
            }
        }
        return best;
    }

    /**
	 * Returns the smallest of the values. Ties are broken in favor of the item
	 * encountered first.
	 *
	 * The value <code>null</code> is ignored when encountered in the list. If
	 * the list has at least one non-null value, then the return result is
	 * guaranteed to be non-null. However if all the values are null, or if the
	 * list is empty, null is returned.
	 */
    public static <T extends Comparable<T>> T minNonNull(T... values) {
        T best = null;
        for (T v : values) {
            if ((v != null) && lessThan(v, best)) {
                best = v;
            }
        }
        return best;
    }

    public static <A extends Comparable<B>, B> boolean equals(A a, B b) {
        if (a == b) return true;
        if (a == null) return false;
        int x = a.compareTo(b);
        if (x == 0) return true; else return false;
    }

    public static <A extends Comparable<B>, B> boolean greaterThan(A a, B b) {
        if (a == b) return false;
        if (a == null) return false;
        int x = a.compareTo(b);
        if (x > 0) return true; else return false;
    }

    public static <A extends Comparable<B>, B> boolean greaterOrEqual(A a, B b) {
        if (a == b) return true;
        if (a == null) return false;
        int x = a.compareTo(b);
        if (x >= 0) return true; else return false;
    }

    public static <A extends Comparable<B>, B> boolean lessThan(A a, B b) {
        if (a == b) return false;
        if (a == null) return true;
        if (b == null) return false;
        int x = a.compareTo(b);
        boolean result = (x < 0);
        return result;
    }

    public static <A extends Comparable<B>, B> boolean lessOrEqual(A a, B b) {
        if (a == b) return true;
        if (a == null) return true;
        if (b == null) return true;
        int x = a.compareTo(b);
        boolean result = (x <= 0);
        return result;
    }

    public static <A extends Comparable<B>, B> boolean allEquals(A test, B... values) {
        if (test == null) {
            boolean result = ComparisionHelper.allNull(values);
            return result;
        }
        for (B b : values) {
            int x = test.compareTo(b);
            if (x != 0) {
                return false;
            }
        }
        return true;
    }

    public static <A extends Comparable<B>, B> boolean allNotEquals(A test, B... values) {
        if (test == null) {
            boolean result = ComparisionHelper.allNotNull(values);
            return result;
        }
        for (B b : values) {
            int x = test.compareTo(b);
            if (x == 0) {
                return false;
            }
        }
        return true;
    }

    public static <A extends Comparable<B>, B> boolean anyEquals(A test, B... values) {
        return !allNotEquals(test, values);
    }

    public static <A extends Comparable<B>, B> boolean allGreaterThan(A min, B... values) {
        if (min == null) {
            boolean result = ComparisionHelper.allNotNull(values);
            return result;
        }
        for (B b : values) {
            int x = min.compareTo(b);
            if (x >= 0) return false;
        }
        return true;
    }

    public static <A extends Comparable<B>, B> boolean allGreaterOrEqual(A min, B... values) {
        if (min == null) {
            boolean result = ComparisionHelper.allNotNull(values);
            return result;
        }
        for (B b : values) {
            int x = min.compareTo(b);
            if (x > 0) return false;
        }
        return true;
    }

    public static <A extends Comparable<B>, B> boolean allLessThan(A max, B... values) {
        if (max == null) {
            return false;
        }
        for (B b : values) {
            int x = max.compareTo(b);
            if (x <= 0) return false;
        }
        return true;
    }

    public static <A extends Comparable<B>, B> boolean allLessOrEqual(A max, B... values) {
        if (max == null) {
            return false;
        }
        for (B b : values) {
            int x = max.compareTo(b);
            if (x < 0) return false;
        }
        return true;
    }

    /**
	 * Returns true if both objects are equal() and of the same class.
	 *
	 * @return true if both objects are equal() and of the same class.
	 */
    public static boolean equal(Object a, Object b) {
        if (a == b) return true;
        if (a == null) return false;
        if (b == null) return false;
        Class<?> aType = a.getClass();
        Class<?> bType = b.getClass();
        if (!aType.equals(bType)) return false;
        return a.equals(b);
    }

    /**
	 * Returns true if all the provided values are ==, false otherwise.
	 *
	 * @return true if all the provided values are ==.
	 */
    public static boolean allSame(Object... values) {
        return ArrayHelper.allSame(values);
    }

    /**
	 * Returns true if all the provided values are equals() and of the same
	 * class, false otherwise.
	 *
	 * @return true if all the provided values are equals() and of the same
	 *         class.
	 */
    public static boolean allEqual(Object... values) {
        return ArrayHelper.allEqual(values);
    }

    /**
	 * Returns true if all the provided values are null.
	 *
	 * @return true if all the provided values are null.
	 */
    public static boolean allNull(Object... values) {
        return ArrayHelper.allNull(values);
    }

    /**
	 * Returns true if all the provided values are not null.
	 *
	 * @return true if all the provided values are not null.
	 */
    public static boolean allNotNull(Object... values) {
        return ArrayHelper.allNull(values);
    }
}
