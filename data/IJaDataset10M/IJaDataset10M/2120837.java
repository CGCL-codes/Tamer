package org.mobicents.media.server.impl.rtp.clock;

import org.mobicents.media.Format;
import org.mobicents.media.format.AudioFormat;
import org.mobicents.media.server.impl.rtp.RtpClock;

/**
 *
 * @author kulikov
 */
public class AudioClock extends RtpClock {

    private double sampleRate;

    @Override
    public void setFormat(Format format) {
        super.setFormat(format);
        AudioFormat fmt = (AudioFormat) format;
        this.sampleRate = 1000 / fmt.getSampleRate();
    }

    @Override
    public long getTime(long timestamp) {
        return (long) (sampleRate * timestamp);
    }

    @Override
    public long getTimestamp(long time) {
        return (long) (time / sampleRate);
    }
}
