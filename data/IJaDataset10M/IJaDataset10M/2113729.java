package prealpha.curve;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Stack;
import prealpha.util.Util;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.math.Matrix3f;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Geometry;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.geom.BufferUtils;

/**
 * <code>Curve</code> defines a collection of points that make up a curve.
 * How this curve is constructed is undefined, and the job of a subclass.
 * The control points are defined as the super class <code>Geometry</code>
 * vertex array. They can be set and accessed as such.
 * <code>Curve</code> is abstract only maintaining the point collection. It
 * defines <code>getPoint</code> and <code>getOrientation</code>. Extending
 * classes are responsible for implementing these methods in the appropriate
 * way.
 * @author Mark Powell
 * @version $Id: Curve.java,v 1.17 2006/05/12 21:16:18 nca Exp $
 */
public abstract class Curve implements Savable {

    private static final long serialVersionUID = 1L;

    protected String name;

    protected FloatBuffer samplingPoints;

    protected int numberOfSamplingPoints;

    protected float length;

    protected float progress;

    private Vector3f buff1;

    private Vector3f buff2;

    private Vector3f buff3;

    protected Curve predecessor;

    protected Curve successor;

    public Curve getPredecessor() {
        return predecessor;
    }

    public Curve getSuccessor() {
        return successor;
    }

    public void setPredecessor(Curve predecessor) {
        this.predecessor = predecessor;
    }

    public void setSuccessor(Curve successor) {
        this.successor = successor;
    }

    public Curve(String name) {
        this.name = name;
        numberOfSamplingPoints = 0;
    }

    /**
     * Constructor creates a <code>Curve</code> object. The sampling point list
     * is set during creation. If the control point list is null or has fewer
     * than 2 points, an exception is thrown.
     * 
     * @param name
     *            the name of the scene element. This is required for
     *            identification and comparision purposes.
     * @param samplingPoints
     *            the points that define the curve. These are copied into the
     *            object, further changes to these Vectors will not affect the
     *            Curve.
     */
    public Curve(String name, Vector3f[] samplingPoints) {
        this.name = name;
        if (null == samplingPoints) {
            throw new JmeException("Control Points may not be null.");
        }
        if (samplingPoints.length < 2) {
            throw new JmeException("There must be at least two sampling points.");
        }
        this.samplingPoints = BufferUtils.createFloatBuffer(samplingPoints);
        numberOfSamplingPoints = samplingPoints.length;
    }

    /**
     *
     * <code>getPoint</code> calculates a point on the curve based on
     * the time, where time is [0, 1]. How the point is calculated is
     * defined by the subclass.
     * @param time the time frame on the curve, [0, 1].
     * @return the point on the curve at a specified time.
     */
    public Vector3f getPoint(float time) {
        return getPoint(time, new Vector3f());
    }

    /**
     * Equivalent to getPoint(float) but instead of creating a new Vector3f object
     * on the heap, the result is stored in store and store is returned.
     * @param time the time frame on the curve: [0, 1].
     * @param store the vector3f object to store the point in.
     * @return store, after receiving the result.
     * @see #getPoint(float) 
     */
    public abstract Vector3f getPoint(float time, Vector3f store);

    public Vector3f getPointByLength(float arclength) {
        return getPointByLength(arclength, new Vector3f());
    }

    public abstract Vector3f getPointByLength(float arclength, Vector3f store);

    /**
     * <code>getOrientation</code> calculates the rotation matrix for any given point along to the line to still be
     * facing in the direction of the line.
     * 
     * @param time the current time (between 0 and 1)
     * @param precision how accurate to (i.e. the next time) to check against.
     * @return the rotation matrix.
     * @see com.jme.curve.Curve#getOrientation(float, float)
     */
    public Quaternion getOrientation(float time, float precision) {
        Quaternion rotation = new Quaternion();
        Vector3f point = getPoint(time);
        Vector3f tangent = point.subtract(getPoint(time + precision));
        tangent = tangent.normalize();
        Vector3f tangent2 = getPoint(time - precision).subtract(point);
        Vector3f normal = tangent.cross(tangent2);
        normal = normal.normalize();
        Vector3f binormal = tangent.cross(normal);
        binormal = binormal.normalize();
        rotation.fromAxes(tangent, normal, binormal);
        return rotation;
    }

    /**
     * <code>getOrientation</code> calculates the rotation matrix for any given point along to the line to still be
     * facing in the direction of the line. A up vector is supplied, this keep the rotation matrix following the line, but
     * insures the object's up vector is not drastically changed.
     * 
     * @param time the current time (between 0 and 1)
     * @param precision how accurate to (i.e. the next time) to check against.
     * @return the rotation matrix.
     * @see com.jme.curve.Curve#getOrientation(float, float)
     */
    public Quaternion getOrientation(float time, float precision, Vector3f up) {
        if (up == null) {
            return getOrientation(time, precision);
        }
        Quaternion rotation = new Quaternion();
        Vector3f tangent = getPoint(time).subtract(getPoint(time + precision));
        tangent = tangent.normalize();
        Vector3f binormal = tangent.cross(up);
        binormal = binormal.normalize();
        Vector3f normal = binormal.cross(tangent);
        normal = normal.normalize();
        rotation.fromAxes(tangent, normal, binormal);
        return rotation;
    }

    public Vector3f getStartingPoint() {
        BufferUtils.populateFromBuffer(buff1, this.samplingPoints, 0);
        return buff1;
    }

    public Vector3f getEndingPoint() {
        BufferUtils.populateFromBuffer(buff1, this.samplingPoints, this.samplingPoints.limit());
        return buff1;
    }

    public boolean hasCollision(Spatial scene) {
        BoundingVolume bound2 = scene.getWorldBound();
        if (bound2 == null) return false;
        for (float f = 0; f <= length; f++) {
            if (bound2.contains(this.getPointByLength(f))) return true;
        }
        return false;
    }

    Plane pl = new Plane();

    /**
	 * returns the length traversed along the curve of a point
	 * returns Float.NaN if there is no collision 
	 * @param point
	 * @return
	 */
    public float checkProgress(Vector3f point, float precision) {
        precision = 1f / precision;
        float f;
        for (f = 0; f < length; f += precision) {
            Vector3f a = getPointByLength(f);
            Vector3f b = getPointByLength(f + 1);
            pl.setConstant(b.subtract(a).dot(a));
            pl.setNormal(b.subtract(a));
            if (pl.pseudoDistance(point) < .5f) return f;
        }
        return -1f;
    }

    public float checkProgress(Vector3f point, float likelyPosition, float precision) {
        precision = 1f / precision;
        float f;
        for (f = likelyPosition - 1; f < length; f += precision) {
            Vector3f a = getPointByLength(f);
            Vector3f b = getPointByLength(f + 1);
            pl.setConstant(b.subtract(a).dot(a));
            pl.setNormal(b.subtract(a));
            if (pl.pseudoDistance(point) < .5f) return f;
        }
        return -1f;
    }

    public String getName() {
        return name;
    }

    public void addPoint(Vector3f v) {
        BufferUtils.addInBuffer(v, samplingPoints, this.numberOfSamplingPoints - 1);
        this.numberOfSamplingPoints++;
    }

    public float getLength() {
        return length;
    }

    public void write(JMEExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(samplingPoints, "samplingPoints", null);
        capsule.write(numberOfSamplingPoints, "numberOfSamplingPoints", 0);
    }

    public void read(JMEImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        capsule.readFloatBuffer("samplingPoints", null);
        capsule.readInt("numberOfSamplingPoints", 0);
    }

    public Class<? extends Curve> getClassTag() {
        return this.getClass();
    }
}
