package nlp.lang.he.morph.erel.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * used to treat an object just as a vector with 1 element.
 */
class SingleElementEnumerator implements Enumeration {

    Object theElement;

    boolean notvisited;

    public SingleElementEnumerator(Object e) {
        theElement = e;
        notvisited = true;
    }

    public boolean hasMoreElements() {
        return notvisited;
    }

    public Object nextElement() {
        if (notvisited) {
            notvisited = false;
            return theElement;
        } else throw new NoSuchElementException("SingleElementEnumerator");
    }
}
