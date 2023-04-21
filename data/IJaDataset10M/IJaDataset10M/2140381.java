package com.gcapmedia.dab.epg.binary;

import org.joda.time.Duration;

/**
 *
 */
public class DurationType implements Encodable {

    /**
	 * Duration value
	 */
    private Duration duration;

    /**
	 * Maximum value
	 */
    private static final int MAX_DURATION = 65535;

    /**
	 * 
	 * @param duration
	 */
    public DurationType(Duration duration) {
        this.duration = duration;
        if (duration.getMillis() / 1000 > MAX_DURATION) {
            throw new IllegalArgumentException("Duration is longer than " + MAX_DURATION + " seconds");
        }
    }

    /**
	 * Returns a type from its byte array representation
	 * @param bytes
	 * @return
	 */
    public static DurationType fromBytes(byte[] bytes) {
        BitParser bits = new BitParser(bytes);
        long seconds = bits.getLong(0, 16);
        return new DurationType(new Duration(seconds * 1000));
    }

    /**
	 * @see com.gcapmedia.dab.epg.binary.Encodable#getBytes()
	 */
    public byte[] getBytes() {
        long seconds = duration.getMillis() / 1000;
        BitBuilder bits = new BitBuilder(16);
        bits.put(0, 16, seconds);
        return bits.toByteArray();
    }

    /**
	 * @see com.gcapmedia.dab.epg.binary.Encodable#getLength()
	 */
    public int getLength() {
        return 16;
    }

    /**
	 * @return Returns the duration
	 */
    public Duration getDuration() {
        return duration;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DurationType)) {
            return false;
        }
        DurationType that = (DurationType) obj;
        return this.duration.isEqual(that.duration);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return duration.toString();
    }
}
