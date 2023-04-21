package verinec.netsim.processors;

import org.jdom.Comment;
import org.jdom.Element;
import verinec.netsim.components.layers.PhysicalNetworkLayer;
import verinec.netsim.components.layers.abstractNetworkLayer;
import verinec.netsim.constants.Protocolls;
import verinec.netsim.entities.packets.Packet;
import verinec.netsim.events.PacketArrivedEventDown;
import verinec.netsim.events.PacketArrivedEventUp;
import desmoj.core.exception.SimAbortedException;
import desmoj.core.report.ErrorMessage;
import desmoj.core.simulator.SimTime;

/**
 * A Processor in Layer 1. It sends(broadcasts) data from the sender to everbody
 * connected to this Layer.
 * 
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class PhysicalProcessor802 extends EventProcessor {

    /**
	 * Creates a new Physical Processor
	 * 
	 * @param layer
	 *            the layer in which one this processor operates
	 */
    public PhysicalProcessor802(abstractNetworkLayer layer) {
        super(layer, Protocolls.PHYSICAL);
    }

    /**
	 * Creates a new Physical Processor
	 */
    public PhysicalProcessor802() {
        super(Protocolls.PHYSICAL);
    }

    /**
	 * @see verinec.netsim.processors.EventProcessor#processUp(verinec.netsim.events.PacketArrivedEventUp)
	 */
    public void processUp(PacketArrivedEventUp event) {
        ErrorMessage msg = new ErrorMessage(event.getModel(), "physical processor got a packet from a lower layer", "PhysicalProcessor802.processUp", "physical processor is in the lowest layer and cannot receive packets from below", "don t send a packet up to physical processor", SimTime.NOW);
        throw new SimAbortedException(msg);
    }

    /**
	 * @see verinec.netsim.processors.EventProcessor#processDown(verinec.netsim.events.PacketArrivedEventDown)
	 */
    public void processDown(PacketArrivedEventDown event) {
        Packet packet = event.getPacket();
        ((Element) event.getLogger()).addContent(new Comment(" nothing in physical layer for now "));
        ((PhysicalNetworkLayer) getLayer()).sendUp(packet, SimTime.NOW.getTimeValue() + 1, event.getSender(), event.getLogger());
    }
}
