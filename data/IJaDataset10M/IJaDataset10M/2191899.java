package org.jnewton;

import java.nio.FloatBuffer;
import org.jnewton.util.BufferUtils;

/**
 * 	Class representing a hinge slider update descriptor 
 * in Newton
 * 
 * @author Justin Schwartz
 */
public final class NewtonHingeSliderUpdateDesc {

    final FloatBuffer buff = BufferUtils.createFloatBuffer(4);

    public float m_accel;

    public float m_minFriction;

    public float m_maxFriction;

    public float m_timestep;

    public NewtonHingeSliderUpdateDesc() {
    }

    final FloatBuffer asFloatBuffer() {
        buff.clear();
        buff.put(m_accel);
        buff.put(m_minFriction);
        buff.put(m_maxFriction);
        buff.put(m_timestep);
        buff.rewind();
        return buff;
    }

    final void fromFloatBuffer(FloatBuffer b) {
        m_accel = b.get();
        m_minFriction = b.get();
        m_maxFriction = b.get();
        m_timestep = b.get();
        b.rewind();
    }
}
