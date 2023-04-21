package com.mtgi.analytics;

import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import com.mtgi.analytics.servlet.SpringSessionContext;

/**
 * <p>Standard implementation of {@link BehaviorTrackingManager}.  BehaviorEvent
 * instances are asynchronously committed to a BehaviorEventPersister when they are
 * complete; user and session IDs for the events are provided by an implementation
 * of {@link SessionContext}.</p>
 * 
 * <p>Event persistence occurs when {@link #flush()} is called (either via JMX control
 * or by the Quartz scheduler), or when the queue of uncommitted events exceeds
 * the configured threshold value.  The flush threshold can be configured with
 * {@link #setFlushThreshold(int)}.</p>
 */
@ManagedResource(objectName = "com.mtgi.analytics:name=BeetManager", description = "Monitor and control basic beet behavior")
public class BehaviorTrackingManagerImpl implements BehaviorTrackingManager, InitializingBean, BeanNameAware, AopInfrastructureBean {

    private static final Log log = LogFactory.getLog(BehaviorTrackingManagerImpl.class);

    private boolean warned;

    private String name;

    private SessionContext sessionContext;

    private BehaviorEventPersister persister;

    private String application;

    private int flushThreshold = 100;

    private TaskExecutor executor;

    private ThreadLocal<BehaviorEvent> event = new ThreadLocal<BehaviorEvent>();

    private LinkedList<BehaviorEvent> writeBuffer = new LinkedList<BehaviorEvent>();

    private Object bufferSync = new Object();

    private volatile int pendingFlush = 0;

    private volatile boolean flushRequested = false;

    private volatile boolean suspended = false;

    private Runnable flushJob = new Runnable() {

        public void run() {
            flush();
        }
    };

    public BehaviorTrackingManagerImpl() {
    }

    public void setBeanName(String name) {
        this.name = name;
    }

    public String getBeanName() {
        return name;
    }

    public BehaviorEvent createEvent(String type, String name) {
        return new BehaviorEvent(event.get(), type, name, application, sessionContext.getContextUserId(), sessionContext.getContextSessionId());
    }

    public void start(BehaviorEvent evt) {
        if (evt.getParent() != event.get()) throw new IllegalStateException("Attempted to start an event that is not a child of the pending event");
        evt.start();
        event.set(evt);
    }

    public void stop(BehaviorEvent evt) {
        BehaviorEvent current = event.get();
        if (evt != current) throw new IllegalStateException("Attempted to stop an event that is not the current event on this thread: got " + evt + " but expected " + current);
        try {
            evt.stop();
        } finally {
            event.set(evt.getParent());
        }
        if (!suspended) {
            synchronized (bufferSync) {
                ++pendingFlush;
                writeBuffer.add(evt);
            }
            flushIfNeeded();
        }
    }

    @ManagedAttribute(description = "Returns true if event logging has been temporarily disabled with the suspend() operation.")
    public boolean isSuspended() {
        return suspended;
    }

    @ManagedOperation(description = "Temporarily suspend logging of behavior events.")
    public String suspend() {
        suspended = true;
        return "Event logging temporarily suspended.  Use resume() to resume logging.";
    }

    @ManagedOperation(description = "Resume logging of behavior events after a previous call to suspend().")
    public String resume() {
        suspended = false;
        return "Event logging resumed.";
    }

    /**
	 * Flush any completed events to the event persister.  This operation can be called
	 * manually via JMX, or can be called on a fixed interval via the Quartz Scheduler.
	 * This operation results in the logging of a "flush" event to the database.
	 * 
	 * @return the number of events persisted
	 */
    @ManagedOperation(description = "Immediately flush all completed events to the behavior tracking database.  Returns the number of events written to the database (not counting the flush event that is also logged)")
    public int flush() {
        LinkedList<BehaviorEvent> oldList = null;
        synchronized (bufferSync) {
            oldList = writeBuffer;
            pendingFlush -= oldList.size();
            writeBuffer = new LinkedList<BehaviorEvent>();
            flushRequested = false;
        }
        if (oldList.isEmpty()) return 0;
        BehaviorEvent flushEvent = new FlushEvent(event.get());
        if (!warned && !flushEvent.isRoot()) {
            warned = true;
            log.warn("Flush is being called from inside an application thread!  It is strongly advised the flush only be called from a dedicated, reduced-priority thread pool (are you using a SyncTaskExecutor in your spring configuration?).");
        }
        EventDataElement data = flushEvent.addData();
        flushEvent.start();
        int count = oldList.size();
        event.set(flushEvent);
        try {
            persister.persist(oldList);
            if (log.isDebugEnabled()) log.debug("Flushed " + count + " events with " + pendingFlush + " remaining");
            return count;
        } finally {
            event.set(flushEvent.getParent());
            data.add("count", count);
            flushEvent.stop();
            LinkedList<BehaviorEvent> temp = new LinkedList<BehaviorEvent>();
            temp.add(flushEvent);
            persister.persist(temp);
        }
    }

    private void flushIfNeeded() {
        boolean requestFlush = false;
        synchronized (bufferSync) {
            if (flushRequested) return;
            if (!writeBuffer.isEmpty() && pendingFlush >= flushThreshold) {
                requestFlush = flushRequested = true;
                if (log.isDebugEnabled()) log.debug("requesting autoflush with " + pendingFlush + " events awaiting save");
            }
        }
        if (requestFlush) executor.execute(flushJob);
    }

    @ManagedAttribute(description = "The application name for events published by this manager")
    public String getApplication() {
        return application;
    }

    @ManagedAttribute(description = "The number of completed events not yet flushed")
    public int getEventsPendingFlush() {
        return pendingFlush;
    }

    /**
	 * Set the name of the application in which this manager operates, for
	 * logging purposes.  This will be the value of {@link BehaviorEvent#getApplication()}
	 * for all events created by this manager.
	 */
    @Required
    public void setApplication(String application) {
        this.application = application;
    }

    /**
	 * Set a session context for the application, used to determine the
	 * current user and session ID for a calling thread.
	 */
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    /**
	 * Provide a persister for saving finished events to the behavior tracking database.
	 * @param persister
	 */
    @Required
    public void setPersister(BehaviorEventPersister persister) {
        this.persister = persister;
    }

    public BehaviorEventPersister getPersister() {
        return persister;
    }

    /**
	 * Provide a task executor on which persistence operations will be performed.
	 */
    @Required
    public void setExecutor(TaskExecutor executor) {
        this.executor = executor;
    }

    public TaskExecutor getExecutor() {
        return executor;
    }

    /**
	 * Specify the maximum number of completed events to queue in memory before
	 * forcing a flush to the persister.  Default is 100 if unspecified.
	 * 
	 * Note that this value is treated as advice and not strictly obeyed.
	 * For example, additional events may accumulate during the time it takes to
	 * rotate the event buffer after the flush threshold is first observed crossed.
	 * 
	 * In other words, persister implementations must not assume that the flush
	 * threshold is a hard upper limit on the batch size of persistence operations.
	 */
    public void setFlushThreshold(int flushThreshold) {
        this.flushThreshold = flushThreshold;
    }

    public void afterPropertiesSet() throws Exception {
        if (sessionContext == null) {
            log.info("No sessionContext specified, using default implementation " + SpringSessionContext.class.getName());
            sessionContext = new SpringSessionContext();
        }
    }

    protected class FlushEvent extends BehaviorEvent {

        private static final long serialVersionUID = 3182195013219330932L;

        protected FlushEvent(BehaviorEvent parent) {
            super(parent, "behavior-tracking", "flush", application, sessionContext.getContextUserId(), sessionContext.getContextSessionId());
        }
    }
}
