package com.sun.j3d.utils.universe;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * A convenience class that effectively creates a series of TransformGroup
 * nodes connected one to another hierarchically. For most applications,
 * creating a MultiTransformGroup containing one transform will suffice.
 * More sophisticated applications that use a complex portal/head tracking
 * viewing system may find that more transforms are needed.
 * <P>
 * When more than one transform is needed, transform[0] is considered the
 * "top most" transform with repsect to the scene graph, (attached to the
 * ViewingPlatform node) and transform[numTransforms - 1] is the "bottom
 * most" transform (the ViewPlatorm object is attached to this transform).
 */
public class MultiTransformGroup {

    TransformGroup[] transforms;

    /**
     * Creates a MultiTransformGroup node that contains a single transform.
     * This is effectively equivalent to creating a single TransformGroup
     * node.
     */
    public MultiTransformGroup() {
        this(1);
    }

    /**
     * Creates a MultiTransformGroup node that contains the specified
     * number of transforms.
     * <P>
     * When more than one transform is needed, transform[0] is considered the
     * "top most" transform with repsect to the scene graph, (attached to the
     * ViewingPlatform node) and transform[numTransforms - 1] is the "bottom
     * most" transform (the ViewPlatorm object is attached to this transform).
     *
     * @param numTransforms The number of transforms for this node to
     *  contain. If this number is less than one, one is assumed.
     */
    public MultiTransformGroup(int numTransforms) {
        if (numTransforms < 1) numTransforms = 1;
        transforms = new TransformGroup[numTransforms];
        transforms[0] = new TransformGroup();
        transforms[0].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transforms[0].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transforms[0].setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        transforms[0].setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        for (int i = 1; i < numTransforms; i++) {
            transforms[i] = new TransformGroup();
            transforms[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            transforms[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            transforms[i].setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            transforms[i].setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            transforms[i - 1].addChild(transforms[i]);
        }
    }

    /**
     * Returns the selected TransformGroup node.
     *
     * @param transform The index of the transform to return.  The indices
     *  are in the range [0..(n - 1)] - where n was the number of transforms
     *  created.  transform[0] is considered the
     *  "top most" transform with repsect to the scene graph, (attached to the
     *  ViewingPlatform node) and transform[numTransforms - 1] is the "bottom
     *  most" transform (the ViewPlatorm object is attached to this transform).
     *
     * @return The TransformGroup node at the designated index. If an out of
     *  range index is given, null is returned.
     */
    public TransformGroup getTransformGroup(int transform) {
        if (transform >= transforms.length || transform < 0) return null;
        return transforms[transform];
    }

    /**
     * Returns the number of transforms in this MultiTransformGroup object.
     *
     * @return The number of transforms in this object.
     */
    public int getNumTransforms() {
        return transforms.length;
    }
}
