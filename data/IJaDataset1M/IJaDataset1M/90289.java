package org.josef.science.math;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.josef.annotations.Status.UnitTests.ABSENT;
import static org.josef.annotations.ThreadSafety.ThreadSafetyLevel.IMMUTABLE;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.annotations.ThreadSafety;
import org.josef.util.CDebug;

/**
 * This class wraps a primitive long value.
 * <br>The main difference between this class and class {@link Long} is that
 * this class has support for arithmetic operations that protect against both
 * underflow and overflow.<br>
 * Design considerations: Class {@link Long} is final so this class extends
 * class {@link Number} instead. This class is immutable and contains private
 * constructors in combination with static factory methods as suggested by
 * Joshua Bloch (Effective Java, Second Edition, item 15).
 * @author Kees Schotanus
 * @version 2.0 $Revision: 2840 $
 */
@Status(stage = PRODUCTION, unitTests = ABSENT)
@Review(by = "Kees Schotanus", at = "2009-09-28")
@ThreadSafety(level = IMMUTABLE)
public final class CLong extends Number implements Comparable<CLong> {

    /**
     * The maximum ordinal for which the factorial can be calculated.
     */
    public static final int MAX_FACTORIAL_ORDINAL = 20;

    /**
     * The maximum ordinal for which the Fibonacci number can be calculated.
     */
    public static final int MAX_FIBONACCI_ORDINAL = 92;

    /**
     * Serial Version UID for this class.
     */
    private static final long serialVersionUID = 7632960530518590116L;

    /**
     * Standard message used by all arithmetic methods.
     */
    private static final String UNDER_OR_OVERFLOW_MESSAGE = "Arithmetic underflow or overflow caused by: %d %s %d.";

    /**
     * The wrapped long value.
     * @serial
     */
    private final long value;

    /**
     * Constructs this CLong from the supplied primitive long value.
     * @param value Primitive long value.
     */
    private CLong(final long value) {
        this.value = value;
    }

    /**
     * Returns the value of this CLong as a byte.
     * @return The value of this CLong as a byte.
     */
    @Override
    public byte byteValue() {
        return (byte) value;
    }

    /**
     * Returns the value of this CLong as a short.
     * @return The value of this CLong as a short.
     */
    @Override
    public short shortValue() {
        return (short) value;
    }

    /**
     * Returns the value of this CLong as an integer.
     * @return The value of this CLong as an integer.
     */
    @Override
    public int intValue() {
        return (int) value;
    }

    /**
     * Returns the value of this CLong as a long.
     * @return The value of this CLong as a long.
     */
    @Override
    public long longValue() {
        return value;
    }

    /**
     * Returns the value of this CLong as a float.
     * @return The value of this CLong as a float.
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this CLong as a double.
     * @return The value of this CLong as a double.
     */
    @Override
    public double doubleValue() {
        return value;
    }

    /**
     * Returns a CLong object holding the value of the supplied value.
     * @param value The long value.
     * @return A CLong object holding the value of the supplied value.
     */
    public static CLong valueOf(final int value) {
        return new CLong(value);
    }

    /**
     * Returns a CLong object holding the value of the supplied value.
     * @param value The long value.
     * @return A CLong object holding the value of the supplied value.
     */
    public static CLong valueOf(final long value) {
        return new CLong(value);
    }

    /**
     * Returns a CLong object holding the value of the supplied value.
     * <br>Depending on the type of the supplied value this may involve
     * rounding. Technically the method {@link Number#longValue()} is used to
     * perform the conversion.
     * @param value The Number that holds the value.
     * @return A CLong object holding the value of the supplied value.
     *  <br>When the supplied value is Not A Number, 0 is returned.
     * @throws NullPointerException When the supplied value is null.
     */
    public static CLong valueOf(final Number value) {
        CDebug.checkParameterNotNull(value, "value");
        return new CLong(value.longValue());
    }

    /**
     * Returns a CLong object holding the parsed value of the supplied value.
     * @param value The String that is parsed.
     * @return A CLong object holding the value of the supplied value, after
     *  parsing.
     * @throws IllegalArgumentException When the supplied value (after parsing)
     *  is not in the range:
     *  [{@link Long#MIN_VALUE} to {@link Long#MAX_VALUE}].
     * @throws NumberFormatException When the supplied value does not contain a
     *  parsable long (this includes a null or empty value).
     * @see Long#parseLong(String)
     */
    public static CLong valueOf(final String value) {
        return valueOf(Long.parseLong(value));
    }

    /**
     * Determines whether this object and the supplied object are the same.
     * <br>The two objects are the same if the supplied object is not null and
     * the supplied object is a CLong and the stored value in both objects is
     * the same.
     * @param object The CLong object to compare with.
     * @return True if both objects are the same, false otherwise.
     */
    @Override
    public boolean equals(final Object object) {
        return object instanceof CLong && this.value == ((CLong) object).value;
    }

    /**
     * Computes a hash code for this object using the same algorithm as
     * {@link java.lang.Long#hashCode()} does.
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return (int) (value ^ (value >> Integer.SIZE));
    }

    /**
     * Compares two CLong objects numerically.
     * @param object CLong object to compare with this object.
     * @return -1 when this < object, 0 when this == object, 1 when this >
     *  object.
     * @throws NullPointerException When the supplied object is null.
     * @see Comparable
     */
    public int compareTo(final CLong object) {
        return value < object.value ? -1 : value == object.value ? 0 : 1;
    }

    /**
     * Adds the supplied value to this value.
     * <br>This method allows for expressions like:
     * CLong value = i.add(x).multiply(y) where i is a CLong and x and y
     * are primitive longs.
     * @param value Value to add to the stored value.
     * @return Result of the addition in a new CLong.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     * @see #add(long, long)
     */
    public CLong add(final long value) {
        return new CLong(add(this.value, value));
    }

    /**
     * Adds y to x.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the addition.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     */
    public static long add(final long x, final long y) {
        final long result = x + y;
        if ((x > 0 && y > 0 && result < 0) || (x < 0 && y < 0 && result > 0)) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "+", y));
        }
        return result;
    }

    /**
     * Subtracts the supplied value from this value.
     * <br>This method allows for expressions like:
     * CLong value = i.subtract(x).multiply(y) where i is a CLong and x
     * and y are primitive integers.
     * @param value Value to subtract from the stored value.
     * @return Result of the subtraction in a new CLong.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     * @see #subtract(long, long)
     */
    public CLong subtract(final long value) {
        return new CLong(subtract(this.value, value));
    }

    /**
     * Subtracts y from x.
     * @param x Operand one.
     * @param y operand two.
     * @return Result of the subtraction.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     */
    public static long subtract(final long x, final long y) {
        final long result = x - y;
        if ((x > 0 && y < 0 && result < 0) || (x < 0 && y > 0 && result > 0)) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "-", y));
        }
        return result;
    }

    /**
     * Multiplies this value by the supplied value.
     * <br>This method allows for expressions like:
     * CLong value = i.multiply(x).subtract(y) where i is a CLong and x
     * and y are primitive longs.
     * @param value Value to multiply by the stored value.
     * @return Result of the multiplication in a new CLong.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     * @see #multiply(long, long)
     */
    public CLong multiply(final long value) {
        return new CLong(multiply(this.value, value));
    }

    /**
     * Multiplies x by y.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the multiplication.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     */
    public static long multiply(final long x, final long y) {
        final long result = x * y;
        if (y != 0 && result / y != x) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "*", y));
        }
        return result;
    }

    /**
     * Divides this value by the supplied value.
     * <br>This method allows for expressions like:
     * CLong value = i.divide(x).add(y) where i is a CLong and x and y are
     * primitive longs.
     * @param value Value by which this value will be divided.
     * @return Result of the division in a new CLong.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied value is 0.
     * @see #divide(long, long)
     */
    public CLong divide(final long value) {
        return new CLong(divide(this.value, value));
    }

    /**
     * Divides x by y.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the division.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied y value is 0.
     */
    public static long divide(final long x, final long y) {
        final long result = x / y;
        if (x == Long.MIN_VALUE && y == -1L) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "/", y));
        }
        return result;
    }

    /**
     * Divides this value by the supplied value and floors the result.
     * <br>This method does not work like integer division in Java. In Java
     * -1 / 4 results in 0 but this method returns a value of -1. It works as if
     * the result of the standard Java division is floored and then returned.
     * @param value Value by which this value will be divided.
     * @return Result of the division in a new CLong.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied value is 0.
     * @see #divideAndFloor(long, long)
     */
    public CLong divideAndFloor(final long value) {
        return new CLong(divideAndFloor(this.value, value));
    }

    /**
     * Divides x by y and floors the result.
     * <br>This method does not work like integer division in Java. In Java
     * -1 / 4 results in 0 but this method returns a value of -1. It works as if
     * the result of the standard Java division is floored and then returned.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the division.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied y value is 0.
     */
    public static long divideAndFloor(final long x, final long y) {
        long result = x / y;
        if (x == Long.MIN_VALUE && y == -1L) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "/", y));
        }
        if (x * y < 0 && x % y != 0) {
            --result;
        }
        return result;
    }

    /**
     * Computes the greatest common divisor of two long values.
     * <br>Given two integers a and b, a common divisor of a and b is an integer
     * which divides both numbers, and the greatest common divisor is the
     * largest such integer.<br>
     * The algorithm used is the one by Euclid found in Book VII of Euclid's
     * 'Elements'.<br>
     * Source: Sourcebook for programmable calculators. ISBN: 0-89512-025-9
     * @param first The first value.
     * @param second The second value.
     * @return Greatest common divisor of the supplied values.
     * @throws IllegalArgumentException When the supplied second parameter is 0.
     */
    public static long greatestCommonDivisor(final long first, final long second) {
        if (second == 0) {
            throw new IllegalArgumentException("Second value can not be 0!");
        }
        long a = first;
        long b = second;
        long r = a % b;
        while (r != 0) {
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }

    /**
     * Computes the least common multiple of two long values.
     * <br> The least common multiple of a and b is the smallest integer that is
     * divisible by both a and b. The least common multiple (lcm) can easily be
     * computed if the greatest common divisor (gcd) of a and b is known
     * (lcm = a*b/gcd).
     * @param a The first long value.
     * @param b The second long value.
     * @return Least common multiple of the supplied values a and b.
     */
    public static long leastCommonMultiple(final long a, final long b) {
        return a * b / greatestCommonDivisor(a, b);
    }

    /**
     * Determines whether the supplied value is a prime.
     * <br>A number is a prime if the number can only be divided by 1 and by
     * itself. By definition 0 and 1 are not primes.<br>
     * You can check whether a number is a prime by dividing the number by all
     * values from 2 up to and including the square root of the number. If none
     * of these divisions results in an integer the number is a prime.<br>
     * The only optimization made here is that the number is only divided by
     * odd numbers from 3 up. In theory this would be twice as fast compared to
     * having no optimization at all but still this method is quite slow for
     * large long values!<br>
     * Did you know that for every n >= 2 at least one prime exists between
     * n and 2 times n?
     * @param value Value to check whether it is a prime number or not.
     * @return True when the supplied value is a prime, false otherwise.
     */
    public static boolean isPrime(final long value) {
        if (value == 2L) {
            return true;
        } else if (value < 2 || value % 2 == 0) {
            return false;
        }
        final long three = 3;
        final long root = (long) Math.sqrt(value);
        for (long i = three; i <= root; i += 2) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Computes the factorial of the supplied value.
     * <br>The factorial of 0 is 1 and for all other positive numbers n it is
     * defined as: 1x2x3x...n<br>
     * This method is often implemented using recursion:<br>
     * <pre><code>
     * public static long factorialRecursive(final long value) {
     *     if (value <= 1) {
     *         return 1;
     *     } else {
     *         return value * factorialRecursive(value - 1);
     *     }
     * }
     * </code></pre>
     * If you check out the actual implementation you will see that not using
     * recursion is better in this case. The current implementation does not
     * have the overhead of method calls and only has to check for a wrong
     * supplied value, once (the implementation above does not check for errors,
     * but if it did, it would do so on every recursive call).
     * @param value Value for which the factorial is calculated.
     * @return Factorial of the supplied value.
     * @throws IllegalArgumentException When supplied value is not in range:
     *  0 <= value <= {@link CLong#MAX_FIBONACCI_ORDINAL}.
     */
    public static long factorial(final long value) {
        if (value < 0 || value > MAX_FACTORIAL_ORDINAL) {
            throw new IllegalArgumentException(String.format("Value: %d, not in the range [%d,%d]", value, 0, MAX_FACTORIAL_ORDINAL));
        }
        long factorial = 1L;
        for (long i = 2L; i <= value; i++) {
            factorial *= i;
        }
        return factorial;
    }

    /**
     * Computes fibonacci number x.
     * <br>The sequence of fibonacci numbers goes like this:<br>
     * <pre><code>
     * #0:  0 By definition
     * #1:  1 By definition
     * #2:  1 For all others it is the sum of the previous two numbers.
     * #3:  2
     * #4:  3
     * #5:  5
     * #6:  8
     * #7: 13
     * #8: 21
     * #9: 34
     * </code></pre>
     * @param x The fibonacci number to compute.
     * @return Fibonacci number x.
     * @throws IllegalArgumentException When the supplied x value is &lt; 0.
     * @throws ArithmeticException When the supplied x value is larger than
     *  {@link #MAX_FIBONACCI_ORDINAL} since the result would not fit into a
     *  long.
     */
    public static long fibonacci(final int x) {
        CDebug.checkParameterTrue(x >= 0, "x should be non-negative but is: " + x);
        if (x > MAX_FIBONACCI_ORDINAL) {
            throw new ArithmeticException(String.format("fibonacci(%d) would overflow!", x));
        }
        long fibonacci;
        if (x == 0) {
            fibonacci = 0;
        } else if (x == 1) {
            fibonacci = 1;
        } else {
            long fibonacciLeft = 0;
            long fibonacciRight = 1;
            fibonacci = 0;
            for (int i = 2; i <= x; ++i) {
                fibonacci = fibonacciLeft + fibonacciRight;
                fibonacciLeft = fibonacciRight;
                fibonacciRight = fibonacci;
            }
        }
        return fibonacci;
    }

    /**
     * Creates a Fibonacci Iterator that allows you to iterate over all the
     * Fibonacci numbers in sequence.
     * @return A Fibonacci Iterator.
     */
    public static FibonacciIterator fibonacciIterator() {
        return new FibonacciIterator();
    }

    /**
     * Iterator suitable for iterating over all the Fibonacci numbers in
     * sequence.
     * @author Kees Schotanus
     * @version 1.0 $Revision: 2840 $
     */
    public static class FibonacciIterator implements Iterator<Long> {

        /**
         * The next fibonacci number in sequence.
         */
        private int ordinal;

        /**
         * The previous Fibonacci number.
         */
        private long previousFibonacciNumber;

        /**
         * The current Fibonacci number.
         */
        private long currentFibonacciNumber = 1;

        /**
         * Determines whether the next Fibonacci number is available.
         * @return True, when the next Fibonacci number is available, false
         *  otherwise.
         */
        public boolean hasNext() {
            return ordinal <= MAX_FIBONACCI_ORDINAL;
        }

        /**
         * Gets the next fibonacci number in sequence.
         * @return The next fibonacci number in sequence.
         * @throws NoSuchElementException When this method is called when there
         *  are no more Fibonacci numbers.
         */
        public Long next() {
            if (hasNext()) {
                long fibonacciNumber;
                if (ordinal == 0) {
                    fibonacciNumber = 0;
                } else if (ordinal == 1) {
                    fibonacciNumber = 1;
                } else {
                    fibonacciNumber = previousFibonacciNumber + currentFibonacciNumber;
                    previousFibonacciNumber = currentFibonacciNumber;
                    currentFibonacciNumber = fibonacciNumber;
                }
                ++ordinal;
                return fibonacciNumber;
            }
            throw new NoSuchElementException("No more Fibonacci numbers available!");
        }

        /**
         * Removing a fibonacci number is unsupported.
         * @throws UnsupportedOperationException Always!
         */
        public void remove() {
            throw new UnsupportedOperationException("Removing a fibonacci number is not supported!");
        }
    }

    /**
     * Converts the stored primitive long value to a String.
     * @return String representation of the stored long value.
     */
    @Override
    public String toString() {
        return Long.toString(value);
    }
}
