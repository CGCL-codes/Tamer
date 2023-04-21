package org.rjam.util;

import java.util.LinkedList;
import org.rjam.api.IQueue;
import org.rjam.base.BaseLogging;

public class SyncronizedLinkedList extends BaseLogging implements IQueue {

    private static final long serialVersionUID = 1L;

    private LinkedList<Object> list = new LinkedList<Object>();

    public synchronized boolean add(Object object) {
        boolean ret = list.add(object);
        if (ret) {
            notifyAll();
        }
        return ret;
    }

    public synchronized Object remove() {
        Object ret = null;
        if (list.size() > 0) {
            try {
                ret = list.remove();
            } catch (Exception e) {
            }
        }
        return ret;
    }

    public synchronized int size() {
        return list.size();
    }

    public synchronized void waitForProducer(int time) {
        if (list.size() < 1) {
            try {
                wait(time);
            } catch (InterruptedException e) {
            }
        }
    }

    public int getObjectCount() {
        return size();
    }

    public synchronized void clear(boolean sync) {
        list.clear();
    }
}
