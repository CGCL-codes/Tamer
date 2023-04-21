package net.sf.jIPtables.log;

/**
 * An IPv6 packet logged by the firewall
 */
public class IPv6Packet extends Packet {

    private int totalLength;

    private int trafficClass;

    private int hopLimit;

    private long flowLabel;

    private int nexthdr;

    private int id;

    private int fragmentOffset;

    @Override
    protected void setField(String field, String value) {
        super.setField(field, value);
        if ("tc".equals(field)) trafficClass = Integer.parseInt(value); else if ("hoplimit".equals(field)) hopLimit = Integer.parseInt(value); else if ("tot_len".equals(field)) totalLength = Integer.parseInt(value); else if ("flowlabel".equals(field)) flowLabel = Long.parseLong(value); else if ("nexthdr".equals(field)) nexthdr = Integer.parseInt(value); else if ("id".equals(field)) id = Integer.parseInt(value); else if ("frag".equals(field)) fragmentOffset = Integer.parseInt(value);
    }

    /**
	 * @return The length of payload in bytes, the next headers are also part of
	 *         the
	 *         payload
	 */
    public int getPayloadLength() {
        return totalLength;
    }

    /**
	 * @return The assigned traffic class
	 */
    public int getTrafficClass() {
        return trafficClass;
    }

    /**
	 * @return The hop limit of the IPv6 packet (commonly it corresponds to the
	 *         ttl
	 *         field in IPv4)
	 */
    public int getHopLimit() {
        return hopLimit;
    }

    /**
	 * @return The flow label
	 */
    public long getFlowLabel() {
        return flowLabel;
    }

    /**
	 * @return The protocol number of the next header
	 */
    public int getNextHeader() {
        return nexthdr;
    }

    /**
	 * @return The IP packet identification id used by the ip fragments
	 */
    public int getPacketId() {
        return id;
    }

    /**
	 * @return The fragment offset of an ip fragment packet
	 */
    public int getFragmentOffset() {
        return fragmentOffset;
    }
}
