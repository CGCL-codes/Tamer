package org.jsresources.apps.jam.audio;

import java.util.List;
import javax.sound.sampled.AudioFormat;
import org.jsresources.apps.jam.Debug;

/**
 */
public class PlaybackAudioFormatComboBoxModel extends AudioFormatComboBoxModel {

    protected AudioFormat[] getAudioFormats() {
        if (Debug.getTraceAudioFormatComboBoxModel()) {
            Debug.out("PlaybackAudioFormatComboBoxModel.getSyncModes(): begin");
        }
        List<AudioFormat> formatsList = SupportedFormats.getInstance().getSupportedPlaybackFormats();
        AudioFormat[] formats = formatsList.toArray(EMPTY_AUDIO_FORMAT_ARRAY);
        if (Debug.getTraceAudioFormatComboBoxModel()) {
            Debug.out("PlaybackAudioFormatComboBoxModel.getSyncModes(): end");
        }
        return formats;
    }

    protected AudioFormat getAudioFormat() {
        if (Debug.getTraceAudioFormatComboBoxModel()) {
            Debug.out("PlaybackAudioFormatComboBoxModel.getSyncMode(): begin");
        }
        AudioFormat format = getAudioQualityModel().getPlaybackFormat();
        if (Debug.getTraceAudioFormatComboBoxModel()) {
            Debug.out("PlaybackAudioFormatComboBoxModel.getSyncMode(): end");
        }
        return format;
    }

    protected void setAudioFormat(AudioFormat audioFormat) {
        if (Debug.getTraceAudioFormatComboBoxModel()) {
            Debug.out("PlaybackAudioFormatComboBoxModel.setSyncMode(): begin");
        }
        getAudioQualityModel().setPlaybackFormat(audioFormat);
        if (Debug.getTraceAudioFormatComboBoxModel()) {
            Debug.out("PlaybackAudioFormatComboBoxModel.setSyncMode(): end");
        }
    }
}
