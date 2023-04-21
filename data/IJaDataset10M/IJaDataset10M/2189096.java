package fr.xebia.xke.concurrency.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import fr.xebia.concurrent.CyclicLatch;
import fr.xebia.xke.concurrency.Basket;
import fr.xebia.xke.concurrency.MyThreadFactory;

public class MyBasket implements Basket {

    private final ExecutorService consumerPool;

    private final CyclicLatch latch;

    private final BlockingQueue<String> queue;

    private final Consumer consumer;

    public MyBasket(int limit, int timeout) {
        consumerPool = Executors.newFixedThreadPool(1, new MyThreadFactory("Consumer"));
        queue = new LinkedBlockingQueue<String>();
        latch = new CyclicLatch(limit, timeout, TimeUnit.SECONDS);
        consumer = new Consumer(queue, latch);
    }

    public void put(String data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

    public void start() {
        System.out.println("MyBasket.start()");
        consumerPool.submit(consumer);
    }

    public void stop() {
        System.out.println("MyBasket.stop()");
        consumerPool.shutdownNow();
        try {
            consumerPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public int getSize() {
        return consumer.getCompteur();
    }

    public int getResetCount() {
        return consumer.getNbCountReached();
    }

    private final class Consumer implements Runnable {

        private final BlockingQueue<String> queue;

        private final List<String> messages = new ArrayList<String>();

        private int compteur = 0;

        private final CyclicLatch latch;

        private int nbTimeout = 0;

        private int nbCountReached = 0;

        Consumer(BlockingQueue<String> queue, CyclicLatch latch) {
            log("Consumer.Consumer()");
            this.queue = queue;
            this.latch = latch;
        }

        public void run() {
            log("Consumer.run()");
            while (true) {
                try {
                    long start = System.nanoTime();
                    boolean awaitTimeOut = !latch.await();
                    if (awaitTimeOut) {
                        log("TimeOut,  " + getElapsedTime(start) + "s");
                        nbTimeout++;
                    } else {
                        log("OverFlow, " + getElapsedTime(start) + "s");
                        nbCountReached++;
                    }
                    process();
                } catch (InterruptedException e) {
                    log(e + " Consumer has been Interrupted, process & break ..");
                    process();
                    break;
                }
            }
        }

        private float getElapsedTime(long start) {
            return ((float) (System.nanoTime() - start)) / ((float) (1000 * 1000 * 1000));
        }

        private void process() {
            queue.drainTo(messages);
            if (messages.size() > 0) {
                log("messages #" + messages.size());
                compteur = compteur + messages.size();
            }
            messages.clear();
        }

        private int getCompteur() {
            return compteur;
        }

        private int getNbTimeout() {
            return nbTimeout;
        }

        private int getNbCountReached() {
            return nbCountReached;
        }
    }

    void log(Object o) {
        System.out.println(Thread.currentThread().getName() + " " + o);
    }
}
