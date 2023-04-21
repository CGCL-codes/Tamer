package server.auth;

import java.io.Serializable;
import com.sun.sgs.auth.Identity;
import common.Constants;

public class AdminIdentityImpl implements Identity, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6945659342399258762L;

    public String getName() {
        return Constants.ADMIN_NAME;
    }

    public void notifyLoggedIn() {
    }

    public void notifyLoggedOut() {
    }
}
