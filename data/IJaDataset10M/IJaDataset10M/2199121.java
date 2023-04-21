package de.uni_bremen.informatik.p2p.peeranha42.core.plugin.security;

import java.security.Permission;
import de.uni_bremen.informatik.p2p.peeranha42.core.plugin.Client;

/**
 * Implements the Security Manager for the Client
 *
 * @author D. Gehrke, M. W?rthele
 */
public class PeeranhaSecurityManager extends SecurityManager {

    /**
     * PeeranhaSecurityManager Implements the SecurityManager for Peeranha
     */
    public PeeranhaSecurityManager() {
        super();
    }

    public void checkExit(int status) {
        if (!Client.isExitAllowed()) {
            throw new SecurityException("Exit not allowed");
        }
    }

    public void checkPermission(Permission perm, Object context) {
    }

    public void checkPermission(Permission perm) {
    }
}
