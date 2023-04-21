package royere.cwi.structure;

/** Selector provides a way to select an object.
 *  Used by Traversal and Criteria object types. */
public interface Selector {

    public boolean meetsCriteria(Object theObject);
}
