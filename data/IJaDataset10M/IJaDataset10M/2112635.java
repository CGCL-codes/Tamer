package rice.pastry.wire;

import java.net.*;
import rice.pastry.*;

/**
 * Wrapper class which contains an object to write and the address
 * it needs to be written to.
 *
 * @version $Id: PendingWrite.java,v 1.1.1.1 2003/06/17 21:10:44 egs Exp $
 *
 * @author Alan Mislove
 */
public class PendingWrite {

    private NodeId destination;

    private InetSocketAddress address;

    private Object o;

    /**
   * Contructs a PendingWrite from an address and an object
   *
   * @param address The destination address of this object
   * @param o The object to be written.
   */
    public PendingWrite(NodeId destination, InetSocketAddress address, Object o) {
        this.destination = destination;
        this.address = address;
        this.o = o;
    }

    /**
   * Returns the destination address of this write
   *
   * @return The destination address of this pending write.
   */
    public NodeId getDestination() {
        return destination;
    }

    /**
   * Returns the destination address of this write
   *
   * @return The destination address of this pending write.
   */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
   * Returns the object to be written
   *
   * @return The object to be written
   */
    public Object getObject() {
        return o;
    }
}
