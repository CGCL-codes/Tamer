package com.atosorigin.nl.agreement.temporal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Basic representation of an object with temporal properties.
 * 
 * @author Jeroen Benckhuijsen
 */
public class TemporalObject<T extends Object> {

    class Entry implements Comparable<Entry> {

        Date date;

        T item;

        /**
		 * @param date
		 * @param item
		 */
        public Entry(Date date, T item) {
            this.date = date;
            this.item = item;
        }

        @Override
        public int compareTo(Entry other) {
            if (other.date.equals(this.date)) {
                return 0;
            }
            return (other.date.before(this.date)) ? -1 : 1;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final Entry other = (Entry) obj;
            if (date == null) {
                if (other.date != null) return false;
            } else if (!date.equals(other.date)) return false;
            return true;
        }
    }

    private Queue<Entry> contents = new PriorityQueue<Entry>();

    /**
	 * @return
	 */
    public T get() throws NoSuchElementException {
        if (this.contents.size() == 0) {
            throw new NoSuchElementException();
        }
        return this.contents.element().item;
    }

    /**
	 * @param at
	 * @return
	 */
    public T get(Date at) {
        T t = tryGet(at);
        if (t == null) {
            throw new IllegalArgumentException("No entries that early (" + at.toString() + ")");
        }
        return t;
    }

    /**
	 * @param at
	 * @return
	 */
    public T tryGet(Date at) {
        Entry comparer = new Entry(at, null);
        for (Entry entry : contents) {
            if (entry.compareTo(comparer) >= 0) {
                return entry.item;
            }
        }
        return null;
    }

    /**
	 * @return
	 */
    public Collection<T> getAll() {
        Collection<T> all = new ArrayList<T>();
        for (Entry entry : contents) {
            all.add(entry.item);
        }
        return all;
    }

    /**
	 * @param date
	 * @param item
	 * @return
	 */
    public void put(Date date, T item) {
        if (item == null) {
            throw new IllegalArgumentException("item (2nd arg. to TemporalObject.put) should be non-null");
        }
        Entry newEntry = new Entry(date, item);
        if (contents.contains(newEntry)) {
            contents.remove(newEntry);
        }
        contents.add(newEntry);
    }

    /**
	 * @param item
	 */
    public void put(T item) {
        put(new Date(), item);
    }
}
