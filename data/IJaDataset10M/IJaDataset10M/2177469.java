package drcl.inet.core.ni;

import drcl.data.*;
import drcl.comp.Port;
import drcl.net.*;
import drcl.comp.queue.ActiveQueueContract;

/**
The class implements the point-to-point network interface and emulates the physical
link propagation.
It is fully specified by the bandwidth, MTU and propagation delay.  
As a network interface, only one packet can be transmitted at a time.
However as an emulated link, multiple packets may be outstanding, the number of which
dependes on the propagation delay, bandwidth and packet size.
 */
public class PointopointNI extends drcl.inet.core.NI_LinkEmulation {

    {
        downPort.setType(Port.PortType_OUT);
    }

    public PointopointNI() {
        super();
    }

    public PointopointNI(String id_) {
        super(id_);
    }

    protected synchronized void process(Object data_, Port inPort_) {
        if (data_ == null) return;
        if (ready == 0) {
            error(data_, "process()", inPort_, "Not ready to txmit");
            return;
        }
        if (data_ == this) {
            ready++;
            pullPort.doSending(ActiveQueueContract.getPullRequest());
            return;
        }
        Packet pkt_ = (Packet) data_;
        if (pkt_.size > mtu) {
            if (isGarbageEnabled()) drop(data_, "pkt size > mtu(" + mtu + ")");
            return;
        }
        double readyTime = getTime() + (double) (pkt_.size << 3) / bw;
        forkAt(pullPort, this, readyTime);
        if (linkEmulation) sendAt(downPort, pkt_, readyTime + propDelay); else sendAt(downPort, pkt_, readyTime);
    }

    public void duplicate(Object source_) {
        super.duplicate(source_);
        PointopointNI that_ = (PointopointNI) source_;
    }

    public void reset() {
        super.reset();
        ready = 1;
    }

    /** Time ready to transmit next packet. */
    protected int ready = 1;

    /** Returns true if the interface is ready to transmit more packets. */
    public boolean isReady() {
        return ready > 0;
    }
}
