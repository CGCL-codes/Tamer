package org.asteriskjava.manager.event;

/**
 * An RtpReceiverStatEvent is triggered at the end of an RTP transmission and reports
 * transmission statistics.<p>
 * Available since Asterisk 1.6<p>
 * It is implemented in <code>main/rtp.c</code>
 *
 * @author srt
 * @version $Id: RtpReceiverStatEvent.java 1141 2008-08-19 18:08:19Z srt $
 * @since 1.0.0
 */
public class RtpReceiverStatEvent extends AbstractRtpStatEvent {

    private static final long serialVersionUID = 1L;

    private Long receivedPackets;

    private Double transit;

    private Long rrCount;

    public RtpReceiverStatEvent(Object source) {
        super(source);
    }

    /**
     * Returns the number of packets received.
     *
     * @return the number of packets received.
     */
    public Long getReceivedPackets() {
        return receivedPackets;
    }

    public void setReceivedPackets(Long receivedPackets) {
        this.receivedPackets = receivedPackets;
    }

    public Double getTransit() {
        return transit;
    }

    public void setTransit(Double transit) {
        this.transit = transit;
    }

    /**
     * Returns the number of receiver reports.
     *
     * @return the number of receiver reports.
     */
    public Long getRrCount() {
        return rrCount;
    }

    public void setRrCount(Long rrCount) {
        this.rrCount = rrCount;
    }
}
