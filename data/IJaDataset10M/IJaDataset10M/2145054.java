package org.avis.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.avis.logging.Log.info;
import static org.avis.logging.Log.warn;

/**
 * Test a single client connection used with high degree of
 * concurrency from a number of threads. This exposes a race condition
 * in Avis client 1.1.0 on Java 1.6.0.
 * 
 * @author Matthew Phillips
 */
public class ConcurrentClientTester {

    private static final int WORKER_COUNT = 20;

    public static final int CHANNELS = 30;

    public static final int MESSAGE_COUNT = 300;

    private static final int CYCLES = 30;

    public static void main(String[] args) throws Exception {
        new ConcurrentClientTester().run();
    }

    protected Elvin elvin;

    protected CyclicBarrier barrier;

    protected volatile int cycle;

    private void run() throws Exception {
        elvin = new Elvin(System.getProperty("elvin", "elvin://localhost"));
        barrier = new CyclicBarrier(WORKER_COUNT + 1, new Runnable() {

            public void run() {
                cycle++;
            }
        });
        ExecutorService executor = newFixedThreadPool(WORKER_COUNT);
        for (int i = 0; i < WORKER_COUNT; i++) executor.execute(new Worker(i));
        while (cycle < CYCLES) {
            barrier.await();
            info("Completed cycle " + cycle, this);
        }
        if (executor.shutdownNow().size() > 0) warn("Tasks did not shut down", this);
        elvin.close();
    }

    public class Worker implements Runnable, NotificationListener {

        private Collection<Subscription> subscriptions;

        private Random random;

        private int notifications;

        public Worker(int taskNumber) {
            this.random = new Random(taskNumber);
            this.subscriptions = new ArrayList<Subscription>();
        }

        public void run() {
            try {
                while (cycle < CYCLES) {
                    notifications = 0;
                    subscribe();
                    sleep(abs(random.nextLong() % 1000));
                    emit();
                    sleep(abs(random.nextLong() % 1000));
                    unsubscribe();
                    barrier.await();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void subscribe() throws IOException {
            for (int i = 0; i < CHANNELS / 2; i++) {
                Subscription subscription = elvin.subscribe("Channel == " + randomChannel());
                subscriptions.add(subscription);
                subscription.addListener(this);
            }
        }

        private int randomChannel() {
            return abs(random.nextInt()) % CHANNELS;
        }

        public void notificationReceived(NotificationEvent e) {
            notifications++;
        }

        private void emit() throws IOException {
            for (int i = 0; i < MESSAGE_COUNT; i++) elvin.send(new Notification("Channel", randomChannel()));
        }

        private void unsubscribe() throws IOException {
            for (Subscription sub : subscriptions) sub.remove();
        }
    }
}
