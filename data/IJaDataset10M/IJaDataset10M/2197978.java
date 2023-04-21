package org.hsqldb.lib;

/**
 * Intended as an asynchronous alternative to HsqlArrayList.  Use HsqlArrayList if
 * the list won't be initialized sequentially and if frequent references to
 * specific element positions will be made.
 *
 * @author jcpeck@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class HsqlLinkedList extends BaseList implements HsqlList {

    /**
     * A reference to the head of the list.  It is a dummy head (that is, the
     * Node for index 0 is actually first.next).
     */
    private Node first;

    /** A reference to the tail of the list */
    private Node last;

    /**
     * Creates a new instance of HsqlLinkedList.
     */
    public HsqlLinkedList() {
        first = new Node(null, null);
        last = first;
        elementCount = 0;
    }

    /**
     * Inserts <code>element</code> at <code>index</code>.
     * @throws IndexOutOfBoundsException if <code>index</code> &lt; 0 or is &gt;
     * <code>size</code>.
     */
    public void add(int index, Object element) {
        if (index == size()) {
            add(element);
        } else if (index > size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " > " + size());
        } else {
            Node current = getInternal(index);
            Node newNext = new Node(current.data, current.next);
            current.data = element;
            current.next = newNext;
            elementCount++;
            if (last == current) {
                last = newNext;
            }
        }
    }

    /**
     * Appends <code>element</code> to the end of this list.
     * @return true
     */
    public boolean add(Object element) {
        last.next = new Node(element, null);
        last = last.next;
        elementCount++;
        return true;
    }

    public void clear() {
        first.next = null;
    }

    /**
     * Gets the element at given position
     * @throws <code>IndexOutOfBoundsException</code> if index is not valid
     * index within the list (0 &lt;= <code>index</code> &lt;
     * <code>size</code>).
     */
    public Object get(int index) {
        return getInternal(index).data;
    }

    /**
     * Removes and returns the element at <code>index</code>.
     * @throws <code>IndexOutOfBoundsException</code> if index is not valid
     * index within the list (0 &lt;= <code>index</code> &lt;
     * <code>size</code>).
     */
    public Object remove(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " >= " + size());
        }
        Node previousToRemove;
        if (index == 0) {
            previousToRemove = first;
        } else {
            previousToRemove = getInternal(index - 1);
        }
        Node toRemove = previousToRemove.next;
        previousToRemove.next = toRemove.next;
        elementCount--;
        if (last == toRemove) {
            last = previousToRemove;
        }
        return toRemove.data;
    }

    /**
     * Replaces the current element at <code>index/code> with
     * <code>element</code>.
     * @return The current element at <code>index</code>.
     */
    public Object set(int index, Object element) {
        Node setMe = getInternal(index);
        Object oldData = setMe.data;
        setMe.data = element;
        return oldData;
    }

    /**
     * Accessor for the size of this linked list.  The size is the total number
     * of elements in the list and is one greater than the largest index in the
     * list.
     * @return The size of this.
     */
    public final int size() {
        return elementCount;
    }

    /**
     * Helper method that returns the Node at <code>index</code>.
     * @param index The index of the Node to return.
     * @return The Node at the given index.
     * @throws <code>IndexOutOfBoundsException</code> if index is not valid
     * index within the list (0 &lt;= <code>index</code> &lt;
     * <code>size</code>).
     */
    protected final Node getInternal(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " >= " + size());
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " < 0");
        }
        if (index == 0) {
            return first.next;
        } else if (index == (size() - 1)) {
            return last;
        } else {
            Node pointer = first.next;
            for (int i = 0; i < index; i++) {
                pointer = pointer.next;
            }
            return pointer;
        }
    }

    /**
     * Inner class that represents nodes within the linked list.  This should
     * be a static inner class to avoid the uneccesary overhead of the
     * containing class "this" pointer.
     * jcpeck@users
     * @version 05/24/2002
     */
    private static class Node {

        public Node next;

        public Object data;

        public Node() {
            next = null;
            data = null;
        }

        public Node(Object data) {
            this.next = null;
            this.data = data;
        }

        public Node(Object data, Node next) {
            this.next = next;
            this.data = data;
        }
    }
}
