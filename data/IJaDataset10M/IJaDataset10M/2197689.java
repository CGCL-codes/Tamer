package com.myapp.videotools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.myapp.util.format.TimeFormatUtil;

/**
 * data class to back the information of following ffmpeg call:
 * 
 * <pre>
 * ffmpeg -i /data/stuff/vidfile.flv 2>&1 | grep -E "Input|Duration|Stream"
 * Input #0, flv, from '/data/stuff/vidfile.flv':
 *   Duration: 00:03:50.2, start: 0.000000, bitrate: 32 kb/s
 *   Stream #0.0: Video: vp6f, yuv420p, 640x480, 30.00 fps(r)
 *   Stream #0.1: Audio: mp3, 22050 Hz, stereo, 32 kb/s
 * </pre>
 */
public final class VideoFile {

    private static final Logger log = LoggerFactory.getLogger(VideoFile.class);

    private final File file;

    private boolean parsed = false;

    private String fileType = null;

    private String videoCodec = null;

    private String audioCodec = null;

    private String audioChannelType = null;

    private int audioSampleRate = -1;

    private int audioBytesPerSecond = -1;

    private int totalBytesPerSecond = -1;

    private int videoBytesPerSecond = -1;

    private double videoFramesPerSecond = -1;

    private int videoWidth = -1;

    private int videoHeight = -1;

    private long lengthMillis = -1;

    private long videoStartOffsetMillis;

    private AppStatistics statistics = AppStatistics.getInstance();

    public VideoFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        this.file = file;
    }

    public VideoFile(String path) {
        this(new File(path));
    }

    public void parse(IVideoFileParser parser) throws IOException {
        final long start = System.currentTimeMillis();
        try {
            parse0(parser);
            parsed = true;
            statistics.incrementFilesParsed();
        } catch (IOException e) {
            statistics.incrementParseFails();
            throw e;
        } catch (RuntimeException e) {
            statistics.incrementParseFails();
            throw e;
        } finally {
            long timeNeeded = System.currentTimeMillis() - start;
            statistics.addTimeSpentWithParsingMetadata(timeNeeded);
        }
    }

    private void parse0(IVideoFileParser parser) throws IOException {
        if (parser == null) throw new NullPointerException();
        if (!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
        if (file.isDirectory()) throw new IllegalArgumentException(file.getAbsolutePath());
        parser.parse(this);
        String path = file.getAbsolutePath();
        if (fileType == null) log.warn("      fileType could not be determined            : {}", path);
        if (videoCodec == null) log.warn("      videoCodec could not be determined          : {}", path);
        if (audioCodec == null) log.trace("      audioCodec could not be determined          : {}", path);
        if (audioChannelType == null) log.trace("      audioChannelType could not be determined    : {}", path);
        if (audioSampleRate <= 0) log.trace("      audioSampleRate could not be determined     : {}", path);
        if (totalBytesPerSecond <= 0) log.trace("      bytesPerSecond could not be determined      : {}", path);
        if (videoFramesPerSecond <= 0) log.trace("      framesPerSecond could not be determined     : {}", path);
        if (videoWidth <= 0) log.warn("      width could not be determined               : {}", path);
        if (videoHeight <= 0) log.warn("      height could not be determined              : {}", path);
        if (audioBytesPerSecond <= 0) log.trace("      audioBytesPerSecond could not be determined : {}", path);
        if (lengthMillis == -1) throw new RuntimeException("mandatory attribute missing: 'length' file: " + path);
    }

    public boolean isParsed() {
        return parsed;
    }

    public File getFile() {
        return file;
    }

    public String getAudioChannelType() {
        return audioChannelType;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public int getAudioSampleRate() {
        return audioSampleRate;
    }

    public int getTotalBytesPerSecond() {
        return totalBytesPerSecond;
    }

    public String getFileType() {
        return fileType;
    }

    public double getVideoFramesPerSecond() {
        return videoFramesPerSecond;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public long getLengthMillis() {
        return lengthMillis;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public double getLengthSeconds() {
        double seconds = lengthMillis;
        seconds /= 1000.0d;
        return seconds;
    }

    public String getFormattedLength() {
        return TimeFormatUtil.formatTimeTo2Digits(getLengthSeconds());
    }

    public String getName() {
        if (file == null) {
            return null;
        }
        return file.getName();
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public void setAudioChannelType(String audioChannelType) {
        this.audioChannelType = audioChannelType;
    }

    public void setAudioSampleRate(int audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public void setTotalBytesPerSecond(int bytesPerSecond) {
        this.totalBytesPerSecond = bytesPerSecond;
    }

    public void setVideoFramesPerSecond(double framesPerSecond) {
        this.videoFramesPerSecond = framesPerSecond;
    }

    public void setVideoWidth(int width) {
        this.videoWidth = width;
    }

    public void setVideoHeight(int height) {
        this.videoHeight = height;
    }

    public void setLengthMillis(long lengthMillis) {
        this.lengthMillis = lengthMillis;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getName());
        builder.append(" [type:");
        builder.append(fileType);
        builder.append(", video: ");
        builder.append(videoCodec);
        builder.append(", audio: ");
        builder.append(audioCodec);
        builder.append(", duration: ");
        builder.append(getLengthSeconds());
        builder.append("]");
        return builder.toString();
    }

    public void setVideoStartOffsetMillis(long offsetMillis) {
        this.videoStartOffsetMillis = offsetMillis;
    }

    public long getVideoStartOffsetMillis() {
        return videoStartOffsetMillis;
    }

    public void setVideoBytesPerSecond(int videoBytesPerSecond) {
        this.videoBytesPerSecond = videoBytesPerSecond;
    }

    public int getVideoBytesPerSecond() {
        return videoBytesPerSecond;
    }

    public void setAudioBytesPerSecond(int audioBytesPerSecond) {
        this.audioBytesPerSecond = audioBytesPerSecond;
    }

    public int getAudioBytesPerSecond() {
        return audioBytesPerSecond;
    }
}
