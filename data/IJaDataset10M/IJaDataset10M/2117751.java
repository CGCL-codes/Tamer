package net.grinder.util;

/**
 * A set that associates an number with each of member. Objects added to the set
 * are allocated the lowest available number, starting at 0.
 *
 * @author Philip Aston
 * @version $Revision:$
 */
public interface AllocateLowestNumber {

    /**
   * Add a new object. If the object already belongs to the set, the existing
   * associated number is returned.
   *
   * @param o
   *            The object.
   * @return The associated number.
   */
    int add(Object o);

    /**
   * Remove an object from the set. The number previously associated with
   * the object (if any) is freed for re-use.
   *
   * @param o The object.
   */
    void remove(Object o);

    /**
   * Call <code>iteratorCallback</code> for each member of the set.
   *
   * @param iteratorCallback Called for each member of the set.
   */
    void forEach(IteratorCallback iteratorCallback);

    /**
   * Iteration callback, see {@link AllocateLowestNumber#forEach}.
   */
    interface IteratorCallback {

        /**
     * Called for a member of the set.
     *
     * @param object The object.
     * @param number The associated number.
     */
        void objectAndNumber(Object object, int number);
    }
}
