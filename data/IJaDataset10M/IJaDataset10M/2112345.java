package org.openymsg.legacy.network.event;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * To From Message Timestamp Location fileTransferReceived y y y y y
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 * @author S.E. Morris
 */
public class SessionFileTransferEvent extends SessionEvent {

    protected URL location = null;

    /**
     * CONSTRUCTORS
     * 
     * @throws MalformedURLException
     */
    public SessionFileTransferEvent(Object object, String to, String from, String message, long timestampInMillis, String location) throws MalformedURLException {
        super(object, to, from, message, timestampInMillis);
        this.location = new URL(location);
    }

    /**
     * Unqualified name of file sent
     */
    public String getFilename() {
        String s = location.getFile();
        if (s.lastIndexOf("/") > 0) s = s.substring(s.lastIndexOf("/") + 1);
        if (s.indexOf("?") >= 0) s = s.substring(0, s.indexOf("?"));
        return s;
    }

    /**
     * Accessors
     */
    public URL getLocation() {
        return location;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString()).append(" location:").append(location);
        return sb.toString();
    }
}
