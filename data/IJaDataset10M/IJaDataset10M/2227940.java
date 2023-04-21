package org.jrman.geom;

public interface BoundingVolume {

    BoundingVolume transform(Transform transform);

    Plane.Side whichSideOf(Plane plane);

    Bounds3f getBoundingBox();

    Bounds2f toBounds2f();

    Bounds2f toBounds2f(float margin);

    float getMinZ();

    BoundingVolume enlarge(float margin);
}
