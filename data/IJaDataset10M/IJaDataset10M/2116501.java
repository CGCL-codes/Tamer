package openr66.authentication;

import java.util.Arrays;

/**
 * @author Frederic Bregier
 *
 */
public class R66SimpleAuth {

    /**
     * Host ID
     */
    public String hostId = null;

    /**
     * Key
     */
    public byte[] key = null;

    /**
     * Is the current host Id an administrator (which can shutdown or change
     * bandwidth limitation)
     */
    public boolean isAdmin = false;

    /**
     * @param hostId
     * @param key
     */
    public R66SimpleAuth(String hostId, byte[] key) {
        this.hostId = hostId;
        this.key = key;
    }

    /**
     * Is the given key a valid one
     *
     * @param newkey
     * @return True if the key is valid (or any key is valid)
     */
    public boolean isKeyValid(byte[] newkey) {
        if (key == null) {
            return true;
        }
        if (newkey == null) {
            return false;
        }
        return Arrays.equals(key, newkey);
    }

    /**
     *
     * @param isAdmin
     *            True if the user should be an administrator
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String toString() {
        return "SimpleAuth: " + hostId + " " + isAdmin;
    }
}
