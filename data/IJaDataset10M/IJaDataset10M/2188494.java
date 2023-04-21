package koala.dynamicjava.tree;

/**
 * The classes that implements this interface can contain a
 * continue statement
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/05/23
 */
public interface ContinueTarget {

    /**
     * Adds a label to this statement
     * @param label the label to add
     */
    void addLabel(String label);

    /**
     * Test whether this statement has the given label
     * @return true if this statement has the given label
     */
    boolean hasLabel(String label);
}
