package org.tolven.exeption;

import javax.ejb.ApplicationException;

@ApplicationException
public class GatekeeperAuthenticationException extends GatekeeperSecurityException {

    public static final String PASSWORD_EXPIRED = "passwordExpired";

    public static final String PASSWORD_EXPIRING = "passwordExpiring";

    private String realm;

    private String userId;

    public GatekeeperAuthenticationException() {
    }

    public GatekeeperAuthenticationException(String message) {
        super(message);
    }

    public GatekeeperAuthenticationException(String message, String userId, String realm) {
        super(message, userId, realm);
    }

    public GatekeeperAuthenticationException(String message, String userId, String realm, Throwable cause) {
        super(message, userId, realm, cause);
    }

    public GatekeeperAuthenticationException(String userId, String realm, Throwable cause) {
        super(userId, realm, cause);
    }

    public GatekeeperAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GatekeeperAuthenticationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getExceptionHeader() {
        return "Authentication Exception:";
    }

    @Override
    public String getRealm() {
        return realm;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
