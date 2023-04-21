package com.semp.gu.codec.video;

import java.io.File;
import java.text.MessageFormat;

public class FfmpegUtil {

    private static final MessageFormat OPTS_INPUT_FILE = new MessageFormat(" -i \"{0}\"");

    private static final MessageFormat OPTS_OUTPUT_FILE = new MessageFormat(" -y \"{0}\"");

    private static final MessageFormat OPTS_OUTPUT_SIZE = new MessageFormat(" -s {0}x{1}");

    private static final MessageFormat OPTS_OUTPUT_FORMAT = new MessageFormat(" -f {0}");

    private static final MessageFormat OPTS_AUDIO_RATE = new MessageFormat(" -ar {0}");

    private static final MessageFormat OPTS_AUDIO_BITRATE = new MessageFormat(" -ab {0}");

    private static final MessageFormat OPTS_AUDIO_CODEC = new MessageFormat(" -acodec {0}");

    private static final MessageFormat OPTS_VIDEO_FPS = new MessageFormat(" -r {0}");

    private static final MessageFormat OPTS_VIDEO_BITRATE = new MessageFormat(" -b {0}k");

    public static final String generateVideoCommand(VideoCodecConfiguration configuration, File inputFile, File outputFile) {
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append(configuration.getFfmpegPath());
        commandBuffer.append(OPTS_INPUT_FILE.format(new String[] { inputFile.getAbsolutePath() }));
        if (configuration.getVideoOutputHeight() > 0 && configuration.getVideoOutputWidth() > 0) {
            commandBuffer.append(OPTS_OUTPUT_SIZE.format(new Integer[] { configuration.getVideoOutputWidth(), configuration.getVideoOutputHeight() }));
        }
        if (configuration.getVideoAudioRate() > 0) {
            commandBuffer.append(OPTS_AUDIO_RATE.format(new String[] { String.valueOf(configuration.getVideoAudioRate()) }));
        }
        if (configuration.getVideoBitRate() > 0) {
            commandBuffer.append(OPTS_VIDEO_BITRATE.format(new String[] { String.valueOf(configuration.getVideoBitRate()) }));
        }
        if (configuration.getVideoFps() > 0) {
            commandBuffer.append(OPTS_VIDEO_FPS.format(new String[] { String.valueOf(configuration.getVideoFps()) }));
        }
        if (configuration.getVideoAudioCodec() != null) {
            commandBuffer.append(OPTS_AUDIO_CODEC.format(new String[] { configuration.getVideoAudioCodec() }));
        }
        if (configuration.getVideoAudioBitRate() > 0) {
            commandBuffer.append(OPTS_AUDIO_BITRATE.format(new String[] { String.valueOf(configuration.getVideoAudioBitRate()) }));
        }
        if (configuration.getVideoOutputFormat() != null) {
            commandBuffer.append(OPTS_OUTPUT_FORMAT.format(new String[] { configuration.getVideoOutputFormat() }));
        }
        commandBuffer.append(OPTS_OUTPUT_FILE.format(new String[] { outputFile.getAbsolutePath() }));
        return commandBuffer.toString();
    }

    public static final String generateImageCommand(VideoCodecConfiguration configuration, File inputFile, File outputFile) {
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append(configuration.getFfmpegPath());
        commandBuffer.append(OPTS_INPUT_FILE.format(new String[] { inputFile.getAbsolutePath() }));
        if (configuration.getVideoOutputHeight() > 0 && configuration.getVideoOutputWidth() > 0) {
            commandBuffer.append(OPTS_OUTPUT_SIZE.format(new Integer[] { configuration.getImageOutputWidth(), configuration.getImageOutputHeight() }));
        }
        if (configuration.getVideoOutputFormat() != null) {
            commandBuffer.append(OPTS_OUTPUT_FORMAT.format(new String[] { configuration.getImageOutputFormat() }));
        }
        commandBuffer.append(" -an -r 1 -vframes 1");
        commandBuffer.append(OPTS_OUTPUT_FILE.format(new String[] { outputFile.getAbsolutePath() }));
        return commandBuffer.toString();
    }
}
