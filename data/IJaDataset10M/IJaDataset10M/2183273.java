package org.xith3d.schedops.movement;

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
     * @param gameMicros the time to get the value at
     */
    protected float getRotationValue(TransformationDirectives.Axes axis, long gameMicros) {
        return (((GroupRotator) groupAnimator).getRotationValue(axis, gameMicros));
    }

    /**
     * Creates a new RotatableGroup with the given TransformationDirectives in use.
     * 
     * @param rotDirecs the new TransformationDirectives
     */
    public RotatableGroup(TransformationDirectives rotDirecs) {
        super(new GroupRotator(rotDirecs));
    }

    /**
     * Creates a new RotatableGroup with default TransformationDirectives in use.
     */
    public RotatableGroup() {
        this(new TransformationDirectives());
    }
}
