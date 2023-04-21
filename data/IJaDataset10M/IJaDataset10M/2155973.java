package org.fest.assertions;

/**
 * Understands a template for assertion methods related to arrays or collections.
 * @param <T> the type of object implementations of this template can verify.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public abstract class GroupAssert<T> extends GenericAssert<T> {

    /**
   * Creates a new <code>{@link GroupAssert}</code>.
   * @param actual the object to verify.
   */
    GroupAssert(T actual) {
        super(actual);
    }

    /**
   * Verifies that the actual group of values is <code>null</code> or empty.
   * @throws AssertionError if the actual group of values is not <code>null</code> or not empty.
   */
    abstract void isNullOrEmpty();

    /**
   * Verifies that the actual group of values is empty.
   * @throws AssertionError if the actual group of values is <code>null</code> or not empty.
   */
    abstract void isEmpty();

    /**
   * Verifies that the actual group of values contains at least on value.
   * @return this assertion object.
   * @throws AssertionError if the actual group of values is <code>null</code> or empty.
   */
    abstract GroupAssert<T> isNotEmpty();

    /**
   * Verifies that the number of values in the actual group is equal to the given one.
   * @param expected the expected number of values in the actual group.
   * @return this assertion object.
   * @throws AssertionError if the number of values of the actual group is not equal to the given one.
   */
    abstract GroupAssert<T> hasSize(int expected);

    /**
   * Returns the size of the actual group (array, collection, etc.)
   * @return the size of the actual group.
   */
    protected abstract int actualGroupSize();
}
