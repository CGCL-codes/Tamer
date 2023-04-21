package org.bing.engine.utility.helper;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DaemonThreadFactory implements ThreadFactory {

    static final AtomicInteger poolNumber = new AtomicInteger(1);

    final ThreadGroup group;

    final AtomicInteger threadNumber = new AtomicInteger(1);

    final String namePrefix;

    final boolean deamon;

    public DaemonThreadFactory(boolean deamon) {
        this.deamon = deamon;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "bing-dtf-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (deamon) {
            t.setDaemon(true);
        }
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
