package org.sgodden.ui.mvc;

/**
 * A flow resolution pointing to a controller as the next
 * invocation step.
 */
public interface ControllerFlowOutcome extends FlowOutcome {

    /**
     * Returns the controller which was configured as the outcome of the current
     * flow step.
     * @return the controller.
     */
    public Object getController();

    /**
     * Returns the name of the method to invoke on the controller, or
     * <code>null</code> if no method name was specified.
     * @return
     */
    public String getMethodName();
}
