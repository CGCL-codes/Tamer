package mpjdev;

import java.util.*;
import java.nio.ByteBuffer;

public abstract class Request {

    boolean isNull = false;

    LinkedList list = new LinkedList();

    public abstract mpjdev.Status iwait();

    public abstract Status itest();

    public abstract void free();

    public abstract boolean isnull();

    public abstract boolean cancel();

    Waitany waitany;

    static class Waitany {

        Request completed;

        int index;

        Request[] reqs;

        Waitany next, prev;

        boolean done;

        synchronized void wakeup() {
            done = true;
            notify();
        }

        synchronized void waitfor() {
            while (!done) {
                try {
                    wait();
                } catch (Exception e) {
                }
            }
        }
    }

    static WaitanyQue waitanyQue = new WaitanyQue();

    static class WaitanyQue {

        Waitany front, back;

        synchronized void remove(Waitany waitany) {
            if (front == back) {
                front = null;
                back = null;
            } else if (front == waitany) {
                front.prev.next = front.next;
                front.next.prev = front.prev;
                front = front.prev;
            } else if (back == waitany) {
                back.prev.next = back.next;
                back.next.prev = back.prev;
                back = back.next;
            } else {
                waitany.prev.next = waitany.next;
                waitany.next.prev = waitany.prev;
            }
        }

        synchronized void add(Waitany waitany) {
            if (listEmpty()) {
                front = waitany;
                back = waitany;
                waitany.next = waitany;
                waitany.prev = waitany;
            } else {
                front.next.prev = waitany;
                waitany.next = front.next;
                front.next = waitany;
                waitany.prev = front;
                back = waitany;
            }
        }

        synchronized Waitany front() {
            return front;
        }

        boolean listEmpty() {
            return (front == null && back == null);
        }
    }

    public static mpjdev.Status iwaitany(mpjdev.Request[] requests) {
        Waitany w = initializeWaitany(requests);
        Request r = null;
        Waitany wr = null;
        while (w.completed == null) {
            if (w == waitanyQue.front()) {
                do {
                    r = MPJDev.dev.peek();
                    wr = processRequest(r);
                    if (wr != w) {
                        w.wakeup();
                    }
                } while (wr != w);
                if (!waitanyQue.listEmpty()) {
                    waitanyQue.front.wakeup();
                }
            } else {
                w.waitfor();
            }
        }
        Status completedStatus = w.completed.iwait();
        completedStatus.index = w.index;
        if (completedStatus == null) {
            System.out.println("not possible 1");
        }
        if (requests[completedStatus.index] == null) {
            System.out.println("completedStatus.index " + completedStatus.index);
            System.out.println("not possible 2");
        }
        requests[completedStatus.index].isNull = true;
        return completedStatus;
    }

    static synchronized Waitany initializeWaitany(Request[] reqs) {
        Waitany w = new Waitany();
        boolean found = false;
        for (int i = 0; i < reqs.length; i++) {
            if (reqs[i] != null && !reqs[i].isNull) {
                if (reqs[i].itest() != null) {
                    w.completed = reqs[i];
                    w.index = i;
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            for (int i = 0; i < reqs.length; i++) {
                if (reqs[i] != null) {
                    reqs[i].waitany = w;
                }
            }
            waitanyQue.add(w);
        }
        w.reqs = reqs;
        return w;
    }

    static synchronized Waitany processRequest(Request r) {
        Waitany w = r.waitany;
        if (w == null) {
            return null;
        }
        w.completed = r;
        for (int i = 0; i < w.reqs.length; i++) {
            if (w.reqs[i] == r) {
                w.index = i;
            }
            if (w.reqs[i] != null) {
                w.reqs[i].waitany = null;
            }
        }
        waitanyQue.remove(w);
        return w;
    }

    protected void complete(mpjdev.Status status) {
        Iterator iter = list.iterator();
        CompletionHandler handler = null;
        while (iter.hasNext()) {
            handler = (CompletionHandler) iter.next();
            handler.handleCompletion(status);
        }
    }

    public void addCompletionHandler(mpjdev.CompletionHandler handler) {
        list.add(handler);
    }
}
