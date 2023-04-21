package biz.evot.util.lang;

public class QueueInt {

    protected QueueIntItem first;

    protected QueueIntItem last;

    protected int nBlocked;

    protected static QueueIntItem freeItemChain;

    protected static final synchronized QueueIntItem createItem(int obj) {
        QueueIntItem item;
        if (freeItemChain == null) {
            item = new QueueIntItem();
        } else {
            item = freeItemChain;
            freeItemChain = freeItemChain.next;
        }
        item.obj = obj;
        item.next = null;
        return item;
    }

    protected static final synchronized void freeItem(QueueIntItem item) {
        item.next = freeItemChain;
        freeItemChain = item;
    }

    /**
	 * Wait until queue is not empty and take object from the queue.
	 * 
	 * @return object which was inserted in the queue the longest time ago.
	 */
    public synchronized int get() {
        if (first == null || nBlocked != 0) {
            do {
                try {
                    nBlocked += 1;
                    wait();
                    nBlocked -= 1;
                } catch (InterruptedException ex) {
                    notify();
                    nBlocked -= 1;
                    throw new RuntimeException("thread is interrupted");
                }
            } while (first == null);
        }
        QueueIntItem item = first;
        int obj = item.obj;
        first = first.next;
        freeItem(item);
        if (nBlocked != 0 && first != null) {
            notify();
        }
        return obj;
    }

    /**
	 * Wait at most <code>timeout</code> miliseconds until queue becomes not
	 * empty and take object from the queue.
	 * 
	 * @param timeout
	 *            the maximum time to wait in milliseconds.
	 * @return object if queue is not empty, <code>null</code> otherwise
	 */
    public synchronized int get(long timeout) {
        if (first == null || nBlocked != 0) {
            if (timeout == 0) {
                return -1;
            }
            long startTime = System.currentTimeMillis();
            do {
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime >= timeout) {
                    return -1;
                }
                try {
                    nBlocked += 1;
                    wait(timeout - currentTime + startTime);
                    nBlocked -= 1;
                } catch (InterruptedException ex) {
                    notify();
                    nBlocked -= 1;
                    throw new RuntimeException("thread is interrupted");
                }
            } while (first == null);
        }
        QueueIntItem item = first;
        int obj = item.obj;
        first = first.next;
        freeItem(item);
        if (nBlocked != 0 && first != null) {
            notify();
        }
        return obj;
    }

    /**
	 * Check if queue is empty.
	 */
    public boolean isEmpty() {
        return first == null;
    }

    /**
	 * Put object in queue. Notify data consumers.
	 */
    public synchronized void put(int obj) {
        if (first == null) {
            first = last = createItem(obj);
        } else {
            last = last.next = createItem(obj);
        }
        if (nBlocked != 0) {
            notify();
        }
    }
}

class QueueIntItem {

    public int obj;

    public QueueIntItem next;
}
