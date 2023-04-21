package org.gjt.universe;

import java.util.Enumeration;

public abstract class OrderQueueSetIndex {

    private static int currentindex = 0;

    private int index;

    private String name;

    private OrderQueueSetIndex prev;

    private OrderQueueSetIndex next;

    private static OrderQueueSetIndex first = null;

    private static OrderQueueSetIndex last = null;

    protected OrderQueueSetIndex(String name) {
        this.name = name;
        index = currentindex++;
        if (first == null) {
            first = this;
        }
        if (last != null) {
            this.prev = last;
            last.next = this;
        }
        last = this;
    }

    public String toString() {
        return this.name;
    }

    int getIndex() {
        return index;
    }

    int size() {
        return currentindex;
    }

    public static OrderQueueSetIndex first() {
        return first;
    }

    public static OrderQueueSetIndex last() {
        return last;
    }

    public OrderQueueSetIndex prev() {
        return this.prev;
    }

    public OrderQueueSetIndex next() {
        return this.next;
    }

    public static Enumeration elements() {
        return new OrderQueueSetIndexEnumeration();
    }

    private static class OrderQueueSetIndexEnumeration implements Enumeration {

        private OrderQueueSetIndex current = first;

        public boolean hasMoreElements() {
            return current != null;
        }

        public Object nextElement() {
            OrderQueueSetIndex tmp = current;
            current = current.next;
            return tmp;
        }
    }
}
