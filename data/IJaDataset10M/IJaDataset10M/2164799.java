package tyrex.security;

import javax.security.auth.callback.Callback;

/**
 * Callback for name/password authentication. Can be used by a login module
 * to request the user name, password and realm in order to authenticate.
 * <p>
 * This information will be supplied by a non-GUI capable container.
 * <p>
 * The realm is supplied by the login module, and may be ignored, used to
 * obtain a suitable name/password, or determine that no name/password is
 * available for that login module.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision: 1.4 $ $Date: 2001/03/12 19:20:18 $
 */
public class NamePasswordCallback implements Callback {

    private String _name;

    private char[] _password;

    private String _realm;

    /**
     * Sets the retrieved name.
     *
     * @param name The retrieved name (may be null)
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Returns the retrieved name.
     *
     * @return The retrieved name (may be null)
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the retrieved password.
     *
     * @param name The retrieved password (may be null)
     */
    public void setPassword(char[] password) {
        _password = password;
    }

    /**
     * Returns the retrieved password.
     *
     * @return The retrieved password (may be null)
     */
    public char[] getPassword() {
        return _password;
    }

    /**
     * Sets the realm.
     *
     * @param name The realm (may be null)
     */
    public void setRealm(String realm) {
        _realm = realm;
    }

    /**
     * Returns the realm.
     *
     * @return The realm (may be null)
     */
    public String getRealm() {
        return _realm;
    }
}
