package spooky.security.auth;

/**
 *
 * @author  ekarlsso
 * @version 
 */
public class SystemPrincipal implements java.security.Principal {

    private String sessionId;

    /** Creates new SystemPrincipal */
    public SystemPrincipal() {
    }

    public SystemPrincipal(String sessionId) {
        sessionId = sessionId;
    }

    public boolean equals(java.lang.Object obj) {
        if ((obj == null) || !(obj instanceof SystemPrincipal)) return false;
        SystemPrincipal sp = (SystemPrincipal) obj;
        return this.sessionId.equals(sp.getName());
    }

    public int hashCode() {
        return sessionId.hashCode();
    }

    public java.lang.String getName() {
        return sessionId;
    }

    public String toString() {
        return "Session id is: " + sessionId;
    }
}
