package net.plugg.server.exceptions;

/**
 * TODO 
 * <p>
 * Created on Mar 20, 2008
 * @author rcracel
 */
public class ServerAlreadyRunningException extends ServerException {

    private static final long serialVersionUID = 2999924677442816539L;

    /**
     * Constructor for ServerAlreadyRunningException
     * <p>
     * Created on Mar 20, 2008
     * @autor rcracel
     */
    public ServerAlreadyRunningException() {
    }

    /**
     * Constructor for ServerAlreadyRunningException
     * <p>
     * Created on Mar 20, 2008
     * @autor rcracel
     * @param message
     */
    public ServerAlreadyRunningException(String message) {
        super(message);
    }

    /**
     * Constructor for ServerAlreadyRunningException
     * <p>
     * Created on Mar 20, 2008
     * @autor rcracel
     * @param cause
     */
    public ServerAlreadyRunningException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor for ServerAlreadyRunningException
     * <p>
     * Created on Mar 20, 2008
     * @autor rcracel
     * @param message
     * @param cause
     */
    public ServerAlreadyRunningException(String message, Throwable cause) {
        super(message, cause);
    }
}
