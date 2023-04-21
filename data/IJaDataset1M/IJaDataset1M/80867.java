package Crawler;

import java.util.*;

public class Queue {

    private List queueList;

    public Queue() {
        queueList = new List("queue");
    }

    public synchronized void enqueue(Object object) {
        queueList.insertAtBack(object);
    }

    public synchronized Object dequeue() throws EmptyListException {
        return queueList.removeFromFront();
    }

    public synchronized boolean isEmpty() {
        return queueList.isEmpty();
    }

    public synchronized Vector getQueueElements() {
        return queueList.getElements();
    }

    public synchronized void print() {
        queueList.print();
    }
}
