package org.perfmon4j;

import org.perfmon4j.ThreadTraceMonitor.UniqueThreadTraceTimerKey;
import org.perfmon4j.remotemanagement.ExternalAppender;
import org.perfmon4j.util.Logger;
import org.perfmon4j.util.LoggerFactory;
import org.perfmon4j.util.MiscHelper;

public class PerfMonTimer {

    private static final Logger logger = LoggerFactory.initLogger(PerfMonTimer.class);

    private static final PerfMonTimer NULL_TIMER = new PerfMonTimer(null, null);

    final PerfMon perfMon;

    final PerfMonTimer next;

    /**
     * Package level... Only PerfMon.class should invoke this constructor
     * Applications using the PerfMonTimer should invoke the static method
     * PerfMonTimer.start();
     */
    PerfMonTimer(PerfMon perfMon, PerfMonTimer next) {
        this.perfMon = perfMon;
        this.next = next;
    }

    public static PerfMonTimer start(PerfMon mon) {
        if (!PerfMon.isConfigured() && !ExternalAppender.isActive()) {
            return NULL_TIMER;
        }
        PerfMonTimer result = mon.getPerfMonTimer();
        final boolean haveActiveTimer = (NULL_TIMER != result);
        final boolean haveActiveThreadTrace = ThreadTraceMonitor.activeThreadTraceFlag.get().isActive();
        if (haveActiveTimer || haveActiveThreadTrace) {
            String monitorName = "";
            UniqueThreadTraceTimerKey wrapperInternalKey = null;
            UniqueThreadTraceTimerKey wrapperExternalKey = null;
            try {
                long startTime = MiscHelper.currentTimeWithMilliResolution();
                monitorName = mon.getName();
                if (haveActiveThreadTrace) {
                    ThreadTraceMonitor.ThreadTracesOnStack tInternalOnStack = ThreadTraceMonitor.getInternalThreadTracesOnStack();
                    ThreadTraceMonitor.ThreadTracesOnStack tExternalOnStack = ThreadTraceMonitor.getExternalThreadTracesOnStack();
                    final boolean haveActiveInternalThreadTrace = tInternalOnStack.isActive();
                    final boolean haveActiveExternalThreadTrace = tExternalOnStack.isActive();
                    if (haveActiveInternalThreadTrace) {
                        wrapperInternalKey = tInternalOnStack.enterCheckpoint(monitorName, startTime);
                    }
                    if (haveActiveExternalThreadTrace) {
                        wrapperExternalKey = tExternalOnStack.enterCheckpoint(monitorName, startTime);
                    }
                }
                if (haveActiveTimer) {
                    result.start(startTime);
                }
            } catch (ThreadDeath th) {
                throw th;
            } catch (Throwable th) {
                logger.logError("Error starting monitor: " + monitorName, th);
                result = NULL_TIMER;
            }
            if (haveActiveThreadTrace) {
                result = new TimerWrapperWithThreadTraceKey(result, wrapperInternalKey, wrapperExternalKey);
            }
        }
        return result;
    }

    public static PerfMonTimer start(String key) {
        return start(key, false);
    }

    /**
     * Pass in true if this is a dynamically generated key (i.e. not a method
     * name or some know value.  This prevents monitors from being created
     * that are not actively attached to appenders.
     * 
     * for example:
     * 	   private void lookupUser(String userName) {
     * 		    PerfMonTimer.start("lookupUser." + userName, true); 
     * 			...
     * 	   }
     */
    public static PerfMonTimer start(String key, boolean isDynamicKey) {
        PerfMonTimer result = NULL_TIMER;
        try {
            if (PerfMon.isConfigured() || ExternalAppender.isActive()) {
                result = start(PerfMon.getMonitor(key, isDynamicKey));
            }
        } catch (ThreadDeath th) {
            throw th;
        } catch (Throwable th) {
            logger.logError("Error starting monitor: " + key, th);
            result = NULL_TIMER;
        }
        return result;
    }

    private void start(long now) {
        if (perfMon != null) {
            perfMon.start(now);
            next.start(now);
        }
    }

    private static void stop(PerfMonTimer timer, boolean abort) {
        try {
            if (timer != NULL_TIMER && timer != null) {
                UniqueThreadTraceTimerKey keyInternal = timer.getUniqueInternalTimerKey();
                if (keyInternal != null) {
                    ThreadTraceMonitor.ThreadTracesOnStack tOnStack = ThreadTraceMonitor.getInternalThreadTracesOnStack();
                    tOnStack.exitCheckpoint(keyInternal);
                }
                UniqueThreadTraceTimerKey keyExternal = timer.getUniqueExternalTimerKey();
                if (keyExternal != null) {
                    ThreadTraceMonitor.ThreadTracesOnStack tOnStack = ThreadTraceMonitor.getExternalThreadTracesOnStack();
                    tOnStack.exitCheckpoint(keyExternal);
                }
                timer.stop(MiscHelper.currentTimeWithMilliResolution(), abort);
            }
        } catch (ThreadDeath th) {
            throw th;
        } catch (Throwable th) {
            logger.logError("Error stopping timer", th);
        }
    }

    public static void abort(PerfMonTimer timer) {
        stop(timer, true);
    }

    public static void stop(PerfMonTimer timer) {
        stop(timer, false);
    }

    private void stop(long now, boolean abort) {
        if (perfMon != null) {
            next.stop(now, abort);
            perfMon.stop(now, abort);
        }
    }

    public static PerfMonTimer getNullTimer() {
        return NULL_TIMER;
    }

    /**
     * Used for the ThreadTraceTimers...
     * @return
     */
    protected UniqueThreadTraceTimerKey getUniqueInternalTimerKey() {
        return null;
    }

    /**
     * Used for the ThreadTraceTimers...
     * @return
     */
    protected UniqueThreadTraceTimerKey getUniqueExternalTimerKey() {
        return null;
    }

    /**
     * This class is only used when we return a Timer that is part of
     * a thread trace.
     */
    private static class TimerWrapperWithThreadTraceKey extends PerfMonTimer {

        private final UniqueThreadTraceTimerKey uniqueInternalThreadTraceTimerKey;

        private final UniqueThreadTraceTimerKey uniqueExternalThreadTraceTimerKey;

        private TimerWrapperWithThreadTraceKey(PerfMonTimer timer, UniqueThreadTraceTimerKey uniqueInternalThreadTraceTimerKey, UniqueThreadTraceTimerKey uniqueExternalThreadTraceTimerKey) {
            super(timer.perfMon, timer.next);
            this.uniqueInternalThreadTraceTimerKey = uniqueInternalThreadTraceTimerKey;
            this.uniqueExternalThreadTraceTimerKey = uniqueExternalThreadTraceTimerKey;
        }

        protected UniqueThreadTraceTimerKey getUniqueInternalTimerKey() {
            return uniqueInternalThreadTraceTimerKey;
        }

        protected UniqueThreadTraceTimerKey getUniqueExternalTimerKey() {
            return uniqueExternalThreadTraceTimerKey;
        }
    }
}
