package org.cyberaide.queue;

public interface Scheduler {

    void cancelTask(String id);

    void suspendTask(String id);

    void resumeTask(String id);

    void stopScheduler();

    void pauseScheduler();

    void resumeScheduler();
}
