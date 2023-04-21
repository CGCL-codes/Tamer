package org.geonetwork.gaap.services.authentication;

import java.util.TimerTask;

/**
 * Task to expiry auth tokens
 *
 * @author Jose
 */
public class AuthExpiryTokensTask extends TimerTask {

    AuthenticationManager authManager;

    AuthExpiryTokensTask(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    public void run() {
        authManager.expiryAuthTokens();
    }
}
