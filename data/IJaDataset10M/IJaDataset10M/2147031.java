package edu.indiana.extreme.q.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import edu.indiana.extreme.q.Queue;
import wsmg.db.PersistantQueueDB;

/**
 * @author Chathura Herath (cherath@cs.indiana.edu)
 */
public class QueueImpl implements Queue {

    private java.util.Queue queue;

    private PersistantQueueDB queueDB;

    private java.util.Queue disposeQueue;

    public static int DISPOSE_QUEUE_FLUSH_SIZE = 100;

    public QueueImpl() throws SQLException {
        this.queue = new ConcurrentLinkedQueue();
        this.disposeQueue = new ConcurrentLinkedQueue();
        this.queueDB = PersistantQueueDB.getInstance();
        KeyValueWrapper obj = null;
        try {
            obj = this.queueDB.retrive();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (obj != null) {
            this.queue.add(obj);
            obj = null;
            try {
                obj = this.queueDB.retrive();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enqueue(Object object, String trackId) {
        KeyValueWrapper wrappedInstance = null;
        try {
            wrappedInstance = this.queueDB.getWrappedInstance(object);
            this.queueDB.save(wrappedInstance, trackId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.queue.add(wrappedInstance);
    }

    public synchronized Object blockingDequeue() {
        while (queue.size() == 0) ;
        KeyValueWrapper obj = (KeyValueWrapper) this.queue.remove();
        this.disposeQueue.add(obj);
        if (this.disposeQueue.size() >= DISPOSE_QUEUE_FLUSH_SIZE) {
            try {
                this.queueDB.delete(this.disposeQueue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return obj.getValue();
    }

    public int size() {
        return this.queue.size();
    }

    public void flush() {
        throw new UnsupportedOperationException();
    }

    public void cleanup() {
        throw new UnsupportedOperationException();
    }
}
