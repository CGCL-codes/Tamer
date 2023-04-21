package org.amityregion5.projectx.common.communication;

import java.io.Serializable;

/**
 * Used in the LobbyWindow. Basically wraps a String and adds a nice
 * boolean for ready states.
 *
 * @author Joe Stein
 */
public class User implements Serializable {

    private static final long serialVersionUID = 123L;

    private String username;

    private boolean ready;

    public User(String u) {
        username = u;
        ready = false;
    }

    public User(String u, boolean r) {
        username = u;
        ready = r;
    }

    public void setReady(boolean r) {
        ready = r;
    }

    public boolean isReady() {
        return ready;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User && ((User) o).username.equals(this.username)) {
            return true;
        } else if (o instanceof String) {
            return this.username.equals((String) o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }
}
