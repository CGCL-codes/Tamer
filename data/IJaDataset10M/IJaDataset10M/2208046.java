package org.mobicents.javax.media.mscontrol.mediagroup.signals.buffer;

/**
 * Triggered by EventBuffer when one of the following conditions happens:
 * - event sequence matches to one of the specified pattern;
 * - the number of detected events reaches specified value.
 * 
 * @author kulikov
 */
public interface BufferListener {

    public void patternMatches(int index, String s);

    public void countMatches(String s);
}
