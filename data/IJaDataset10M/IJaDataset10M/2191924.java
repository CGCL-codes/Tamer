package net.nycjava.skylight1.service.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import net.nycjava.skylight1.service.CountdownObserver;
import net.nycjava.skylight1.service.CountdownPublicationService;

public class CountdownPublicationServiceImpl implements CountdownPublicationService {

    private int duration = 0;

    private int currentCount = 0;

    private boolean stopRequested = false;

    private Set<CountdownObserver> countdownObservers = new HashSet<CountdownObserver>();

    private CounterStatus currentStatus = CounterStatus.uninitialized;

    private Timer countdownTimer;

    public void addObserver(CountdownObserver anObserver) {
        countdownObservers.add(anObserver);
        if (this.currentStatus == CounterStatus.running) {
            int remain = getRemainingTime();
            notifyObservers(remain);
        }
    }

    public boolean removeObserver(CountdownObserver anObserver) {
        final boolean existed = countdownObservers.remove(anObserver);
        return existed;
    }

    public void setDuration(int seconds) {
        duration = seconds;
    }

    private int getRemainingTime() {
        int aRemainingTime = this.duration - currentCount;
        return aRemainingTime;
    }

    class CountdownTask extends TimerTask {

        public void run() {
            if (duration == 0) return;
            currentStatus = CounterStatus.running;
            if (currentCount < duration && stopRequested == false) {
                currentCount = currentCount + 1;
                notifyObservers(getRemainingTime());
            }
            if (currentCount == duration) {
                currentStatus = CounterStatus.finished;
            }
            if (stopRequested) {
                currentStatus = CounterStatus.stopped;
            }
        }
    }

    public void startCountdown() {
        if (this.duration == 0) {
            return;
        } else if (this.currentStatus == CounterStatus.running) {
            return;
        }
        countdownTimer = new Timer();
        CountdownTask countdownTask = new CountdownTask();
        long zeroDelay = 0;
        countdownTimer.scheduleAtFixedRate(countdownTask, zeroDelay, 1000);
    }

    public void stopCountdown() {
        stopRequested = true;
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        currentStatus = CounterStatus.stopped;
    }

    private void notifyObservers(int aRemainingTime) {
        List<CountdownObserver> countdownObserversSnapshot = new LinkedList<CountdownObserver>(countdownObservers);
        for (final CountdownObserver countdownObserver : countdownObserversSnapshot) {
            countdownObserver.countdownNotification(aRemainingTime);
        }
    }

    @Override
    public CounterStatus getStatus() {
        return currentStatus;
    }
}
