package com.frinika.softsynth.string;

/**
 *
 * @author pjl
 */
public interface BowString extends Source {

    public int getMaxLengthInSamples();

    public float getPeriod();

    public void getStringState(float[] buff);

    public void pluckAt();

    public void setBowAttenuation(float f);

    public void setBowPosition(float f);

    public void setBowPressure(float f);

    public void setBowVelocity(float f);

    public void setLength(int i);
}
