package algs.model.search;

/**
 * Binary Search in Java given a pre-sorted array of the parameterized type. 
 *
 * @param T   elements of the collection being searched are of this type.
 *            The parameter T must implement Comparable.  
 * 
 * @author George Heineman
 * @version 1.0, 6/15/08
 * @since 1.0
 */
public class BinarySearch<T extends Comparable<T>> {

    /** Search for target in collection. Return true on success. */
    public boolean search(T[] collection, T target) {
        if (target == null) {
            return false;
        }
        int low = 0, high = collection.length - 1;
        while (low <= high) {
            int ix = (low + high) / 2;
            int rc = target.compareTo(collection[ix]);
            if (rc < 0) {
                high = ix - 1;
            } else if (rc > 0) {
                low = ix + 1;
            } else {
                return true;
            }
        }
        return false;
    }
}
