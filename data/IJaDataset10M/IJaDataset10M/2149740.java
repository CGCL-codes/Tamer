package org.npsnet.v.services.math;

import org.npsnet.v.kernel.ServiceProvider;

/**
 * The extrapolator factory service.
 *
 * @author Andrzej Kapolka
 */
public interface ExtrapolatorFactory extends ServiceProvider {

    /**
     * Creates and returns a new <code>TransformExtrapolator</code>.
     *
     * @return the newly created <code>TransformExtrapolator</code>
     */
    public TransformExtrapolator newTransformExtrapolator();

    /**
     * Creates and returns a new <code>Vector3dExtrapolator</code>.
     *
     * @return the newly created <code>Vector3dExtrapolator</code>
     */
    public Vector3dExtrapolator newVector3dExtrapolator();

    /**
     * Creates and returns a new <code>Quat4dExtrapolator</code>.
     *
     * @return the newly created <code>Quat4dExtrapolator</code>
     */
    public Quat4dExtrapolator newQuat4dExtrapolator();

    /**
     * Creates and returns a new <code>DoubleExtrapolator</code>.
     *
     * @return the newly created <code>DoubleExtrapolator</code>
     */
    public DoubleExtrapolator newDoubleExtrapolator();
}
