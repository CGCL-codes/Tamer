package org.or5e.core.transcode;

import java.io.File;
import java.io.Serializable;

public class VideoInfo implements Serializable {

    private static final long serialVersionUID = 1451854587085766168L;

    public enum AUDIO_MODE {

        MONO, STREO
    }

    ;

    private File videoSrc = null;

    private String videoName = null;

    private String videoDuration = null;

    private String videoType = null;

    private Integer videoWidth = null;

    private Integer videoHeight = null;

    private Float frameRate = null;

    private Integer bitRate = null;

    private String audioType = null;

    private Integer audioFrequency = null;

    private AUDIO_MODE audioMode = null;

    public final File getVideoSrc() {
        return videoSrc;
    }

    public final void setVideoSrc(File videoSrc) {
        this.videoSrc = videoSrc;
    }

    public final String getVideoName() {
        return videoName;
    }

    public final void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public final String getVideoDuration() {
        return videoDuration;
    }

    public final void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public final String getVideoType() {
        return videoType;
    }

    public final void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public final Integer getVideoWidth() {
        return videoWidth;
    }

    public final void setVideoWidth(Integer videoWidth) {
        this.videoWidth = videoWidth;
    }

    public final Integer getVideoHeight() {
        return videoHeight;
    }

    public final void setVideoHeight(Integer videoHeight) {
        this.videoHeight = videoHeight;
    }

    public final Float getFrameRate() {
        return frameRate;
    }

    public final void setFrameRate(Float frameRate) {
        this.frameRate = frameRate;
    }

    public final Integer getBitRate() {
        return bitRate;
    }

    public final void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public final String getAudioType() {
        return audioType;
    }

    public final void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public final Integer getAudioFrequency() {
        return audioFrequency;
    }

    public final void setAudioFrequency(Integer audioFrequency) {
        this.audioFrequency = audioFrequency;
    }

    public final AUDIO_MODE getAudioMode() {
        return audioMode;
    }

    public final void setAudioMode(AUDIO_MODE audioMode) {
        this.audioMode = audioMode;
    }

    @Override
    public String toString() {
        return "---------------------------------" + "\nVideo Name: " + this.videoName + "\nVideo Duration: " + videoDuration + "\nVideo type: " + videoType + "\nVideo Width: " + videoWidth + "\nVideo Height: " + videoHeight + "\nFrame Rate: " + frameRate + "\nBit Rate: " + bitRate + "\nAudio type: " + audioType + "\nAudio Frequence: " + audioFrequency + "\n---------------------------------";
    }
}
