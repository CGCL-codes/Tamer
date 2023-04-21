package oxygen.wiki;

import java.io.Serializable;

/**
 * Represents a lock object, which encapsulates the user editing the page,
 * the page being edited, and the time at which the lock to edit the page 
 * was acquired.
 * @author ugorji
 */
public class WikiEditLock implements Serializable {

    private String username;

    private long locktime;

    private String pagename;

    public WikiEditLock(String _username, String _pagename) {
        username = _username;
        pagename = _pagename;
        locktime = System.currentTimeMillis();
    }

    public String getUsername() {
        return username;
    }

    public String getPagename() {
        return pagename;
    }

    public long getLocktime() {
        return locktime;
    }

    public String toString() {
        return ("username: " + username + ", pagename: " + pagename);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WikiEditLock)) return false;
        WikiEditLock o2 = (WikiEditLock) o;
        if ((username == null && o2.username != null) || (!username.equals(o2.username)) || (pagename == null && o2.pagename != null) || (!pagename.equals(o2.pagename))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = 17;
        i = (username != null) ? (i ^ username.hashCode()) : i;
        i = (pagename != null) ? (i ^ pagename.hashCode()) : i;
        return i;
    }
}
