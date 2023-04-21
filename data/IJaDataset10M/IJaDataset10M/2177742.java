package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class that lets several lists be iterated over as one.
 * @author Petter Hansson
 *
 */
@SuppressWarnings({ "serial" })
public class ListView<E> extends LinkedList<List<E>> {

    public ListView() {
        super();
    }

    public ListView(final Collection<? extends List<E>> c) {
        super(c);
    }

    public Iterator<E> elementIterator() {
        return new ElementIterator();
    }

    private class ElementIterator implements Iterator<E> {

        Iterator<List<E>> primIt;

        Iterator<E> secIt;

        public ElementIterator() {
            primIt = listIterator();
            if (primIt.hasNext()) {
                secIt = primIt.next().listIterator();
                if (!secIt.hasNext()) {
                    secIt = null;
                }
            } else {
                secIt = null;
            }
        }

        @Override
        public boolean hasNext() {
            return secIt != null;
        }

        @Override
        public E next() {
            if (secIt == null) {
                throw new NoSuchElementException("Invalid element");
            }
            final E e = secIt.next();
            if (!secIt.hasNext()) {
                if (primIt.hasNext()) {
                    secIt = primIt.next().listIterator();
                    if (!secIt.hasNext()) {
                        secIt = null;
                    }
                } else {
                    secIt = null;
                }
            }
            return e;
        }

        @Override
        public void remove() {
            if (secIt == null) {
                throw new NoSuchElementException("Invalid element");
            }
            secIt.remove();
        }
    }
}
