package vidis.modules.vectorClockAlgorithm;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

/**
 * vector clock algorithm node, even cooler :-)
 * @author Dominik
 *
 */
public class VectorClockAlgorithmNode extends AUserNode {

    private VectorTime localTimeVector = new VectorTime(this, 0);

    @Display(name = "Automatische Events")
    public boolean autoEvents = false;

    @Display(name = "header1")
    public String getName() {
        return getId();
    }

    @Display(name = "header2")
    public String toString() {
        return "[" + getTimeVector() + "]";
    }

    @Override
    public void init() {
    }

    @Display(name = "Erzeuge Event")
    public void erzeugeEvent() {
        macheEventAktion();
    }

    @Display(name = "Autom.Events")
    public void toggleAutoEvents() {
        autoEvents = !autoEvents;
    }

    private void macheEventAktion() {
        increaseLocalTime();
        for (IUserLink link : this.getConnectedLinks()) {
            send(link, new VectorClockAlgorithmPacket(getTimeVector()));
        }
    }

    public VectorTime getTimeVector() {
        return localTimeVector;
    }

    public int getLocalTime() {
        return localTimeVector.getNodeTime(this);
    }

    public void execute() {
        if (autoEvents && Math.random() < 0.03) {
            macheEventAktion();
        }
    }

    private void increaseLocalTime() {
        getTimeVector().update(this, getLocalTime() + 1);
    }

    /**
     * simply wraps the send functionality to unify advanced parameters such as processing time
     * @param link the link to send over
     * @param packet the packet to send
     */
    private void send(IUserLink link, VectorClockAlgorithmPacket packet) {
        send(packet, link, 1 + (long) (Math.random() * 2));
    }

    private void receive(VectorClockAlgorithmPacket packet) {
        increaseLocalTime();
        VectorTime remoteTimeVector = packet.getTime();
        getTimeVector().update(remoteTimeVector);
    }

    public void receive(IUserPacket packet) {
        if (packet instanceof VectorClockAlgorithmPacket) {
            receive((VectorClockAlgorithmPacket) packet);
        } else {
        }
    }
}
