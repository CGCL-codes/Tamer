package net.sourceforge.ondex.core.base;

import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.core.ONDEXIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Iterator interface for Collections.
 * 
 * @author taubertj
 * 
 */
public abstract class AbstractONDEXIterator<AnyType> implements ONDEXIterator<AnyType> {

    /**
	 * Returns true if this collection contains no elements.
	 * 
	 * @return true if this collection contains no elements
	 */
    public boolean isEmpty() {
        return (size() == 0);
    }

    /**
	 * Throws UnsupportedOperationException.
	 */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
	 * This is a costly method use with care Copies content into a Collection.
	 * 
	 * @return Collection<AnyType>
	 */
    public Collection<AnyType> toCollection() {
        if (size() == 0) return new ArrayList<AnyType>();
        ArrayList<AnyType> list = new ArrayList<AnyType>();
        while (hasNext()) {
            list.add(next());
        }
        reset();
        return list;
    }

    /**
	 * Checks for all objects of a given Collection to be contained in this
	 * iterator.
	 * 
	 * @param c
	 *            Collection<?>
	 * @return boolean
	 * @throws AccessDeniedException 
	 */
    public boolean containsAll(Collection<?> c) throws AccessDeniedException {
        if (size() == 0) return false;
        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                reset();
                return false;
            }
        }
        reset();
        return true;
    }

    /**
	 * Checks for all objects of a given AbstractONDEXIterator to be contained
	 * in this iterator.
	 * 
	 * @param it
	 *            AbstractONDEXIterator<?>
	 * @return boolean
	 * @throws AccessDeniedException 
	 */
    public boolean containsAll(ONDEXIterator<?> it) throws AccessDeniedException {
        if (size() == 0) return false;
        while (it.hasNext()) {
            if (!contains(it.next())) {
                reset();
                return false;
            }
        }
        reset();
        it.reset();
        return true;
    }
}
