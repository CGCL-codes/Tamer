package com.sefer.dragonfly.client.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 守护线程工厂,用于替换JDK调度框架默认线程工厂
 * 
 * @author xiaofeng 2011-9-9 上午09:35:08
 */
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
        namePrefix = "iris-monitor-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (deamon) {
            t.setDaemon(true);
        }
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }

    public Thread newThread(Runnable r, int count) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), count);
        if (deamon) {
            t.setDaemon(true);
        }
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
