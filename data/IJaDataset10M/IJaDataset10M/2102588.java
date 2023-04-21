package org.mil.bean.bo;

public class TrackFactory {

    public static Track newInstance(char type) {
        if ('G' == type) return newGeneralTrack();
        if ('V' == type) return newVideoTrack();
        if ('A' == type) return newAudioTrack();
        if ('T' == type) return newTextTrack();
        return null;
    }

    public static GeneralTrack newGeneralTrack() {
        return new GeneralTrack();
    }

    public static VideoTrack newVideoTrack() {
        return new VideoTrack();
    }

    public static AudioTrack newAudioTrack() {
        return new AudioTrack();
    }

    public static TextTrack newTextTrack() {
        return new TextTrack();
    }
}
