package com.bix.util.blizfiles.m2;

import java.nio.ByteBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * This class encapsulates information about a "bone" in a M2 file. Most of the
 * information was obtained from the wiki found at: {@link http
 * ://madx.dk/wowdev/wiki/index.php?title=M2/WotLK#Bones}
 * <p>
 * As this structure is generally useful only when reading from M2 files and
 * it's subordinates, there are no setters, only getters to retrieve information
 * about the structure of a model. Consumers of this class would likely create
 * adapter or proxy classes to convert them into something more useful based on
 * whatever API they are using.
 * 
 * @author squid
 * 
 * @version 1.0.0
 */
public class M2Bone {

    private Log log = LogFactory.getLog(M2Bone.class);

    private int geosetAnim;

    private int flags;

    private short parentBoneId;

    private M2Bone parentBone;

    private short geosetId;

    private int unknown2;

    private Vector3f transformation;

    private M2VectorAnimationBlock transformationAnimation;

    private Quaternion rotation;

    private M2QuaternionAnimationBlock rotationAnimation;

    private Vector3f scaling;

    private M2VectorAnimationBlock scalingAnimation;

    private Vector3f pivotPoint;

    private String name;

    public M2Bone(String name) {
        this.name = name;
    }

    public M2Bone(String name, ByteBuffer bb) {
        this.name = name;
        read(bb);
    }

    public void read(ByteBuffer bb) {
        this.geosetAnim = bb.getInt();
        this.flags = bb.getInt();
        this.parentBoneId = bb.getShort();
        this.geosetId = bb.getShort();
        this.unknown2 = bb.getInt();
        log.debug("  geosetAnim[" + this.getGeosetAnim() + "]");
        log.debug("  flags[0x" + Integer.toHexString(this.getFlags()) + "]");
        log.debug("  parentBoneId[" + this.getParentBoneId() + "]");
        log.debug("  geosetId[" + this.getGeosetId() + "]");
        log.debug("  unknown2[0x" + Integer.toHexString(this.getUnknown2()) + "]");
        log.debug("  transformationAnimation:");
        this.transformationAnimation = new M2VectorAnimationBlock(bb);
        log.debug("  rotationAnimation:");
        this.rotationAnimation = new M2QuaternionAnimationBlock(bb);
        log.debug("  scalingAnimation:");
        this.scalingAnimation = new M2VectorAnimationBlock(bb);
        this.pivotPoint = new Vector3f();
        this.pivotPoint.x = bb.getFloat();
        this.pivotPoint.y = bb.getFloat();
        this.pivotPoint.z = bb.getFloat();
        log.debug("  pivotPoint[" + this.getPivotPoint().getX() + "," + this.getPivotPoint().getY() + "," + this.getPivotPoint().getZ() + "]");
    }

    public String getName() {
        return this.name;
    }

    /**
	 * @return the geosetAnim
	 */
    public int getGeosetAnim() {
        return geosetAnim;
    }

    /**
	 * @return the parentBoneId
	 */
    public short getParentBoneId() {
        return parentBoneId;
    }

    /**
	 * @return the geosetId
	 */
    public short getGeosetId() {
        return geosetId;
    }

    /**
	 * @return the unknown2
	 */
    public int getUnknown2() {
        return unknown2;
    }

    /**
	 * @return the transform
	 */
    public Vector3f getTransformation() {
        return transformation;
    }

    /**
	 * @return the rotation
	 */
    public Quaternion getRotation() {
        return rotation;
    }

    /**
	 * @return the scaling
	 */
    public Vector3f getScaling() {
        return scaling;
    }

    /**
	 * @return the flags
	 */
    public int getFlags() {
        return flags;
    }

    /**
	 * @return the pivotPoint
	 */
    public Vector3f getPivotPoint() {
        return pivotPoint;
    }

    /**
	 * Calculate the matrix for a given animation sequence and time.
	 * 
	 * @param animationSequence
	 *          The animation sequence to calculate the matrix for.
	 * @param time
	 *          The time into the animation sequence.
	 * @return
	 */
    public Matrix4f lastCalcMatrix;

    public Vector3f lastTranslation;

    public Quaternion lastRotation;

    public Vector3f lastScaling;

    public Matrix4f calculateMatrixJme(int animationSequence, int time) {
        boolean didTranslation = false;
        boolean didRotation = false;
        boolean didScaling = false;
        Matrix4f m = new Matrix4f();
        m.setTranslation(this.pivotPoint.x, this.pivotPoint.y, this.pivotPoint.z);
        Vector3f transVec = this.getTransformationAnimation().getKeyFrameDataValue(animationSequence, time);
        Quaternion rotQuat = this.getRotationAnimation().getKeyFrameDataValue(animationSequence, time);
        Vector3f scaleVec = this.getScalingAnimation().getKeyFrameDataValue(animationSequence, time);
        com.jme.math.Matrix4f transMat = new com.jme.math.Matrix4f();
        if (transVec != null) {
            transMat.setTranslation(transVec);
            m = m.mult(transMat);
            didTranslation = true;
            this.lastTranslation = transVec;
        } else {
            this.lastTranslation = new Vector3f();
        }
        com.jme.math.Matrix4f rotMat = new com.jme.math.Matrix4f();
        if (rotQuat != null) {
            rotMat.setRotationQuaternion(rotQuat);
            m = m.mult(rotMat);
            didRotation = true;
            this.lastRotation = rotQuat;
        } else {
            this.lastRotation = new Quaternion();
        }
        if (scaleVec != null) {
            if (scaleVec.x > 10) {
                scaleVec.x = 1.0f;
            }
            if (scaleVec.y > 10) {
                scaleVec.y = 1.0f;
            }
            if (scaleVec.z > 10) {
                scaleVec.z = 1.0f;
            }
            Matrix4f scaleMat = new Matrix4f();
            scaleMat.scale(scaleVec);
            m = m.mult(scaleMat);
            this.lastScaling = scaleVec;
            didScaling = true;
        } else {
            this.lastScaling = new Vector3f(1.0f, 1.0f, 1.0f);
        }
        if (didTranslation == false && didRotation == false && didScaling == false) {
            m = new com.jme.math.Matrix4f();
        } else {
            com.jme.math.Vector3f unpivot = new com.jme.math.Vector3f(-this.pivotPoint.x, -this.pivotPoint.y, -this.pivotPoint.z);
            com.jme.math.Matrix4f unpiv = new com.jme.math.Matrix4f();
            unpiv.setTranslation(unpivot);
            m = m.mult(unpiv);
        }
        this.lastCalcMatrix = m;
        return m;
    }

    /**
	 * @return the transformationAnimation
	 */
    public M2VectorAnimationBlock getTransformationAnimation() {
        return transformationAnimation;
    }

    /**
	 * @return the rotationAnimation
	 */
    public M2QuaternionAnimationBlock getRotationAnimation() {
        return rotationAnimation;
    }

    /**
	 * @return the scalingAnimation
	 */
    public M2VectorAnimationBlock getScalingAnimation() {
        return scalingAnimation;
    }

    /**
	 * @return the parentBone
	 */
    public M2Bone getParentBone() {
        return parentBone;
    }

    /**
	 * @param parentBone
	 *          the parentBone to set
	 */
    public void setParentBone(M2Bone parentBone) {
        this.parentBone = parentBone;
    }
}
