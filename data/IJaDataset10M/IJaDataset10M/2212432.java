package officeserver.office.request;

import java.io.Serializable;

/**
 * @author Chris Bayruns
 * 
 */
public class AppointmentSelfRequest extends AppointmentRequest implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2200994636006102540L;

    /**
     * @author Chris Bayruns
     * @param type
     *            The type of request
     * @param myArgs
     *            The arguments
     */
    public AppointmentSelfRequest(TYPE type, Object... myArgs) {
        super(type, myArgs);
        this.self = true;
    }
}
