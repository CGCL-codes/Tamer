package imi.scene.animation;

import imi.scene.animation.channel.PJointChannel;
import imi.scene.PJoint;
import imi.scene.animation.channel.ChannelOptimizer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Logger;
import javolution.util.FastTable;

/**
 * Defines an animation cycle within an animation group
 * 
 * @author Lou Hayt
 * @author Ronald Dahlgren
 */
public class AnimationCycle implements Serializable {

    /** Serialization version number **/
    private static final long serialVersionUID = 1l;

    /** Logger ref **/
    private static final Logger logger = Logger.getLogger(AnimationCycle.class.getName());

    /** The name of the cycle **/
    private String m_name = "Untitled Cycle";

    /** For every joint we have a channel that contains all of the frames for that joint during the animation duration */
    private final FastTable<PJointChannel> m_JointChannels = new FastTable<PJointChannel>();

    /** Cached duration of this cycle **/
    private transient float fDuration = 0.0f;

    /**
     * Empty Constructor
     */
    public AnimationCycle() {
    }

    /**
     * Create a new animation cycle with the given name.
     * @param name
     */
    public AnimationCycle(String name) {
        if (name != null) m_name = name;
    }

    /**
     * Calculate the pose for the provided animated entity using the provided state
     * @param state
     * @param animated
     */
    void calculateFrame(AnimationState state, Animated animated) {
        int jointIndex = 0;
        for (PJointChannel jointChannel : m_JointChannels) {
            PJoint joint = animated.getJoint(jointChannel.getTargetJointName());
            if (joint != null) {
                state.getCursor().setJointIndex(jointIndex);
                jointChannel.setTargetGeometry(animated.getTargetGeometry());
                jointChannel.calculateFrame(joint, state);
                jointIndex++;
            } else logger.warning("Unable to locate joint " + jointChannel.getTargetJointName());
        }
    }

    /**
     * Get the joint channel associated with the specified joint.
     * @param jointName The target joint
     * @return The channel, or null if none was found.
     */
    public PJointChannel getChannel(String jointName) {
        for (PJointChannel channel : m_JointChannels) if (channel.getTargetJointName().equals(jointName)) return channel;
        return null;
    }

    /**
     * Calculate the pose for the transition cycle of the provided animated entity
     * with the provided state. This is then blended with the joints' current
     * transform. If the joints were already in the current cycle's pose, then
     * calling this method will perform the transition blending.
     * @param state
     * @param animated
     */
    void applyTransitionPose(AnimationState state, Animated animated) {
        float lerpCoefficient = state.getTimeInTransition() / state.getTransitionDuration();
        int jointIndex = 0;
        for (PJointChannel jointChannel : m_JointChannels) {
            PJoint joint = animated.getJoint(jointChannel.getTargetJointName());
            if (joint != null) {
                state.getCursor().setCurrentTransitionJointIndex(jointIndex);
                jointChannel.applyTransitionPose(joint, state, lerpCoefficient);
                jointIndex++;
            } else logger.warning("Unable to locate joint " + jointChannel.getTargetJointName());
        }
    }

    /**
     * @return the name of this animation cycle
     */
    public String getName() {
        return m_name;
    }

    /**
     * @param name - the name of this animation cycle
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Retrieve the duration of this animation cycle
     * @return
     */
    public float getDuration() {
        return fDuration;
    }

    /**
     * Recalculate the duration. This should be called after modifying the
     * joint channels.
     * @return
     */
    public float recalculateDuration() {
        fDuration = 0.0f;
        for (PJointChannel channel : m_JointChannels) fDuration = Math.max(fDuration, channel.getEndTime());
        return fDuration;
    }

    /**
     * Add the provided joint channel to the internal collection.
     * @param newChannel
     */
    public void addJointChannel(PJointChannel newChannel) {
        m_JointChannels.add(newChannel);
    }

    /**
     * This method optimizes all the internal joint channels. The provided
     * float should be a value between 0 and 1, with 1 representing lossless
     * quality and 0 representing maximum optimization and compression.
     * @param quality
     */
    public void optimizeChannels(float quality) {
        ChannelOptimizer optimizer = new ChannelOptimizer();
        if (quality < 0 || quality > 1) return;
        for (int i = 0; i < m_JointChannels.size(); ++i) {
            PJointChannel channel = m_JointChannels.get(i);
            channel = optimizer.optimize(channel, quality);
            m_JointChannels.set(i, channel);
        }
    }

    @Override
    public String toString() {
        return m_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnimationCycle other = (AnimationCycle) obj;
        if ((this.m_name == null) ? (other.m_name != null) : !this.m_name.equals(other.m_name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.m_name != null ? this.m_name.hashCode() : 0);
        return hash;
    }

    float getAverageTimeStep() {
        return m_JointChannels.getFirst().getAverageStepTime();
    }

    /**
     * Serialization helper
     * @param in
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        recalculateDuration();
    }
}
