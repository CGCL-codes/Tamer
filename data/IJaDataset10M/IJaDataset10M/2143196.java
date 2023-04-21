package officeserver.office.network;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Chris Bayruns
 * 
 */
public class RequestQueue {

    private static BlockingQueue<RequestQueueWrapper> rQueue;

    private static boolean working = false;

    private static void initQueue() {
        rQueue = new LinkedBlockingQueue<RequestQueueWrapper>();
    }

    public static void enqueue(RequestQueueWrapper in) {
        if (rQueue == null) {
            initQueue();
        }
        try {
            rQueue.put(in);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasWork() {
        if (rQueue == null) {
            initQueue();
        }
        return !rQueue.isEmpty() && !working;
    }

    public static void doWork() {
        RequestQueueWrapper node = null;
        if (rQueue == null) {
            initQueue();
        }
        if (hasWork()) {
            try {
                node = rQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (node != null && node.getRequest() != null) {
                working = true;
                node.processRequest();
                working = false;
            }
        }
    }
}
