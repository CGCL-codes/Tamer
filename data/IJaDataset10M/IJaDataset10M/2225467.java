package gizmo.uk.toolkit.rpc;

/**
 * <p>Error sub class for all RpcServlet exceptions and errors exception those thrown by registered
 * services</p>
 *
 * @author gareth bond
 */
public class RpcError extends Error {

    /**
     * <p>Construct a new RpcError</p>
     */
    public RpcError() {
        super();
    }

    /**
     * <p>Construct a new RpcError</p>
     *
     * @param message
     */
    public RpcError(String message) {
        super(message);
    }

    /**
     * <p>Construct a new RpcError</p>
     *
     * @param message
     * @param throwable
     */
    public RpcError(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * <p>Construct a new RpcError</p>
     * 
     * @param throwable
     */
    public RpcError(Throwable throwable) {
        super(throwable);
    }
}
