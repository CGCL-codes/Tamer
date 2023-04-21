package net.sf.fmj.media.multiplexer;

import javax.media.*;
import javax.media.format.*;
import net.sf.fmj.media.*;

public class RawSyncBufferMux extends RawBufferMux {

    boolean mpegBFrame = false;

    boolean mpegPFrame = false;

    protected boolean monoIncrTime = false;

    private long monoStartTime = 0;

    private long monoTime = 0;

    private Object waitLock = new Object();

    private boolean resetted = false;

    private boolean masterTrackEnded = false;

    static AudioFormat mpegAudio = new AudioFormat(AudioFormat.MPEG_RTP);

    static VideoFormat mpegVideo = new VideoFormat(VideoFormat.MPEG_RTP);

    static int THRESHOLD = 80;

    static int LEEWAY = 5;

    public RawSyncBufferMux() {
        super();
        timeBase = new RawMuxTimeBase();
        allowDrop = true;
        clock = new BasicClock();
        try {
            clock.setTimeBase(timeBase);
        } catch (Exception e) {
        }
    }

    /**
     * Returns a descriptive name for the plug-in. This is a user readable
     * string.
     */
    @Override
    public String getName() {
        return "Raw Sync Buffer Multiplexer";
    }

    @Override
    public boolean initializeTracks(Format[] trackFormats) {
        if (!super.initializeTracks(trackFormats)) return false;
        masterTrackID = 0;
        for (int i = 0; i < trackFormats.length; i++) {
            if (trackFormats[i] instanceof AudioFormat) masterTrackID = i;
        }
        return true;
    }

    /**
     * Process the buffer and multiplex it with data from other tracks. The
     * multiplexed output is sent to the output <tt>DataSource</tt>.
     * 
     * @param buffer
     *            the input buffer
     * @param trackID
     *            the index identifying the track where the input buffer
     *            belongs.
     * @return BUFFER_PROCESSED_OK if the processing is successful. Other
     *         possible return codes are defined in PlugIn.
     * @see PlugIn
     */
    @Override
    public int process(Buffer buffer, int trackID) {
        if ((buffer.getFlags() & Buffer.FLAG_RTP_TIME) != 0) {
            buffer.setFlags((buffer.getFlags() & ~Buffer.FLAG_RTP_TIME) | Buffer.FLAG_RELATIVE_TIME);
        }
        if (mc[trackID] != null && mc[trackID].isEnabled()) mc[trackID].process(buffer);
        if ((streams == null) || (buffer == null) || (trackID >= streams.length)) {
            return PlugIn.BUFFER_PROCESSED_FAILED;
        }
        if (buffer.isDiscard()) return BUFFER_PROCESSED_OK;
        if ((buffer.getFlags() & Buffer.FLAG_NO_WAIT) == 0) {
            if (buffer.getFormat() instanceof AudioFormat) {
                if (mpegAudio.matches(buffer.getFormat())) waitForPT(buffer.getTimeStamp(), trackID); else waitForPT(mediaTime[trackID], trackID);
            } else if (buffer.getTimeStamp() >= 0) {
                if (mpegVideo.matches(buffer.getFormat()) && (buffer.getFlags() & Buffer.FLAG_RTP_MARKER) != 0) {
                    byte[] payload = (byte[]) buffer.getData();
                    int offset = buffer.getOffset();
                    int ptype = payload[offset + 2] & 0x07;
                    if (ptype > 2) {
                        mpegBFrame = true;
                    } else if (ptype == 2) {
                        mpegPFrame = true;
                    }
                    if (ptype > 2 || (ptype == 2 && !mpegBFrame) || (ptype == 1 && !(mpegBFrame | mpegPFrame))) {
                        waitForPT(buffer.getTimeStamp(), trackID);
                    }
                } else {
                    waitForPT(buffer.getTimeStamp(), trackID);
                }
            }
        }
        updateTime(buffer, trackID);
        buffer.setFlags(buffer.getFlags() | Buffer.FLAG_NO_SYNC);
        if (!(buffer.getFormat() instanceof AudioFormat) || mpegAudio.matches(buffer.getFormat())) {
            if (monoIncrTime) {
                monoTime = monoStartTime + buffer.getTimeStamp() - mediaStartTime * 1000000;
                buffer.setTimeStamp(monoTime);
            }
        }
        if (buffer.isEOM() && trackID == masterTrackID) masterTrackEnded = true;
        buffer.setHeader(new Long(System.currentTimeMillis()));
        return streams[trackID].process(buffer);
    }

    @Override
    public void reset() {
        super.reset();
        mpegBFrame = false;
        mpegPFrame = false;
        synchronized (waitLock) {
            resetted = true;
            waitLock.notify();
        }
    }

    @Override
    public void setMediaTime(Time now) {
        super.setMediaTime(now);
        monoStartTime = monoTime + 10;
    }

    @Override
    public void syncStart(Time at) {
        masterTrackEnded = false;
        super.syncStart(at);
    }

    /**
     * Update the media time per track.
     */
    @Override
    protected void updateTime(Buffer buf, int trackID) {
        if (buf.getFormat() instanceof AudioFormat) {
            if (mpegAudio.matches(buf.getFormat())) {
                if (buf.getTimeStamp() < 0) {
                    if (systemStartTime >= 0) mediaTime[trackID] = (mediaStartTime + System.currentTimeMillis() - systemStartTime) * 1000000;
                } else mediaTime[trackID] = buf.getTimeStamp();
            } else {
                long t = ((AudioFormat) buf.getFormat()).computeDuration(buf.getLength());
                if (t >= 0) mediaTime[trackID] += t; else mediaTime[trackID] = buf.getTimeStamp();
            }
        } else {
            if (buf.getTimeStamp() < 0 && systemStartTime >= 0) mediaTime[trackID] = (mediaStartTime + System.currentTimeMillis() - systemStartTime) * 1000000; else mediaTime[trackID] = buf.getTimeStamp();
        }
        timeBase.update();
    }

    private void waitForPT(long pt, int trackID) {
        long delay;
        pt = pt / 1000000;
        if (masterTrackID == -1 || trackID == masterTrackID) {
            if (systemStartTime < 0) delay = 0; else delay = (pt - mediaStartTime) - (System.currentTimeMillis() - systemStartTime);
        } else {
            delay = pt - mediaTime[masterTrackID] / 1000000;
        }
        if (delay > 2000) return;
        while (delay > LEEWAY && !masterTrackEnded) {
            if (delay > THRESHOLD) delay = THRESHOLD;
            synchronized (waitLock) {
                try {
                    waitLock.wait(delay);
                } catch (Exception e) {
                    break;
                }
                if (resetted) {
                    resetted = false;
                    break;
                }
            }
            if (masterTrackID == -1 || trackID == masterTrackID) delay = (pt - mediaStartTime) - (System.currentTimeMillis() - systemStartTime); else delay = pt - mediaTime[masterTrackID] / 1000000;
        }
    }
}
