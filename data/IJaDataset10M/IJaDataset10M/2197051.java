package com.ibm.JikesRVM;

/**
 * See VM_Proxy
 *
 * @author Susan Flynn Hummel
 * @author Bowen Alpern
 */
final class VM_ProxyWaitingQueue extends VM_AbstractThreadQueue implements VM_Uninterruptible {

    private VM_Proxy tail;

    private VM_Proxy head;

    private int id;

    VM_ProxyWaitingQueue(int id) {
        this.id = id;
    }

    boolean isEmpty() {
        return (head == null);
    }

    void enqueue(VM_Thread t) {
        enqueue(t.proxy);
    }

    void enqueue(VM_Proxy p) {
        if (head == null) {
            head = p;
        } else {
            tail.waitingNext = p;
        }
        tail = p;
    }

    VM_Thread dequeue() {
        while (head != null) {
            VM_Proxy p = head;
            head = head.waitingNext;
            if (head == null) tail = null;
            VM_Thread t = p.unproxy();
            if (t != null) return t;
        }
        return null;
    }

    int length() {
        int i = 0;
        VM_Proxy p = head;
        while (p != null) {
            i = i + 1;
            p = p.waitingNext;
        }
        return i;
    }

    boolean contains(VM_Thread t) {
        VM_Proxy p = head;
        while (p != null) {
            if (p.patron == t) return true;
            p = p.waitingNext;
        }
        return false;
    }

    void dump() {
        for (VM_Proxy p = head; p != null; p = p.waitingNext) if (p.patron != null) p.patron.dump();
        VM.sysWrite("\n");
    }
}
