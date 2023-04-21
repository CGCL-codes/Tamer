package com.umc.beans;

public class AudioTrack {

    private String language = null;

    /**Z.B. MP3*/
    private String codec = null;

    private String bitrate = null;

    /**Anzahl der Kanäle*/
    private String channels = null;

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }
}
