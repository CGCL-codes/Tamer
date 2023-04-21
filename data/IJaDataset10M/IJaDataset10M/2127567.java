package chapter2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Exercise_2_4_3_OrderedLinkedList<E extends Comparable<E>> extends Exercise_2_4_3_Base<E> {

    private int N;

    private Node first;

    private Node last;

    private class Node {

        private E item;

        private Node next;

        private Node previous;
    }

    public Exercise_2_4_3_OrderedLinkedList() {
        N = 0;
    }

    public E removeMaximum() {
        if (isEmpty()) throw new RuntimeException("Stack Underflow");
        E item = getLast().item;
        getLast().previous.next = null;
        N--;
        return item;
    }

    public E pop() {
        if (isEmpty()) throw new RuntimeException("Stack underflow");
        E item = getFirst().item;
        Node newFirst = getFirst().next;
        setFirst(newFirst);
        N--;
        return item;
    }

    public void insert(E item) {
        if (isEmpty()) {
            setFirst(new Node());
            getFirst().item = item;
            getFirst().previous = null;
            getFirst().next = null;
            setLast(getFirst());
            return;
        }
        Node after = getFirst();
        while (after.next != null) {
            if (item.compareTo(after.item) <= 0) {
                break;
            }
            after = after.next;
        }
        Node before = new Node();
        before.item = item;
        before.next = after;
        before.previous = (after.previous != null) ? after.previous : null;
        after.previous = before;
        N++;
    }

    public void enqueue(E item) {
        Node oldLast = getLast();
        setLast(new Node());
        last.item = item;
        last.next = null;
        last.previous = oldLast;
        oldLast.next = getLast();
        N++;
    }

    public E peek() {
        if (!isEmpty()) throw new RuntimeException("Stack underflow");
        return getFirst().item;
    }

    public boolean isEmpty() {
        return getFirst() == null;
    }

    public int size() {
        return N;
    }

    Node getFirst() {
        return first;
    }

    public Node getLast() {
        return last;
    }

    public void setFirst(Node node) {
        first = node;
    }

    public void setLast(Node node) {
        last = node;
    }

    public Iterator iterator() {
        return new LIFOIterator();
    }

    private class LIFOIterator implements Iterator<E> {

        private Node current = getFirst();

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            Node n = getFirst();
            while (n.next.next != null) {
                n = n.next;
            }
            n.next = null;
            N--;
        }

        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E item = current.item;
            current = current.next;
            return item;
        }
    }
}
