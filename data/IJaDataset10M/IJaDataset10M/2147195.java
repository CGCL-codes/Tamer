package org.monet.reportgenerator.shared.control.impl.action;

import org.monet.reportgenerator.shared.control.Action;
import org.monet.reportgenerator.shared.model.UserCredentials;

@SuppressWarnings("serial")
public class ActionAuthentication extends Action {

    private UserCredentials credentials;

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }
}
