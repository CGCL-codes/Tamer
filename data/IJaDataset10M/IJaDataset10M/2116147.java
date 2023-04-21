package org.mobicents.sdp;

import java.util.HashMap;

/**
 * Defines relation between audio/video format and RTP payload number as
 * specified by Audio/Video Profile spec.
 * 
 * http://www.iana.org/assignments/rtp-parameters
 * 
 * @author Oleg Kulikov
 * @author amit bhayani
 */
public abstract class AVProfile {

    public static final String AUDIO = "audio";

    public static final String VIDEO = "video";

    public static final RTPAudioFormat PCMU = new RTPAudioFormat(0, AudioFormat.ULAW, 8000, 8, 1);

    public static final RTPAudioFormat GSM = new RTPAudioFormat(3, AudioFormat.GSM, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final RTPAudioFormat G723 = new RTPAudioFormat(4, AudioFormat.G723, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final RTPAudioFormat DVI4_8K = new RTPAudioFormat(5, AudioFormat.DVI4, 8000, 4, 1);

    public static final RTPAudioFormat DVI4_16K = new RTPAudioFormat(6, AudioFormat.DVI4, 16000, 4, 1);

    public static final RTPAudioFormat LPC = new RTPAudioFormat(7, AudioFormat.LPC, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final RTPAudioFormat PCMA = new RTPAudioFormat(8, AudioFormat.ALAW, 8000, 8, 1);

    public static final RTPAudioFormat G722 = new RTPAudioFormat(9, AudioFormat.G722, 8000, 8, 1);

    public static final RTPAudioFormat L16_STEREO = new RTPAudioFormat(10, AudioFormat.LINEAR, 44100, 16, 2, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);

    public static final RTPAudioFormat L16_MONO = new RTPAudioFormat(11, AudioFormat.LINEAR, 44100, 16, 1, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);

    public static final RTPAudioFormat G729 = new RTPAudioFormat(18, AudioFormat.G729, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final RTPAudioFormat SPEEX = new RTPAudioFormat(97, AudioFormat.SPEEX, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final RTPAudioFormat DTMF = new RTPAudioFormat(101, "telephone-event", 8000, AudioFormat.NOT_SPECIFIED, AudioFormat.NOT_SPECIFIED);

    public static final RTPVideoFormat H261 = new RTPVideoFormat(31, VideoFormat.H261, 90000);

    private static final HashMap<Integer, RTPFormat> audioFormats = new HashMap();

    private static final HashMap<Integer, RTPFormat> videoFormats = new HashMap();

    static {
        audioFormats.put(PCMU.getPayloadType(), PCMU);
        audioFormats.put(GSM.getPayloadType(), GSM);
        audioFormats.put(G723.getPayloadType(), G723);
        audioFormats.put(DVI4_8K.getPayloadType(), DVI4_8K);
        audioFormats.put(DVI4_16K.getPayloadType(), DVI4_16K);
        audioFormats.put(LPC.getPayloadType(), LPC);
        audioFormats.put(PCMA.getPayloadType(), PCMA);
        audioFormats.put(G722.getPayloadType(), G722);
        audioFormats.put(SPEEX.getPayloadType(), SPEEX);
        audioFormats.put(G729.getPayloadType(), G729);
        audioFormats.put(L16_STEREO.getPayloadType(), L16_STEREO);
        audioFormats.put(L16_MONO.getPayloadType(), L16_MONO);
    }

    static {
        videoFormats.put(H261.getPayloadType(), H261);
    }

    /**
     * Gets the audio format related to payload type.
     * 
     * @param pt the payload type
     * @return AudioFormat object.
     */
    public static RTPAudioFormat getAudioFormat(int pt) {
        return (RTPAudioFormat) audioFormats.get(pt);
    }

    /**
     * Gets the video format related to payload type.
     * 
     * @param pt the payload type
     * @return VideoFormat object.
     */
    public static RTPVideoFormat getVideoFormat(int pt) {
        return (RTPVideoFormat) videoFormats.get(pt);
    }
}
