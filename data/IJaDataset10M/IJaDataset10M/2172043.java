package ch.iserver.ace.net.impl.discovery;

import ch.iserver.ace.net.impl.RemoteUserProxyExt;

/**
 *
 */
public interface DiscoveryManager {

    public RemoteUserProxyExt[] getPeersWithNoSession();

    public void setSessionEstablished(String userId);

    public void setSessionTerminated(String userId);

    public boolean hasSessionEstablished(String userId);

    public RemoteUserProxyExt getUser(String userId);

    /**
	 * Gets the number of discovered users.
	 * 
	 * @return the number of discovered users
	 */
    public int getSize();
}
