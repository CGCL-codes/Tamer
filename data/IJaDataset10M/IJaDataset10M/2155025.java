package Beans.Responses;

import Utilities.ApplicationException;
import java.util.ArrayList;

/**
 * Instances of this class are used when returning a response to a server management
 * request.
 *
 * @author Angel Sanadinov
 */
public class ServerManagementResponse extends ResponseBean {

    /**
     * No-argument constructor.
     *
     * @see ResponseBean#ResponseBean() 
     */
    public ServerManagementResponse() {
    }

    /**
     * Constructs the response object using the supplied data.
     *
     * @param requestResult the result of the request. Set to <code>true</code> for
     *                      success or to <code>false</code> for failure
     * @param exception an exception that occurred during request processing, if applicable
     */
    public ServerManagementResponse(boolean requestResult, ApplicationException exception) {
        super(requestResult, exception);
    }

    @Override
    public ArrayList<ApplicationException> getExceptions() {
        return null;
    }

    /**
     * Returns a textual representation of the object. <br>
     *
     * It is in the form: "ClassName: (super.toString)".
     *
     * @return the object represented as a String
     */
    @Override
    public String toString() {
        return ServerManagementResponse.class.getSimpleName() + ": (" + super.toString() + ")";
    }
}
