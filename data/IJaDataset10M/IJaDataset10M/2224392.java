package edu.ucla.sspace.vector;

/**
 * An Integer based Vector.  Methods which modify the state of a {@code Vector}
 * are optional.  Implementations that are not modifiable should throw an {@code
 * UnsupportedOperationException} if such methods are called.  These methods are
 * marked as "optional" in the specification for the interface.
 *
 * @author Keith Stevens
 */
public interface IntegerVector extends Vector<Integer> {

    /**
     * Changes the value in the vector by a specified amount (optional
     * operation).  If there is not a value set at index, delta should be set to
     * the actual value.
     *
     * @param index index to change.
     * @param delta the amount to change by.
     * @return the resulting value at the index
     */
    int add(int index, int delta);

    /**
     * Returns the value of this vector at the given index.
     *
     * @param index index to retrieve.
     * @return value at index.
     */
    int get(int index);

    /**
     * Returns the value of the vector at the given index as a {@code Integer}.
     *
     * @param {@inheritDoc}
     * @return {@inheritDoc}
     */
    Integer getValue(int index);

    /**
     * Sets the value in this vector (optional operation).
     *
     * @param index index to set.
     * @param value value to set in the vector.
     */
    void set(int index, int value);

    /**
     * Returns an {@code int} array representing this vector.  The returned
     * array will be "safe" in that no changes to the array will be reflected in
     * the vector, and likewise for changes to to the vector.  The caller is
     * thus free to modify the returned array.
     *
     * @return an {@code int} array of this vector.
     */
    int[] toArray();
}
