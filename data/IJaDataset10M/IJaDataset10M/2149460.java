package org.donnchadh.gaelbot.tasks;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import org.donnchadh.gaelbot.tasks.factory.LinkVisitorTaskFactory;

public abstract class AbstractUrlQueueProcessingTask implements Runnable, LinkVisitorTaskFactory {

    private final Queue<String> urlQueue;

    private final ExecutorService executor;

    public AbstractUrlQueueProcessingTask(Queue<String> urlQueue, ExecutorService executor) {
        this.urlQueue = urlQueue;
        this.executor = executor;
    }

    public void run() {
        while (!isUrlQueueEmpty()) {
            processNextUrlAndWaitForMore();
        }
    }

    protected void processNextUrlAndWaitForMore() {
        processNextUrl();
        waitForMoreUrls();
    }

    protected void processNextUrl() {
        final String newLink = urlQueue.remove();
        beforeProcessUrl(newLink);
        doProcessUrl(newLink);
    }

    protected void doProcessUrl(final String newLink) {
        executor.execute(buildLinkVisitorTask(newLink));
    }

    public abstract Runnable buildLinkVisitorTask(String newLink);

    protected void waitForMoreUrls() {
        while (isUrlQueueEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected void beforeProcessUrl(final String newLink) {
    }

    protected boolean isUrlQueueEmpty() {
        return getUrlQueue().isEmpty();
    }

    protected Queue<String> getUrlQueue() {
        return urlQueue;
    }
}
