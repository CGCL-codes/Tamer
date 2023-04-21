package com.pbxworkbench.pbx.mock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import com.pbxworkbench.pbx.IPbxCallObserver;
import com.pbxworkbench.pbx.CallOriginationAbortedCause;

public class TestCallObserver implements IPbxCallObserver {

    private Semaphore answered = new Semaphore(1);

    private Semaphore hungup = new Semaphore(1);

    private long timeout;

    public TestCallObserver(long timeout) {
        answered.drainPermits();
        hungup.drainPermits();
        this.timeout = timeout;
    }

    public void onHangup() {
        hungup.release();
    }

    public void onAnswer() {
        answered.release();
    }

    public void onOriginationAborted(CallOriginationAbortedCause cause) {
        answered.release();
        hungup.release();
    }

    public boolean hasHungup() throws InterruptedException {
        return hungup.tryAcquire(timeout, TimeUnit.MILLISECONDS);
    }

    public boolean hasAnswered() throws InterruptedException {
        return answered.tryAcquire(timeout, TimeUnit.MILLISECONDS);
    }

    public void onCallStart() {
    }
}
