package org.xith3d.behaviors.impl;

import org.xith3d.behaviors.TransformationDirectives;
import org.xith3d.utility.interpolate.AngleInterpolater;

/**
 * This class is useful to automatically rotate a branch in your scenegraph.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class RotatableGroup extends AnimatableGroup {

    /**
     * @return a reference to this group's AngleInterpolater object
     * 
     * @param axis the axis to get the AngleInterpolater for
     */
    protected AngleInterpolater getAngleInterpolater(TransformationDirectives.Axes axis) {
        return (((GroupRotator) groupAnimator).getAngleInterpolater(axis));
    }

    /**
     * @return the current rotation value [0; 2*pi] of the specified axis
     * 
     * @param axis the axis to get the AngleInterpolater for
     * @param gameTime the time to get the value at
     */
    protected float getRotationValue(TransformationDirectives.Axes axis, long gameTime) {
        return (((GroupRotator) groupAnimator).getRotationValue(axis, gameTime));
    }

    /**
     * Creates a new RotatableGroup with the given TransformationDirectives in use.
     * 
     * @param rotDirecs the new TransformationDirectives
     */
    public RotatableGroup(TransformationDirectives rotDirecs) {
        super(new GroupRotator(rotDirecs));
        groupAnimator.setTransformNode(this);
    }

    /**
     * Creates a new RotatableGroup with default TransformationDirectives in use.
     */
    public RotatableGroup() {
        this(new TransformationDirectives());
    }
}
