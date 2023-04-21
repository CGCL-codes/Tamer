package bitWave.examples.visualization.util;

import bitWave.linAlg.Quaternion;
import bitWave.linAlg.Vec4;
import bitWave.visualization.Feature;

/**
 * Example implementation for a feature.
 * @author fw
 *
 */
public class Body implements Feature {

    protected final Vec4 m_position;

    protected Quaternion m_attitude;

    /**
     * Creates a new body at the given position with the given attitude.
     * @param position The body position. The vector is cloned to ensure that the position
     * is not accidentally shared with another body.
     * @param attitude The attitude of the body. The quaternion is cloned to ensure that
     * the attitude is not accidentally shared with another body.
     * @throws CloneNotSupportedException if one of the arguments passed to the constructor
     * failed to produce a clone.
     */
    public Body(final Vec4 position, final Quaternion attitude) throws CloneNotSupportedException {
        this.m_position = position.clone();
        this.m_attitude = attitude.clone();
    }

    public Vec4 getPosition() {
        return this.m_position;
    }

    public Quaternion getAttitude() {
        return this.m_attitude;
    }
}
