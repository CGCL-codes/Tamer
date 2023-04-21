package org.dfdaemon.il2.core.exec;

import java.util.concurrent.ThreadFactory;

/**
 * @author octo
 */
public class DaemonThreadFactory implements ThreadFactory {

    String prefix;

    int count = 0;

    int priority = Thread.NORM_PRIORITY;

    static ThreadGroup group = new ThreadGroup("dcghelper");

    public DaemonThreadFactory(String prefix) {
        this.prefix = "daemon." + prefix + "-";
    }

    public DaemonThreadFactory(String prefix, int priority) {
        this.prefix = "daemon." + prefix + "-";
        this.priority = priority;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, prefix + count++);
        t.setDaemon(true);
        t.setPriority(priority);
        return t;
    }
}
