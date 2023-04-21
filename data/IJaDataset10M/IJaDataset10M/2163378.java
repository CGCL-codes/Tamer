package net.sf.ruleminer.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author michal
 *
 */
public class Trie<T> {

    public static class Entry<Q> {

        private Map<Q, Entry<Q>> children;

        Entry() {
            this.children = new HashMap<Q, Entry<Q>>();
        }

        public Entry<Q> getChild(Q item) {
            return this.children.get(item);
        }

        public Entry<Q> putChild(Q item) {
            Entry<Q> child = new Entry<Q>();
            this.children.put(item, child);
            return child;
        }

        public boolean isEmpty() {
            return this.children.isEmpty();
        }
    }

    Entry<T> root;

    /**
	 * 
	 */
    public Trie() {
        super();
        this.root = new Entry<T>();
    }

    /**
	 * @param items
	 */
    public void put(Iterator<T> items) {
        Entry<T> actual = this.root;
        while (items.hasNext()) {
            T item = items.next();
            Entry<T> child = actual.getChild(item);
            if (child == null) {
                child = actual.putChild(item);
            }
            actual = child;
        }
    }

    /**
	 * @param items
	 */
    public void put(Iterable<T> items) {
        put(items.iterator());
    }

    /**
	 * @return
	 */
    public Entry<T> getRoot() {
        return this.root;
    }

    /**
	 * @param item
	 * @return
	 */
    public Entry<T> get(T item) {
        return this.root.getChild(item);
    }
}
