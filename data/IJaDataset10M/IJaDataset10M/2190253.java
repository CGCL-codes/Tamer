package org.mobicents.mscontrol.sdp;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * Defines relation between audio/video format and RTP payload number as
 * specified by Audio/Video Profile spec.
 * 
 * @author Oleg Kulikov
 */
public class AVProfile implements Cloneable {

    public static final String AUDIO = "audio";

    public static final String VIDEO = "video";

    public static final AudioFormat AMR = new AudioFormat("AMR", 8000, 8, 1);

    public static final AudioFormat PCMU = new AudioFormat(AudioFormat.ULAW, 8000, 8, 1);

    public static final AudioFormat PCMA = new AudioFormat(AudioFormat.ALAW, 8000, 8, 1);

    public static final AudioFormat SPEEX = new AudioFormat(AudioFormat.SPEEX, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final AudioFormat G729 = new AudioFormat(AudioFormat.G729, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final AudioFormat GSM = new AudioFormat(AudioFormat.GSM, 8000, AudioFormat.NOT_SPECIFIED, 1);

    public static final AudioFormat MPEG4_GENERIC = new AudioFormat("mpeg4-generic", 8000, AudioFormat.NOT_SPECIFIED, 2);

    public static final AudioFormat L16_STEREO = new AudioFormat(AudioFormat.LINEAR, 44100, 16, 2, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);

    public static final AudioFormat L16_MONO = new AudioFormat(AudioFormat.LINEAR, 44100, 16, 1, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);

    public static final AudioFormat DTMF = new AudioFormat("telephone-event", 8000, AudioFormat.NOT_SPECIFIED, AudioFormat.NOT_SPECIFIED);

    public static final VideoFormat H261 = new VideoFormat(VideoFormat.H261, 25, 90000);

    public static final VideoFormat MP4V = new VideoFormat("MP4V-ES", 25, 90000);

    public static final VideoFormat H263 = new VideoFormat("H263", 25, 90000);

    private final HashMap<Integer, AudioFormat> audio = new HashMap();

    private final HashMap<Integer, VideoFormat> video = new HashMap();

    public AVProfile() {
        audio.put(0, PCMU);
        audio.put(8, PCMA);
        audio.put(99, AMR);
        audio.put(2, G729);
        audio.put(3, GSM);
        audio.put(16, L16_STEREO);
        audio.put(17, L16_MONO);
        audio.put(101, DTMF);
        video.put(45, H261);
        video.put(34, H263);
    }

    public void setProfile(Hashtable<Integer, Format> profile) {
        audio.clear();
        video.clear();
        Set<Integer> keys = profile.keySet();
        for (Integer key : keys) {
            Format f = profile.get(key);
            if (f instanceof AudioFormat) {
                audio.put(key, (AudioFormat) f);
            } else if (f instanceof VideoFormat) {
                video.put(key, (VideoFormat) f);
            }
        }
    }

    public Hashtable<Integer, Format> getProfile() {
        Hashtable<Integer, Format> profile = new Hashtable();
        profile.putAll(audio);
        profile.putAll(video);
        return profile;
    }

    public HashMap<Integer, AudioFormat> getAudioFormats() {
        return audio;
    }

    public HashMap<Integer, VideoFormat> getVideoFormats() {
        return video;
    }

    /**
     * Gets the audio format related to payload type.
     * 
     * @param pt the payload type
     * @return AudioFormat object.
     */
    public AudioFormat getAudioFormat(int pt) {
        return audio.get(pt);
    }

    /**
     * Gets the video format related to payload type.
     * 
     * @param pt the payload type
     * @return VideoFormat object.
     */
    public VideoFormat getVideoFormat(int pt) {
        return video.get(pt);
    }

    @Override
    public AVProfile clone() {
        AVProfile profile = new AVProfile();
        profile.setProfile(this.getProfile());
        return profile;
    }
}
