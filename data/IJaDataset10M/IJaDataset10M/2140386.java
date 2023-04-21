package net.sf.joafip.store.service.objectfortest;

import java.util.Iterator;

public class LucList {

    private class Link {

        private Object object;

        private Link previous;

        private Link next;
    }

    ;

    private Link root;

    private Link last;

    public void add(final Object object) {
        if (last == null) {
            root = new Link();
            last = root;
            root.object = object;
        } else {
            final Link newLast = new Link();
            newLast.object = object;
            last.next = newLast;
            newLast.previous = last;
            last = newLast;
        }
    }

    public boolean remove(final Object object) {
        Link current = root;
        while (current != null && current.object != object) {
            current = current.next;
        }
        final boolean remove;
        if (current == null) {
            remove = false;
        } else {
            remove = true;
            final Link previous = current.previous;
            final Link next = current.next;
            if (previous != null) {
                previous.next = next;
            }
            if (next != null) {
                next.previous = previous;
            }
        }
        return remove;
    }

    private class LucListIterator implements Iterator<Object> {

        private Link current;

        public LucListIterator(final LucList list) {
            current = list.root;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Object next() {
            final Object object = current.object;
            current = current.next;
            return object;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Object> iterator() {
        return new LucListIterator(this);
    }
}
