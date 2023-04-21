package org.springframework.webflow.execution;

/**
 * Thrown when a flow definition was found during a lookup operation
 * but could not be constructed.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowConstructionException extends FlowLocatorException {

    /**
	 * Creates an exception indicating a flow definition could not be found.
	 * @param flowId the flow id
	 * @param registeredFlowIds all flow ids known to the registry generating this exception
	 */
    public FlowConstructionException(String flowId, Throwable cause) {
        super(flowId, "An exception occured constructing the flow with id '" + flowId + "'", cause);
    }
}
