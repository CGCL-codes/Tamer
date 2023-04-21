package org.apache.commons.threadpool;

/**
 * NullObject pattern impl of ThreadPoolMonitor
 */
public class NullThreadPoolMonitor implements ThreadPoolMonitor {

    public void handleThrowable(Class clazz, Runnable runnable, Throwable t) {
    }
}
