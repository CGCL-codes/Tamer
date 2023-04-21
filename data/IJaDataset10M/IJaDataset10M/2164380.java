package com.ixora.common.thread;

import com.ixora.common.Startable;
import com.ixora.common.exception.AppRuntimeException;
import com.ixora.common.logging.AppLogger;
import com.ixora.common.logging.AppLoggerFactory;

/**
 * Asynchronous processor of runnables.
 * @author Daniel Moraru
 */
public final class RunQueue implements Startable {

    /** Logger */
    private static final AppLogger logger = AppLoggerFactory.getLogger(RunQueue.class);

    /** PCQueue */
    private BlockingQueue<Runnable> queue;

    /** Queue consumer thread */
    private Thread consumer;

    /** Stop flag */
    private volatile boolean bStop = false;

    /** Stop and wait flag */
    private volatile boolean bStopAndWait = false;

    /** Flag set to true when the last runnable finished processing */
    private volatile boolean bLastRunnableFinished = false;

    /**
	 * Constructs a processor as a daemon.
	 */
    public RunQueue() {
        this(true);
    }

    /**
	 * Constructor for AsynchProcessor.
	 * @param daemon True if it must run as a daemon.
	 */
    public RunQueue(boolean daemon) {
        super();
        this.queue = new BlockingQueue<Runnable>();
        this.consumer = new Thread("RunQueue") {

            public void run() {
                RunQueue.this.processQueue();
            }
        };
        this.consumer.setDaemon(daemon);
    }

    /**
	 * @see com.ixora.common.Startable#start()
	 */
    public void start() {
        this.consumer.start();
    }

    /**
     * @see com.ixora.common.Startable#stop()
     */
    public void stop() {
        try {
            this.bStop = true;
            this.consumer.interrupt();
            this.consumer.join();
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    /**
	 * It will run asynchronously the given runnable.
	 * @param r
	 */
    public void run(Runnable r) {
        try {
            if (bStopAndWait) {
                throw new AppRuntimeException("Queue was stopped");
            }
            this.queue.put(r);
        } catch (InterruptedException e) {
            ;
        }
    }

    /**
	 * @return The number of elements awaiting execution.
	 */
    public int getNoOfQueuedElements() {
        return this.queue.size();
    }

    /**
	 * Processes queue entries.
	 */
    private void processQueue() {
        while (!bStop) {
            Runnable run = null;
            try {
                run = (Runnable) this.queue.get();
                bLastRunnableFinished = false;
            } catch (InterruptedException e) {
                break;
            }
            try {
                run.run();
            } catch (Throwable e) {
                logger.error(e);
            } finally {
                bLastRunnableFinished = true;
            }
        }
    }

    public void stopAndWaitForQueueToClear() {
        bStopAndWait = true;
        while (this.queue.size() > 0 || !bLastRunnableFinished) {
            ;
        }
        stop();
    }
}
