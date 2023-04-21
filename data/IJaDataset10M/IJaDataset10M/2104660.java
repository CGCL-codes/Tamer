package org.speakmon.babble.events;

import java.util.EventObject;

/**
 * This event models the response to a LINKS command.
 * @version $Id: LinksEvent.java 239 2004-07-28 05:09:17Z speakmon $
 * @author Ben Speakmon
 */
public class LinksEvent extends EventObject {

    /**
     * Holds value of property mask.
     */
    private String mask;

    /**
     * Holds value of property hostname.
     */
    private String hostname;

    /**
     * Holds value of property hopCount.
     */
    private int hopCount;

    /**
     * Holds value of property serverInfo.
     */
    private String serverInfo;

    /**
     * Holds value of property done.
     */
    private boolean done;

    /**
     * Creates a new LinksEvent.
     * @param source the server mask the links were retrieved for
     */
    public LinksEvent(Object source) {
        super(source);
        mask = (String) source;
    }

    /**
     * Returns the server mask the links were returned for.
     * @return the server mask
     */
    public String getMask() {
        return this.mask;
    }

    /**
     * Returns this server's hostname.
     * @return the server's hostname
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * Sets this server's hostname.
     * @param hostname this server's hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Returns the hop count to this server.
     * @return the hop count
     */
    public int getHopCount() {
        return this.hopCount;
    }

    /**
     * Sets the hop count to this server.
     * @param hopCount the hop count
     */
    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    /**
     * Returns the admin-configured info for this server.
     * @return the server info
     */
    public String getServerInfo() {
        return this.serverInfo;
    }

    /**
     * Sets the admin-configured info for this server.
     * @param serverInfo the server info
     */
    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * Returns true if this is the last reply to the LINKS command.
     * @return true if this is the last reply
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Sets whether this is the last reply to the LINKS command.
     * @param done true if this is the last reply
     */
    public void setDone(boolean done) {
        this.done = done;
    }
}
