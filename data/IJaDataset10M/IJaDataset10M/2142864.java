package local.server;

import java.util.Enumeration;

/** AuthenticationService is the interface used by a SIP server to access to
  * an AAA repository.
  */
public interface AuthenticationService extends Repository {

    /** Adds a new user at the database.
     * @param user the user name
     * @param key the user key
     * @return this object */
    public AuthenticationService addUser(String user, byte[] key);

    /** Sets the user key.
     * @param user the user name
     * @param key the user key
     * @return this object */
    public AuthenticationService setUserKey(String user, byte[] key);

    /** Gets the user key.
     * @param user the user name
     * @return the user key */
    public byte[] getUserKey(String user);
}
