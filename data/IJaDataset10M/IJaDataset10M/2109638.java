package net.sf.mailsomething.auth.impl;

import java.util.Vector;
import net.sf.mailsomething.auth.Authenticator;
import net.sf.mailsomething.auth.Challenge;

/**
 * @author Stig Tanggaard
 * @created 06-05-2003
 * 
 */
public class SimpleAuthenticator implements Authenticator {

    private UserList userlist;

    private Vector loggedin;

    public SimpleAuthenticator(UserList userlist) {
        this.userlist = userlist;
        loggedin = new Vector();
    }

    /**
	 * @see net.sf.mailsomething.auth.Authenticator#getChallenge(String)
	 */
    public Challenge getChallenge(String userid) {
        return null;
    }

    /**
	 * @see net.sf.mailsomething.auth.Authenticator#allowsSimpleAuth()
	 */
    public boolean allowsSimpleAuth() {
        return true;
    }

    /**
	 * @see net.sf.mailsomething.auth.Authenticator#authenticate(String, String)
	 */
    public boolean authenticate(String username, String password) {
        return false;
    }

    public boolean isAuthenticated(String username) {
        for (int i = 0; i < loggedin.size(); i++) {
            if (((String) loggedin.elementAt(i)).equals(username)) {
                return true;
            }
        }
        return false;
    }
}
