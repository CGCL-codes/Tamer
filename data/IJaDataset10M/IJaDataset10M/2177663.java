package org.fest.assertions;

import static org.fest.assertions.PrimitiveFail.*;

/**
 * Understands assertion methods for <code>byte</code>s. To create a new instance of this class use the
 * method <code>{@link Assertions#assertThat(byte)}</code>.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 */
public final class ByteAssert extends PrimitiveAssert {

    private final byte actual;

    private static final byte ZERO = (byte) 0;

    ByteAssert(byte actual) {
        this.actual = actual;
    }

    /**
   * Sets the description of the actual value, to be used in as message of any <code>{@link AssertionError}</code>
   * thrown when an assertion fails. This method should be called before any assertion method, otherwise any assertion
   * failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(value).<strong>as</strong>(&quot;Some value&quot;).isEqualTo(otherValue);
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ByteAssert as(String description) {
        description(description);
        return this;
    }

    /**
   * Alternative to <code>{@link #as(String)}</code>, since "as" is a keyword in
   * <a href="http://groovy.codehaus.org/" target="_blank">Groovy</a>. This method should be called before any assertion
   * method, otherwise any assertion failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(value).<strong>describedAs</strong>(&quot;Some value&quot;).isEqualTo(otherValue);
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ByteAssert describedAs(String description) {
        return as(description);
    }

    /**
   * Sets the description of the actual value, to be used in as message of any <code>{@link AssertionError}</code>
   * thrown when an assertion fails. This method should be called before any assertion method, otherwise any assertion
   * failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(value).<strong>as</strong>(new BasicDescription(&quot;Some value&quot;)).isEqualTo(otherValue);
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ByteAssert as(Description description) {
        description(description);
        return this;
    }

    /**
   * Alternative to <code>{@link #as(Description)}</code>, since "as" is a keyword in
   * <a href="http://groovy.codehaus.org/" target="_blank">Groovy</a>. This method should be called before any assertion
   * method, otherwise any assertion failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(value).<strong>describedAs</strong>(new BasicDescription(&quot;Some value&quot;)).isEqualTo(otherValue);
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ByteAssert describedAs(Description description) {
        return as(description);
    }

    /**
   * Verifies that the actual <code>byte</code> value is equal to the given one.
   * @param expected the value to compare the actual one to.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not equal to the given one.
   */
    public ByteAssert isEqualTo(byte expected) {
        failIfNotEqual(description(), actual, expected);
        return this;
    }

    /**
   * Verifies that the actual <code>byte</code> value is not equal to the given one.
   * @param value the value to compare the actual one to.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is equal to the given one.
   */
    public ByteAssert isNotEqualTo(byte value) {
        failIfEqual(description(), actual, value);
        return this;
    }

    /**
   * Verifies that the actual <code>byte</code> value is greater than the given one.
   * @param value the given value.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not greater than the given one.
   */
    public ByteAssert isGreaterThan(byte value) {
        failIfNotGreaterThan(description(), actual, value);
        return this;
    }

    /**
   * Verifies that the actual <code>byte</code> value is less than the given one.
   * @param value the given value.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not less than the given one.
   */
    public ByteAssert isLessThan(byte value) {
        failIfNotLessThan(description(), actual, value);
        return this;
    }

    /**
   * Verifies that the actual <code>byte</code> value is greater or equal to the given one.
   * @param value the given value.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not greater than or equal to the given one.
   */
    public ByteAssert isGreaterThanOrEqualTo(byte value) {
        failIfNotGreaterThanOrEqualTo(description(), actual, value);
        return this;
    }

    /**
   * Verifies that the actual <code>byte</code> value is less or equal to the given one.
   * @param value the given value.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not less than or equal to the given one.
   */
    public ByteAssert isLessThanOrEqualTo(byte value) {
        failIfNotLessThanOrEqualTo(description(), actual, value);
        return this;
    }

    /**
   * Verifies that the actual <code>byte</code> value is positive.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not positive.
   */
    public ByteAssert isPositive() {
        return isGreaterThan(ZERO);
    }

    /**
   * Verifies that the actual <code>byte</code> value is negative.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not negative.
   */
    public ByteAssert isNegative() {
        return isLessThan(ZERO);
    }

    /**
   * Verifies that the actual <code>byte</code> value is equal to zero.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>byte</code> value is not equal to zero.
   */
    public ByteAssert isZero() {
        return isEqualTo(ZERO);
    }
}
