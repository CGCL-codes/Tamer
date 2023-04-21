package org.databene.feed4testng;

import java.util.Iterator;
import org.databene.benerator.Generator;
import org.databene.benerator.wrapper.ProductWrapper;

/**
 * Helper class which wraps an array generator from Benerator with 
 * an {@link Iterator} interface and optionally limits the invocation count.<br/><br/>
 * Created: 19.04.2010 06:31:54
 * @since 0.6.2
 * @author Volker Bergmann
 */
public class FeedIterator implements Iterator<Object[]> {

    private Generator<Object[]> generator;

    private ProductWrapper<Object[]> next;

    public FeedIterator(Generator<Object[]> generator) {
        this.generator = generator;
        this.next = new ProductWrapper<Object[]>();
        fetchNext();
    }

    public boolean hasNext() {
        return (next != null);
    }

    public Object[] next() {
        if (next == null) throw new IllegalStateException("No data available in next(). Call hasNext() to check.");
        Object[] result = next.unwrap();
        fetchNext();
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove() is not supported");
    }

    private void fetchNext() {
        this.next = generator.generate(next);
    }
}
