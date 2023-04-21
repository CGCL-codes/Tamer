package org.appspy.client.common;

public class CollectorInfo {

    public static final int STATUS_RUNNING = 0;

    public static final int STATUS_STOPPED = 1;

    public static final int STATUS_BAD_CONFIG = 2;

    public static final int STATUS_ERROR = 3;

    protected int mStatus = STATUS_STOPPED;

    protected long mTotalHits = 0;

    protected long mTotalCollected = 0;

    protected Throwable mThrowable = null;

    protected CollectorInfo() {
    }

    public void hit() {
        mTotalHits++;
    }

    public long getTotalHits() {
        return mTotalHits;
    }

    public long getTotalCollected() {
        return mTotalCollected;
    }

    public void collect() {
        mTotalCollected++;
    }

    public int getStatus() {
        return mStatus;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setStatus(int status, Throwable t) {
        mStatus = status;
        mThrowable = t;
    }
}
