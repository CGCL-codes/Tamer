package org.jmonit.web;

import java.util.Collection;
import java.util.Iterator;
import org.jmonit.Monitor;
import org.jmonit.reporting.Visitable;

public final class MonitorsIterator implements Iterator<Visitable> {

    private final Iterator<Monitor> iterator;

    private final Collection<Class> features;

    private Monitor next;

    /**
     * @param iterator
     * @param path
     * @param feature
     */
    public MonitorsIterator(Iterator<Monitor> iterator, Collection<Class> features) {
        this.iterator = iterator;
        this.features = features;
        findNext();
    }

    public boolean hasNext() {
        return next != null;
    }

    public Visitable next() {
        final Monitor monitor = next;
        Visitable visit = new VisitableMonitor(monitor, features);
        findNext();
        return visit;
    }

    private void findNext() {
        next = null;
        while (iterator.hasNext()) {
            Monitor m = iterator.next();
            if (features == null || m.getFeatures().containsAll(features)) {
                next = m;
                break;
            }
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
