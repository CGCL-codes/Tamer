package org.ezvolve;

/**
 * <p>The single-objective fitness of a candidate solution. Valid fitness values
 * range from negative infinity to positive infinity.</p>
 *
 * <p><strong>Integer Values</strong></p>
 *
 * <p>The constructors take the value as a {@code double} argument, this is
 * also sufficient precision for any {@code int} value. The following
 * constructions will produce equal {@code Fitness} objects:</p>
 * 
 * <p>{@code new Fitness(100)}<br>{@code new Fitness(100.0)}</p>
 *
 * <p>For convenience, an accessor {@link #intValue()} is provided, which
 * will return the value as an {@code int}, dropping any fractional part. This
 * will throw an {@code ArithmeticException} if the whole part of the fitness
 * value is not between -2<sup>31</sup> and 2<sup>31</sup>-1. If not known to
 * be in the {@code int} range, it is best to use {@link #doubleValue()}.</p>
 *
 * @author Lee Christie
 */
public final class Fitness {

    private final double value;

    /**
     * Constructs a new {@code Fitness} of the specified value.
     *
     * @param value the fitness value of the solution, negative infinity
     *        to positive infinity
     * @throws IllegalArgumentException if {@code value} is not a number
     */
    public Fitness(double value) {
        if (!Double.isNaN(value)) {
            if (value == -0.0) {
                this.value = 0.0;
            } else {
                this.value = value;
            }
        } else {
            throw new IllegalArgumentException("value = " + value + ", expected: -Infinity to +Infinity");
        }
    }

    /**
     * Returns the fitness value of the solution as a {@code double}.
     *
     * @return the fitness value of the solution, always negative infinity
     *         to positive infinity
     * @see #intValue()
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Compares the specified object with this fitness for equality. Returns
     * {@code true} if and only if the specified object is a fitness
     * representing an equal value to this fitness. This method conforms to the
     * general contract of {@link java.lang.Object#equals(java.lang.Object)} and
     * is consistent with {@link #hashCode()}.
     * <p>
     * Note that due to the nature of floating-point arithmetic, fitnesses
     * constructed from <em>computed</em> {@code double} values may be subject
     * to rounding errors.
     *
     * @param o the object to be compared for equality with this fitness
     * @return {@code true} if the specified object is equal to this fitness
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Fitness)) {
            return false;
        }
        final Fitness other = (Fitness) o;
        if (other.doubleValue() != doubleValue()) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this fitness. The hash code algorithm is
     * unspecified and hence may be subject to change between versions of the
     * API. This method conforms to the general contract of
     * {@link java.lang.Object#hashCode()} and is consistent with
     * {@link #equals(java.lang.Object)}.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        final long longValue = Double.doubleToLongBits(value);
        int result = 17;
        result = 31 * result + (int) (longValue ^ (longValue >>> 32));
        return result;
    }

    /**
     * Returns the fitness value of this as an <code>int</code> (by casting to
     * type {@code int}). This is normally only used if the fitness is known to
     * be an integer between -2<sup>31</sup> and 2<sup>31</sup>-1, otherwise
     * {@link #doubleValue()} should be used.
     *
     * @return the fitness value of the solution, always -2<sup>31</sup>
     *         to 2<sup>31</sup>-1
     * @see #doubleValue
     * @throws ArithmeticException if the whole part of the fitness value
     * is not between -2<sup>31</sup> and 2<sup>31</sup>-1
     */
    public int intValue() {
        if (value < ((double) (Integer.MAX_VALUE)) + 1.0 && value > ((double) (Integer.MIN_VALUE)) - 1.0) {
            return (int) value;
        } else {
            throw new ArithmeticException("cannot cast " + value + " to an int");
        }
    }

    /**
     * Returns a string representation of the fitness.
     *
     * @return a string representation of the fitness, not {@code null}
     */
    @Override
    public String toString() {
        return getClass().getName() + "[value=" + value + "]";
    }
}
