package com.apachetune.core;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public interface WorkItemLifecycleListener {

    void onInitialized(WorkItem workItem);

    void onDisposed(WorkItem workItem);
}
