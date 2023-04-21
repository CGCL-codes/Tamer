package com.sun.j3d.utils.behaviors.interpolators;

import javax.media.j3d.*;
import java.util.*;
import javax.vecmath.*;
import com.sun.j3d.internal.J3dUtilsI18N;

/**
 * TCBSplinePathInterpolator behavior.  This class defines the base class for
 * all TCB (Kochanek-Bartels) Spline Path Interpolators.  
 *
 * @since Java3D 1.1
 */
public abstract class TCBSplinePathInterpolator extends TransformInterpolator {

    private int keysLength;

    /**
     * An array of KBKeyFrame for interpolator
     */
    protected TCBKeyFrame[] keyFrames;

    /**
     * This value is the distance between knots 
     * value which can be used in further calculations by the subclass.
     */
    protected float currentU;

    /**
     * The lower knot
     */
    protected int lowerKnot;

    /**
     * The upper knot
     */
    protected int upperKnot;

    /**
     * Constructs a TCBSplinePathInterpolator node with a null alpha value and
     * a null target of TransformGroup
     *
     * @since Java 3D 1.3
     */
    TCBSplinePathInterpolator() {
    }

    /**
     * @deprecated As of Java 3D version 1.3, replaced by 
     * <code>TCBSplinePathInterpolator(Alpha, TransformGroup, TCBKeyFrame[]) </code>
     */
    public TCBSplinePathInterpolator(Alpha alpha, TCBKeyFrame keys[]) {
        this(alpha, null, keys);
    }

    /**
     * Constructs a new TCBSplinePathInterpolator object that interpolates
     * between keyframes with specified alpha, target and default axisOfTransform
     * set to identity. It takes at least two key frames. The first key 
     * frame's knot must have a value of 0.0 and the last knot must have a
     * value of 1.0.  An intermediate key frame with index k must have a 
     * knot value strictly greater than the knot value of a key frame with 
     * index less than k. Once this constructor has all the valid key frames
     * it creates its own list of key fames that duplicates the first key frame
     * at the beginning of the list and the last key frame at the end of the
     * list. 
     * @param alpha the alpha object for this interpolator
     * @param target the TransformGroup node effected by this TCBSplinePathInterpolator
     * @param keys an array of TCBKeyFrame. Requires at least two key frames.
     *
     * @since Java 3D 1.3
     */
    public TCBSplinePathInterpolator(Alpha alpha, TransformGroup target, TCBKeyFrame keys[]) {
        super(alpha, target);
        processKeyFrames(keys);
    }

    /**
     * Constructs a new TCBSplinePathInterpolator object that interpolates
     * between keyframes with specified alpha, target and axisOfTransform.
     * It takes at least two key frames. The first key 
     * frame's knot must have a value of 0.0 and the last knot must have a
     * value of 1.0.  An intermediate key frame with index k must have a 
     * knot value strictly greater than the knot value of a key frame with 
     * index less than k. Once this constructor has all the valid key frames
     * it creates its own list of key fames that duplicates the first key frame
     * at the beginning of the list and the last key frame at the end of the
     * list. 
     * @param alpha the alpha object for this interpolator
     * @param target the TransformGroup node effected by this TCBSplinePathInterpolator
     * @param axisOfTransform the transform that defines the local coordinate
     * @param keys an array of TCBKeyFrame. Requires at least two key frames.
     *
     * @since Java 3D 1.3
     */
    public TCBSplinePathInterpolator(Alpha alpha, TransformGroup target, Transform3D axisOfTransform, TCBKeyFrame keys[]) {
        super(alpha, target, axisOfTransform);
        processKeyFrames(keys);
    }

    /**
     * Process the new array of key frames
     */
    private void processKeyFrames(TCBKeyFrame keys[]) {
        keysLength = keys.length;
        if (keysLength < 2) {
            throw new IllegalArgumentException(J3dUtilsI18N.getString("TCBSplinePathInterpolator0"));
        }
        if (keys[0].knot < -0.0001 || keys[0].knot > 0.0001) {
            throw new IllegalArgumentException(J3dUtilsI18N.getString("TCBSplinePathInterpolator1"));
        }
        if (keys[keysLength - 1].knot - 1.0 < -0.0001 || keys[keysLength - 1].knot - 1.0 > 0.0001) {
            throw new IllegalArgumentException(J3dUtilsI18N.getString("TCBSplinePathInterpolator2"));
        }
        for (int i = 0; i < keysLength; i++) {
            if (i > 0 && keys[i].knot < keys[i - 1].knot) {
                throw new IllegalArgumentException(J3dUtilsI18N.getString("TCBSplinePathInterpolator3"));
            }
        }
        keyFrames = new TCBKeyFrame[keysLength + 2];
        keyFrames[0] = new TCBKeyFrame();
        keyFrames[0] = keys[0];
        for (int i = 1; i < keysLength + 1; i++) {
            keyFrames[i] = keys[i - 1];
        }
        keyFrames[keysLength + 1] = new TCBKeyFrame();
        keyFrames[keysLength + 1] = keys[keysLength - 1];
        keysLength += 2;
    }

    /**
     * This method retrieves the length of the key frame array.
     * @return the number of key frames 
     */
    public int getArrayLength() {
        return keysLength - 2;
    }

    /**
     * This method retrieves the key frame at the specified index.
     * @param index the index of the key frame requested
     * @return the key frame at the associated index
     */
    public TCBKeyFrame getKeyFrame(int index) {
        return this.keyFrames[index + 1];
    }

    /**
     * This method computes the bounding knot indices and interpolation value
     * "CurrentU" given the specified value of alpha and the knots[] array.
     * @param alphaValue alpha value between 0.0 and 1.0
     *
     * @since Java 3D 1.3
     */
    protected void computePathInterpolation(float alphaValue) {
        int i = 1;
        int len = keysLength - 2;
        while ((alphaValue > keyFrames[i].knot) && (i < len)) {
            i++;
        }
        if (i == 1) {
            currentU = 0f;
            lowerKnot = 1;
            upperKnot = 2;
        } else {
            currentU = (alphaValue - keyFrames[i - 1].knot) / (keyFrames[i].knot - keyFrames[i - 1].knot);
            lowerKnot = i - 1;
            upperKnot = i;
        }
    }

    /**
     * @deprecated As of Java 3D version 1.3, replaced by
     * <code>computePathInterpolation(float)</code>
     */
    protected void computePathInterpolation() {
        float value = (this.getAlpha()).value();
        computePathInterpolation(value);
    }

    /**
     * Copies all TCBSplinePathInterpolator information from
     * <code>originalNode</code> into
     * the current node.  This method is called from the
     * <code>cloneNode</code> method which is, in turn, called by the
     * <code>cloneTree</code> method.<P> 
     *
     * @param originalNode the original node to duplicate.
     * @param forceDuplicate when set to <code>true</code>, causes the
     *  <code>duplicateOnCloneTree</code> flag to be ignored.  When
     *  <code>false</code>, the value of each node's
     *  <code>duplicateOnCloneTree</code> variable determines whether
     *  NodeComponent data is duplicated or copied.
     *
     * @exception RestrictedAccessException if this object is part of a live
     *  or compiled scenegraph.
     *
     * @see Node#duplicateNode
     * @see Node#cloneTree
     * @see NodeComponent#setDuplicateOnCloneTree
     */
    public void duplicateNode(Node originalNode, boolean forceDuplicate) {
        super.duplicateNode(originalNode, forceDuplicate);
        TCBSplinePathInterpolator originalSpline = (TCBSplinePathInterpolator) originalNode;
        setAlpha(originalSpline.getAlpha());
        keysLength = originalSpline.keysLength;
        keyFrames = new TCBKeyFrame[keysLength];
        System.arraycopy(originalSpline.keyFrames, 0, keyFrames, 0, keysLength);
    }
}
