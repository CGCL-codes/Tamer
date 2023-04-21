package ch.oblivion.comixviewer.enginetester.support;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import ch.oblivion.comixviewer.engine.http.DownloadManager;
import ch.oblivion.comixviewer.engine.http.HttpTask;

public class TaskCounter {

    private Logger logger = Logger.getLogger(TaskCounter.class);

    private final DownloadManager manager;

    private final int maxTasks;

    private final AtomicInteger taskCounter;

    public TaskCounter(DownloadManager manager, int maxTasks) {
        this.manager = manager;
        this.maxTasks = maxTasks;
        taskCounter = new AtomicInteger(0);
    }

    public void addTask(HttpTask task) {
        int taskCount;
        do {
            taskCount = taskCounter.get();
        } while (taskCount < maxTasks && !taskCounter.compareAndSet(taskCount, taskCount + 1));
        if (taskCount < maxTasks) {
            logger.info("Added task " + task);
            manager.addTask(task);
        } else {
            logger.info("Shutdown test");
            manager.shutdown();
        }
    }

    public int getTaskCounter() {
        return taskCounter.get();
    }

    public void shutdown() {
        manager.shutdown();
    }
}
