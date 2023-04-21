package com.googlecode.psiprobe.model.jmx;

public class AsyncClusterSender extends SyncClusterSender {

    private long inQueueCounter;

    private long outQueueCounter;

    private long queueSize;

    private long queuedNrOfBytes;

    public long getInQueueCounter() {
        return inQueueCounter;
    }

    public void setInQueueCounter(long inQueueCounter) {
        this.inQueueCounter = inQueueCounter;
    }

    public long getOutQueueCounter() {
        return outQueueCounter;
    }

    public void setOutQueueCounter(long outQueueCounter) {
        this.outQueueCounter = outQueueCounter;
    }

    public long getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(long queueSize) {
        this.queueSize = queueSize;
    }

    public long getQueuedNrOfBytes() {
        return queuedNrOfBytes;
    }

    public void setQueuedNrOfBytes(long queuedNrOfBytes) {
        this.queuedNrOfBytes = queuedNrOfBytes;
    }
}
