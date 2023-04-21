package net.sourceforge.insim4j.client.events;

import net.sourceforge.insim4j.client.InSimHost;
import net.sourceforge.insim4j.insim.interfaces.IInSimResponsePacket;

/**
 * ResponsePacketEvent occurs when new {@link IInSimResponsePacket} has been received.
 *
 * @author Jiří Sotona
 *
 */
public class ResponsePacketEvent {

    /**
	 * Source of event.
	 */
    private Object fSource;

    /**
	 * {@link InSimHost} which sent response packet.
	 */
    private InSimHost fHost;

    /**
	 * Response packet, which was received.
	 */
    private IInSimResponsePacket fPacket;

    public ResponsePacketEvent(final Object pSource, final InSimHost pHost, final IInSimResponsePacket pPacket) {
        fSource = pSource;
        fHost = pHost;
        fPacket = pPacket;
    }

    /**
	 * Getter.<br />
	 * Source of event.
	 * 
	 * @return the source
	 */
    public Object getSource() {
        return fSource;
    }

    /**
	 * Getter.
	 * 
	 * @return the host
	 */
    public InSimHost getHost() {
        return this.fHost;
    }

    /**
	 * Getter.<br />
	 * Response packet, which was received. 
	 * 
	 * @return the packet
	 */
    public IInSimResponsePacket getPacket() {
        return fPacket;
    }
}
