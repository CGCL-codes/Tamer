package uk.org.ogsadai.resource.request;

/**
 * Listens to changes of the request execution status.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface RequestExecutionStatusListener {

    /**
     * Notifies this listener that the request execution status has changed.
     * 
     * @param status
     *            the new status
     */
    void setRequestExecutionStatus(RequestExecutionStatus status);
}
