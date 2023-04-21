package com.pcmsolutions.device.EMU.E4.remote;

import java.io.Serializable;

/**
 * User: paulmeehan
 * Date: 25-Aug-2004
 * Time: 11:04:29
 */
public interface SampleHeader extends Serializable {

    public byte getBitsPerWord();

    public byte getNumChannels();

    public byte getLoopControl();

    public int getPeriodInNS();

    public int getLengthInSampleFrames();

    public int getLoopStart();

    public int getLoopEnd();
}
