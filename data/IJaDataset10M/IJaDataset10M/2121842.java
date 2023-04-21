package se.omnitor.protocol.rtp;

import java.util.Date;

/**
* This class encapsulates all the per source state information. Every source
* keeps track of all the other sources in the multicast group, from which it 
* has received a RTP or RTCP Packet. It is necessry to keep track of per state
* source information in order to provide effective reception quality feedback
* to all the sources that are in the multicast group. 
*
* @author Unknown
*/
public class Source extends Object {

    /**
     * Since Packets lost is a 24 bit number, it should be clamped at
     * WRAPMAX = 0xFFFFFFFF
     */
    protected static final long WRAPMAX = 0xFFFFFFFF;

    /**
     * source SSRC uint 32.
     */
    protected long ssrc;

    /**
     * Fraction of RTP data packets from source SSRC lost since the previous
     * SR or RR packet was sent, expressed as a fixed point number with the
     * binary point at the left edge of the field.  To get the actual fraction
     * multiply by 256 and take the integral part
     */
    protected double fraction;

    /**
     * Cumulative number of packets lost (signed 24bits).
     */
    protected long lost;

    /**
     * extended highest sequence number received.
     */
    protected long last_seq;

    /**
     * Interarrival jitter.
     */
    protected long jitter;

    /**
     * Last SR Packet from this source.
     */
    protected long lst;

    /**
     * Delay since last SR packet.
     */
    protected double dlsr;

    /**
     * Is this source and ActiveSender.
     */
    protected boolean activeSender;

    /**
     * Time the last RTCP Packet was received from this source.
     */
    protected double timeOfLastRTCPArrival;

    /**
     * Time the last RTP Packet was received from this source.
     */
    protected double timeOfLastRTPArrival;

    /**
     * Time the last Sender Report RTCP Packet was received from this source.
     */
    protected double timeofLastSRRcvd;

    /**
     * Total Number of RTP Packets Received from this source
     */
    protected int noOfRTPPacketsRcvd;

    /**
     * Sequence Number of the first RTP packet received from this source
     */
    protected long base_seq;

    /**
     * Number of RTP Packets Expected from this source 
     */
    protected long expected;

    /**
     * No of  RTP Packets expected last time a Reception Report was sent 
     */
    protected long expected_prior;

    /**
     * No of  RTP Packets received last time a Reception Report was sent
     */
    protected long received_prior;

    /**
     * Highest Sequence number received from this source
     */
    protected long max_seq;

    /**
     * Keep track of the wrapping around of RTP sequence numbers, since RTP 
     * Seq No. are only 16 bits
     */
    protected long cycles;

    /**
     * Constructor requires an SSRC for it to be a valid source. The
     * constructor initializes all the source class members to a default value
     *
     * @param sourceSSRC SSRC of the new source
     */
    public Source(long sourceSSRC) {
        long time = currentTime();
        ssrc = sourceSSRC;
        fraction = 0;
        lost = 0;
        last_seq = 0;
        jitter = 0;
        lst = 0;
        dlsr = 0;
        activeSender = false;
        timeOfLastRTCPArrival = time;
        timeOfLastRTPArrival = time;
        timeofLastSRRcvd = time;
        noOfRTPPacketsRcvd = 0;
        base_seq = 0;
        expected_prior = 0;
        received_prior = 0;
    }

    /**
    * Returns the extended maximum sequence for a source
    * considering that sequences cycle.
    *
    * @return  Sequence Number 
    *
    */
    public long getExtendedMax() {
        return (cycles + max_seq);
    }

    /**
    * This safe sequence update function will try to 
    * determine if seq has wrapped over resulting in a
    * new cycle.  It sets the cycle -- source level 
    * variable which keeps track of wraparounds.
    *
    * @param seq  Sequence Number
    */
    public void updateSeq(long seq) {
        if (max_seq == 0) max_seq = seq; else {
            if (max_seq - seq > 0.5 * WRAPMAX) cycles += WRAPMAX;
            max_seq = seq;
        }
    }

    /**
     * Updates the various statistics for this source , for example Packets 
     * Lost, Fraction lost, Delay since last SR etc, according to the data
     * gathered since a last SR or RR was sent out.  This method is called
     * prior to sending a Sender Report(SR)or a Receiver Report(RR) which will
     * include a Reception Report block about this source.
     *
     * @return Always returns zero. 
     */
    public int updateStatistics() {
        last_seq = getExtendedMax();
        expected = getExtendedMax() - base_seq + 1;
        lost = expected - noOfRTPPacketsRcvd;
        if (lost > 0xffffff) lost = 0xffffff;
        long expected_interval = expected - expected_prior;
        expected_prior = expected;
        long received_interval = noOfRTPPacketsRcvd - received_prior;
        received_prior = noOfRTPPacketsRcvd;
        long lost_interval = expected_interval - received_interval;
        if (expected_interval == 0 || lost_interval <= 0) fraction = 0; else fraction = (lost_interval << 8) / (double) expected_interval;
        dlsr = (timeofLastSRRcvd - currentTime()) / 65536;
        return 0;
    }

    /**
    *   Returns current time from the Date().getTime() function.
    *
    *   @return The current time.
    */
    private long currentTime() {
        return (new Date()).getTime();
    }
}
