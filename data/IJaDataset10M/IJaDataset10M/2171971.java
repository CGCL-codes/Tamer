package agonism.ce;

/**
 * This exception is thrown by Capabilities implementors when a named
 * action does not exist. Note that this is not a checked exception as it should
 * not occur in normal usage.
 */
public class UnknownActionException extends RuntimeException {

    private final Capabilities m_capabilities;

    private final String m_actionName;

    public UnknownActionException(Capabilities capabilities, String actionName) {
        m_capabilities = capabilities;
        m_actionName = actionName;
    }

    public Capabilities getCapabilities() {
        return m_capabilities;
    }

    public String getActionName() {
        return m_actionName;
    }

    public String getMessage() {
        return m_actionName + " was not found on actor " + m_capabilities.getActor();
    }
}
