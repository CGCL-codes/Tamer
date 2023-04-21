package edu.washington.assist.audio;

/**
 * An implementor of this interface can produce an abitrary number
 * of independent copies of the same audio data in the form of an
 * AudioStream.
 * 
 * @author anthop
 */
public interface AudioStreamFactory {

    /**
	 * Returns a copy of the AudioStream.
	 */
    public AudioStream getCopyOfStream();
}
