package org.hypergraphdb.app.dataflow;

/**
 * 
 * <p>
 * Represents an exception originating from the DataFlow framework.
 * </p>
 *
 * @author Borislav Iordanov
 *
 */
public class DataFlowException extends RuntimeException {

    private static final long serialVersionUID = -1;

    public DataFlowException(String msg) {
        super(msg);
    }

    public DataFlowException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DataFlowException(Throwable cause) {
        super(cause);
    }
}
