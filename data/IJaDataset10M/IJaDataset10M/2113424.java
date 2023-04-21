package name.huzhenbo.java.collection;

import java.util.LinkedList;

public class Queue<T> {

    private LinkedList<T> queue;

    public Queue() {
        queue = new LinkedList<T>();
    }

    public void en(T i) {
        queue.addLast(i);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public T de() {
        return queue.removeFirst();
    }

    public int size() {
        return queue.size();
    }

    public T first() {
        return queue.getFirst();
    }
}
