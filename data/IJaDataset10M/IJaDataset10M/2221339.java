package com.sun.javame.sensor;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.sensor.SensorInfo;
import javax.microedition.sensor.SensorListener;
import com.sun.javame.sensor.helper.IdentityWrapper;

/**
 * This class is responsible for callbacks to SensorListener registered with
 * SensorInfo.
 */
class AvailabilityPoller implements AvailabilityListener {

    /**
     * Holds Vector of SensorListener-s for each SensorInfo.
     * <i>@GuardedBy("infoListenerMap")</i>
     */
    private final Hashtable infoListenerMap = new Hashtable();

    /**
     * Stores the last SensorInfo availability state. The value updated during
     * each notification call.
     * <i>@GuardedBy("infoListenerMap")</i>
     */
    private final Hashtable lastAvailable = new Hashtable();

    /**
     * Stores the current SensorInfo availability state which is used if there
     * are more SensorListener-s for one SensorInfo. The value is also updated
     * by the {@link #notifyAvailability(SensorInfo, boolean)} called from
     * {@link AvailabilityNotifier}. The content is erased on each poller loop.
     * <i>@GuardedBy("infoListenerMap")</i>
     */
    private final Hashtable cachedAvailability = new Hashtable();

    /** Lock used to sleep in each poller loop. */
    private final Object sleepLock = new Object();

    /**
     * The polling worker thread.
     * <i>@GuardedBy("infoListenerMap")</i>
     */
    private PollerWorker pollingThread;

    /** Executor used to fire SensorListener notifications. */
    private final GuardedExecutor executor = new GuardedExecutor();

    /** Time in milliseconds the poller sleeps between runs. */
    private final int pollerSleep;

    /**
     * Create availability poller with specific delay.
     *
     * @param pollerSleep time in milliseconds the poller will sleep between
     *        runs
     */
    AvailabilityPoller(int pollerSleep) {
        this.pollerSleep = pollerSleep;
    }

    /**
     * Set SensorInfo cached availability and notify the sleep lock.
     */
    public void notifyAvailability(SensorInfo info, boolean available) {
        synchronized (infoListenerMap) {
            IdentityWrapper infoWrap = new IdentityWrapper(info);
            cachedAvailability.put(infoWrap, available ? Boolean.TRUE : Boolean.FALSE);
        }
        synchronized (sleepLock) {
            sleepLock.notify();
        }
    }

    /**
     * If not present add listener and call appropriate callback method. This
     * will also call {@link AvailabilityNotifier#startMonitoringAvailability()}
     * if the {@link SensorInfo} implements it and the first SensorListener is
     * registered.
     *
     * @param listener SensorListener which is registered
     * @param info SensorInfo which changes the listener listens to
     */
    void addListener(SensorListener listener, SensorInfo info) {
        IdentityWrapper infoWrap = new IdentityWrapper(info);
        IdentityWrapper listenWrap = new IdentityWrapper(listener);
        synchronized (infoListenerMap) {
            Vector listeners = (Vector) infoListenerMap.get(infoWrap);
            if (listeners == null) {
                listeners = new Vector();
                infoListenerMap.put(infoWrap, listeners);
            }
            if (listeners.size() == 0 && info instanceof AvailabilityNotifier) {
                ((AvailabilityNotifier) info).startMonitoringAvailability(this);
                listeners.addElement(listenWrap);
            } else if (!listeners.contains(listenWrap)) {
                listeners.addElement(listenWrap);
            } else {
                listener = null;
            }
            if (listener != null) {
                final Boolean available = info.isAvailable() ? Boolean.TRUE : Boolean.FALSE;
                lastAvailable.put(infoWrap, available);
                if (available.booleanValue()) {
                    listener.sensorAvailable(info);
                } else {
                    listener.sensorUnavailable(info);
                }
                ensureStarted();
                infoListenerMap.notify();
            }
        }
    }

    /**
     * Remove the SensorListener from every SensorInfo to which it is
     * registered. This will also call
     * {@link AvailabilityNotifier#stopMonitoringAvailability()}, if the
     * {@link SensorInfo} implements it and the last SensorListener is
     * unregistered.
     *
     * @param listener SensorListener which is unregistered
     */
    void removeListener(SensorListener listener) {
        synchronized (infoListenerMap) {
            IdentityWrapper listenWrap = new IdentityWrapper(listener);
            Enumeration enumeration = infoListenerMap.keys();
            Vector infosToRemove = new Vector();
            while (enumeration.hasMoreElements()) {
                IdentityWrapper key = (IdentityWrapper) enumeration.nextElement();
                Vector listeners = (Vector) infoListenerMap.get(key);
                if (listeners.contains(listenWrap)) {
                    listeners.removeElement(listenWrap);
                    if (listeners.isEmpty()) {
                        infosToRemove.addElement(key);
                        SensorInfo info = (SensorInfo) key.getWrapped();
                        if (info instanceof AvailabilityNotifier) {
                            ((AvailabilityNotifier) info).stopMonitoringAvailability(this);
                        }
                    }
                }
            }
            for (int i = 0; i < infosToRemove.size(); i++) {
                infoListenerMap.remove(infosToRemove.elementAt(i));
            }
        }
    }

    /**
     * Ensure that the worker thread is running. If it does not exist (null) or
     * died for some reason (isAlive == false), then we create new and start it.
     */
    private void ensureStarted() {
        if (pollingThread == null || !pollingThread.isAlive()) {
            pollingThread = new PollerWorker();
            pollingThread.start();
        }
    }

    /**
     * Executor class which shields the current thread from unchecked exceptions
     * that can shut it down.
     */
    private static final class GuardedExecutor {

        public void execute(Runnable runnable) {
            try {
                runnable.run();
            } catch (Throwable ignore) {
            }
        }
    }

    /**
     * Class holding the state informations about the upcoming listener
     * callback.
     * <i>@Immutable</i>
     */
    private static final class ToCall implements Runnable {

        private final SensorInfo info;

        private final SensorListener listener;

        private final boolean available;

        public ToCall(SensorInfo info, SensorListener listener, boolean available) {
            this.info = info;
            this.listener = listener;
            this.available = available;
        }

        public void run() {
            if (available) {
                listener.sensorAvailable(info);
            } else {
                listener.sensorUnavailable(info);
            }
        }
    }

    /**
     * The poller worker thread which in a infinite loop fetches SensorInfo
     * availability and calls appropriate listeners.
     */
    private class PollerWorker extends Thread {

        public void run() {
            while (true) {
                synchronized (infoListenerMap) {
                    while (infoListenerMap.isEmpty()) {
                        try {
                            infoListenerMap.wait();
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
                long startTimeMillis = System.currentTimeMillis();
                Vector toCall = new Vector();
                synchronized (infoListenerMap) {
                    Enumeration enumeration = infoListenerMap.keys();
                    while (enumeration.hasMoreElements()) {
                        IdentityWrapper key = (IdentityWrapper) enumeration.nextElement();
                        SensorInfo info = (SensorInfo) key.getWrapped();
                        Boolean available = (Boolean) cachedAvailability.get(key);
                        if (available == null) {
                            available = info.isAvailable() ? Boolean.TRUE : Boolean.FALSE;
                            cachedAvailability.put(key, available);
                        }
                        Boolean oldAvailable = (Boolean) lastAvailable.put(key, available);
                        if (!available.equals(oldAvailable)) {
                            Vector listeners = (Vector) infoListenerMap.get(key);
                            for (int i = 0; i < listeners.size(); i++) {
                                SensorListener listener = (SensorListener) ((IdentityWrapper) listeners.elementAt(i)).getWrapped();
                                toCall.addElement(new ToCall(info, listener, available.booleanValue()));
                            }
                        }
                    }
                    cachedAvailability.clear();
                }
                for (int i = 0; i < toCall.size(); i++) {
                    Runnable runnable = (Runnable) toCall.elementAt(i);
                    executor.execute(runnable);
                }
                long millisToWait = pollerSleep - (System.currentTimeMillis() - startTimeMillis);
                if (millisToWait > 0) {
                    synchronized (sleepLock) {
                        try {
                            sleepLock.wait(millisToWait);
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
            }
        }
    }
}
