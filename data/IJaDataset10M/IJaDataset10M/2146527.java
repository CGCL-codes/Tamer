package com.ibm.tuningfork.traceGenerationExample;

import com.ibm.tuningfork.tracegen.IFeedlet;
import com.ibm.tuningfork.tracegen.ILogger;
import com.ibm.tuningfork.tracegen.ITimerEvent;

public class SyntheticTransactionThread extends Thread {

    protected final double durationSec;

    double privateWorkAmount = 0.019;

    double synchedWorkAmount = 0.001;

    double sleepTimeSec = 0;

    int allocAmountKB = 20;

    int liveMemoryKB = 1000;

    boolean randomize = true;

    protected byte[][] allocatedMemory;

    protected int allocCursor;

    protected final ITimerEvent transactionEvent;

    protected final ITimerEvent synchronizeEvent;

    protected final ITimerEvent allocateEvent;

    protected final ITimerEvent sleepEvent;

    protected final IFeedlet traceFeedlet;

    protected final CPUBurner cpuBurner;

    public SyntheticTransactionThread(String id, double duration, ILogger logger, CPUBurner quatloo) {
        this.durationSec = duration;
        this.cpuBurner = quatloo;
        this.traceFeedlet = logger.makeFeedlet(SyntheticWorkloadClient.TRANSACTION_FEEDLET_PREFIX + id, "Simulated Transactions " + id);
        this.transactionEvent = logger.makeTimerEvent(SyntheticWorkloadClient.TRANSACTION_NAME_PREFIX + id);
        this.allocateEvent = logger.makeTimerEvent("Allocate by Worker " + id);
        this.synchronizeEvent = logger.makeTimerEvent("Synchronize by Worker " + id);
        this.sleepEvent = logger.makeTimerEvent("Sleep by Worker " + id);
        allocatedMemory = new byte[liveMemoryKB][];
    }

    public void run() {
        traceFeedlet.bindToCurrentThread();
        long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1e9 < durationSec) {
            doPrivateWork();
            doSharedWork();
            doSleep();
        }
    }

    protected void doPrivateWork() {
        double onLen = randomize ? privateWorkAmount : (privateWorkAmount * Math.random());
        if (onLen > 0) {
            transactionEvent.start();
            cpuBurner.doWorkFor(onLen);
            transactionEvent.stop();
            if (false) {
                allocateEvent.start();
                for (int i = 0; i < allocAmountKB; i++) {
                    allocatedMemory[allocCursor % allocatedMemory.length] = new byte[1000];
                    allocCursor++;
                }
                allocateEvent.stop();
            }
        }
    }

    protected void doSharedWork() {
        double synchLen = randomize ? synchedWorkAmount : synchedWorkAmount * Math.random();
        if (synchLen > 0) {
            synchronizeEvent.start();
            cpuBurner.doSynchronizedWorkFor(synchLen);
            synchronizeEvent.stop();
        }
    }

    protected void doSleep() {
        double sleepLen = randomize ? sleepTimeSec : sleepTimeSec * Math.random();
        if (sleepTimeSec > 0) {
            sleepEvent.start();
            try {
                Thread.sleep((int) (sleepLen * 1000));
            } catch (InterruptedException ie) {
            }
            sleepEvent.stop();
        }
    }
}
