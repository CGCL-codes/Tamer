package org.turnlink.sclm.service;

import org.turnlink.sclm.model.AccountSession;

public interface SessionManager {

    public AccountSession createAccountSession(String userName, String password);

    public AccountSession getActiveSession(String token);

    public void removeOldSessions();
}
