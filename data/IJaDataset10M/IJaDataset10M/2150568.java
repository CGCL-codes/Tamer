package xpusp.model;

import java.util.*;

/** 
 * This class represents a system user.
 * @sf $Header: /cvsroot/xpusp/xpusp/sources/xpusp/model/User.java,v 1.6 2001/12/10 09:25:54 krico Exp $
 */
public abstract class User {

    /** Login for this user
     */
    private String login;

    /** The password (encrypted) for this user. 
     * @see xpusp.model.Utility#cryptPassword
     */
    private byte[] password;

    public User() {
    }

    /**
     * Constructor that creates a new user with a give login, and encrypts the password sent as
     * a string and sets the result as this instances password
     */
    public User(String l, String p) {
        setLogin(l);
        setPassword(Utility.cryptPassword(p.getBytes()));
    }

    /** 
     * The type of this user
     */
    public final Class getType() {
        return this.getClass();
    }

    /**
   * get the login
   */
    public final String getLogin() {
        return login;
    }

    /**
   * get the password
   */
    public final byte[] getPassword() {
        return password;
    }

    /**
   * set the login
   */
    public final void setLogin(String s) {
        login = s;
    }

    /**
   * Set the password (already encrypted)
   */
    public final void setPassword(byte[] s) {
        password = s;
    }

    /**
   * Encrypt the string and set the encrypted byte[] as password
   */
    public final void setPassword(String s) {
        if (s == null) setPassword((byte[]) null); else setPassword(Utility.cryptPassword(s.getBytes()));
    }

    /**
     * Checks if the string, when encrytped equals to this users password.
     * @return true if the encryption of toCrypt is equal to getPassowrd()
     */
    public final boolean isPassword(String toCrypt) {
        if (getPassword() == null || toCrypt == null) return false;
        return Utility.checkPassword(getPassword(), toCrypt);
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof User) {
            User other = (User) o;
            if (Utility.equals(getLogin(), other.getLogin()) && Utility.equals(getPassword(), other.getPassword())) return true;
        }
        return false;
    }

    public String toString() {
        return this.getClass().getName() + " [login=" + getLogin() + "]";
    }
}
