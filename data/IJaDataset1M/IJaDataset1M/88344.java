package org.dvb.media;

/**
 * Allow applications to register and unregister their interest in events
 * related to the availability and presentation of subtitles.
 */
public interface SubtitlingEventControl extends org.davic.media.SubtitlingLanguageControl {

    /**
	 * Add a listener for subtitle events
	 * @param l the listener to report the events to
	 */
    public void addSubtitleListener(SubtitleListener l);

    /**
	 * Remove a listener for subtitle events
	 * @param l the listener to remove
	 */
    public void removeSubtitleListener(SubtitleListener l);
}
