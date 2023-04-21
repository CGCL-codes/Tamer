package computergraphicsproject.Engines.CameraHandling;

import computergraphicsproject.utilities.MathUtils;
import computergraphicsproject.utilities.Point3D;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author hussein
 */
public class CameraHandler {

    private float x = 0, y = 0, z = 0, tx = 0, ty = 0, tz = 0;

    private Point3D startPos = null;

    private Point3D endPos = null;

    private Point3D dirPos = null;

    private Point3D startTarget = null;

    private Point3D endTarget = null;

    private Point3D dirTarget = null;

    private float stepSize = 0;

    private float t = 0;

    private Boolean isAnimating = false;

    private boolean animatingForward = true;

    private float eyeDisplacement = 0.25f;

    private float targetPointOffset = 0;

    /**
     *
     * @param tx
     * @param ty
     * @param tz
     */
    public void setTargetPoint(float tx, float ty, float tz) {
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        disableAnimation();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        disableAnimation();
    }

    /**
     * 
     */
    public void applyCamera() {
        synchronized (isAnimating) {
            if (isAnimating == true) {
                Point3D currentPos = MathUtils.quadraticBezierCurve(startPos, endPos, dirPos, t);
                Point3D currentTarget = MathUtils.quadraticBezierCurve(startTarget, endTarget, dirTarget, t);
                x = currentPos.getX();
                y = currentPos.getY();
                z = currentPos.getZ();
                tx = currentTarget.getX();
                ty = currentTarget.getY();
                tz = currentTarget.getZ();
                if (animatingForward) {
                    t += stepSize;
                } else {
                    t -= stepSize;
                }
                if (t < 0) {
                    animatingForward = true;
                }
                if (t > 1) {
                    animatingForward = false;
                }
            }
        }
        GLU.gluLookAt(x, y, z, tx, ty, tz, 0, 1, 0);
    }

    private Vector3f getRightNormalizedVector() {
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f fwd = new Vector3f(tx - x, ty - y, tz - z);
        Vector3f resultOfCross = new Vector3f();
        Vector3f.cross(fwd, up, resultOfCross);
        Vector3f normalizedResult = new Vector3f();
        resultOfCross.normalise(normalizedResult);
        return normalizedResult;
    }

    public void applyRightCamera() {
        Vector3f displacementVector = getRightNormalizedVector();
        GLU.gluLookAt(x - displacementVector.getX() * eyeDisplacement, y - displacementVector.getY() * eyeDisplacement, z - displacementVector.getZ() * eyeDisplacement, tx + displacementVector.getX() * targetPointOffset, ty + displacementVector.getY() * targetPointOffset, tz + displacementVector.getZ() * targetPointOffset, 0, 1, 0);
    }

    public void applyLeftCamera() {
        Vector3f displacementVector = getRightNormalizedVector();
        GLU.gluLookAt(x + displacementVector.getX() * eyeDisplacement, y + displacementVector.getY() * eyeDisplacement, z + displacementVector.getZ() * eyeDisplacement, tx - displacementVector.getX() * targetPointOffset, ty - displacementVector.getY() * targetPointOffset, tz - displacementVector.getZ() * targetPointOffset, 0, 1, 0);
    }

    public void animateCameraOnBezierCurve(Point3D startPos, Point3D endPos, Point3D dirPos, Point3D startTarget, Point3D endTarget, Point3D dirTarget, float stepSize) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.dirPos = dirPos;
        this.endPos = endPos;
        this.endTarget = endTarget;
        this.dirTarget = dirTarget;
        this.startTarget = startTarget;
        this.stepSize = stepSize;
    }

    public void enableAnimation() {
        synchronized (isAnimating) {
            isAnimating = true;
        }
    }

    public void disableAnimation() {
        synchronized (isAnimating) {
            isAnimating = false;
        }
    }
}
