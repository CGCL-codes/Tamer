package xenon3d.vector;

/**
 * A three-element tuple of Euler angles represented by single-precision
 * floating point x,y,z components. An euler-angle tuple is a set of rotations
 * around the three exis vectors of the 3D coordinate system (x,y,z). The euler
 * rotation angles are applied first about the X, then Y then Z axis. These
 * rotations are applied using a static frame of reference. In other words, the
 * orientation of the Y rotation axis is not affected by the X rotation and the
 * orientation of the Z rotation axis is not affected by the X or Y rotation.
 * @author Volker Everts
 * @version 0.1 - 18.09.2011: Created
 */
public class EulerAngle3f extends Tuple3f {

    /** The serial version UID. */
    private static final long serialVersionUID = 20110918L;

    /**
     * Constructs and initializes an EulerAngle3f using a rotation of 0 degrees
     * for all axis vectors.
     */
    public EulerAngle3f() {
        super();
    }

    /**
     * Constructs and initializes anEulerAngle3f from the specified x, y and z
     * angles given in degrees.
     * @param x the angle of rotation around the x axis
     * @param y the angle of rotation around the y axis
     * @param z the angle of rotation around the z axis
     */
    public EulerAngle3f(float x, float y, float z) {
        super(x, y, z);
    }

    /**
     * Constructs and initializes an EulerAngle4f from the specified general
     * tuple (which might very well be an EulerAngle3f itself).
     * @param t the general tuple
     */
    public EulerAngle3f(Tuple3f t) {
        super(t);
    }

    /**
     * Sets the value of this euler-angle to the normalized rotational equivalent
     * of the specified axis-angle.<p>
     * Equations<p>
     * <code>
     * heading = atan2(y * sin(angle)- x * z * (1 - cos(angle)), 1 - (y2 + z2 ) * (1 - cos(angle)))
     * attitude = asin(x * y * (1 - cos(angle)) + z * sin(angle))
     * bank = atan2(x * sin(angle)-y * z * (1 - cos(angle)) , 1 - (x2 + z2) * (1 - cos(angle)))
     * </code>
     * Singularities<p>
     * <code>
     * Straight up: heading = 2 * atan2(x * sin(angle/2),cos(angle/2)), bank = 0
     * Straight down: heading = -2 * atan2(x * sin(angle/2),cos(angle/2)), bank = 0
     * </code>
     * @param a the AxisAngle4f
     */
    public final void set(AxisAngle4f a) {
        float mag = a.normSq();
        if (mag == 0.0f) throw new IllegalArgumentException();
        float ax = a.x;
        float ay = a.y;
        float az = a.z;
        if (!FMath.isEqual(mag, 1.0f, FMath.EPS)) {
            mag = 1.0f / FMath.sqrt(mag);
            ax *= mag;
            ay *= mag;
            az *= mag;
        }
        float sn = FMath.sin(a.w);
        float cs = FMath.cos(a.w);
        float t = 1.0f - cs;
        float s = ax * ay * t + az * sn;
        if (s > 1.0f - FMath.EPS) {
            y = 2.0f * FMath.atan2(ax * FMath.sin(a.w * 0.5f), FMath.cos(a.w * 0.5f));
            x = 90.0f;
            z = 0.0f;
        } else if (s < -1.0f + FMath.EPS) {
            y = -2.0f * FMath.atan2(ax * FMath.sin(a.w * 0.5f), FMath.cos(a.w * 0.5f));
            x = -90.0f;
            z = 0.0f;
        } else {
            y = FMath.atan2(ay * sn - ax * az * t, 1.0f - (ay * ay + az * az) * t);
            x = FMath.asin(ax * ay * t + az * sn);
            z = FMath.atan2(ax * sn - ay * az * t, 1.0f - (ax * ax + az * az) * t);
        }
    }

    /**
     * Sets the value of this euler-angle to the normalized rotational equivalent
     * of the passed quaternion.<p>
     * Equations<p>
     * <code>
     * heading = atan2(2 * qy * qw - 2 * qx * qz , 1 - 2 * qy2 - 2 * qz2)
     * attitude = asin(2 * qx * qy + 2 * qz * qw)
     * bank = atan2(2 * qx * qw - 2 * qy * qz , 1 - 2 * qx2 - 2 * qz2)
     * </code>
     * Singularities<p>
     * <code>
     * North pole: qx * qy + qz * qw = 0.5
     * which gives: heading = 2 * atan2(x, w), bank = 0
     * South pole: qx * qy + qz * qw = -0.5
     * which gives: heading = -2 * atan2(x, w), bank = 0
     * </code>
     * @param q the Quat4f
     */
    public final void set(Quat4f q) {
        float mag = q.normSq();
        if (mag == 0.0f) throw new IllegalArgumentException();
        float qx = q.x;
        float qy = q.y;
        float qz = q.z;
        float qw = q.w;
        if (!FMath.isEqual(mag, 1.0f, FMath.EPS)) {
            mag = 1.0f / FMath.sqrt(mag);
            qx *= mag;
            qy *= mag;
            qz *= mag;
            qw *= mag;
        }
        float t = qx * qy + qz * qw;
        if (t > 0.5f - FMath.EPS) {
            y = 2.0f * FMath.atan2(qx, qw);
            x = 90.0f;
            z = 0.0f;
        } else if (t < -0.5f + FMath.EPS) {
            y = -2.0f * FMath.atan2(qx, qw);
            x = -90.0f;
            z = 0.0f;
        } else {
            x = FMath.asin(2.0f * t);
            y = FMath.atan2(2.0f * qy * qw - 2.0f * qx * qz, 1.0f - 2.0f * qy * qy - 2.0f * qz * qz);
            z = FMath.atan2(2.0f * qx * qw - 2.0f * qy * qz, 1.0f - 2.0f * qx * qy - 2.0f * qz * qz);
        }
    }

    /**
     * Sets the value of this euler-angle to the normalized rotational component
     * of the passed rotation matrix.<p>
     * Equations<p>
     * <code>
     * heading = atan2(-m31, m11)
     * attitude = asin(m21)
     * bank = atan2(-m23, m22)
     * </code>
     * Singularities<p>
     * <code>
     * North pole: m21 = 1
     * which gives: heading = atan2(m13, m33), bank = 0
     * South pole: m21 = -1
     * which gives: heading = atan2(m13, m33), bank = 0
     * </code>
     * @param mat the Matrix3f
     */
    public final void set(Matrix3f mat) {
        conversion(mat.scale, mat.m11, mat.m21, mat.m31, mat.m22, mat.m13, mat.m23, mat.m33);
    }

    /**
     * Sets the value of this euler-angle to the normalized rotational component
     * of the passed transformation matrix.
     * @see set(Matrix3f) for equations
     * @param mat the Matrix4f
     */
    public final void set(Matrix4f mat) {
        conversion(mat.scale, mat.m11, mat.m21, mat.m31, mat.m22, mat.m13, mat.m23, mat.m33);
    }

    /**
     * Helper method for conversion of matrix data to euler angles.
     */
    private void conversion(float scale, float m11, float m21, float m31, float m22, float m13, float m23, float m33) {
        if (scale < FMath.EPS) throw new IllegalArgumentException();
        m21 /= scale;
        if (m21 > 1.0f - FMath.EPS) {
            y = FMath.atan2(m13, m33);
            x = 90.0f;
            z = 0.0f;
        } else if (m21 < -1.0f + FMath.EPS) {
            y = FMath.atan2(m13, m33);
            x = -90.0f;
            z = 0.0f;
        } else {
            x = FMath.asin(m21);
            y = FMath.atan2(-m31, m11);
            z = FMath.atan2(-m23, m22);
        }
    }
}
