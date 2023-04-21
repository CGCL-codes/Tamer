package openvend.cgi;

import openvend.lang.OvException;

/**
 * Thrown when an application attempts to use a {@link openvend.cgi.OvCgiParam} that is not declared.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.3 $
 * @since 1.0
 */
public class OvNoSuchCgiParamException extends OvException {

    private static final long serialVersionUID = 6164027041110099222L;

    /**
     * Creates a new CGI parameter exception with no detail message or root cause.<p>
     */
    public OvNoSuchCgiParamException() {
        super();
    }

    /**
     * Creates a new CGI parameter exception with a root cause.<p>
     * 
     * @param rootCause the root cause
     */
    public OvNoSuchCgiParamException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Creates a new CGI parameter exception with a detail message.<p>
     * 
     * @param message the detail message
     */
    public OvNoSuchCgiParamException(String message) {
        super(message);
    }

    /**
     * Creates a new CGI parameter exception with a detail message and root cause.<p>
     * 
     * @param message the detail message
     * @param rootCause the cause of the exception
     */
    public OvNoSuchCgiParamException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
