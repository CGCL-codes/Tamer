package org.wfmc.wapi;

/**
 * @author Adrian Price
 */
public class WMUnsupportedOperationException extends WMWorkflowException {

    private static final long serialVersionUID = -5759667029599116053L;

    private final String operation;

    public WMUnsupportedOperationException(String operation) {
        super(new WMError(WMError.WM_UNSUPPORTED));
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public String toString() {
        return getClass().getName() + '@' + System.identityHashCode(this) + "[operation='" + operation + '\'' + ']';
    }
}
