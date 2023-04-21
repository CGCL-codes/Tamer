package se.kth.ict.id2203.broadcast.pb.lazy;

import se.kth.ict.id2203.broadcast.pb.PbDeliver;
import se.kth.ict.id2203.link.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class DataMessage extends Flp2pDeliver implements Comparable<DataMessage> {

    private PbDeliver deliverEvent;

    private int sequenceNumber;

    public DataMessage(Address source, PbDeliver deliverEvent, int sequenceNumber) {
        super(source);
        this.deliverEvent = deliverEvent;
        this.sequenceNumber = sequenceNumber;
    }

    public PbDeliver getDeliverEvent() {
        return deliverEvent;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataMessage other = (DataMessage) obj;
        if ((this.getSource() == null) ? (other.getSource() != null) : !this.getSource().equals(other.getSource())) {
            return false;
        }
        if ((this.deliverEvent == null) ? (other.deliverEvent != null) : !this.deliverEvent.equals(other.deliverEvent)) {
            return false;
        }
        if (this.sequenceNumber != other.sequenceNumber) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.getSource() != null ? this.getSource().hashCode() : 0);
        hash = 41 * hash + (this.deliverEvent != null ? this.deliverEvent.hashCode() : 0);
        hash = 41 * hash + this.sequenceNumber;
        return hash;
    }

    @Override
    public int compareTo(DataMessage o) {
        return this.getSource().equals(o.getSource()) ? Integer.compare(sequenceNumber, o.sequenceNumber) : 0;
    }
}
