package de.inovox.pipeline.input;

import java.util.Iterator;
import java.util.List;

/**
 * Iterator that operates on a list of ImportObjects
 * 
 * @author Carsten Burghardt
 * @version $Id: GenericImportObjectIterator.java 9 2006-11-24 22:07:35Z carsten $
 */
public class GenericImportObjectIterator implements Iterator {

    protected Iterator it;

    /**
     * Constructor
     */
    public GenericImportObjectIterator(List files) {
        it = files.iterator();
    }

    public boolean hasNext() {
        boolean ret = it.hasNext();
        return ret;
    }

    /**
     * Convert File to Document
     * @see java.util.ListIterator#next()
     */
    public Object next() {
        return it.next();
    }

    public void remove() {
        it.remove();
    }
}
