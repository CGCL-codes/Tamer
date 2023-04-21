package sun.security.action;

import java.security.Security;

/**
 * A convenience class for retrieving the boolean value of a security property
 * as a privileged action.
 *
 * <p>An instance of this class can be used as the argument of
 * <code>AccessController.doPrivileged</code>.
 *
 * <p>The following code retrieves the boolean value of the security
 * property named <code>"prop"</code> as a privileged action: <p>
 *
 * <pre>
 * boolean b = java.security.AccessController.doPrivileged
 *              (new GetBooleanSecurityPropertyAction("prop")).booleanValue();
 * </pre>
 *
 */
public class GetBooleanSecurityPropertyAction implements java.security.PrivilegedAction<Boolean> {

    private String theProp;

    /**
     * Constructor that takes the name of the security property whose boolean
     * value needs to be determined.
     *
     * @param theProp the name of the security property
     */
    public GetBooleanSecurityPropertyAction(String theProp) {
        this.theProp = theProp;
    }

    /**
     * Determines the boolean value of the security property whose name was
     * specified in the constructor.
     *
     * @return the <code>Boolean</code> value of the security property.
     */
    public Boolean run() {
        boolean b = false;
        try {
            String value = Security.getProperty(theProp);
            b = (value != null) && value.equalsIgnoreCase("true");
        } catch (NullPointerException e) {
        }
        return b;
    }
}
