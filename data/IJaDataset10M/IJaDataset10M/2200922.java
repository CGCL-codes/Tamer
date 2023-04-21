package ch.iserver.ace.net.impl.discovery;

import ch.iserver.ace.net.impl.DiscoveryCallback;
import ch.iserver.ace.util.ParameterValidator;

/**
 *
 */
public class DiscoveryManagerFactory {

    private static DiscoveryManager instance;

    public static DiscoveryManager getDiscoveryManager(DiscoveryCallback callback) {
        if (instance == null) {
            ParameterValidator.notNull("callback", callback);
            instance = new DiscoveryManagerImpl(callback);
        }
        return instance;
    }
}
