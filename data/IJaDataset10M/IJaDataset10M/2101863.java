package javax.media.ding3d;

import java.util.ArrayList;
import java.util.logging.Level;

class BehaviorScheduler extends Ding3dThread {

    /**
     * The virtual universe that owns this BehaviorScheduler
     */
    VirtualUniverse univ = null;

    UnorderList processList[];

    IndexedUnorderSet scheduleList;

    BehaviorStructure behaviorStructure;

    int stopCount = -1;

    /**
     * These are used for start/stop BehaviorScheduler
     */
    long lastStartTime;

    long lastStopTime;

    Object intervalTimeLock = new Object();

    /**
     * Some variables used to name threads correctly
     */
    private static int numInstances = 0;

    private int instanceNum = -1;

    private synchronized int newInstanceNum() {
        return (++numInstances);
    }

    int getInstanceNum() {
        if (instanceNum == -1) instanceNum = newInstanceNum();
        return instanceNum;
    }

    BehaviorScheduler(ThreadGroup t, VirtualUniverse universe) {
        super(t);
        setName("Ding3d-BehaviorScheduler-" + getInstanceNum());
        this.univ = universe;
        behaviorStructure = universe.behaviorStructure;
        scheduleList = behaviorStructure.scheduleList;
        processList = behaviorStructure.processList;
        type = Ding3dThread.BEHAVIOR_SCHEDULER;
    }

    void stopBehaviorScheduler(long[] intervalTime) {
        stopCount = 2;
        VirtualUniverse.mc.sendRunMessage(univ, Ding3dThread.BEHAVIOR_SCHEDULER);
        while (!userStop) {
            MasterControl.threadYield();
        }
        synchronized (intervalTimeLock) {
            intervalTime[0] = lastStartTime;
            intervalTime[1] = lastStopTime;
        }
    }

    void startBehaviorScheduler() {
        synchronized (intervalTimeLock) {
            stopCount = -1;
            userStop = false;
            VirtualUniverse.mc.setWork();
        }
    }

    void deactivate() {
        active = false;
        if (stopCount >= 0) {
            userStop = true;
        }
    }

    /**
     * The main loop for the Behavior Scheduler.
     * Main method for firing off vector of satisfied conditions that
     * are contained in the condMet vector.  Method is synchronized
     * because it is modifying the current wakeup vectors in the
     * clean (emptying out satisfied conditions) and processStimulus
     * (adding conditions again if wakeupOn called) calls.
     */
    void doWork(long referenceTime) {
        BehaviorRetained arr[];
        UnorderList list;
        int i, size, interval;
        lastStartTime = Ding3dClock.currentTimeMillis();
        if (stopCount >= 0) {
            VirtualUniverse.mc.sendRunMessage(univ, Ding3dThread.BEHAVIOR_SCHEDULER);
            if (--stopCount == 0) {
                userStop = true;
            }
        }
        for (interval = 0; interval < BehaviorRetained.NUM_SCHEDULING_INTERVALS; interval++) {
            list = processList[interval];
            if (list.isEmpty()) {
                continue;
            }
            arr = (BehaviorRetained[]) list.toArray(false);
            size = list.arraySize();
            for (i = 0; i < size; i++) {
                BehaviorRetained behavret = arr[i];
                synchronized (behavret) {
                    Behavior behav = (Behavior) behavret.source;
                    if (!behav.isLive() || !behavret.conditionSet || (behavret.wakeupCondition == null)) {
                        continue;
                    }
                    if (behavret.wakeupCondition.trigEnum == null) {
                        behavret.wakeupCondition.trigEnum = new WakeupCriteriaEnumerator(behavret.wakeupCondition, WakeupCondition.TRIGGERED_ELEMENTS);
                    } else {
                        behavret.wakeupCondition.trigEnum.reset(behavret.wakeupCondition, WakeupCondition.TRIGGERED_ELEMENTS);
                    }
                    behavret.conditionSet = false;
                    WakeupCondition wakeupCond = behavret.wakeupCondition;
                    synchronized (behavret) {
                        behavret.inCallback = true;
                        univ.inBehavior = true;
                        try {
                            behav.processStimulus(wakeupCond.trigEnum);
                        } catch (RuntimeException e) {
                            behavret.conditionSet = false;
                            System.err.println("Exception occurred during Behavior execution:");
                            e.printStackTrace();
                        } catch (Error e) {
                            behavret.conditionSet = false;
                            System.err.println("Error occurred during Behavior execution:");
                            e.printStackTrace();
                        }
                        univ.inBehavior = false;
                        behavret.inCallback = false;
                    }
                    if (behavret.conditionSet == false) {
                        if (wakeupCond != null) {
                            wakeupCond.cleanTree(behaviorStructure);
                        }
                        behavret.wakeupCondition = null;
                        behavret.active = false;
                        scheduleList.remove(behavret);
                    } else {
                        behavret.handleLastWakeupOn(wakeupCond, behaviorStructure);
                    }
                }
            }
            list.clear();
        }
        behaviorStructure.handleAWTEvent();
        behaviorStructure.handleBehaviorPost();
        lastStopTime = Ding3dClock.currentTimeMillis();
        if (MasterControl.isStatsLoggable(Level.FINE)) {
            VirtualUniverse.mc.recordTime(MasterControl.TimeType.BEHAVIOR, (lastStopTime - lastStartTime) * 1000000);
        }
    }

    void free() {
        behaviorStructure = null;
        getThreadData(null, null).thread = null;
        univ = null;
        for (int i = BehaviorRetained.NUM_SCHEDULING_INTERVALS - 1; i >= 0; i--) {
            processList[i].clear();
        }
        scheduleList.clear();
    }
}
