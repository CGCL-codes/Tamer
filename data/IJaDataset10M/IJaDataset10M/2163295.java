package lejos.realtime;

import java.util.ArrayList;

public class AsyncEvent {

    private final ArrayList<AsyncEventHandler> listHandler = new ArrayList<AsyncEventHandler>();

    /**
     *	Create a new AsyncEvent object.
     */
    public AsyncEvent() {
    }

    /**
     *  Add a handler to the set of handlers associated with this event.
     *
     * An instance of AsyncEvent may have more than one associated handler.
     * However, adding a handler to an event has no effect if the handler is
     * already attached to the event.
     *
     * The execution of this method is atomic with respect to the execution of
     * the fire() method.
     *
     * Since this affects the constraints expressed in the release parameters
     * of an existing schedulable object, this may change the feasibility of
     * the current system. This method does not change feasibility set of any
     * scheduler, and no feasibility test is performed.
     *
     * Note, there is an implicit reference to the handler stored in this. The
     * assignment must be valid under any applicable memory assignment rules.
     *
     * @param handler
     *          The new handler to add to the list of handlers already
     * associated with this. If the handler is already associated with the
     * event, the call has no effect.
     *
     * @throws IllegalArgumentException
     *      Thrown if handler is null. 
     */
    public void addHandler(AsyncEventHandler handler) {
        if (!listHandler.contains(handler)) {
            listHandler.add(handler);
        }
    }

    /**
     *    Remove a handler from the set associated with this event. 
     *
     *    The execution of this method is atomic with respect to the execution
     *    of the fire() method.
     *
     *    A removed handler continues to execute until its fireCount becomes
     *    zero and it completes.
     *
     *    Since this affects the constraints expressed in the release 
     *    parameters of an existing schedulable object, this may change the 
     *    feasibility of the current system. This method does not change the 
     *    feasibility set of any scheduler, and no feasibility test is 
     *    performed.
     *
     *    @param handler
     *		The handler to be disassociated from this. If null nothing
     *		happens. If the handler is not already associated with this
     *		then nothing happens.
     */
    public void removeHandler(AsyncEventHandler handler) {
        listHandler.remove(handler);
    }

    /**
     *	Test to see if the handler given as the parameter is associated with 
     *	this.
     *
     *  @param handler
     *  	The handler to be tested to determine if it is associated with 
     *  	this.
     *   
     *  @return
     *        True if the parameter is associated with this. False if handler 
     *        is null or the parameters is not associated with this.
     */
    public boolean handledBy(AsyncEventHandler handler) {
        return listHandler.contains(handler);
    }

    /**
     *
     *    Associate a new handler with this event and remove all existing handlers. The execution of this method is atomic with respect to the execution of the fire() method.
     *
     *    Since this affects the constraints expressed in the release parameters of the existing schedulable objects, this may change the feasibility of the current system. This method does not change the feasibility set of any scheduler, and no feasibility test is performed.*
     *
     *    Parameters:
     *        handler - The new instance of AsyncEventHandler to be associated with this. If handler is null then no handler will be associated with this (i.e., remove all handlers).
     *    Throws:
     *        IllegalAssignmentError - Thrown if this AsyncEvent cannot hold a reference to handler.
     *
     *createReleaseParameters
     */
    public void setHandler(AsyncEventHandler handler) {
        listHandler.clear();
        listHandler.add(handler);
    }

    /**
     *	Create a ReleaseParameters object appropriate to the release characteristics of this event. The default is the most pessimistic: AperiodicParameters. This is typically called by code that is setting up a handler for this event that will fill in the parts of the release parameters for which it has values, e.g., cost. The returned ReleaseParameters object is not bound to the event. Any changes in the event's release parameters are not reflected in previously returned objects.
     *
     *    If an event returns PeriodicParameters, there is no requirement for an implementation to check that the handler is released periodically.
     *
     *    Returns:
     *        A new ReleaseParameters object.
     */
    public ReleaseParameters createReleaseParameters() {
        return null;
    }

    /**
     *    Binds this to an external event, a happening. The meaningful values of happening are implementation dependent. This instance of AsyncEvent is considered to have occurred whenever the happening is triggered. More than one happening can be bound to the same AsyncEvent. However, binding a happening to an event has no effect if the happening is already bound to the event.
     *
     *    When an event, which is declared in a scoped memory area, is bound to an external happening, the reference count of that scoped memory area is incremented (as if there is an external real-time thread accessing the area). The reference count is decremented when the event is unbound from the happening.
     *
     *    Parameters:
     *        happening - An implementation dependent value that binds this instance of AsyncEvent to a happening.
     *    Throws:
     *        UnknownHappeningException - Thrown if the String value is not supported by the implementation.
     *        java.lang.IllegalArgumentException - Thrown if happening is null.
     *
     */
    public void bindTo(java.lang.String happening) {
    }

    /**
     *    Removes a binding to an external event, a happening. The meaningful values of happening are implementation dependent. If the associated event is declared in a scoped memory area, the reference count for the memory area is decremented.
     *
     *    Parameters:
     *        happening - An implementation dependent value representing some external event to which this instance of AsyncEvent is bound.
     *    Throws:
     *        UnknownHappeningException - Thrown if this instance of AsyncEvent is not bound to the given happening or the given happening is not supported by the implementation.
     *        java.lang.IllegalArgumentException - Thrown if happening is null.
     */
    public void unbindTo(java.lang.String happening) {
    }

    /**
     * Fire this instance of AsyncEvent.
     *
     * The asynchronous event handlers associated with this event will be
     * released.      * If no handlers are attached then the method does
     * nothing. An AsyncEvent that has been bound to an external happening can
     * still be fired by the application code.
     *
     * If the instance of AsyncEvent has more than one instance of 
     * {@link AsyncEventHandler} with release parameters object of type
     * {@link AperiodicParameters} attached and the execution of 
     * {@code AsyncEvent.fire()} introduces the requirement to throw at least
     * one type of exception, then all instances of AsyncEventHandler not
     * affected by the exception are handled normally.
     *
     * If the instance of {@link AsyncEvent} has more than one instance of
     * {@link AsyncEventHandler} with release parameters object of type
     * {@link SporadicParameters} attached and the execution of
     * {@code AsyncEvent.fire()} introduces the simultaneous requirement to
     * throw more than one type of exception or error then
     * MITViolationException has precedence over
     * ArrivalTimeQueueOverflowException.
     *
     */
    public void fire() {
        for (int i = 0; i < listHandler.size(); i++) {
            try {
                listHandler.get(i).getAndIncrementPendingFireCount();
            } catch (Exception e) {
                System.err.println("erreur : " + e.getMessage());
            }
        }
    }
}
