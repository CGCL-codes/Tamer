package jfun.yan.element;

import java.util.Set;
import jfun.util.Misc;

/**
 * This implementation stores component instances into a java.util.Set.
 * <p>
 * @author Ben Yu
 * Nov 12, 2005 2:29:12 PM
 */
public class SetStore implements ElementStore {

    private final Set set;

    /**
   * Get the Set object storing the component instances.
   */
    public Set getSet() {
        return set;
    }

    /**
   * Create a SetStore object.
   * @param set the set to store component instances.
   */
    public SetStore(Set set) {
        this.set = set;
    }

    public void storeElement(int ind, Object obj) {
        set.add(obj);
    }

    public boolean equals(Object obj) {
        if (obj instanceof SetStore) {
            final SetStore other = (SetStore) obj;
            return set == other.set;
        } else return false;
    }

    public int hashCode() {
        return System.identityHashCode(set);
    }

    public String toString() {
        return Misc.getTypeName(set.getClass());
    }
}
