package iicm.utils3d;

import iicm.ge3d.GE3D;
import java.io.PrintStream;

public class Camera {

    protected float[] position_ = { 0.0f, 0.0f, 0.0f };

    protected Quaternion orientation_ = new Quaternion();

    static final int X = 0;

    static final int Y = 1;

    static final int Z = 2;

    /**
   * reset the Camera to its default position/orientation
   */
    public void reset() {
        position_[X] = position_[Y] = position_[Z] = 0.0f;
        orientation_.reset();
    }

    /**
   * make the Camera leveled with ground plane
   */
    public void levelize() {
        orientation_.levelize();
    }

    /**
   * untilt the Camera (up is (0, 1, 0))
   */
    public void untilt() {
        orientation_.untilt();
    }

    /**
   * set Camera via GE3D
   * @param viewangle vertical field of view
   * @param winaspect window aspect (width/height)
   * @param hither near clipping plane
   * @param yon far clipping plane
   */
    public void setCamera(float viewangle, float winaspect, float hither, float yon) {
        float[] axisangle = orientation_.getAxisAngle();
        GE3D.setPerspectiveCamera(position_, axisangle, viewangle, winaspect, hither, yon);
    }

    /**
   * zoom (dolly) camera (away from viewing plane)
   */
    public void zoomOut(float f) {
        float[] n = orientation_.transformAxis(2);
        float[] tran = new float[3];
        tran[0] = f * n[0];
        tran[1] = f * n[1];
        tran[2] = f * n[2];
        translateVec(tran);
    }

    /**
   * translate camera parallel to viewing plane
   */
    public void translateVP(float x, float y, float aspect, float scale) {
        float[] u = orientation_.transformAxis(0);
        float[] v = orientation_.transformAxis(1);
        x *= aspect * scale;
        y *= scale;
        float[] tran = new float[3];
        tran[0] = x * u[0] + y * v[0];
        tran[1] = x * u[1] + y * v[1];
        tran[2] = x * u[2] + y * v[2];
        translateVec(tran);
    }

    /**
   * translate camera along a given vector
   */
    public void translateVec(float[] tran) {
        position_[0] += tran[0];
        position_[1] += tran[1];
        position_[2] += tran[2];
    }

    /**
   * rotate horizontally and vertical around camera position.
   * (angles given in radians)
   */
    public void rotateXYposition(float l2r, float t2b) {
        float[] v = orientation_.transformAxis(1);
        Quaternion rotation;
        if (t2b != 0.0f) {
            float[] u = orientation_.transformAxis(0);
            rotation = new Quaternion(u, t2b);
            rotation.multiply(new Quaternion(v, l2r));
        } else rotation = new Quaternion(v, l2r);
        orientation_.multiplyLeft(rotation);
        orientation_.renormalize();
    }

    /**
   * rotate horizontally and vertically about arbitrary center.
   * (angles given in radians)
   */
    public void rotateXYcenter(float l2r, float t2b, float[] center) {
        float[] u = orientation_.transformAxis(0);
        float[] v = orientation_.transformAxis(1);
        Quaternion rotation = new Quaternion(u, t2b);
        rotation.multiply(new Quaternion(v, l2r));
        position_ = rotation.rotatePointCenter(position_, center);
        orientation_.multiplyLeft(rotation);
        orientation_.renormalize();
    }

    /**
   * approach a target position. move a fraction of k of the distance
   * between current position and target position towards the target
   * (away from target if k < 0). do not go nearer to target than near
   * clipping plane hither.
   */
    public void approachPosition(float[] poi, float ktran, float hither) {
        float[] pos = position_;
        Vec3f poi_pos = new Vec3f(poi[X] - pos[X], poi[Y] - pos[Y], poi[Z] - pos[Z]);
        if (ktran < 0 || (1 - ktran) * (1 - ktran) * Vec3f.dot(poi_pos, poi_pos) > hither * hither) {
            poi_pos.scale(ktran);
            translateVec(poi_pos.value_);
        }
    }

    /**
   * approach a surface normal vector. adjust line of sight by a
   * fraction of k to approach the negative surface normal vector
   * (away from it if k < 0). normal must be normalized.
   * does not change camera tilt.
   * @see #interpolateViews
   */
    public void approachNormal(float[] poi, float[] normal, float krot) {
        float[] n = orientation_.transformAxis(2);
        float[] rotaxisangle;
        Vec3f pos_poi = new Vec3f(position_);
        pos_poi.decrease(poi);
        pos_poi.normalize();
        rotaxisangle = Quaternion.rotationBetweenVectors(pos_poi.value_, normal);
        if (rotaxisangle[3] != 0.0f) {
            Quaternion rotation = new Quaternion(rotaxisangle, rotaxisangle[3] * krot);
            position_ = rotation.rotatePointCenter(position_, poi);
        }
        rotaxisangle = Quaternion.rotationBetweenVectors(n, normal);
        if (rotaxisangle[3] != 0.0f) {
            Quaternion rotation = new Quaternion(rotaxisangle, rotaxisangle[3] * krot);
            orientation_.multiplyLeft(rotation);
            orientation_.renormalize();
        }
    }

    /**
   * calculates a new camera position and orientation between the two viewpoints, defined
   * by the Cameras cam1 & cam2. 
   */
    public void interpolateViews(Camera cam1, Camera cam2, float t) {
        interpolateViews(cam1.position_, cam1.orientation_, cam2.position_, cam2.orientation_, t);
    }

    /**
   * calculates a new camera position and orientation between the two viewpoints, defined
   * by the two pairs of position and orientation.
   */
    public void interpolateViews(float[] pos1, Quaternion or1, float[] pos2, Quaternion or2, float t) {
        float[] pos = position_;
        float t1 = 1.0f - t;
        pos[X] = t1 * pos1[X] + t * pos2[X];
        pos[Y] = t1 * pos1[Y] + t * pos2[Y];
        pos[Z] = t1 * pos1[Z] + t * pos2[Z];
        orientation_ = Quaternion.slerp(or1, or2, t);
    }

    /**
   * get the viewing ray (for picking). direction is not normalized.
   * @param fx horicontal fraction (0 = left, 1 = right)
   * @param fy vertical fraction (0 = bottom, 1 = top)
   * viewplane, winaspect, near, far clipping planes not managed by Camera itself (see #setCamera)
   */
    public Ray viewingRay(float fx, float fy, float viewangle, float winaspect, float near, float far) {
        float[] pos = position_;
        float[] u = orientation_.transformAxis(0);
        float[] v = orientation_.transformAxis(1);
        float[] n = orientation_.transformAxis(2);
        float vph = 2.0f * (float) Math.tan(viewangle / 2.0);
        Vec3f dir = new Vec3f(n);
        dir.negate();
        dir.sincrease((float) (fx - 0.5) * vph * winaspect, u);
        dir.sincrease((float) (fy - 0.5) * vph, v);
        return new Ray(pos, dir.value_, near, far);
    }

    /**
   * print camera values
   */
    public void printValues(PrintStream os) {
        os.println("");
        float[] v = position_;
        os.println("position " + v[0] + " " + v[1] + " " + v[2]);
        v = orientation_.getAxisAngle();
        os.println("orientation " + v[0] + " " + v[1] + " " + v[2] + " " + v[3]);
        os.println("");
    }
}
