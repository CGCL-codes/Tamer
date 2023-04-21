package org.skycastle.shape;

import com.jme.math.Vector3f;
import java.io.Serializable;

/**
 * Specifies the x,y, and z location of any ring and spoke of a bipolar shape.
 *
 * @author Hans Haggstrom
 */
public interface Profile extends Serializable {

    /**
     * @param relativeSpokePosition the relative spoke to get the position for, 0..1.
     * @param relativeRingPosition the relative ring to get the position for, 0..1.
     * @return the position dictated by the profile for the specified spoke and ring.
     * The position will still be scaled by the radius and length of the final bipolar shape.
     */
    Vector3f getPosition(float relativeSpokePosition, float relativeRingPosition, Vector3f positionOut);
}
