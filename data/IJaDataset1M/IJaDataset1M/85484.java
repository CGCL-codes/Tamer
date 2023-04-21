package net.kano.joscar.ratelim;

import net.kano.joscar.DefensiveTools;
import net.kano.joscar.snac.ClientSnacProcessor;
import net.kano.joscar.snac.CmdType;
import net.kano.joscar.snac.SnacQueueManager;
import net.kano.joscar.snac.SnacRequest;
import net.kano.joscar.snaccmd.conn.RateClassInfo;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Manages the SNAC queue for a single SNAC processor (or "connection").
 * Instances of this class must be obtained from a
 * <code>RateLimitingQueueMgr</code>'s {@link RateLimitingQueueMgr#getQueueMgr
 * getQueueMgr} or {@link RateLimitingQueueMgr#getQueueMgrs() getQueueMgrs}
 * methods; an instance is created automatically upon assigning a
 * <code>RateLimitingQueueMgr</code> as the SNAC queue manager for a given SNAC
 * processor.
 */
public final class ConnectionQueueMgrImpl implements ConnectionQueueMgr {

    /** The "parent" rate queue manager of this connection queue manager. */
    private final RateLimitingQueueMgr queueMgr;

    /** The rate monitor used by this connection queue manager. */
    private final RateMonitor monitor;

    /** The SNAC processor whose SNAC queues are being managed. */
    private final ClientSnacProcessor snacProcessor;

    /** Whether this connection is paused. */
    private boolean paused = false;

    /** A map from <code>RateClassMonitor</code>s to <code>RateQueue</code>s. */
    private final Map<RateClassMonitor, RateQueue> queues = new IdentityHashMap<RateClassMonitor, RateQueue>();

    /** A rate listener used to monitor rate events. */
    private RateListener rateListener = new RateListener() {

        public void detached(RateMonitor rateMonitor, ClientSnacProcessor processor) {
            rateMonitor.removeListener(this);
        }

        public void reset(RateMonitor rateMonitor) {
            synchronized (ConnectionQueueMgrImpl.this) {
                clearQueues();
            }
        }

        public void gotRateClasses(RateMonitor monitor) {
            updateRateClasses();
        }

        public void rateClassUpdated(RateMonitor monitor, RateClassMonitor classMonitor, RateClassInfo rateInfo) {
            queueMgr.getRunner().update();
        }

        public void rateClassLimited(RateMonitor rateMonitor, RateClassMonitor rateClassMonitor, boolean limited) {
            queueMgr.getRunner().update();
        }
    };

    /**
     * Creates a new SNAC processor queue manager with the given parent rate
     * manager for the given SNAC processor.
     *
     * @param queueMgr this connection queue manager's parent rate manager
     * @param processor the SNAC processor to manage
     */
    ConnectionQueueMgrImpl(RateLimitingQueueMgr queueMgr, ClientSnacProcessor processor) {
        DefensiveTools.checkNull(queueMgr, "queueMgr");
        DefensiveTools.checkNull(processor, "processor");
        this.queueMgr = queueMgr;
        this.monitor = new RateMonitor(processor);
        this.snacProcessor = processor;
        monitor.addListener(rateListener);
    }

    /**
     * Returns the rate monitor being used to determine when to send SNAC
     * commands on the associated SNAC connection.
     *
     * @return the rate monitor being used
     */
    public RateMonitor getRateMonitor() {
        return monitor;
    }

    /**
     * Returns the rate queue being used for the rate class associated with the
     * given rate class monitor.
     *
     * @param classMonitor a rate class monitor
     * @return the rate queue associated with the given rate class monitor
     */
    private synchronized RateQueue getRateQueue(RateClassMonitor classMonitor) {
        DefensiveTools.checkNull(classMonitor, "classMonitor");
        return queues.get(classMonitor);
    }

    /**
     * Returns the rate queue in which a command of the given type would be
     * placed. Note that, normally, any number of calls to this method with the
     * same command type will return a reference to the same
     * <code>RateQueue</code> for the duration of the underlying SNAC
     * connection. That is, <code>RateQueue</code> references can safely be kept
     * for the duration of a SNAC connection. To be notified of when rate
     * information changes, one could use code such as the following:
     * <pre>
connQueueMgr.getRateMonitor().addListener(myRateListener);
     * </pre>
     * When new rate information is received (that is, when {@link
     * RateListener#gotRateClasses} is called), old rate queues are discarded
     * and new ones are created as per the new rate information.
     * <br>
     * <br>
     * This method should only return <code>null</code> in the case that no
     * rate information has yet been received or the server did not specify
     * a default rate class (this is very abnormal behavior and will most likely
     * never happen when using AOL's servers).
     *
     * @param type the command type whose rate queue is to be returned
     * @return the rate queue used for the given command type
     */
    @Nullable
    public synchronized RateQueue getRateQueue(CmdType type) {
        DefensiveTools.checkNull(type, "type");
        RateClassMonitor cm = monitor.getMonitor(type);
        if (cm == null) return null;
        return getRateQueue(cm);
    }

    /**
     * Queues a SNAC request on the associated connection.
     *
     * @param request the request to enqueue
     *
     * @see SnacQueueManager#queueSnac(ClientSnacProcessor, SnacRequest)
     */
    void queueSnac(SnacRequest request) {
        DefensiveTools.checkNull(request, "request");
        CmdType type = CmdType.ofCmd(request.getCommand());
        RateQueue queue = getRateQueue(type);
        if (queue == null) {
            queueMgr.sendSnac(snacProcessor, request);
        } else {
            queue.enqueue(request);
            queueMgr.getRunner().update();
        }
    }

    /**
     * Clears the SNAC queue for the associated connection.
     *
     * @see SnacQueueManager#clearQueue(ClientSnacProcessor)
     */
    synchronized void clearQueue() {
        for (RateQueue queue : queues.values()) queue.clear();
        paused = false;
    }

    /**
     * Pauses the SNAC queue for the associated connection.
     *
     * @see SnacQueueManager#pause(ClientSnacProcessor)
     */
    synchronized void pause() {
        assert !paused;
        paused = true;
    }

    /**
     * Unpauses the SNAC queue for the associated connection.
     *
     * @see SnacQueueManager#unpause(ClientSnacProcessor)
     */
    synchronized void unpause() {
        assert paused;
        paused = false;
        queueMgr.getRunner().update();
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    /**
     * Discards all open rate queues and creates new ones based on the rate
     * monitor's current rate class information.
     */
    private synchronized void updateRateClasses() {
        List<RateClassMonitor> monitors = monitor.getMonitors();
        Collection<RateQueue> queueArray = clearQueues();
        List<SnacRequest> reqs = new LinkedList<SnacRequest>();
        for (RateQueue queue : queueArray) {
            queue.dequeueAll(reqs);
        }
        for (RateClassMonitor monitor : monitors) {
            queues.put(monitor, new RateQueue(this, monitor, new SnacRequestSender() {

                public void sendRequests(List<SnacRequest> toSend) {
                    for (SnacRequest request : toSend) {
                        queueMgr.sendSnac(snacProcessor, request);
                    }
                }
            }));
        }
        for (SnacRequest req : reqs) {
            queueSnac(req);
        }
        QueueRunner<RateLimitingEventQueue> runner = queueMgr.getRunner();
        runner.getQueue().addQueues(queues.values());
    }

    /**
     * Removes all queues from the list of queues and returns them.
     *
     * @return the rate queues formerly in the queue list
     */
    private synchronized Collection<RateQueue> clearQueues() {
        Collection<RateQueue> vals = queues.values();
        queueMgr.getRunner().getQueue().removeQueues(vals);
        queues.clear();
        return DefensiveTools.getUnmodifiableCopy(vals);
    }

    /**
     * Clears the rate queues and stops listening for rate events.
     */
    synchronized void detach() {
        clearQueue();
        clearQueues();
        monitor.detach();
    }

    public String toString() {
        return "ConnectionQueueMgr: " + "paused=" + paused + ", queues=" + queues.keySet();
    }
}
