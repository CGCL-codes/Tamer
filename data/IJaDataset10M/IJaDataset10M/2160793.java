package org.springframework.webflow.execution.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.AttributeMap;
import org.springframework.webflow.Event;
import org.springframework.webflow.ExternalContext;
import org.springframework.webflow.Flow;
import org.springframework.webflow.FlowArtifactException;
import org.springframework.webflow.FlowExecutionControlContext;
import org.springframework.webflow.FlowSession;
import org.springframework.webflow.FlowSessionStatus;
import org.springframework.webflow.State;
import org.springframework.webflow.StateException;
import org.springframework.webflow.ViewSelection;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowExecutionListenerLoader;
import org.springframework.webflow.execution.FlowLocator;

/**
 * Default implementation of FlowExecution that uses a stack-based data
 * structure to manage
 * {@link org.springframework.webflow.FlowSession flow sessions}. This class is
 * closely coupled with <code>FlowSessionImpl</code> and
 * <code>FlowControlContextImpl</code>. The three classes work together to
 * form a complete flow execution implementation based on a finite state
 * machine.
 * <p>
 * This implementation of FlowExecution is serializable so it can be safely
 * stored in an HTTP session or other persistent store such as a file, database,
 * or client-side form field.
 * <p>
 * Note: this implementation synchronizes the signalEvent Flow execution entry
 * point. It is locked on a per client basis for this flow execution.
 * Synchronization prevents a client from being able to signal other events
 * before previously signaled ones have processed in-full, preventing possible
 * race conditions.
 * 
 * @see org.springframework.webflow.FlowSession
 * @see org.springframework.webflow.execution.impl.FlowSessionImpl
 * @see org.springframework.webflow.execution.impl.FlowExecutionControlContextImpl
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowExecutionImpl implements FlowExecution, Externalizable {

    /**
	 * The serialization version id.
	 */
    private static final long serialVersionUID = -898397026261844347L;

    private static final Log logger = LogFactory.getLog(FlowExecutionImpl.class);

    /**
	 * The execution's root flow; the top level flow that acts as the starting
	 * point for this flow execution.
	 */
    private transient Flow flow;

    /**
	 * A data structure for attributes shared by all flow sessions.
	 */
    private AttributeMap scope = new AttributeMap();

    /**
	 * The stack of active, currently executing flow sessions. As subflows are
	 * spawned, they are pushed onto the stack. As they end, they are popped off
	 * the stack.
	 */
    private LinkedList flowSessions = new LinkedList();

    /**
	 * A thread-safe listener list, holding listeners monitoring the lifecycle
	 * of this flow execution.
	 */
    private transient FlowExecutionListeners listeners;

    /**
	 * Set only on deserialization so this object can be fully reconstructed.
	 */
    private String flowId;

    /**
	 * Default constructor required for externalizable serialization. Should NOT
	 * be called programmatically.
	 */
    public FlowExecutionImpl() {
    }

    /**
	 * Create a new flow execution executing the provided flow. This constructor
	 * is mainly used for testing
	 * @param flow the root flow of this flow execution
	 */
    public FlowExecutionImpl(Flow flow) {
        this(flow, new FlowExecutionListener[0]);
    }

    /**
	 * Create a new flow execution executing the provided flow.
	 * @param flow the root flow of this flow execution
	 * @param listeners the listeners interested in flow execution lifecycle
	 * events
	 */
    public FlowExecutionImpl(Flow flow, FlowExecutionListener[] listeners) {
        Assert.notNull(flow, "The root flow definition is required");
        this.flow = flow;
        this.listeners = new FlowExecutionListeners(listeners);
        if (logger.isDebugEnabled()) {
            logger.debug("Created new execution of flow '" + flow.getId() + "'");
        }
    }

    public String getCaption() {
        return "FlowExecution:flow=[" + (getFlow() != null ? getFlow().getId() : flowId) + "]";
    }

    public boolean isActive() {
        return !flowSessions.isEmpty();
    }

    public Flow getFlow() {
        return flow;
    }

    public FlowSession getActiveSession() {
        return getActiveSessionInternal();
    }

    public AttributeMap getScope() {
        assertActive();
        return scope;
    }

    public ViewSelection start(ExternalContext externalContext) throws StateException {
        Assert.state(!isActive(), "This flow is already executing -- you cannot call 'start(ExternalContext)' more than once");
        FlowExecutionControlContext context = createControlContext(externalContext);
        getListeners().fireRequestSubmitted(context);
        try {
            try {
                ViewSelection selectedView = context.start(getFlow(), getFlow().getStartState(), null);
                return pause(context, selectedView);
            } catch (StateException e) {
                return pause(context, handleException(e, context));
            }
        } finally {
            getListeners().fireRequestProcessed(context);
        }
    }

    /**
	 * Handles an exception that occured performing an operation on this flow
	 * execution. First trys the set of exception handlers associated with the
	 * offending state, then the handlers at the flow level.
	 * @param exception the exception that occured
	 * @param context the state context the exception occured in
	 * @return the selected error view
	 * @throws StateException rethrows the exception it was not handled at the
	 * state or flow level
	 */
    protected ViewSelection handleException(StateException exception, FlowExecutionControlContext context) throws StateException {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to handle exception [" + exception + "]");
        }
        if (exception.getState() != null) {
            try {
                ViewSelection selectedView = exception.getState().handleException(exception, context);
                if (logger.isDebugEnabled()) {
                    logger.debug("State '" + exception.getState().getId() + "' handled exception");
                }
                return selectedView;
            } catch (StateException e) {
            }
        }
        try {
            ViewSelection selectedView = exception.getFlow().handleException(exception, context);
            if (logger.isDebugEnabled()) {
                logger.debug("Flow '" + exception.getFlow().getId() + "' handled exception");
            }
            return selectedView;
        } catch (StateException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Rethrowing unhandled state exception");
            }
            throw exception;
        }
    }

    public ViewSelection signalEvent(String eventId, ExternalContext externalContext) throws StateException {
        assertActive();
        if (logger.isDebugEnabled()) {
            logger.debug("Resuming this execution on user event '" + eventId + "'");
        }
        FlowExecutionControlContext context = createControlContext(externalContext);
        getListeners().fireRequestSubmitted(context);
        try {
            try {
                resume(context);
                ViewSelection selectedView = context.signalEvent(new Event(externalContext, eventId));
                return pause(context, selectedView);
            } catch (StateException e) {
                return pause(context, handleException(e, context));
            }
        } finally {
            getListeners().fireRequestProcessed(context);
        }
    }

    /**
	 * Resume this flow execution.
	 * @param context the state request context
	 */
    protected void resume(FlowExecutionControlContext context) {
        getActiveSessionInternal().setStatus(FlowSessionStatus.ACTIVE);
        getListeners().fireResumed(context);
    }

    /**
	 * Pause this flow execution.
	 * @param context the state request context
	 * @param selectedView the initial selected view to render
	 * @return the selected view to render
	 */
    protected ViewSelection pause(FlowExecutionControlContext context, ViewSelection selectedView) {
        if (!isActive()) {
            return selectedView;
        }
        getActiveSessionInternal().setStatus(FlowSessionStatus.PAUSED);
        getListeners().firePaused(context, selectedView);
        if (logger.isDebugEnabled()) {
            if (selectedView != null) {
                logger.debug("Paused to render " + selectedView + " and wait for user input");
            } else {
                logger.debug("Paused to wait for user input");
            }
        }
        return selectedView;
    }

    public FlowExecutionListeners getListeners() {
        return listeners;
    }

    /**
	 * Create a flow execution control context for given event.
	 * <p>
	 * The default implementation uses the <code>FlowControlContextImpl</code>
	 * class. Subclasses can override this to use a custom class.
	 * @param externalContext the external context
	 */
    protected FlowExecutionControlContext createControlContext(ExternalContext externalContext) {
        return new FlowExecutionControlContextImpl(this, externalContext);
    }

    /**
	 * Returns the currently active flow session.
	 * @throws IllegalStateException this execution is not active
	 */
    protected FlowSessionImpl getActiveSessionInternal() throws IllegalStateException {
        assertActive();
        return (FlowSessionImpl) flowSessions.getLast();
    }

    /**
	 * Returns the parent flow session of the currently active flow session.
	 * @return the parent flow session
	 * @throws IllegalArgumentException when this execution is not active or
	 * when the current flow session has no parent (e.g. is the root flow
	 * session)
	 */
    protected FlowSession getParentSession() throws IllegalArgumentException {
        assertActive();
        Assert.state(!getActiveSession().isRoot(), "There is no parent flow session for the currently active flow session");
        return (FlowSession) flowSessions.get(flowSessions.size() - 2);
    }

    /**
	 * Returns the flow session associated with the root flow.
	 * @throws IllegalStateException this execution is not active
	 */
    protected FlowSession getRootSession() throws IllegalStateException {
        assertActive();
        return (FlowSession) flowSessions.getFirst();
    }

    /**
	 * Check that this flow execution is active and throw an exception if it's
	 * not.
	 */
    protected void assertActive() throws IllegalStateException {
        if (!isActive()) {
            throw new IllegalStateException("This flow execution is not active, it has either ended or has never been started.");
        }
    }

    /**
	 * Sets the attributes shared by all sessions.
	 * @param scope the data shared by all sessions.
	 */
    public void setScope(AttributeMap scope) {
        this.scope = scope;
    }

    /**
	 * Set the state that is currently active in this flow execution.
	 * @param newState the new current state
	 */
    protected void setCurrentState(State newState) {
        getActiveSessionInternal().setState(newState);
    }

    /**
	 * Activate a new <code>FlowSession</code> for the flow definition with
	 * the input provided. Pushes the new flow session onto the stack.
	 * @param flow the flow definition
	 * @param input the flow session input
	 * @return the new flow session
	 */
    public FlowSession activateSession(Flow flow, AttributeMap input) {
        FlowSessionImpl session;
        if (!flowSessions.isEmpty()) {
            FlowSessionImpl parent = getActiveSessionInternal();
            parent.setStatus(FlowSessionStatus.SUSPENDED);
            session = createFlowSession(flow, input, parent);
        } else {
            session = createFlowSession(flow, input, null);
        }
        flowSessions.add(session);
        session.setStatus(FlowSessionStatus.STARTING);
        if (logger.isDebugEnabled()) {
            logger.debug("Starting " + session);
        }
        return session;
    }

    /**
	 * Create a new flow session object. Subclasses can override this to return
	 * a special implementation if required.
	 * @param flow the flow that should be associated with the flow session
	 * @param input the input parameters used to populate the flow session
	 * @param parent the flow session that should be the parent of the newly
	 * created flow session
	 * @return the newly created flow session
	 */
    protected FlowSessionImpl createFlowSession(Flow flow, AttributeMap input, FlowSessionImpl parent) {
        return new FlowSessionImpl(flow, input, parent);
    }

    public FlowSession endActiveFlowSession() {
        FlowSessionImpl endingSession = (FlowSessionImpl) flowSessions.removeLast();
        endingSession.setStatus(FlowSessionStatus.ENDED);
        if (!flowSessions.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resuming session '" + getActiveSessionInternal().getFlow().getId() + "' in state '" + getActiveSessionInternal().getState().getId() + "'");
            }
            getActiveSessionInternal().setStatus(FlowSessionStatus.ACTIVE);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("[Ended] - this execution is now inactive");
            }
        }
        return endingSession;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        flowId = (String) in.readObject();
        scope = (AttributeMap) in.readObject();
        flowSessions = (LinkedList) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        if (getFlow() != null) {
            out.writeObject(getFlow().getId());
        } else {
            out.writeObject(flowId);
        }
        out.writeObject(scope);
        out.writeObject(flowSessions);
    }

    public synchronized void rehydrate(FlowLocator flowLocator, FlowExecutionListenerLoader listenerLoader) {
        if (isHydrated()) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Rehydrating");
        }
        Assert.notNull(flowLocator, "The flow locator is required");
        Assert.notNull(flowId, "The root flow id was not set during deserialization: was this flow execution deserialized properly?");
        flow = flowLocator.getFlow(flowId);
        flowId = null;
        Iterator it = flowSessions.iterator();
        while (it.hasNext()) {
            FlowSessionImpl session = (FlowSessionImpl) it.next();
            session.rehydrate(new FlowSessionFlowLocator(flow, flowLocator));
        }
        if (isActive()) {
            Assert.isTrue(getFlow() == getRootSession().getFlow(), "The root flow of the execution should be the same as the flow in the root flow session");
        }
        if (listenerLoader != null) {
            listeners = new FlowExecutionListeners(listenerLoader.getListeners(flow));
        } else {
            listeners = new FlowExecutionListeners();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Rehydrated");
        }
    }

    private static class FlowSessionFlowLocator implements FlowLocator {

        private FlowLocator flowLocator;

        private Flow rootFlow;

        public FlowSessionFlowLocator(Flow rootFlow, FlowLocator flowLocator) {
            this.rootFlow = rootFlow;
            this.flowLocator = flowLocator;
        }

        public Flow getFlow(String id) throws FlowArtifactException {
            if (rootFlow.getId().equals(id)) {
                return rootFlow;
            } else if (rootFlow.containsInlineFlow(id)) {
                return rootFlow.getInlineFlow(id);
            } else {
                return flowLocator.getFlow(id);
            }
        }
    }

    /**
	 * Returns whether this flow execution is hydrated.
	 */
    protected boolean isHydrated() {
        return flow != null;
    }

    public String toString() {
        if (!isActive()) {
            return "[Inactive " + getCaption() + "]";
        } else {
            if (isHydrated()) {
                return new ToStringCreator(this).append("flow", getFlow().getId()).append("activeFlow", getActiveSession().getFlow().getId()).append("currentState", getActiveSession().getState().getId()).append("flowSessions", flowSessions).toString();
            } else {
                return "[Unhydrated " + getCaption() + "]";
            }
        }
    }
}
