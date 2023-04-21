package de.javagimmicks.concurrent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import de.javagimmicks.concurrent.impl.DefaultLockProviderFactory;

public class MultiLockProviderTest {

    private static final int NUM_RESOURCES = 20;

    private static final int NUM_WORKERS = 50;

    private static final long WAIT_TIME = 1000;

    private static final long WORK_TIME = 250;

    private static final int FRACTION_EX = 10;

    private static final int FRACTION_LOCKS = 4;

    private static final PrintStream LOG;

    private static final Random RANDOM = new Random();

    private static final List<Character> RESOURCES = new ArrayList<Character>(NUM_RESOURCES);

    private static final Format FORMAT = new MessageFormat("{0,number,00}");

    private static final MultiLockProvider<Character> PROVIDER = DefaultLockProviderFactory.createHashBasedInstance();

    static {
        for (int i = 0; i < NUM_RESOURCES; ++i) {
            RESOURCES.add((char) ('a' + i));
        }
        PrintStream log;
        try {
            log = new PrintStream(new FileOutputStream("status.log"));
        } catch (FileNotFoundException e) {
            log = System.out;
        }
        LOG = log;
    }

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<Thread>(NUM_WORKERS);
        for (int i = 1; i <= NUM_WORKERS; ++i) {
            Thread thread = new Thread(new TimeoutTryWorker(i));
            threads.add(thread);
            thread.start();
        }
        JOptionPane.showMessageDialog(null, "Click to exit");
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    protected static class TimeoutTryWorker extends Worker {

        public TimeoutTryWorker(int id) {
            super(id);
        }

        @Override
        protected boolean doLock(MultiLock<Character> lock) throws InterruptedException {
            return lock.tryLock(WAIT_TIME, TimeUnit.MILLISECONDS);
        }
    }

    protected static class TryWorker extends Worker {

        public TryWorker(int id) {
            super(id);
        }

        @Override
        protected boolean doLock(MultiLock<Character> lock) throws InterruptedException {
            return lock.tryLock();
        }
    }

    protected static class StrictLockWorker extends Worker {

        public StrictLockWorker(int id) {
            super(id);
        }

        @Override
        protected boolean doLock(MultiLock<Character> lock) throws InterruptedException {
            lock.lock();
            return true;
        }
    }

    protected abstract static class Worker implements Runnable {

        protected final int _id;

        public Worker(int id) {
            _id = id;
        }

        protected abstract boolean doLock(MultiLock<Character> lock) throws InterruptedException;

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                List<Character> resources = getResources();
                boolean shared = RANDOM.nextInt(FRACTION_EX) != 0;
                MultiReadWriteLock<Character> rwLock = PROVIDER.newLock(resources);
                MultiLock<Character> lock = shared ? rwLock.readLock() : rwLock.writeLock();
                log(shared, resources, "ready");
                try {
                    long time = System.currentTimeMillis();
                    if (doLock(lock)) {
                        time = System.currentTimeMillis() - time;
                        log(shared, resources, "lock (" + time + ")");
                        try {
                            Thread.sleep(WORK_TIME);
                        } catch (InterruptedException e) {
                            log(shared, resources, "aborted work");
                            lock.unlock();
                            log(shared, resources, "unlock");
                            break;
                        }
                        lock.unlock();
                        log(shared, resources, "unlock");
                    } else {
                        log(shared, resources, "failed");
                    }
                } catch (InterruptedException e) {
                    log(shared, resources, "interrupted");
                    break;
                }
            }
        }

        public String toString() {
            return "Worker" + FORMAT.format(new Object[] { _id });
        }

        protected List<Character> getResources() {
            List<Character> result = new ArrayList<Character>(NUM_RESOURCES);
            for (Character c : RESOURCES) {
                if (RANDOM.nextInt(FRACTION_LOCKS) == 0) {
                    result.add(c);
                }
            }
            return result;
        }

        protected void log(boolean shared, List<Character> resources, String message) {
            String output = new StringBuilder().append(toString()).append(" ").append(shared ? "SH " : "EX ").append(message).append(" ").append(resources.toString()).toString();
            LOG.println(output);
        }
    }
}
