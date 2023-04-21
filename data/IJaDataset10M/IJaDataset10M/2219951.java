package net.sf.insim4j.packetfactory.events;

import net.sf.insim4j.InSimHost;
import net.sf.insim4j.ResponsePacket;
import net.sf.insim4j.ResponsePacketEvent;
import net.sf.insim4j.insim.InSimResponsePacket;

/**
 * ResponsePacketEvent occurs when new {@link InSimResponsePacket} has been
 * received. Meant to be subclassed by concrete response packet events.
 *
 * @author Jiří Sotona
 *
 */
abstract class AbstractResponsePacketEvent implements ResponsePacketEvent {

    /**
	 * Source of event.
	 */
    private final Object fSource;

    /**
	 * {@link InSimHost} which sent response packet.
	 */
    private final InSimHost fHost;

    /**
	 * Response packet, which was received.
	 */
    private final ResponsePacket fPacket;

    /**
	 * Create instance.
	 *
	 * @param source
	 *            source of event
	 * @param host
	 *            host which the event is related to
	 * @param packet
	 *            packet of event
	 */
    public AbstractResponsePacketEvent(final Object source, final InSimHost host, final ResponsePacket packet) {
        fSource = source;
        fHost = host;
        fPacket = packet;
    }

    @Override
    public Object getSource() {
        return fSource;
    }

    @Override
    public InSimHost getHost() {
        return fHost;
    }

    @Override
    public ResponsePacket getPacket() {
        return fPacket;
    }
}
