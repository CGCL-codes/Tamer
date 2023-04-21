package ca.sandstorm.luminance.camera;

import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.sandstorm.luminance.Engine;
import ca.sandstorm.luminance.MatrixTrackingGL;
import ca.sandstorm.luminance.math.Ray;
import android.opengl.GLU;
import android.opengl.Matrix;

/**
 * 6DOF Camera class. View matrix is represented by eye, target, up vectors. eye
 * is the position of the camera. target is the line of the sight of the eye. up
 * is the vector used to define what up is in terms of the camera matrix.
 * 
 * The class uses Quaternions to represent rotations.
 * 
 * @author halsafar
 * 
 */
public class Camera {

    private static final Logger _logger = LoggerFactory.getLogger(Camera.class);

    private Vector3f _up;

    private Vector3f _target;

    private Vector3f _eye;

    private Vector3f _lookDirection;

    private Vector3f _strafeDirection;

    private float[] _lastProjectionMat = null;

    private float[] _lastModelViewMat = null;

    private Quat4f _qRotation;

    private Quat4f _qView;

    private Quat4f _qNewView;

    private Quat4f _qConjugate;

    private int _viewX;

    private int _viewY;

    private int _viewWidth;

    private int _viewHeight;

    private float _fov;

    private float _aspect;

    private float _zNear;

    private float _zFar;

    private Ray _tmpRay;

    /**
     * Basic Constructor. Initializes all variables.
     */
    public Camera() {
        _logger.debug("Camera()");
        _up = new Vector3f(0, 1, 0);
        _target = new Vector3f(0, 0, 0);
        _eye = new Vector3f(0, 0, 0);
        _lookDirection = new Vector3f();
        _strafeDirection = new Vector3f();
        _lastProjectionMat = new float[16];
        _lastModelViewMat = new float[16];
        _qRotation = new Quat4f();
        _qView = new Quat4f();
        _qNewView = new Quat4f();
        _qConjugate = new Quat4f();
        _tmpRay = new Ray(0, 0, 0, 0, 0, 0);
    }

    public float getFov() {
        return _fov;
    }

    public void setFov(float _fov) {
        this._fov = _fov;
    }

    public float getAspect() {
        return _aspect;
    }

    public void setAspect(float _aspect) {
        this._aspect = _aspect;
    }

    public float getZNear() {
        return _zNear;
    }

    public void setZNear(float _zNear) {
        this._zNear = _zNear;
    }

    public float getZFar() {
        return _zFar;
    }

    public void setZFar(float _zFar) {
        this._zFar = _zFar;
    }

    /**
     * Return the current eye of the camera.
     * 
     * @return
     */
    public Vector3f getEye() {
        return _eye;
    }

    /**
     * Return the current target of the camera.
     * 
     * @return
     */
    public Vector3f getTarget() {
        return _target;
    }

    /**
     * Sets the eye.
     * 
     * @param v
     *            - Vector to set the eye to
     */
    public void setEye(Vector3f v) {
        _eye = new Vector3f(v);
        updateViewDirection();
    }

    /**
     * Sets the eye.
     * 
     * @param x
     *            x pos of the eye.
     * @param y
     *            y pos of the eye.
     * @param z
     *            z pos of the eye.
     */
    public void setEye(float x, float y, float z) {
        _eye.x = x;
        _eye.y = y;
        _eye.z = z;
        updateViewDirection();
    }

    /**
     * Sets the target for the eye.
     * 
     * @param v
     *            - Vector to set the target to
     */
    public void setTaret(Vector3f v) {
        _target = new Vector3f(v);
        updateViewDirection();
    }

    /**
     * Sets the target.
     * 
     * @param x
     *            x pos of the target.
     * @param y
     *            y pos of the target.
     * @param z
     *            z pos of the target.
     */
    public void setTarget(float x, float y, float z) {
        _target.x = x;
        _target.y = y;
        _target.z = z;
        updateViewDirection();
    }

    /**
     * Sets the up axis for the eye.
     * 
     * @param v
     *            - Vector to set the up axis to
     */
    public void setUp(Vector3f v) {
        _target = v;
    }

    /**
     * Sets the up axis.
     * 
     * @param x
     *            x val of the up axis.
     * @param y
     *            y val of the up axis.
     * @param z
     *            z val of the up axis.
     */
    public void setUp(float x, float y, float z) {
        _up.x = x;
        _up.y = y;
        _up.z = z;
    }

    /**
     * Sets the OpenGL view port. This is controlled by the camera so multiple
     * viewports are easily possible.
     * 
     * @param gl
     *            OpenGL context, local scope.
     * @param x
     *            x pos of the screen
     * @param y
     *            y pos of the screen
     * @param w
     *            width of the screen
     * @param h
     *            height of the screen
     */
    public void setViewPort(int x, int y, int w, int h) {
        _logger.debug("setViewPort(" + x + ", " + y + ", " + w + ", " + h + ")");
        _viewX = x;
        _viewY = y;
        _viewWidth = w;
        _viewHeight = h;
    }

    /**
     * Set the perspective matrix for opengl.
     * 
     * @param gl
     *            OpenGL context, local scope.
     * @param fov
     *            field of view for the projection
     * @param aspect
     *            aspect ratio (example: 16:9 vs 4:3)
     * @param zNear
     *            near clipping plane
     * @param zFar
     *            far clipping plane
     */
    public void setPerspective(float fov, float aspect, float zNear, float zFar) {
        _logger.debug("setPerspective(" + fov + ", " + aspect + ", " + zNear + ", " + zFar + ")");
        _fov = fov;
        _aspect = aspect;
        _zNear = zNear;
        _zFar = zFar;
    }

    /**
     * Update the view matrix. Use this to set the camera up before rendering
     * objects.
     * 
     * @TODO - we shouldnt need to save matrices every frame
     * 
     * @param gl
     *            OpenGL context, local scope.
     */
    public void updateViewMatrix(GL10 gl) {
        gl.glViewport(_viewX, _viewY, _viewWidth, _viewHeight);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, _fov, _aspect, _zNear, _zFar);
        getCurrentProjection(gl);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, _eye.x, _eye.y, _eye.z, _target.x, _target.y, _target.z, _up.x, _up.y, _up.z);
        getCurrentModelView(gl);
    }

    /**
     * Updates the look direction vector referenced often
     */
    private void updateViewDirection() {
        _lookDirection.set(_target);
        _lookDirection.sub(_eye);
        _lookDirection.normalize();
    }

    public void move(float distance, float xDir, float yDir, float zDir) {
        _eye.x += xDir * distance;
        _eye.y += yDir * distance;
        _eye.z += zDir * distance;
        _target.x += xDir * distance;
        _target.y += yDir * distance;
        _target.z += zDir * distance;
    }

    public void moveForward(float distance) {
        updateViewDirection();
        move(distance, _lookDirection.x, _lookDirection.y, _lookDirection.z);
    }

    public void moveLeft(float distance) {
        updateViewDirection();
        _strafeDirection.cross(_lookDirection, _up);
        move(distance, _strafeDirection.x, _strafeDirection.y, _strafeDirection.z);
    }

    public void moveUp(float distance) {
        updateViewDirection();
        _strafeDirection.cross(_lookDirection, _up);
        _strafeDirection.cross(_strafeDirection, _lookDirection);
        move(distance, _strafeDirection.x, _strafeDirection.y, _strafeDirection.z);
    }

    public void rotateCamera(float AngleDir, float xSpeed, float ySpeed, float zSpeed) {
        _qRotation.set(new AxisAngle4f(xSpeed, ySpeed, zSpeed, AngleDir));
        _qView.x = _target.x - _eye.x;
        _qView.y = _target.y - _eye.y;
        _qView.z = _target.z - _eye.z;
        _qView.w = 0;
        _qConjugate.conjugate(_qRotation);
        _qRotation.mul(_qView);
        _qRotation.mul(_qConjugate);
        _qNewView = _qRotation;
        _target.x = _eye.x + _qNewView.x;
        _target.y = _eye.y + _qNewView.y;
        _target.z = _eye.z + _qNewView.z;
    }

    public void getCurrentModelView(GL10 gl) {
        getMatrix(gl, GL10.GL_MODELVIEW, _lastModelViewMat);
    }

    /**
     * Record the current projection matrix state. Has the side effect of
     * setting the current matrix state to GL_PROJECTION
     * 
     * @param gl
     *            context
     */
    public void getCurrentProjection(GL10 gl) {
        getMatrix(gl, GL10.GL_PROJECTION, _lastProjectionMat);
    }

    /**
     * Fetches a specific matrix from opengl
     * 
     * @param gl
     *            context
     * @param mode
     *            of the matrix
     * @param mat
     *            initialized float[16] array to fill with the matrix
     */
    private void getMatrix(GL10 gl, int mode, float[] mat) {
        MatrixTrackingGL gl2 = (MatrixTrackingGL) gl;
        gl2.glMatrixMode(mode);
        gl2.getMatrix(mat, 0);
    }

    /**
     * Calculates the transform from screen coordinate system to world
     * coordinate system coordinates for a specific point, given a camera
     * position.
     * 
     * @param touch
     *            Vec2 point of screen touch, the actual position on physical
     *            screen (ej: 160, 240)
     * @param cam
     *            camera object with x,y,z of the camera and screenWidth and
     *            screenHeight of the device.
     * @return Ray based on camera view and touch location in WCS.  null if no collision found.
     */
    public Ray getWorldCoord(Vector2f touch) {
        _logger.debug("GetWorldCoords(" + touch + ")");
        float screenW = Engine.getInstance().getViewWidth();
        float screenH = Engine.getInstance().getViewHeight();
        float[] invertedMatrix, transformMatrix, normalizedInPoint, outPointNear, outPointFar;
        invertedMatrix = new float[16];
        transformMatrix = new float[16];
        normalizedInPoint = new float[4];
        outPointNear = new float[4];
        outPointFar = new float[4];
        int oglTouchY = (int) (screenH - touch.y);
        float winz = 0.0f;
        normalizedInPoint[0] = (float) ((touch.x) * 2.0f / screenW - 1.0);
        normalizedInPoint[1] = (float) ((oglTouchY) * 2.0f / screenH - 1.0);
        normalizedInPoint[2] = 2f * winz - 1.0f;
        normalizedInPoint[3] = 1.0f;
        Matrix.multiplyMM(transformMatrix, 0, _lastProjectionMat, 0, _lastModelViewMat, 0);
        Matrix.invertM(invertedMatrix, 0, transformMatrix, 0);
        Matrix.multiplyMV(outPointNear, 0, invertedMatrix, 0, normalizedInPoint, 0);
        if (outPointNear[3] == 0.0) {
            _logger.error("World coords: Could not calculate world coordinates");
            return null;
        }
        winz = 1.0f;
        normalizedInPoint[2] = 2f * winz - 1.0f;
        Matrix.multiplyMV(outPointFar, 0, invertedMatrix, 0, normalizedInPoint, 0);
        if (outPointFar[3] == 0.0) {
            _logger.error("World coords: Could not calculate world coordinates");
            return null;
        }
        float xNear = outPointNear[0] / outPointNear[3];
        float yNear = outPointNear[1] / outPointNear[3];
        float zNear = outPointNear[2] / outPointNear[3];
        float xFar = outPointFar[0] / outPointFar[3];
        float yFar = outPointFar[1] / outPointFar[3];
        float zFar = outPointFar[2] / outPointFar[3];
        Vector3f dir = new Vector3f(xFar - xNear, yFar - yNear, zFar - zNear);
        dir.normalize();
        _tmpRay.setPosition(xNear, yNear, zNear);
        _tmpRay.setDirection(dir.x, dir.y, dir.z);
        return _tmpRay;
    }
}
