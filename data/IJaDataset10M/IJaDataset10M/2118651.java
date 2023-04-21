package net.sf.jerkbot.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         Jerkbot thread that keeps statistics, can be useful for reporting or debugging to identify threads easily
 * @version 0.0.1
 */
public class JerkbotThread extends Thread {

    public static final String DEFAULT_NAME = "JerkbotThread";

    private static final AtomicInteger created = new AtomicInteger();

    private static final AtomicInteger alive = new AtomicInteger();

    private static final Logger log = LoggerFactory.getLogger(JerkbotThread.class.getName());

    public JerkbotThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public JerkbotThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                log.error("UNCAUGHT in thread " + t.getName(), e);
            }
        });
    }

    @Override
    public void run() {
        log.debug("Created {}", getName());
        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            log.debug("Exiting {}", getName());
        }
    }

    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }
}
