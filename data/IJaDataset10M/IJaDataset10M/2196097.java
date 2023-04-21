package org.rdv.auth;

import java.util.EventListener;

/**
 * A listener interface for changes to an authentication.
 * 
 * @author Jason P. Hanley
 */
public interface AuthenticationListener extends EventListener {

    /**
   * Called when the authentication changes.
   * 
   * @param event  the authentication event
   */
    public void authenticationChanged(AuthenticationEvent event);
}
