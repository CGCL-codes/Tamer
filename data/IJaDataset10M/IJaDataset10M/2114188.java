package org.openmim.icq2k;

import java.io.*;
import java.net.*;
import java.util.*;
import org.openmim.*;
import org.openmim.icq.util.joe.*;
import org.openmim.infrastructure.taskmanager.*;
import org.openmim.infrastructure.scheduler.Scheduler;

public class ReconnectManager {

    private static final org.apache.log4j.Logger CAT = org.apache.log4j.Logger.getLogger(ReconnectManager.class.getName());

    private final Scheduler sche = new Scheduler(ICQ2KMessagingNetwork.REQPARAM_RECONNECTOR_THREADCOUNT_MAXIMUM, ICQ2KMessagingNetwork.REQPARAM_RECONNECTOR_THREADCOUNT_OPTIMUM);

    static void initReconnectManagerState(SessionReconnecting ses) {
        ses.reconnectManagerState = 1;
    }

    static void reconnectFailed(SessionReconnecting ses) {
        int s = ses.reconnectManagerState;
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug(ses + ": r_state: " + r_state2string(s));
        if (s == 3) return;
        if (s < 1 || s > 3) s = 1; else ++s;
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("r_state replaced to " + r_state2string(s));
        ses.reconnectManagerState = s;
    }

    public ReconnectManager() {
    }

    private boolean initialised = false;

    private synchronized boolean isInitialised() {
        return initialised;
    }

    private final synchronized void checkInitialised() {
        if (!initialised) throw new RuntimeException("not initialized");
    }

    private synchronized void runAt(long time, Task t) {
        checkInitialised();
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug(t + " scheduled at ~" + ((time - System.currentTimeMillis()) / 1000) + " seconds later");
        sche.runAt(time, t);
    }

    private synchronized void runNow(Task t) {
        checkInitialised();
        sche.runNow(t);
    }

    /**
    "ReloginProcess:"
    Event state 1: Perform single relogin after 5+Math.random()*10seconds.  If it fails, enter stage 2.
    Event state 2: Perform single relogin after 50 seconds+Math.random()*20seconds.  If it fails, enter stage 3.
    Event state 3: Perform relogin after 4.5 minutes+Math.random()*60seconds. If it fails, perform relogins infinitely,
                   with the interval of 4.5 minutes+Math.random()*60seconds, until succeeded.
    During any event state, any offline status reason categories except for network errors remove
    the event from the relogin process, and cause the offline status notification to be sent.
  */
    void scheduleReconnect(SessionReconnecting ev) {
        scheduleReconnect(ev, -1);
    }

    void scheduleReconnect(SessionReconnecting ev, int approxSeconds) {
        if (approxSeconds > 0) ev.reconnectManagerState = 4; else {
            if (ev.reconnectManagerState == 4) initReconnectManagerState(ev);
            if (ev.isRegisteredReloginsMaximumReached()) {
                if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("relogins for " + ev + " are too frequent, sleeping");
                scheduleEvent(ev, 1000L * (60 * ICQ2KMessagingNetworkReconnecting.REQPARAM_NETWORK_CONDITIONS_SWING__SLEEP_TIME_WHEN_MAXIMUM_REACHED_MINUTES + (int) (Math.random() * 60)));
                return;
            }
        }
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug(ev.getLoginId() + ": r_sched_event: ev.r_state: " + r_state2string(ev.reconnectManagerState));
        switch(ev.reconnectManagerState) {
            case 1:
                scheduleEvent(ev, (long) (1000 * (5 + Math.random() * 10)));
                break;
            case 2:
                scheduleEvent(ev, (long) (1000 * (50 + Math.random() * 20)));
                break;
            case 3:
                scheduleEvent(ev, (long) (1000 * (9 * 30 + Math.random() * 60)));
                break;
            case 4:
                scheduleEvent(ev, (long) (1000 * (approxSeconds * (0.7 + Math.random() * 0.3))));
                break;
            default:
                Lang.ASSERT_FALSE("invalid state: " + r_state2string(ev.reconnectManagerState));
        }
    }

    public static final String r_state2string(int r_state) {
        switch(r_state) {
            case 1:
                return "r_state_stagei_5_sec";
            case 2:
                return "r_state_stageii_1_min";
            case 3:
                return "r_state_stageiii_5_min";
            case 4:
                return "r_state_try_once";
            default:
                return "invalid_r_state(r_state==" + r_state + ")";
        }
    }

    private synchronized void scheduleEvent(final SessionReconnecting ev, long afterMillis) {
        checkInitialised();
        runAt(System.currentTimeMillis() + afterMillis, ev.reconnectTask = new MessagingTask("reconnect of " + ev, ev) {

            public void run() throws Exception {
                if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug(ev.getLoginId() + ": task launched, attempting reconnect, session r_state=" + r_state2string(ev.reconnectManagerState));
                ev.reconnect();
            }
        });
    }

    synchronized void cancelReconnect(SessionReconnecting ev) {
        if (ev.reconnectTask != null) {
            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug(ev.getLoginId() + ": reconnect canceled");
            ev.cancel(ev.reconnectTask);
            ev.reconnectTask = null;
        }
    }

    public synchronized void init() {
        try {
            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("ReconnectManager.init() enter");
            sche.init();
            initialised = true;
        } finally {
            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("ReconnectManager.init() leave");
        }
    }

    public synchronized void deinit() {
        try {
            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("ReconnectManager.deinit() enter");
            if (!initialised) return;
            initialised = false;
            sche.deinit();
        } finally {
            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("ReconnectManager.deinit() leave");
        }
    }
}
