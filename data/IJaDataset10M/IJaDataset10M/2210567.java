package org.springframework.webflow.execution;

import org.springframework.util.Assert;
import org.springframework.webflow.Flow;

/**
 * A aimple flow execution listener loader that simply returns a static listener
 * array on each invocation. For more elaborate needs see the
 * {@link ConditionalFlowExecutionListenerLoader}.
 * 
 * @see ConditionalFlowExecutionListenerLoader
 * 
 * @author Keith Donald
 */
public final class StaticFlowExecutionListenerLoader implements FlowExecutionListenerLoader {

    /**
	 * The listener array to return when {@link #getListeners(Flow)} is invoked. 
	 */
    private final FlowExecutionListener[] listeners;

    /**
	 * Creates a new listener loader that returns an empty listener array on
	 * each invocation.
	 */
    public StaticFlowExecutionListenerLoader() {
        this(new FlowExecutionListener[0]);
    }

    /**
	 * Creates a new listener loader that returns the provided listener array on
	 * each invocation. Clients should not attempt to modify the passed in array
	 * as no deep copy is made.
	 * @param listeners the listener array.
	 */
    public StaticFlowExecutionListenerLoader(FlowExecutionListener[] listeners) {
        Assert.notNull(listeners, "The listener array is required");
        this.listeners = listeners;
    }

    public FlowExecutionListener[] getListeners(Flow flow) {
        return listeners;
    }
}
