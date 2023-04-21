package org.dhmp.util;

/**
 * Circular Queue is a queue implemented using linked list that reuse the existent
 * entries to store new data or appends new entry if all existent entries are not
 * free (for instance value has added but still has not read). Once the value has read
 * using get(), the corresponding entry is freed. Therefore the size of this linkedlist
 * is the number of entries in a queue during the peak. Use shrink() to free-up unused
 * entries.
 * @author hideyuki
 */
public class CircularQueue extends LinkedList {

    static final long serialVersionUID = 9066405791200089027L;

    protected CircularQueueEntry head, tail;

    /** Creates a new instance of CircularQueue */
    public CircularQueue() {
        super();
    }

    public class CircularQueueEntry extends Entry {

        boolean free = false;

        public CircularQueueEntry(Object o) {
            super(o);
        }
    }

    private CircularQueueEntry after(Entry e) {
        return (e == last) ? (CircularQueueEntry) first : (CircularQueueEntry) e.next;
    }

    public boolean add(Object o) {
        if (size == 0) {
            addLast(o);
            head = tail = (CircularQueueEntry) first;
        } else if (head == tail && head.free) {
            head.data = o;
            head.free = false;
        } else {
            CircularQueueEntry next = after(tail);
            if (next == head) {
                addAfter(tail, o);
            } else {
                tail = next;
                tail.data = o;
                tail.free = false;
            }
        }
        return true;
    }

    protected void addAfter(Entry e, Object o) {
        modCount++;
        CircularQueueEntry newEntry = (CircularQueueEntry) newEntry(o);
        newEntry.previous = e;
        newEntry.next = e.next;
        if (e.next != null) {
            e.next.previous = newEntry;
        }
        e.next = newEntry;
        if (e == last) {
            last = newEntry;
        }
        tail = newEntry;
        size++;
    }

    public Object get() {
        Entry e = getEntry();
        return (e == null) ? null : e.data;
    }

    protected Entry getEntry() {
        if (size == 0) return null;
        if (head.free) return null;
        Entry e = head;
        head.free = true;
        head = (head == tail) ? head : after(head);
        return e;
    }

    public boolean isEmpty() {
        return (head == tail) ? (head == null || head.free == true) : false;
    }

    public void shrink() {
        Entry e = after(tail);
        while (e != head) {
            Entry next = after(e);
            removeEntry(e);
            e = next;
        }
    }

    public Entry newEntry(Object o) {
        return new CircularQueueEntry(o);
    }
}
