package uk.org.toot.audio.dynamics;

import uk.org.toot.audio.core.AudioBuffer;

public interface DynamicsVariables {

    public void update(float sampleRate);

    public boolean isBypassed();

    public boolean isRMS();

    public float getThreshold();

    public float getInverseThreshold();

    public float getThresholddB();

    public float getInverseRatio();

    public float getKneedB();

    public float getAttack();

    public int getHold();

    public float getRelease();

    public float getDepth();

    public float getDryGain();

    public float getGain();

    public float getHysteresis();

    public void setDynamicGain(float gain);

    public AudioBuffer getKeyBuffer();
}
