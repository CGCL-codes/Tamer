package com.ontotext.ordi.iterator;

import info.aduna.iteration.CloseableIteration;
import java.util.NoSuchElementException;
import org.openrdf.query.QueryEvaluationException;
import com.ontotext.ordi.tripleset.TStatement;

/**
 * This class converts the ORDI's result iterator to Sesame's CloseableIteration<X,
 * Exception>.
 * 
 * @author vassil
 * 
 * @param <X>
 */
public class ConvertingIteratorImpl<X> implements CloseableIteration<X, QueryEvaluationException> {

    private CloseableIterator<? extends TStatement> iterator;

    private boolean isClosed = false;

    public ConvertingIteratorImpl(CloseableIterator<? extends TStatement> iterator) {
        if (iterator == null) {
            throw new IllegalArgumentException();
        }
        this.iterator = iterator;
    }

    public boolean hasNext() {
        if (isClosed) throw new NoSuchElementException("Iterator is closed!");
        return iterator.hasNext();
    }

    public void remove() {
        if (isClosed) throw new NoSuchElementException("Iterator is closed!");
        iterator.remove();
    }

    public void close() {
        isClosed = true;
        iterator.close();
    }

    @SuppressWarnings("unchecked")
    public X next() throws QueryEvaluationException {
        if (isClosed || hasNext() == false) throw new NoSuchElementException();
        try {
            return (X) iterator.next();
        } catch (ClassCastException e) {
            throw new QueryEvaluationException(e);
        }
    }
}
