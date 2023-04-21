package org.gudy.azureus2.core3.util;

import java.net.InetAddress;

public interface HostNameToIPResolverListener {

    /**
		 * 
		 * @param address	null if resolution failed
		 */
    public void hostNameResolutionComplete(InetAddress address);
}
