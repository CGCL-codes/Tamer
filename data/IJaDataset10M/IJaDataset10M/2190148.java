package spdrender.raytracer;

import spdrender.raytracer.base.*;

/**
 * The camera primitive.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class Camera {

    private Vector3D o, d;

    /** Creates a new camera positioned at the origin facing to the X axis. */
    public Camera() {
        o = new Vector3D();
        d = new Vector3D(1, 0, 0);
    }

    /** Creates a new camera with the specified origin and the specified direction.
     * 
     * @param origin The origin of the camera.
     * @param direction A normalized vector who represents the direction of the camera.
     */
    public Camera(Vector3D origin, Vector3D direction) {
        o = origin;
        d = direction;
    }

    /** Creates a camera with the specified origin and direction coordinates.
     * <b>Note:</b>  The direction vector gets normalized in this constructor.
     * 
     * @param ox X-coordinate of the origin vector.
     * @param oy Y-coordinate of the origin vector.
     * @param oz Z-coordinate of the origin vector.
     * @param dx X-coordinate of the direction vector.
     * @param dy Y-coordinate of the direction vector.
     * @param dz Z-coordinate of the direction vector.
     */
    public Camera(float ox, float oy, float oz, float dx, float dy, float dz) {
        o = new Vector3D(ox, oy, oz);
        d = new Vector3D(dx, dy, dz).normalize();
    }

    /** Retrieves the camera's origin.
     * 
     * @return A 3d vector representing the origin of the camera.
     */
    public Vector3D getOrigin() {
        return o;
    }

    /** Retrieves the camera's direction.
     * 
     * @return A 3d normalized vector representing the direction of the camera.
     */
    public Vector3D getDirection() {
        return d;
    }

    /** Sets the origin vector of this camera
     * 
     * @param origin The new origin of the camera.
     */
    public void setOrigin(Vector3D origin) {
        o = origin;
    }

    /** Sets the direction vector of the camera
     * 
     * @param direction The new direction of the camera.  <b>Note:</b> The vector must be normalized.
     */
    public void setDirection(Vector3D direction) {
        d = direction;
    }

    @Override
    public String toString() {
        return "Camera: origin " + o + " direction " + d;
    }
}
