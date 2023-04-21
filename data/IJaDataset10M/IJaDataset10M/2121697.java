package org.springframework.webflow.executor;

import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.FlowException;
import org.springframework.webflow.core.collection.MutableAttributeMap;

/**
 * The central facade and entry-point service interface into the Spring Web Flow system for <i>driving the executions of
 * flow definitions</i>. This interface defines a coarse-grained system boundary suitable for invocation by most
 * clients.
 * <p>
 * Implementations of this interface abstract away much of the internal complexity of the web flow execution subsystem,
 * which consists of launching and resuming managed flow executions.
 * 
 * @author Keith Donald
 */
public interface FlowExecutor {

    /**
	 * Launch a new execution of the flow with the provided id.
	 * @param flowId the flow definition identifier; should be unique among all top-level flow definitions (required).
	 * @param input input to pass to the new execution on startup (optional)
	 * @param context access to the calling environment (required)
	 */
    public FlowExecutionResult launchExecution(String flowId, MutableAttributeMap input, ExternalContext context) throws FlowException;

    /**
	 * Resume the flow execution with the provided execution key.
	 * @param flowExecutionKey the key of a paused execution of the flow definition
	 * @param context access to the calling environment
	 */
    public FlowExecutionResult resumeExecution(String flowExecutionKey, ExternalContext context) throws FlowException;
}
