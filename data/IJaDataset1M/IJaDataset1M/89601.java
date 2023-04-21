package verinec.netsim.firewall.packetFilter.matchList.ipv4;

import verinec.netsim.entities.packets.IPPacket;
import verinec.netsim.firewall.packetFilter.matchList.ISimpleMatch;

/**Interface for all IP Match Cases.
 * 
 * @author jason.hug@unifr.ch
 * @version $Revision$
 *
 */
public interface IPacketMatchIP extends ISimpleMatch {

    /**
	 * Checks wether an IP packet matches to one of the set rules.
	 * @param ipPacket IP Packet that should be inspected.
	 * @return True if the IP packet matched to all of the specified rules.
	 */
    public boolean match(IPPacket ipPacket);
}
