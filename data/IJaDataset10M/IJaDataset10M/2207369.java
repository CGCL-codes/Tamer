package com.objectwave.appArch;

import java.io.IOException;

/**
*  An event queue.  Can be used for threadsave serial execution of events.  Queue
* elements are added to the queue and when the queue element's turn is up, the queue
* element is notified that it is time to execute.
* @author Dave Hoag
* @see com.objectwave.appArch.QueueElement
*/
public class Queue implements Runnable {

    Thread thread;

    public boolean alive = true;

    boolean debug = false;

    QueueControl queues, currentQueue, workingQueue;

    /**
	*/
    public Queue() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**  Wake us up if we are waiting.  If not waiting, then just add the element to
	* our growing wait list of elements (queues).  If the element is null, it means that
	* we should setup our process queue and began executing.
	*/
    public synchronized void bind(QueueElement element) throws IOException {
        if (element == null) {
            setUpProcessQueue();
            return;
        }
        if (currentQueue == null) {
            queues = new QueueControl(null, element);
            currentQueue = queues;
        } else {
            currentQueue.nextElement = new QueueControl(null, element);
            currentQueue = currentQueue.nextElement;
        }
        this.thread.interrupt();
    }

    /**
	*/
    public Thread getThread() {
        return thread;
    }

    /**  Wait until an event is place on queue.
	*/
    public synchronized void loopForBinding() {
        while (queues == null) {
            try {
                wait();
            } catch (InterruptedException ex) {
                if (debug) System.out.println("Interrupting Queue");
            }
            break;
        }
    }

    /**
	*/
    public void processQueue() {
        QueueControl control = workingQueue;
        while (control != null) {
            control.element.myTurn();
            control = control.nextElement;
        }
    }

    /**
	*/
    public void run() {
        while (alive) {
            try {
                loopForBinding();
                bind(null);
                processQueue();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    /**
	* We need very few synchronized methods with this implementation. A call to this
	* will copy the current queue to the working queue.  This will be an ordered list
	* of events.  The processing of the queue can then execute unsynchronized until all
	* elements in the queue have been processed.
	*/
    public synchronized void setUpProcessQueue() {
        workingQueue = queues;
        currentQueue = null;
        queues = null;
    }
}
