package mujmail.ordering;

/**
 * Compares mail headers. Is intended for using in sorting algorithms. 
 */
public interface Comparator {

    /**
     * Compares two objects.
     * 
     * @param o1 first Object to be compared
     * @param o2 second object to be compared
     * @return number < 1 if objects are in correct order (o1 < o2 for natural
     *         ordering), 0 if they are same or number > 0 in other cases
     * @throws IllegalArgumentException when two incomparable objects are
     *         passed as parameters
     */
    public int compare(Object o1, Object o2) throws IllegalArgumentException;
}
