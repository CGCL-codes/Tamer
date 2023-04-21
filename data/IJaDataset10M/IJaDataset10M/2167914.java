package org.jsmtpd.core.send;

import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.generic.threadpool.BusyThreadPoolException;
import org.jsmtpd.generic.threadpool.GrowingThreadPool;
import org.jsmtpd.generic.threadpool.ThreadPool;

/**
 * checks for mail on mqueue to be delivered, then passes them to a delivery thread
 * @author Jean-Francois POUX
 */
public class DeliveryPicker extends Thread {

    private ThreadPool pool = null;

    private boolean running = true;

    private QueueService queueService;

    public DeliveryPicker() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ReadConfig readConfig = ReadConfig.getInstance();
        queueService = QueueService.getInstance();
        int numThreads = readConfig.getIntegerProperty("dMaxInstances");
        pool = new GrowingThreadPool(numThreads, "org.jsmtpd.core.send.DeliveryWorker", "S");
        this.start();
    }

    public void run() {
        while (running) {
            if (pool.hasFreeThread()) {
                Email e = null;
                e = queueService.getEmail();
                if (e != null) {
                    try {
                        pool.assignFreeThread(e);
                    } catch (BusyThreadPoolException e1) {
                        queueService.requeueMail(e);
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e2) {
                    }
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void shutdown() {
        running = false;
        wake();
        try {
            this.join();
        } catch (InterruptedException e) {
        }
        pool.forceShutdown();
        queueService.shutdownService();
    }

    public void wake() {
        this.interrupt();
    }
}
