package edu.mit.csail.sls.wami.applet.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

/**
 * Interface to something that can provide an audio input stream.
 * 
 */
public interface Recorder {

    /**
         * Listener for events on the recorder
         * 
         */
    public interface Listener {

        /**
         * Called when recording starts
         * 
         */
        void recordingHasStarted();

        /**
         * Called when recording finishes
         * 
         */
        void recordingHasEnded();
    }

    /**
         * Add a listener
         * 
         * @param listener
         *                The listener
         * 
         */
    public void addListener(Listener listener);

    /**
         * Remove a listener
         * 
         * @param listener
         *                The listener
         * 
         */
    public void removeListener(Listener listener);

    /**
         * Get an AudioInputStream for the specified format (or something
         * close).
         * 
         * @param desiredAudioFormat
         *                The audio format desired
         * 
         * @return An AudioInputStream.
         * 
         */
    public AudioInputStream getAudioInputStream(AudioFormat desiredAudioFormat) throws LineUnavailableException;

    /**
         * Returns true if recording is in progress
         * 
         */
    public boolean isRecording();

    /**
         * Stops recording if it is in progress.
         * 
         */
    public void closeLine();
}
