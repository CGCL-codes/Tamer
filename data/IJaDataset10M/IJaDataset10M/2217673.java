package chapter2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Exercise_2_4_3_UnorderedLinkedList<E extends Comparable<E>> extends Exercise_2_4_3_Base<E> {

    private int N;

    private Node first;

    private Node last;

    private class Node {

        private E item;

        private Node next;

        private Node previous;
    }

    public Exercise_2_4_3_UnorderedLinkedList() {
        N = 0;
    }

    public E removeMaximum() {
        if (isEmpty()) throw new RuntimeException("Stack Underflow");
        Node current = getFirst();
        Node max = current;
        while (current.next != null) {
            max = max.item.compareTo(current.next.item) < 0 ? max : current.next;
        }
        E item = max.item;
        if (max.previous == null) {
            if (max.next != null) {
                max.next.previous = null;
            }
        } else {
            if (max.next != null) {
                max.next.previous = max.previous;
                max.previous.next = max.next;
            } else {
                max.previous.next = null;
            }
        }
        max = null;
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
        Node oldFirst = getFirst();
        setFirst(new Node());
        getFirst().item = item;
        getFirst().next = oldFirst;
        getFirst().previous = null;
        oldFirst.previous = getFirst();
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
