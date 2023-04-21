package org.bing.adapter.com.caucho.burlap.client;

/**
 * Encapsulates a remote address when no stub is available, e.g. for
 * Java MicroEdition.
 */
public class BurlapRemote {

    private String type;

    private String url;

    /**
   * Creates a new Burlap remote object.
   *
   * @param type the remote stub interface
   * @param url the remote url
   */
    public BurlapRemote(String type, String url) {
        this.type = type;
        this.url = url;
    }

    /**
   * Creates an uninitialized Burlap remote.
   */
    public BurlapRemote() {
    }

    /**
   * Returns the remote api class name.
   */
    public String getType() {
        return type;
    }

    /**
   * Returns the remote URL.
   */
    public String getURL() {
        return url;
    }

    /**
   * Sets the remote URL.
   */
    public void setURL(String url) {
        this.url = url;
    }

    /**
   * Defines the hashcode.
   */
    public int hashCode() {
        return url.hashCode();
    }

    /**
   * Defines equality
   */
    public boolean equals(Object obj) {
        if (!(obj instanceof BurlapRemote)) return false;
        BurlapRemote remote = (BurlapRemote) obj;
        return url.equals(remote.url);
    }

    /**
   * Readable version of the remote.
   */
    public String toString() {
        return "[Remote " + url + "]";
    }
}
