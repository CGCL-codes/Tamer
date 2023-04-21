package org.mobicents.slee.runtime.activity;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.slee.resource.FailureReason;
import javax.transaction.Transaction;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.event.EventContext;

/**
 * 
 * Manages the queuing of events for a specific activity. Note that this impl of
 * {@link ActivityEventQueueManager} is only thread safe if the local AC
 * executive service is single thread.
 * 
 * @author Eduardo Martins
 * 
 */
public class ActivityEventQueueManagerImpl implements ActivityEventQueueManager {

    private static final Logger logger = Logger.getLogger(ActivityEventQueueManagerImpl.class);

    private boolean doTraceLogs = logger.isTraceEnabled();

    /**
	 * stores the activity end event when set
	 */
    private EventContext activityEndEvent;

    private boolean activityEndEventRouted;

    /**
	 * the set of pending events, i.e., events not committed yet, for this
	 * activity
	 */
    private Set<EventContext> pendingEvents;

    /**
	 * the events hold due to barriers set
	 */
    private Deque<EventContext> eventsBarriered;

    /**
	 * the transactions that hold barriers to the activity event queue
	 */
    private Set<Transaction> eventBarriers;

    /**
	 * the local view of the related activity context
	 */
    private final LocalActivityContextImpl localAC;

    /**
	 * 
	 * @param localAC
	 */
    public ActivityEventQueueManagerImpl(LocalActivityContextImpl localAC) {
        this.localAC = localAC;
    }

    @Override
    public void pending(final EventContext event) {
        if (doTraceLogs) {
            logger.trace("Pending event of type " + event.getEventTypeId() + " in AC with handle " + event.getActivityContextHandle());
        }
        event.getReferencesHandler().add(localAC.getActivityContextHandle());
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (pendingEvents == null) {
                    pendingEvents = new HashSet<EventContext>(4);
                }
                pendingEvents.add(event);
            }
        };
        localAC.getExecutorService().execute(r);
    }

    @Override
    public void commit(final EventContext event) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (activityEndEvent == null) {
                    commit(event, true);
                } else {
                    if (doTraceLogs) {
                        logger.trace("Unable to commit event of type " + event.getEventTypeId() + " in AC with handle " + event.getActivityContextHandle() + ", the activity end event is already committed");
                    }
                    if (!event.isActivityEndEvent()) {
                        event.eventProcessingFailed(FailureReason.OTHER_REASON);
                    }
                }
            }
        };
        localAC.getExecutorService().execute(r);
    }

    @Override
    public void fireNotTransacted(final EventContext event) {
        event.getReferencesHandler().add(localAC.getActivityContextHandle());
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (activityEndEvent == null) {
                    commit(event, false);
                } else {
                    if (doTraceLogs) {
                        logger.trace("Unable to commit event of type " + event.getEventTypeId() + " in AC with handle " + event.getActivityContextHandle() + ", the activity end event is already committed");
                    }
                    if (!event.isActivityEndEvent()) {
                        event.eventProcessingFailed(FailureReason.OTHER_REASON);
                    }
                }
            }
        };
        localAC.getExecutorService().execute(r);
    }

    private void commit(EventContext event, boolean isPendingEvent) {
        if (isPendingEvent) {
            if (pendingEvents == null || !pendingEvents.remove(event)) {
                if (doTraceLogs) {
                    logger.trace("Unable to commit event of type " + event.getEventTypeId() + " in AC with handle " + event.getActivityContextHandle() + ", the event was not found in the pending events set.");
                }
                event.eventProcessingFailed(FailureReason.OTHER_REASON);
                return;
            }
        }
        if (eventBarriers == null || eventBarriers.isEmpty()) {
            commitAndNotSuspended(event);
        } else {
            if (eventsBarriered == null) {
                eventsBarriered = new LinkedList<EventContext>();
            }
            eventsBarriered.add(event);
        }
    }

    private void commitAndNotSuspended(EventContext event) {
        if (doTraceLogs) {
            logger.trace("Commiting event of type " + event.getEventTypeId() + " in AC with handle " + event.getActivityContextHandle());
        }
        if (event.isActivityEndEvent()) {
            activityEndEvent = event;
            routeActivityEndEventIfNeeded();
        } else {
            localAC.setActivityReferencesCheck(null);
            localAC.getExecutorService().routeEvent(event);
            routeActivityEndEventIfNeeded();
        }
    }

    private void routeActivityEndEventIfNeeded() {
        if (pendingEvents == null || pendingEvents.isEmpty()) {
            if (activityEndEvent == null || activityEndEventRouted) {
                return;
            }
            activityEndEventRouted = true;
            localAC.getExecutorService().routeEvent(activityEndEvent);
        }
    }

    @Override
    public void rollback(final EventContext event) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (doTraceLogs) {
                    logger.trace("Rolled back event of type " + event.getEventTypeId() + " in AC with handle " + event.getActivityContextHandle());
                }
                if (pendingEvents != null && pendingEvents.remove(event)) {
                    routeActivityEndEventIfNeeded();
                }
            }
        };
        localAC.getExecutorService().execute(r);
    }

    @Override
    public void createBarrier(final Transaction transaction) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (eventBarriers == null) {
                    eventBarriers = new HashSet<Transaction>(2);
                }
                eventBarriers.add(transaction);
            }
        };
        localAC.getExecutorService().execute(r);
    }

    @Override
    public void removeBarrier(final Transaction transaction) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (eventBarriers != null && eventBarriers.remove(transaction)) {
                    if (eventBarriers.isEmpty()) {
                        if (eventsBarriered != null) {
                            EventContext e = null;
                            while (true) {
                                e = eventsBarriered.pollFirst();
                                if (e == null) {
                                    break;
                                } else {
                                    if (!e.isActivityEndEvent()) {
                                        commitAndNotSuspended(e);
                                    } else {
                                        activityEndEvent = e;
                                    }
                                }
                            }
                        }
                        routeActivityEndEventIfNeeded();
                    }
                }
            }
        };
        localAC.getExecutorService().execute(r);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            return ((ActivityEventQueueManagerImpl) obj).localAC.equals(this.localAC);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return localAC.hashCode();
    }
}
