package common;

import java.io.Serializable;

/**
 * A class that maintains the informations necessary to contact
 * a user. In this case - IP address.
 */
public class UserConnectionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public String ipAddress;
}
