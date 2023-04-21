package tests.generics;

import java.util.*;

public class IlrIntPropertyList<Key> {

    public final class Link {

        protected final Key key;

        protected int value;

        protected Link next;

        Link(Key key, int value) {
            this.key = key;
            this.value = value;
        }

        Link(Key key, int value, Link next) {
            this(key, value);
            this.next = next;
        }

        public Key getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        public Link getNext() {
            return next;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    protected Link first;

    public void add(Key key, int value) {
        first = new Link(key, value, first);
    }

    public int get(Key key) {
        for (Link l = first; l != null; l = l.next) {
            if (l.key == key) {
                return l.value;
            }
        }
        return -1;
    }

    public Link getLink(Key key) {
        for (Link l = first; l != null; l = l.next) {
            if (l.key == key) {
                return l;
            }
        }
        return null;
    }

    public boolean contains(Key key) {
        for (Link l = first; l != null; l = l.next) {
            if (l.key == key) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return (first == null);
    }

    public boolean remove(Key key) {
        Link previous = first;
        for (Link l = first; l != null; l = l.next) {
            if (l.key == key) {
                removeLink(previous, l);
                return true;
            }
            previous = l;
        }
        return false;
    }

    public Link removeLink(Key key) {
        Link previous = first;
        for (Link l = first; l != null; l = l.next) {
            if (l.key == key) {
                removeLink(previous, l);
                return l;
            }
            previous = l;
        }
        return null;
    }

    private void removeLink(Link previous, Link l) {
        if (first == l) {
            Link n = l.next;
            first = n;
        } else {
            Link n = l.next;
            if (previous != null) {
                previous.next = n;
            }
        }
    }

    public Link clear() {
        Link l = first;
        first = null;
        return l;
    }

    public int getSize() {
        int i = 0;
        for (Link l = first; l != null; l = l.next) {
            i++;
        }
        return i;
    }
}
