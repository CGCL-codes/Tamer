package org.htmlparser.util.sort;

/**
 * Provides a mechanism to abstract the sort process.
 * Classes implementing this interface are collections of Ordered objects
 * that are to be sorted by the Sort class and are
 * not necessarily Vectors or Arrays of Ordered objects.
 * @see Sort
 */
public interface Sortable {

    /**
     * Returns the first index of the Sortable.
     * @return The index of the first element.
     */
    public int first();

    /**
     * Returns the last index of the Sortable.
     * @return The index of the last element.
     * If this were an array object this would be (object.length - 1).
     */
    public int last();

    /**
     * Fetch the object at the given index.
     * @param index The item number to get.
     * @param reuse If this argument is not null, it is an object
     * acquired from a previous fetch that is no longer needed and
     * may be returned as the result if it makes mores sense to alter
     * and return it than to fetch or create a new element. That is, the
     * reuse object is garbage and may be used to avoid allocating a new
     * object if that would normally be the strategy.
     * @return The Ordered object at that index.
     */
    public Ordered fetch(int index, Ordered reuse);

    /**
     * Swaps the elements at the given indicies.
     * @param i One index.
     * @param j The other index.
     */
    public void swap(int i, int j);
}
