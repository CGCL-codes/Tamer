package org.timepedia.chronoscope.client.util.junit;

/**
 * Factory for creating test objects for use by {@link ObjectSmokeTest}.
 * 
 * @author
 */
public interface TestObjectFactory {

    /**
   * Creates instances of the class-to-be-tested. The following assertions
   * should hold:
   * <ul>
   * <li> For any valid index i, repeated calls to <tt>getInstance(i)</tt>
   * should return new instances that are all logically equal to one another
   * <li> For all valid index pairs (i,j) where i != j, <tt>getInstance(i)</tt>
   * and <tt>getInstance(j)</tt> should return objects that are NOT logically
   * equal.
   * </ul>
   * 
   * @param index - a value in the range [0 .. {@link #instanceCount()} - 1]
   */
    Object getInstance(int index);

    /**
   * Returns the number of unique test instances that {@link #getInstance(int)}
   * is capable of generating.
   */
    int instanceCount();
}
