package weiss.util;

/**
 * Set interface.
 */
public interface Set extends Collection {

    /**
     * This method is not part of standard Java 1.2.
     * Like contains, it checks if x is in the set.
     * If it is, it returns the reference to the matching
     * object; otherwise it returns null.
     * @param x the object to search for.
     * @return if contains(x) is false, the return value is null;
     * otherwise, the return value is the object that causes
     * contains(x) to return true.
     */
    Object getMatch(Object x);
}
