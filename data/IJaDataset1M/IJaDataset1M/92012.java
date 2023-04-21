package com.simconomy.common.servlet;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * A abstract RemoteServiceServlet with some nice methods to make life a bit easier.
 *
 * @author Marco van Weverwijk
 *
 */
public class AbstractRemoteServiceServlet extends RemoteServiceServlet {

    private static final long serialVersionUID = 1L;

    /**
	 * Returns the username of the user thats logged in.
	 *
	 * @return the username of the user thats logged in.
	 */
    public String getUserName() {
        String userName = null;
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null) {
            Object obj = authentication.getPrincipal();
            if (obj instanceof org.acegisecurity.userdetails.UserDetails) {
                userName = ((UserDetails) obj).getUsername();
            } else if (obj != null) {
                userName = obj.toString();
            }
        }
        return userName;
    }
}
