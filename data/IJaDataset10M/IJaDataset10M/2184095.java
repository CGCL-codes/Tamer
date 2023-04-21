package rice.pastry.wire.messaging.datagram;

import java.io.*;
import rice.pastry.wire.*;
import rice.pastry.*;

/**
 * Class which represents a "ping" message sent through the
 * udp pastry system.
 *
 * @version $Id: PingMessage.java,v 1.1.1.1 2003/06/17 21:10:44 egs Exp $
 *
 * @author Alan Mislove
 */
public class PingMessage extends DatagramMessage {

    private transient WireNodeHandle handle;

    /**
   * Constructor
   */
    public PingMessage(NodeId source, NodeId destination, int num, WireNodeHandle handle) {
        super(source, destination, num);
        this.handle = handle;
    }

    public String toString() {
        return "PingMessage from " + getSource() + " to " + getDestination();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        handle.pingStarted();
        oos.defaultWriteObject();
    }
}
