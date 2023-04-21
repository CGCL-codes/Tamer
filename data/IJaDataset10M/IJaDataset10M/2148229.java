package org.tzi.use.util;

import java.util.Collection;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Arrays;

/**
 * Skeleton for implementations of the Bag interface.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public abstract class AbstractBag extends AbstractCollection implements Bag {

    /**
     * Compares the specified Object with this Bag for equality.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Bag)) return false;
        Collection c = (Collection) o;
        if (c.size() != size()) {
            return false;
        }
        Object[] thisBagAsArray = toArray();
        Object[] objBagAsArray = c.toArray();
        Arrays.sort(thisBagAsArray);
        Arrays.sort(objBagAsArray);
        for (int i = 0; i < thisBagAsArray.length; i++) {
            if (!thisBagAsArray[i].equals(objBagAsArray[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hash code value for this Bag.
     */
    public int hashCode() {
        int h = 0;
        Iterator i = iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj != null) h += obj.hashCode();
        }
        return h;
    }
}
