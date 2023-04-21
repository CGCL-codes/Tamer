package orgx.jdom;

import java.util.*;
import orgx.jdom.filter.*;

/**
 * Traverse a parent's children that match the supplied filter.
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.6 $, $Date: 2007/11/10 05:28:59 $
 */
class FilterIterator implements Iterator {

    private Iterator iterator;

    private Filter filter;

    private Object nextObject;

    private static final String CVS_ID = "@(#) $RCSfile: FilterIterator.java,v $ $Revision: 1.6 $ $Date: 2007/11/10 05:28:59 $ $Name: jdom_1_1 $";

    public FilterIterator(Iterator iterator, Filter filter) {
        if ((iterator == null) || (filter == null)) {
            throw new IllegalArgumentException("null parameter");
        }
        this.iterator = iterator;
        this.filter = filter;
    }

    public boolean hasNext() {
        if (nextObject != null) {
            return true;
        }
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (filter.matches(obj)) {
                nextObject = obj;
                return true;
            }
        }
        return false;
    }

    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Object obj = nextObject;
        nextObject = null;
        return obj;
    }

    public void remove() {
        iterator.remove();
    }
}
