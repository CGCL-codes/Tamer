package verinec.netsim.firewall.packetFilter.matchList.portRange;

import java.util.logging.Logger;
import org.jdom.Element;
import verinec.netsim.entities.packets.UDPPacket;

/**
 * @author jason.hug@unifr.ch
 * @version $Revision: 825 $
 *
 */
public class MatchSourcePortRange extends PortRange implements IPacketMatchUDP {

    private final String ruleName = "match-source-port-range";

    private Logger logger;

    /**Creates a new Match Source Port Range rule.
	 * The Match Source Port Range rule holds all needed information to check if an incoming packet
	 * matches to this specified rule.
	 * @param node Element which holds the Match Source Port Range rule configurations.
	 */
    public MatchSourcePortRange(Element node) {
        super(node);
        logger = Logger.getLogger(getClass().getName());
        logger.entering(this.getClass().getName(), "MatchSourcePortRange()");
    }

    /**
	 * @see verinec.netsim.firewall.packetFilter.matchList.portRange.IPacketMatchUDP#match(verinec.netsim.entities.packets.UDPPacket)
	 */
    public boolean match(UDPPacket udpPacket) {
        boolean r;
        r = this.matchPortRange(udpPacket.getSrcPort());
        return this.handleNegate(r);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return super.toString();
    }

    /**
	 * @see verinec.netsim.firewall.packetFilter.matchList.ISimpleMatch#createLogEntry()
	 */
    public Element createLogEntry() {
        Element el = new Element(this.getRuleName());
        el.setAttribute("negate", String.valueOf(this.isNegate()));
        el.setAttribute("low", String.valueOf(this.getLow()));
        el.setAttribute("hi", String.valueOf(this.getHi()));
        return el;
    }

    /**
	 * @see verinec.netsim.firewall.packetFilter.matchList.ISimpleMatch#getRuleName()
	 */
    public String getRuleName() {
        return this.ruleName;
    }
}
