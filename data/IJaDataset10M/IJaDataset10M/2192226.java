package org.npsnet.v.services.math;

import javax.media.j3d.*;

/**
 * An interface for transform extrapolators.
 *
 * @author Andrzej Kapolka
 */
public interface TransformExtrapolator {

    /**
     * Pushes a transform onto the extrapolation queue.
     *
     * @param transform the transform to push
     * @param transformTime the time at which the transform was valid
     */
    public void pushTransform(Transform3D transform, long transformTime);

    /**
     * Gets the extrapolated transform at the specified time.
     *
     * @param time the time to extrapolate
     * @param transform a <code>Transform3D</code> to hold the result
     */
    public void getTransform(long time, Transform3D transform);
}
