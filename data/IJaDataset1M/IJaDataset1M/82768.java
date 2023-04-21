package fr.lig.sigma.astral.common.event;

import fr.lig.sigma.astral.common.AxiomNotVerifiedException;
import fr.lig.sigma.astral.common.Batch;
import fr.lig.sigma.astral.query.QueryRuntime;
import fr.lig.sigma.astral.source.SourceBlocker;
import java.util.Iterator;
import java.util.Set;

/**
 * The event scheduler is the event manager of the Astral engine. This one ensures that each event is executed
 * right on time. It must support normal events as well as independent events. See technical documentation for
 * more details on how it must work to respect the right semantics.
 *
 * @author Loic Petit
 */
public interface EventScheduler extends TimeChangeListener {

    /**
     * Schedule a normal event at the timestamp asked. For coherency reasons, it is advised to push with a timestamp
     * greater than the global timestamp
     *
     * @param b     The batch id
     * @param event The processor
     */
    void pushEvent(Batch b, EventProcessor event);

    /**
     * Stop accepting push starting from the given timestamp if and only if this method has been called at least the
     * same account than the number of sources
     *
     * @param timestamp The last treated timestamp
     */
    void togglePush(long timestamp);

    /**
     * Get the global time. If no event has yet been processed, returns T0
     *
     * @return a batch as described
     */
    Batch getGlobalTime();

    /**
     * Returns the T0 attribute of the scheduler
     *
     * @return The starting timestamp, 0 by default
     */
    Batch getT0();

    /**
     * Register a processor to be an independent processor. It then gets an ID that it will need to give when pushing
     * an indy event. (NB: This management is made to be able to transfer the ID through the network)
     *
     * @param processor The independent processor
     * @return The associated indy processor's ID
     */
    int registerIndependentProcessor(EventProcessor processor);

    /**
     * Push an indy event at the given timestamp. If the given timestamp is lower than the global it MUST be processed
     * immediately (it is the only time that the scheduler executes a task by himself).
     *
     * @param b  The batch id
     * @param id The indy processor's ID
     * @throws AxiomNotVerifiedException Threw by the processed event
     */
    void pushIndependentEvent(Batch b, int id) throws AxiomNotVerifiedException;

    /**
     * Does the scheduler has another event to serve? If not, the query is officially finished
     * This method will block if no event is available but the query is not finished.
     *
     * @return true if there is a processor to execute
     * @throws InterruptedException - If it is interrupted in the waiting process
     */
    boolean hasNext() throws InterruptedException;

    /**
     * Returns the next processor to execute
     *
     * @return The event processor
     */
    WaitingEntry<Batch, EventProcessor> next();

    /**
     * New feature :). A scheduler can depend on another scheduler. Therefore, the current time will note change if
     * the dependent scheduler is not up-to-date. Can help guarantee transaction orders.
     *
     * @param es The dependent scheduler
     */
    void addDependentEventScheduler(EventScheduler es);

    /**
     * Registers a new time change listener which will be called whenever this scheduler safely changes time.
     *
     * @param tcl The time change listener
     */
    void addTimeChangeListener(TimeChangeListener tcl);

    /**
     * Get the runtime attached to this scheduler
     *
     * @return The runtime
     */
    QueryRuntime getRuntime();

    /**
     * Set the runtime attached to this scheduler
     *
     * @param runtime The runtime
     */
    void setRuntime(QueryRuntime runtime);

    /**
     * Get the number of event that are currently in the waiting list
     *
     * @return The number of events
     */
    int getQueueCount();

    /**
     * Get the set of dependent schedulers
     *
     * @return The set
     */
    Set<EventScheduler> getDependentSchedulers();

    /**
     * Experimental!
     * Set the wait mode of the scheduler. In case of FULL WAIT, the scheduler will wait for ALL dependent schedulers to
     * change time. In the other case, the maximum time between all schedulers is taken, therefore no wait is performed.
     *
     * @param fullWait the mode
     */
    void setWaitMode(boolean fullWait);
}
