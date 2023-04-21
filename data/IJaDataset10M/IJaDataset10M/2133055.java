package de.tud.kom.nat.nattrav.broker;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import de.tud.kom.nat.comm.msg.IPeerID;

/**
 * This class provides some static methods which are used to get the relay hosts
 * of the application callback in an organized way.
 *
 * @author Matthias Weinert
 */
public class RelayHostTools {

    /** 
	 * Returns the relay hosts for a given address, as returned by the
	 * application.
	 * 
	 * @param addr target address
	 * @return relay hosts to target address
	 */
    protected static HashSet<InetSocketAddress> getRelayHosts(IApplicationCallback callback, IPeerID targetID) {
        HashSet<InetSocketAddress> relayHosts = new HashSet<InetSocketAddress>();
        relayHosts.addAll(callback.getRelayHostsFor(targetID));
        return relayHosts;
    }

    /**
	 * This method rates the relay hosts and returns a queue where the first <tt>InetSocketAddress</tt>
	 * contains the best host and so on.
	 * 
	 * @param targetHost target host
	 * @return ordered queue with best relay host first
	 */
    public static Queue<InetSocketAddress> getRatedRelayHosts(IApplicationCallback callback, IPeerID targetID) {
        return getRatedRelayHosts(getRelayHosts(callback, targetID));
    }

    /**
	 * This method limits the amount of relay hosts.
	 * 
	 * @param relayHosts collection of available relay hosts
	 * @return queue of relay hosts
	 */
    protected static Queue<InetSocketAddress> getRatedRelayHosts(HashSet<InetSocketAddress> relayHosts) {
        LinkedList<InetSocketAddress> rated = new LinkedList<InetSocketAddress>();
        Iterator<InetSocketAddress> it = relayHosts.iterator();
        int count = 0;
        while (it.hasNext() && count++ < MAX_CHECK_RELAY_HOSTS) {
            rated.add(it.next());
        }
        return rated;
    }

    /** Maximum amount of hosts that is checked for relay capabilities */
    private static final int MAX_CHECK_RELAY_HOSTS = 15;
}
