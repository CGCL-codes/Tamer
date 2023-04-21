package org.red5.server.session;

import org.red5.server.api.session.ISession;

/**
 * Represents the most basic type of "Session", loosely modeled after the HTTP Session used
 * in J2EE applications.
 *
 *
 * @author The Red5 Project (red5@osflash.org) 
 * @author Paul Gregoire (mondain@gmail.com)   
 */
public class Session implements ISession {

    private static final long serialVersionUID = 2893666721L;

    protected long created;

    protected boolean active;

    protected String sessionId;

    protected String destinationDirectory;

    protected String clientId;

    {
        created = System.currentTimeMillis();
        active = true;
    }

    public Session() {
    }

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getCreated() {
        return created;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void reset() {
        clientId = null;
    }

    public boolean isActive() {
        return active;
    }

    public void end() {
        active = false;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setDestinationDirectory(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String getDestinationDirectory() {
        return destinationDirectory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Session other = (Session) obj;
        if (sessionId == null) {
            if (other.sessionId != null) return false;
        } else if (!sessionId.equals(other.sessionId)) return false;
        return true;
    }
}
