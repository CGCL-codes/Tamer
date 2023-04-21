package edu.cmu.sphinx.tools.riddler.persist.audio;

import javax.persistence.Entity;

/**
 * document me!
 * @author Garrett Weinberg
 */
@Entity
public class FloatAudioDescriptor extends AudioDescriptor {

    float[] data;

    public FloatAudioDescriptor() {
    }

    public FloatAudioDescriptor(int samplesPerSecond, int channelCount, float[] data, String filename) {
        super(samplesPerSecond, channelCount, filename);
        this.data = data;
    }

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }
}
