package net.azib.ipscan.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.azib.ipscan.config.ScannerConfig;
import net.azib.ipscan.core.state.ScanningState;
import net.azib.ipscan.core.state.StateMachine;
import net.azib.ipscan.core.state.StateTransitionListener;
import net.azib.ipscan.core.state.StateMachine.Transition;
import net.azib.ipscan.feeders.Feeder;
import net.azib.ipscan.util.InetAddressUtils;

/**
 * Main scanning thread that spawns other threads.
 * 
 * @author Anton Keks
 */
public class ScannerDispatcherThread extends Thread implements ThreadFactory, StateTransitionListener {

    private static final long UI_UPDATE_INTERVAL_MS = 150;

    private ScannerConfig config;

    private Scanner scanner;

    private StateMachine stateMachine;

    private ScanningResultList scanningResultList;

    private Feeder feeder;

    private AtomicInteger numActiveThreads = new AtomicInteger();

    ThreadGroup threadGroup;

    ExecutorService threadPool;

    private ScanningProgressCallback progressCallback;

    private ScanningResultCallback resultsCallback;

    public ScannerDispatcherThread(Feeder feeder, Scanner scanner, StateMachine stateMachine, ScanningProgressCallback progressCallback, ScanningResultList scanningResults, ScannerConfig scannerConfig, ScanningResultCallback resultsCallback) {
        setName(getClass().getSimpleName());
        this.config = scannerConfig;
        this.stateMachine = stateMachine;
        this.progressCallback = progressCallback;
        this.resultsCallback = resultsCallback;
        this.threadGroup = new ThreadGroup(getName());
        this.threadPool = Executors.newFixedThreadPool(config.maxThreads, this);
        setDaemon(true);
        this.feeder = feeder;
        this.scanner = scanner;
        this.scanningResultList = scanningResults;
        try {
            this.scanningResultList.initNewScan(feeder);
            scanner.init();
        } catch (RuntimeException e) {
            stateMachine.reset();
            throw e;
        }
    }

    public void run() {
        try {
            stateMachine.addTransitionListener(this);
            long lastNotifyTime = 0;
            try {
                ScanningSubject subject = null;
                while (feeder.hasNext() && stateMachine.inState(ScanningState.SCANNING)) {
                    Thread.sleep(config.threadDelay);
                    if ((numActiveThreads.intValue() < config.maxThreads)) {
                        subject = feeder.next();
                        if (config.skipBroadcastAddresses && InetAddressUtils.isLikelyBroadcast(subject.getAddress())) {
                            continue;
                        }
                        ScanningResult result = scanningResultList.createResult(subject.getAddress());
                        resultsCallback.prepareForResults(result);
                        AddressScannerTask scanningTask = new AddressScannerTask(subject, result);
                        threadPool.execute(scanningTask);
                    }
                    long now = System.currentTimeMillis();
                    if (now - lastNotifyTime >= UI_UPDATE_INTERVAL_MS) {
                        lastNotifyTime = now;
                        progressCallback.updateProgress(subject.getAddress(), numActiveThreads.intValue(), feeder.percentageComplete());
                    }
                }
            } catch (InterruptedException e) {
            }
            stateMachine.stop();
            threadPool.shutdown();
            try {
                while (!threadPool.awaitTermination(UI_UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS)) {
                    progressCallback.updateProgress(null, numActiveThreads.intValue(), 100);
                }
            } catch (InterruptedException e) {
            }
            scanner.cleanup();
            stateMachine.complete();
        } finally {
            stateMachine.removeTransitionListener(this);
        }
    }

    /**
	 * Local stateMachine transition listener.
	 * Currently used to kill all running threads if user says so.
	 */
    public void transitionTo(ScanningState state, Transition transition) {
        if (state == ScanningState.KILLING) {
            threadGroup.interrupt();
        }
    }

    /**
	 * This will create threads for the pool
	 */
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r);
        thread.setDaemon(true);
        return thread;
    }

    /**
	 * This thread gets executed for each scanned IP address to do the actual
	 * scanning.
	 */
    class AddressScannerTask implements Runnable {

        private ScanningSubject subject;

        private ScanningResult result;

        AddressScannerTask(ScanningSubject subject, ScanningResult result) {
            this.subject = subject;
            this.result = result;
            numActiveThreads.incrementAndGet();
        }

        public void run() {
            Thread.currentThread().setName(getClass().getSimpleName() + ": " + subject);
            try {
                scanner.scan(subject, result);
                resultsCallback.consumeResults(result);
            } finally {
                numActiveThreads.decrementAndGet();
            }
        }
    }
}
