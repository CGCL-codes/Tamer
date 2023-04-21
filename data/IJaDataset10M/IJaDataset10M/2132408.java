package org.matsim.events.parallelEventsHandler;

import java.util.ArrayList;
import java.util.LinkedList;

public class ConcurrentListSPSC<T> {

    private LinkedList<T> inputBuffer = new LinkedList<T>();

    private LinkedList<T> outputBuffer = new LinkedList<T>();

    public void add(T element) {
        synchronized (inputBuffer) {
            inputBuffer.add(element);
        }
    }

    public void add(ArrayList<T> list) {
        synchronized (inputBuffer) {
            inputBuffer.addAll(list);
        }
    }

    public T remove() {
        if (outputBuffer.size() > 0) {
            return outputBuffer.poll();
        }
        if (inputBuffer.size() > 0) {
            synchronized (inputBuffer) {
                LinkedList<T> tempList = null;
                tempList = inputBuffer;
                inputBuffer = outputBuffer;
                outputBuffer = tempList;
            }
            return outputBuffer.poll();
        }
        return null;
    }
}
