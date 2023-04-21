package com.ontotext.ordi.iterator;

import com.ontotext.ordi.exception.ORDIRuntimeException;
import info.aduna.iteration.CloseableIteration;

/**
 * This class wraps the Sesame CloseableIteration type and provides ORDI
 * friendly iteration interface. The type is used to integrated the Sesame Query
 * engine functionality in ORDI.
 * 
 * @author vassil
 */
public class WrappedSesameIteratorImpl<E> implements CloseableIterator<E> {

    private CloseableIteration<E, ? extends Exception> iter;

    public WrappedSesameIteratorImpl(CloseableIteration<E, ? extends Exception> iter) {
        if (iter == null) {
            throw new IllegalArgumentException();
        }
        this.iter = iter;
    }

    public void close() {
        try {
            iter.close();
        } catch (Exception e) {
            throw new ORDIRuntimeException(e);
        }
    }

    public boolean hasNext() {
        try {
            return iter.hasNext();
        } catch (Exception e) {
            throw new ORDIRuntimeException(e);
        }
    }

    public E next() {
        try {
            return iter.next();
        } catch (Exception e) {
            throw new ORDIRuntimeException(e);
        }
    }

    public void remove() {
        try {
            iter.remove();
        } catch (Exception e) {
            throw new ORDIRuntimeException(e);
        }
    }
}
