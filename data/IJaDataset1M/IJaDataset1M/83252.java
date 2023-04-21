package de.fuberlin.wiwiss.jenaext;

import java.util.Iterator;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.NiceIterator;

/**
 * This iterator converts the elements of an iterator over ID-based triples to
 * actual triples.
 *
 * @author Olaf Hartig
 */
public class DecodingTriplesIterator extends NiceIterator<Triple> implements ExtendedIterator<Triple> {

    /** the iterator that is being converted */
    private final Iterator<IdBasedTriple> base;

    public DecodingTriplesIterator(Iterator<IdBasedTriple> base) {
        assert base != null;
        this.base = base;
    }

    @Override
    public final boolean hasNext() {
        return base.hasNext();
    }

    @Override
    public final Triple next() {
        return base.next().triple;
    }

    @Override
    public final void remove() {
        base.remove();
    }
}
