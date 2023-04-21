package net.sf.josceleton.connection.api.test;

import net.sf.josceleton.core.api.entity.joint.Joint;
import net.sf.josceleton.core.api.entity.joint.Skeleton;
import net.sf.josceleton.core.api.entity.location.Coordinate;

/**
 * Value object containing method arguments for {@link MotionStreamListener}.
 * 
 * @since 0.4
 */
public class OnMovedParameter {

    private final Joint movedJoint;

    private final Coordinate updatedCoordinate;

    private final Skeleton skeleton;

    public OnMovedParameter(final Joint movedJoint, final Coordinate updatedCoordinate, final Skeleton skeleton) {
        this.movedJoint = movedJoint;
        this.updatedCoordinate = updatedCoordinate;
        this.skeleton = skeleton;
    }

    public final Joint getMovedJoint() {
        return this.movedJoint;
    }

    public final Coordinate getUpdatedCoordinate() {
        return this.updatedCoordinate;
    }

    public final Skeleton getSkeleton() {
        return this.skeleton;
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if ((other instanceof OnMovedParameter) == false) {
            return false;
        }
        final OnMovedParameter that = (OnMovedParameter) other;
        return this.getMovedJoint().equals(that.getMovedJoint()) && this.getUpdatedCoordinate().equals(that.getUpdatedCoordinate()) && this.getSkeleton().equals(that.getSkeleton());
    }

    @Override
    public final int hashCode() {
        return this.skeleton.hashCode();
    }
}
