package verinec.netsim.events;

import verinec.netsim.components.layers.abstractNetworkLayer;
import verinec.netsim.entities.packets.Packet;
import verinec.netsim.loggers.ILogger;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ModelComponent;

/**
 * Event with common properties for a packet that arrived from an upper layer
 * down to a layer.
 * 
 * @author Dominik Jungo
 * @version $Revision:835 $
 */
public class PacketArrivedEventDown extends PacketArrivedEvent {

    /**
	 * Constructs a new Event for a arriving packet.
	 * 
	 * @param model
	 *            a model
	 * @param sender
	 *            the sender of the packet
	 * @param receiver
	 *            the receiver of the packet
	 * @param packet
	 *            the packet
	 * @param logger
	 *            the Logger of this Event that may be used later to log
	 *            consequences of this event.
	 */
    public PacketArrivedEventDown(Model model, ModelComponent sender, ModelComponent receiver, Packet packet, ILogger logger) {
        super(model, sender, receiver, packet, logger);
    }

    /**
	 * @see desmoj.core.simulator.Event#eventRoutine(desmoj.core.simulator.Entity)
	 */
    public void eventRoutine(Entity arg0) {
        abstractNetworkLayer nl = (abstractNetworkLayer) getReceiver();
        nl.getDownEvent(this);
    }
}
