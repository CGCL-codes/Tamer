package jdbm.helper;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for String objects.  Delegates to String.compareTo().
 *
 * @author <a href="mailto:boisvert@intalio.com">Alex Boisvert</a>
 * @version $Id: StringComparator.java,v 1.5 2005/06/25 23:12:31 doomdark Exp $
 */
public final class StringComparator implements Comparator, Serializable {

    /**
     * Version id for serialization.
     */
    static final long serialVersionUID = 1L;

    /**
     * Compare two objects.
     *
     * @param obj1 First object
     * @param obj2 Second object
     * @return a positive integer if obj1 > obj2, 0 if obj1 == obj2,
     *         and a negative integer if obj1 < obj2
     */
    public int compare(Object obj1, Object obj2) {
        if (obj1 == null) {
            throw new IllegalArgumentException("Argument 'obj1' is null");
        }
        if (obj2 == null) {
            throw new IllegalArgumentException("Argument 'obj2' is null");
        }
        return ((String) obj1).compareTo((String) obj2);
    }
}
