package org.springframework.webflow.execution.repository;

import org.springframework.webflow.execution.FlowExecutionKey;

/**
 * Thrown when the flow execution with the persistent identifier provided could not be restored.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowExecutionRestorationFailureException extends FlowExecutionAccessException {

    /**
	 * Creates a new flow execution restoration failure exception.
	 * @param flowExecutionKey the key of the execution that could not be restored
	 * @param cause the root cause of the restoration failure
	 */
    public FlowExecutionRestorationFailureException(FlowExecutionKey flowExecutionKey, Exception cause) {
        super(flowExecutionKey, "A problem occurred restoring the flow execution with key '" + flowExecutionKey + "'", cause);
    }
}
