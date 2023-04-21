package com.parfumball.analyzer.ip;

/**
 * A utility class for doing TCP SEQ/ACK analysis.
 *  
 * @author prasanna
 */
public class TCPSeqAck {

    /**
     * The initial sequence number.
     */
    long seq = -1L;

    /**
     * The initial ack number.
     */
    long ack = -1L;

    /**
     * Create a new instance with the given sequence number.
     */
    public TCPSeqAck(long seq) {
        this.seq = seq;
    }

    /**
     * Create a new instance with the given base sequence and
     * ack numbers.
     * 
     * @param seq
     * @param ack
     */
    public TCPSeqAck(long seq, long ack) {
        this.seq = seq;
        this.ack = ack;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public long getAck() {
        return ack;
    }

    public void setAck(long ack) {
        this.ack = ack;
    }
}
