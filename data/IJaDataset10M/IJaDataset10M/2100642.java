package fulmine.event;

import static fulmine.util.Utils.SPACING_4_CHARS;
import static fulmine.util.Utils.logException;
import static fulmine.util.Utils.safeToString;
import static fulmine.util.Utils.string;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.SystemUtils;
import fulmine.AbstractLifeCycle;
import fulmine.ILifeCycle;
import fulmine.context.FulmineContext;
import fulmine.context.IFulmineContext;
import fulmine.event.listener.IEventListener;
import fulmine.event.system.ISystemEvent;
import fulmine.event.system.ISystemEventListener;
import fulmine.event.system.UnsubscribeEvent;
import fulmine.util.concurrent.ITaskExecutor;
import fulmine.util.concurrent.ITaskHandler;
import fulmine.util.concurrent.Task;
import fulmine.util.concurrent.TaskExecutor;
import fulmine.util.log.AsyncLog;

/**
 * A single-thread execution engine that takes {@link IEvent} objects off a task
 * queue, finds the {@link IEventListener} instances registered against the
 * {@link IEventSource} that generated the event, determines if the event is
 * compatible with the events the listener can accept (see
 * {@link IEventListener#getEventTypeFilter()}) and calls
 * {@link IEventListener#update(IEvent)} if the event is compatible.
 * <p>
 * On shutdown via the {@link #destroy()} method, a special event (
 * {@link #shutdownEvent}) is added to the processor's {@link #events}. The
 * processor continues to process its event queue after the destroy method but
 * when the processor encounters the shutdown event, it shuts down and the
 * processor thread ceases execution. This technique allows the event queue to
 * be drained before the processor terminates. There may be some situations
 * where events that are generated during the context shutdown need to be
 * processed (e.g. {@link UnsubscribeEvent}s may need to be handled by closing
 * external resources gracefully) and this delayed shutdown allows this to be
 * achieved.
 * 
 * @author Ramon Servadei
 */
public final class EventProcessor extends AbstractLifeCycle implements ILifeCycle, ITaskHandler<IEvent> {

    private static final AsyncLog LOG = new AsyncLog(EventProcessor.class);

    /** The active component that performs the work */
    private final ITaskExecutor executor;

    /** The context this processor is associated with */
    private final IFulmineContext context;

    /**
     * Provides thread-bound access to the frame identifier of the event the
     * processor is handling.
     */
    private static final ThreadLocal<IEventFrameExecution> currentFrame = new ThreadLocal<IEventFrameExecution>();

    /**
     * A marker event to destroy an {@link EventProcessor}
     * 
     * @author Ramon Servadei
     */
    private static final class DestroyProcessorEvent extends AbstractEvent implements ISystemEvent {

        public DestroyProcessorEvent() {
            super();
            setSource(new EventSource("DestroyProcessorEventSource"));
        }
    }

    /** The event that signals an {@link EventProcessor} to shutdown */
    private static final DestroyProcessorEvent shutdownEvent = new DestroyProcessorEvent();

    /** The string description */
    private final String toString;

    /**
     * Get the frame identifier of the event the processor is currently
     * handling. This is thread-bound.
     * 
     * @return the {@link IEventFrameExecution} of the event that is being
     *         processed.
     */
    public static final IEventFrameExecution getCurrentFrame() {
        return currentFrame.get();
    }

    /**
     * Standard constructor for an event processor backed by a standard priority
     * thread. The thread is held in the {@link FulmineContext} normal priority
     * thread group.
     * 
     * @param name
     *            the name of the thread for the processor
     * @param context
     *            the context this processor is associated with
     */
    public EventProcessor(String name, IFulmineContext context) {
        this(name, context.getEventProcessorThreadGroup(), context);
    }

    /**
     * Constructor for an event processor backed by a thread of priority equal
     * to the maximum priority of the thread group argument
     * 
     * @param name
     *            the name of the thread for the processor
     * @param threadGroup
     *            the thread group for the thread, this also defines the
     *            priority of the thread
     * @param context
     *            the context this processor is associated with
     */
    public EventProcessor(String name, ThreadGroup threadGroup, IFulmineContext context) {
        super();
        this.context = context;
        this.executor = new TaskExecutor(threadGroup, name, context);
        this.toString = string(this, name);
    }

    public void handleTask(IEvent task) {
        processEvent(task);
    }

    @Override
    protected AsyncLog getLog() {
        return LOG;
    }

    /**
     * Destroy the processor.
     */
    @Override
    protected void doDestroy() {
        queue(shutdownEvent);
    }

    @Override
    protected void doStart() {
        this.executor.start();
    }

    /**
     * Process the event by notifying all {@link IEventListener} instances
     * registered against the {@link IEventSource} of the event.
     * 
     * @param event
     *            the event
     */
    private void processEvent(IEvent event) {
        try {
            if (event == null) {
                return;
            }
            if (event == EventProcessor.shutdownEvent) {
                this.executor.destroy();
            }
            currentFrame.set(event.getFrame());
            List<IEventListener> listeners = event.getSource().getListeners();
            if (listeners == null || listeners.size() == 0) {
                logNoListeners(event);
            } else {
                for (IEventListener eventListener : listeners) {
                    if (eventListener == null) {
                        continue;
                    }
                    try {
                        if (event instanceof ISystemEvent) {
                            if (eventListener instanceof ISystemEventListener) {
                                filterEvent(event, eventListener);
                            } else {
                                if (getLog().isTraceEnabled()) {
                                    getLog().trace("System event " + safeToString(event) + " not being handled by " + safeToString(eventListener));
                                }
                            }
                        } else {
                            filterEvent(event, eventListener);
                        }
                    } catch (Exception e) {
                        logException(getLog(), safeToString(eventListener) + " handling " + safeToString(event), e);
                    }
                }
            }
            if (event.getTriggerEvent() != null) {
                this.context.queueEvent(event.getTriggerEvent());
            }
        } catch (Exception e) {
            logException(getLog(), safeToString(event), e);
        } finally {
            currentFrame.remove();
        }
    }

    /**
     * Helper method to log the fact that there are no listeners for the event
     * 
     * @param event
     *            the event for which there are no listeners
     */
    private void logNoListeners(IEvent event) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("No listeners for " + safeToString(event));
        }
    }

    /**
     * Check if the event listener can handle the event, if it can then let the
     * listener handle it.
     * 
     * @param event
     *            the event, never <code>null</code>
     * @param eventListener
     *            the event listener
     */
    private void filterEvent(IEvent event, IEventListener eventListener) {
        final Class<? extends IEvent>[] eventFilter = eventListener.getEventTypeFilter();
        if (getLog().isTraceEnabled()) {
            getLog().trace("Filtering event=" + safeToString(event) + " for listener=" + safeToString(eventListener) + " with filter=" + Arrays.deepToString(eventFilter));
        }
        if (eventFilter == null) {
            if (getLog().isInfoEnabled()) {
                getLog().info("No event filter from " + safeToString(eventListener) + ", this listener will not receive " + event.toIdentityString());
            }
            return;
        }
        for (int i = 0; i < eventFilter.length; i++) {
            Class<? extends IEvent> eventType = eventFilter[i];
            if (eventType.isInstance(event)) {
                eventListener.update(event);
                return;
            }
        }
    }

    /**
     * Add the event onto the processor's queue
     * 
     * @param event
     *            the event to add the queue
     */
    public void queue(final IEvent event) {
        this.executor.execute(new Task<IEvent>(this, event));
    }

    /**
     * Get the current stack trace for the {@link EventProcessor}
     * 
     * @return a formatted {@link String} of the current stack trace with each
     *         stack call on a new line
     */
    public String getStackTrace() {
        final StackTraceElement[] stackTrace = this.executor.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stackTrace.length; i++) {
            sb.append(SystemUtils.LINE_SEPARATOR);
            sb.append(SPACING_4_CHARS).append(stackTrace[i]);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString;
    }
}
