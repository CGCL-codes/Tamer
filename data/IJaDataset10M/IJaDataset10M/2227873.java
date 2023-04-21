package org.jscience.biology.lsystems.growing;

import javax.media.j3d.TransformGroup;

/**
 * AnimationGroup is made as collection of scaling and translation
 * transformgroups which belong together. Also the start and end times of the
 * animation of this group are stored in here.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class AnimationGroup implements Comparable {

    /** The start time of animation of this objects. */
    public float m_fStartTime;

    /** The time when to stop animation */
    public float m_fEndTime;

    /** The age of the objects. This is the end time minus the start time. */
    public float m_fAge;

    /** The transformgroup which represents the scaling of a shape. */
    public TransformGroup m_tgScale;

    /**
     * The transformgroup where to make the translation of the subtree following
     * the scaled element.
     */
    public TransformGroup m_tgTrans;

    /**
     * Constructor for leafs. Leafs only have a scale transformgroup so this
     * constructor can be used. Also start and end time of animation are needed.
     *
     * @param fStartTime
     *            The time when to start the animation.
     * @param fEndTime
     *            The time when to stop the animation.
     * @param tgScale
     *            The transformgroup of the shape which has to be scaled.
     */
    public AnimationGroup(float fStartTime, float fEndTime, TransformGroup tgScale) {
        this(fStartTime, fEndTime, tgScale, null);
    }

    /**
     * Constructor for branches. Here you need the transformgroup for the
     * translation as well.
     *
     * @param fStartTime
     *            The time when to start the animation.
     * @param fEndTime
     *            The time when to stop the animation.
     * @param tgScale
     *            The transformgroup of the shape which has to be scaled.
     * @param tgTrans
     *            The transformgroup for the translation of the subtree.
     */
    public AnimationGroup(float fStartTime, float fEndTime, TransformGroup tgScale, TransformGroup tgTrans) {
        m_fStartTime = fStartTime;
        m_fEndTime = fEndTime;
        m_fAge = fEndTime - fStartTime;
        m_tgScale = tgScale;
        m_tgTrans = tgTrans;
    }

    /**
     * Override this method for the comparable interface which is used to sort
     * the list.
     *
     * @param obj
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Object obj) {
        if (obj instanceof AnimationGroup == false) {
            return 0;
        }
        AnimationGroup ag = (AnimationGroup) obj;
        if (m_fStartTime < ag.m_fStartTime) {
            return -1;
        }
        if (m_fStartTime > ag.m_fStartTime) {
            return 1;
        }
        return 0;
    }

    /**
     * Makes a string representation of this object.
     *
     * @return The string representing this object.
     */
    public String toString() {
        return "StartTime: " + m_fStartTime + " EndTime: " + m_fEndTime;
    }
}
