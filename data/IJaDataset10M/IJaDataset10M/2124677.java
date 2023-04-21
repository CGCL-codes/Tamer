package org.asteriskjava.manager.event;

/**
 * Abstract base class for RTP statistics events.<p>
 *
 * @author srt
 * @version $Id: AbstractRtpStatEvent.java 1164 2008-09-18 02:40:44Z sprior $
 * @since 1.0.0
 */
public abstract class AbstractRtpStatEvent extends ManagerEvent {

    private static final long serialVersionUID = 1L;

    private Long ssrc;

    private Long lostPackets;

    private Double jitter;

    public AbstractRtpStatEvent(Object source) {
        super(source);
    }

    /**
     * Returns the synchronization source identifier that uniquely identifies the source of a stream.
     *
     * @return the synchronization source identifier.
     */
    public Long getSsrc() {
        return ssrc;
    }

    public void setSsrc(Long ssrc) {
        this.ssrc = ssrc;
    }

    /**
     * Returns the number of lost packets.
     *
     * @return the number of lost packets.
     */
    public Long getLostPackets() {
        return lostPackets;
    }

    public void setLostPackets(Long lostPackets) {
        this.lostPackets = lostPackets;
    }

    public Double getJitter() {
        return jitter;
    }

    public void setJitter(Double jitter) {
        this.jitter = jitter;
    }
}
