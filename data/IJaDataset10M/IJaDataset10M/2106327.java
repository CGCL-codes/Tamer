package org.fest.assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.fest.assertions.Fail.*;

/**
 * Understands assertion methods for <code>boolean</code> arrays. To create a new instance of this class use the
 * method <code>{@link Assertions#assertThat(boolean[])}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class BooleanArrayAssert extends ArrayAssert<boolean[]> {

    BooleanArrayAssert(boolean... actual) {
        super(actual);
    }

    /**
   * Sets the description of the actual value, to be used in as message of any <code>{@link AssertionError}</code>
   * thrown when an assertion fails. This method should be called before any assertion method, otherwise any assertion
   * failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(values).<strong>as</strong>(&quot;Some values&quot;).isNotEmpty();
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public BooleanArrayAssert as(String description) {
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
   * assertThat(values).<strong>describedAs</strong>(&quot;Some values&quot;).isNotEmpty();
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public BooleanArrayAssert describedAs(String description) {
        return as(description);
    }

    /**
   * Sets the description of the actual value, to be used in as message of any <code>{@link AssertionError}</code>
   * thrown when an assertion fails. This method should be called before any assertion method, otherwise any assertion
   * failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(values).<strong>as</strong>(new BasicDescription(&quot;Some values&quot;)).isNotEmpty();
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public BooleanArrayAssert as(Description description) {
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
   * assertThat(values).<strong>describedAs</strong>(new BasicDescription(&quot;Some values&quot;)).isNotEmpty();
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public BooleanArrayAssert describedAs(Description description) {
        return as(description);
    }

    /**
   * Verifies that the actual <code>boolean</code> array contains the given values.
   * @param values the values to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is <code>null</code>.
   * @throws AssertionError if the actual <code>boolean</code> array does not contain the given values.
   */
    public BooleanArrayAssert contains(boolean... values) {
        isNotNull();
        assertContains(list(values));
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array contains the given values <strong>only</strong>.
   * @param values the values to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is <code>null</code>.
   * @throws AssertionError if the actual <code>boolean</code> array does not contain the given objects, or if the
   *          actual <code>boolean</code> array contains elements other than the ones specified.
   */
    public BooleanArrayAssert containsOnly(boolean... values) {
        isNotNull();
        assertContainsOnly(list(values));
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array does not contain the given values.
   * @param values the values the array should exclude.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is <code>null</code>.
   * @throws AssertionError if the actual <code>boolean</code> array contains any of the given values.
   */
    public BooleanArrayAssert excludes(boolean... values) {
        isNotNull();
        assertExcludes(list(values));
        return this;
    }

    List<Object> copyActual() {
        return list(actual);
    }

    private List<Object> list(boolean[] values) {
        List<Object> list = new ArrayList<Object>();
        for (boolean value : values) list.add(value);
        return list;
    }

    /**
   * Verifies that the actual <code>boolean</code> array satisfies the given condition.
   * @param condition the given condition.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array does not satisfy the given condition.
   * @throws IllegalArgumentException if the given condition is null.
   */
    public BooleanArrayAssert satisfies(Condition<boolean[]> condition) {
        assertSatisfies(condition);
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array does not satisfy the given condition.
   * @param condition the given condition.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array satisfies the given condition.
   * @throws IllegalArgumentException if the given condition is null.
   */
    public BooleanArrayAssert doesNotSatisfy(Condition<boolean[]> condition) {
        assertDoesNotSatisfy(condition);
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array is not <code>null</code>.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is <code>null</code>.
   */
    public BooleanArrayAssert isNotNull() {
        assertArrayNotNull();
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array contains at least on element.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is <code>null</code>.
   * @throws AssertionError if the actual <code>boolean</code> array is empty.
   */
    public BooleanArrayAssert isNotEmpty() {
        assertNotEmpty();
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array is equal to the given array. Array equality is checked by
   * <code>{@link Arrays#equals(boolean[], boolean[])}</code>.
   * @param expected the given array to compare the actual array to.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is not equal to the given one.
   */
    public BooleanArrayAssert isEqualTo(boolean[] expected) {
        if (!Arrays.equals(actual, expected)) fail(errorMessageIfNotEqual(actual, expected));
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array is not equal to the given array. Array equality is checked by
   * <code>{@link Arrays#equals(boolean[], boolean[])}</code>.
   * @param array the given array to compare the actual array to.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is equal to the given one.
   */
    public BooleanArrayAssert isNotEqualTo(boolean[] array) {
        if (Arrays.equals(actual, array)) fail(errorMessageIfEqual(actual, array));
        return this;
    }

    /**
   * Verifies that the number of elements in the actual <code>boolean</code> array is equal to the given one.
   * @param expected the expected number of elements in the actual <code>boolean</code> array.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is <code>null</code>.
   * @throws AssertionError if the number of elements in the actual <code>boolean</code> array is not equal to the given
   *          one.
   */
    public BooleanArrayAssert hasSize(int expected) {
        assertHasSize(expected);
        return this;
    }

    /**
   * Returns the number of elements in the actual <code>boolean</code> array.
   * @return the number of elements in the actual <code>boolean</code> array.
   */
    protected int actualGroupSize() {
        isNotNull();
        return actual.length;
    }

    /**
   * Verifies that the actual <code>boolean</code> array is the same as the given array.
   * @param expected the given array to compare the actual array to.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is not the same as the given one.
   */
    public BooleanArrayAssert isSameAs(boolean[] expected) {
        assertSameAs(expected);
        return this;
    }

    /**
   * Verifies that the actual <code>boolean</code> array is not the same as the given array.
   * @param expected the given array to compare the actual array to.
   * @return this assertion object.
   * @throws AssertionError if the actual <code>boolean</code> array is the same as the given one.
   */
    public BooleanArrayAssert isNotSameAs(boolean[] expected) {
        assertNotSameAs(expected);
        return this;
    }
}
