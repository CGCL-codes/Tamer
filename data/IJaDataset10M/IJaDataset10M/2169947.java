package verinec.netsim.processors;

import java.util.logging.Logger;
import verinec.netsim.components.layers.TransportLayer;
import verinec.netsim.components.layers.abstractNetworkLayer;
import verinec.netsim.constants.Protocolls;
import verinec.netsim.entities.packets.TCPPacket;
import verinec.netsim.events.PacketArrivedEventDown;
import verinec.netsim.events.PacketArrivedEventUp;
import verinec.netsim.util.net.sockets.SocketImpl;

/**
 * a TCP Processor (implemenatation of the TCP Protocol)
 * the biggest part of the TCP code is in #verinec.netsim.util.net.sockets.SocketImpl
 * 
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class TCPProcessor extends EventProcessor {

    private Logger jlogger;

    /**
	 * creates a new TCP Layer
	 * 
	 * @param layer
	 *            layer in which this processor operates
	 */
    public TCPProcessor(abstractNetworkLayer layer) {
        super(layer, Protocolls.TCP);
        jlogger = Logger.getLogger(getClass().getName());
    }

    /**
	 * creates a new TCP Layer
	 */
    public TCPProcessor() {
        super(Protocolls.TCP);
        jlogger = Logger.getLogger(getClass().getName());
    }

    /**
	 * @see verinec.netsim.processors.EventProcessor#processUp(verinec.netsim.events.PacketArrivedEventUp)
	 */
    public void processUp(PacketArrivedEventUp event) {
        jlogger.entering(getClass().getName(), "processUp", new Object[] { event });
        TCPPacket packet = (TCPPacket) event.getPacket();
        SocketImpl impl = ((TransportLayer) getLayer()).getSocketImpl(packet.getDstPort());
        if (impl == null) {
            impl = ((TransportLayer) getLayer()).getSocketImpl(0);
        }
        if (impl != null) {
            impl.setLogger(event.getLogger());
            impl.proccessUp(packet);
        } else {
            jlogger.severe("did not found the corresponding socket to process " + packet + " . Dropping packet.");
        }
        jlogger.exiting(getClass().getName(), "processUp");
    }

    /**
	 * @see verinec.netsim.processors.EventProcessor#processDown(verinec.netsim.events.PacketArrivedEventDown)
	 */
    public void processDown(PacketArrivedEventDown event) {
    }
}
