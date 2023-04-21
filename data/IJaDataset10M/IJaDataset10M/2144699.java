package edu.mit.csail.sls.wami.applet.sound;

import java.nio.channels.ReadableByteChannel;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import org.spnt.applet.SpntSpeechDetectorListener;

/** Generic speech detector API
 **/
public interface SpeechDetector extends ReadableByteChannelCreator {

    interface AudioSource extends SampleBuffer.DataReader {

        /**
	 * @return The audio format of the data
	 */
        AudioFormat getFormat();
    }

    /** Listens for speech
     *
     * @param audioSource Audio data
     *
     * @param channel Which channel to use
     *
     * @param useSpeechDetector Whether or not the speech detector
     * should be used.
     *
     **/
    void listen(AudioSource audioSource, int channel, boolean useSpeechDetector);

    /** Start reading samples looking for speech, calling listeners.
     *
     * @param useSpeechDetector If set, speech detection is used.
     * Otherwise, all samples are passed through to reader.
     **/
    void enable(boolean useSpeechDetector);

    /** Waits for the detector to finish processing all samples
     *
     **/
    void waitDone();

    /** Stop reading samples
     *
     **/
    void disable();

    /** Returns an AudioInputStream that reads the utterance
     *
     **/
    AudioInputStream createReader();

    /** Returns an AudioInputStream that reads a channel of the utterance
     *
     **/
    AudioInputStream createReader(int channel);

    /** Returns a ReadableByteChannel that reads the utterance
     *
     **/
    ReadableByteChannel createReadableByteChannel();

    /** Returns a ReadableByteChannel that reads a channel of the utterance
     *
     **/
    ReadableByteChannel createReadableByteChannel(int channel);

    /** Returns a ReadableByteChannel[] that reads a channel of the utterance
    *
    **/
    ReadableByteChannel[] createReadableByteChannels(int n, int channel);

    /** Returns the audio format
     *
     **/
    AudioFormat getFormat();

    /** Add a listener for speech events
     *
     * @param listener The listener to add
     **/
    void addListener(SpntSpeechDetectorListener listener);

    /** Remove a listener for speech events
     *
     * @param listener The listener to remove
     **/
    void removeListener(SpntSpeechDetectorListener listener);

    /** Read the peak level and reset to 0
     *
     * @return The peak level
     **/
    double readPeakLevel();

    /**
     * Get the names of parameters which can be set for this detector
     */
    public String[] getParameterNames();

    /**
     * An interface to set parameters particular to different detectors
     * @param parameter
     * @param value
     */
    public void setParameter(String parameter, double value);

    /**
     * Gets the value of a named parameter, as a string
     */
    public double getParameter(String parameter);
}
