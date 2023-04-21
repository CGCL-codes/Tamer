package gruntspud.authentication;

/**
 * PasswordPair pairs a <code>PasswordKey</code> with the actual password
 * 
 * @author magicthize
 */
public class PasswordPair implements Comparable {

    private PasswordKey key;

    private String userPassword;

    private boolean persistant;

    /**
	 * Creates a new PasswordPair object.
	 * 
	 * @param key key
	 * @param userPassword password
	 */
    public PasswordPair(PasswordKey key, String userPassword) {
        this(key, userPassword, false);
    }

    /**
	 * Creates a new PasswordPair object.
	 * 
	 * @param key key
	 * @param userPassword password
	 * @param persistant save the password permanently
	 */
    public PasswordPair(PasswordKey key, String userPassword, boolean persistant) {
        setKey(key);
        setUserPassword(userPassword);
        setPersistant(persistant);
    }

    public int compareTo(Object o) {
        PasswordPair p = (PasswordPair) o;
        if ((isPersistant() && p.isPersistant()) || (!isPersistant() && !p.isPersistant())) {
            return key.compareTo(((PasswordPair) o).getKey());
        } else if (isPersistant()) {
            return 1;
        } else if (p.isPersistant()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
	 * Set the key for this password pair
	 * 
	 * @param key key
	 */
    public void setKey(PasswordKey key) {
        this.key = key;
    }

    /**
	 * Get the key for the password pair
	 * 
	 * @return key
	 */
    public PasswordKey getKey() {
        return key;
    }

    /**
	 * Set the password for the password pair
	 * 
	 * @param userPassword password
	 */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
	 * Get the password for the password pair
	 * 
	 * @return password
	 */
    public String getUserPassword() {
        return userPassword;
    }

    /**
	 * Set whether the password pair should be saved permanently. If <code>false</code> then the password is only saved while
	 * this instance of Gruntspud is running
	 * 
	 * @param persistant save password permanently
	 */
    public void setPersistant(boolean persistant) {
        this.persistant = persistant;
    }

    /**
	 * Get whether the password pair should be saved permanently. If <code>false</code> then the password is only saved while
	 * this instance of Gruntspud is running
	 * 
	 * @return save password permanently
	 */
    public boolean isPersistant() {
        return persistant;
    }
}
