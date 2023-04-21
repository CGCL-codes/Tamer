package alp;

/**
 * @author niki.waibel{@}gmx.net
 */
public final class SessionSecurityEvent extends SessionEvent {

    private boolean auth, encr;

    SessionSecurityEvent(boolean authenticated, boolean encrypted) {
        super(SessionEvent.Type.security);
        this.auth = authenticated;
        this.encr = encrypted;
    }

    public boolean isAuthenticated() {
        return auth;
    }

    public boolean isEncrypted() {
        return encr;
    }
}
