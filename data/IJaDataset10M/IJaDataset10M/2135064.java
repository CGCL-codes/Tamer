package org.gocha.gef.iterator;

import java.util.Iterator;
import org.gocha.gef.*;

/**
 *
 * @author gocha
 */
public class SelectionIterable implements Iterable<Glyph> {

    public SelectionIterable(Iterable<Glyph> iterable) {
        this.iterable = iterable;
    }

    private Iterable<Glyph> iterable;

    public Iterator<Glyph> iterator() {
        return new SelectionIterator(iterable.iterator());
    }
}
