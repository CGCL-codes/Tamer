package net.sf.mailsomething.auth;

/**
 * 
 * Should probably be renamed to Authenticator, like util.Authenticator
 * 
 * Note: I am no expert in security, so the design of this may be wrong.
 * 
 * 
 * @author Stig Tanggaard
 * @created 05-05-2003
 */
public interface Authenticator {

    /**
	 * Method to get a challenge. A challenge is some word/string or similar
	 * encrypted with the user?s public key. The peer which needs to be
	 * authenticated calls this, and responds with the decrypted string.
	 * @param username
	 * @return byte[]
	 */
    public Challenge getChallenge(String userid);

    public boolean allowsSimpleAuth();

    /**
	 * Okay, I have added this method to be used in a none p2p realm,
	 * for example if a peer creates his local realm and creates some
	 * users to access the pop3 server through another program. 
	 * @param username
	 * @param password
	 * @return boolean
	 */
    public boolean authenticate(String username, String password);

    /**
	 * Returns true if a user is already authenticated. Only problem
	 * existing in this scheme is to handle from where the user
	 * access the authenticator. We need to make sure that another
	 * peer doesnt 'capture' the session of the authenticated user.
	 * 
	 * @param username
	 * @return boolean
	 */
    public boolean isAuthenticated(String username);
}
