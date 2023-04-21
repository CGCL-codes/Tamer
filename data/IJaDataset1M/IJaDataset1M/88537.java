package net.sf.insim4j.event;

import net.sf.insim4j.InSimHost;
import net.sf.insim4j.outsim.OutSimPacket;

/**
 * OutSimPacketEvent occurs when new {@link OutSimPacket} has been received.
 *
 * @author Jiří Sotona
 *
 */
public class OutSimPacketEvent extends AbstractResponsePacketEvent {

    /**
	 * Create instance.
	 *
	 * @param source
	 *            source of event
	 * @param host
	 *            host which the event is related to
	 * @param packet
	 *            OutSim response packet of event
	 */
    public OutSimPacketEvent(final Object source, final InSimHost host, final OutSimPacket packet) {
        super(source, host, packet);
    }

    @Override
    public OutSimPacket getPacket() {
        return (OutSimPacket) super.getPacket();
    }
}
