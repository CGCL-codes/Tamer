package eu.more.diaball.login;

import eu.more.diaball.interfaces.User;

public class UserImpl implements User {

    String username;

    String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserImpl(String username) {
        this.username = username;
    }

    public String getName() {
        return username;
    }
}
