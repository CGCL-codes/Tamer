package alp;

/**
 * @author niki.waibel{@}gmx.net
 */
public class QueueEvent extends java.util.EventObject {

    enum Type {

        receivedPacketDroppedBecauseSequence, receivedPacketQueued, timeoutWaitingWillReceiveFirstInQueue, receivedPacketWaitedFor, foundPacketWaitedForInQueue
    }

    ;

    private Type type;

    private int packetSeq;

    private java.net.DatagramPacket packet;

    private int threadId;

    private int lossFirst;

    private int lossLast;

    QueueEvent() {
        super(new Object());
    }

    QueueEvent(Type type, int packetSeq) {
        this(type, packetSeq, null);
    }

    QueueEvent(Type type, int packetSeq, java.net.DatagramPacket packet) {
        super(new Object());
        this.type = type;
        this.packetSeq = packetSeq;
        this.packet = packet;
        this.threadId = -1;
    }

    @Override
    public final String toString() {
        String s = "QueueEvent: type=" + type + " packetSeq=" + packetSeq;
        if (threadId != -1) {
            s += " threadId=" + threadId;
        }
        return s;
    }

    final java.net.DatagramPacket getPacket() {
        return packet;
    }

    final void setPacket(java.net.DatagramPacket packet) {
        this.packet = packet;
    }

    final int getPacketSequence() {
        return packetSeq;
    }

    final void setPacketSequence(int packetSeq) {
        this.packetSeq = packetSeq;
    }

    final Type getType() {
        return type;
    }

    final void setType(Type type) {
        this.type = type;
    }

    final int getLossFirst() {
        return lossFirst;
    }

    final void setLossFirst(int lossFirst) {
        this.lossFirst = lossFirst;
    }

    final int getLossLast() {
        return lossLast;
    }

    final void setLossLast(int lossLast) {
        this.lossLast = lossLast;
    }

    final void setThread(int t) {
        this.threadId = t;
    }
}
