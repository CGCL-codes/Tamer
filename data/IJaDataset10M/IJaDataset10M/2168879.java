package net.abhijat.se.process.integration.cvsi;

import net.abhijat.apps.util.async.WorkQueue;
import net.abhijat.apps.util.async.WorkRequest;

class WorkerPoolConnectionHandlerFactory {

    private WorkQueue queue;

    WorkerPoolConnectionHandlerFactory(int numWorkers) {
        this.queue = new WorkQueue("svn-gatekeepers", numWorkers);
    }

    WorkerPoolConnectionHandler newHandler(ConnectionHandler actualHandler) {
        return new WorkerPoolConnectionHandler(actualHandler, this);
    }

    void addToQueue(WorkRequest request) {
        queue.executeAsynchronously(request);
    }
}
