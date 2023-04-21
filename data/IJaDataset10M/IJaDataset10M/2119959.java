package net.sourceforge.bricksviewer.view;

import java.util.*;
import net.sourceforge.bricksviewer.geometry.Point3D;

public class Camera {

    private float sceneXAngle, sceneYAngle;

    private Point3D location;

    private Point3D lookAt;

    private Collection listeners = new ArrayList();

    /**
     * Add a camera event listener.
     *
     * @param the CameraEventListener to add
     */
    public void addEventListener(CameraEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a camera event listener.
     *
     * @param the CameraEventListener to remove
     */
    public void removeEventListener(CameraEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Get the location of the camera.
     *
     * @return the Point3D location of the camera
     */
    public Point3D getLocation() {
        return location;
    }

    /**
     * Set the location of the camera.
     *
     * @param val the Point3D location of the camera
     */
    public void setLocation(Point3D val) {
        location = val;
        notifyListeners();
    }

    /**
     * Set the location of the camera.
     *
     * @param x the float x coordinate location of the camera
     * @param y the float y coordinate location of the camera
     * @param z the float z coordinate location of the camera
     */
    public void setLocation(float x, float y, float z) {
        location = new Point3D(x, y, z);
        notifyListeners();
    }

    /**
     * Get the location pointed to by the camera.
     *
     * @return the Point3D location pointed to by the camera
     */
    public Point3D getLookAt() {
        return lookAt;
    }

    /**
     * Set the location pointed to by the camera.
     *
     * @param val the Point3D location pointed to by the camera.
     */
    public void setLookAt(Point3D val) {
        lookAt = val;
        notifyListeners();
    }

    /**
     * Set the location pointed to by the camera.
     *
     * @param x the float x coordinate location pointed to by the camera
     * @param y the float y coordinate location pointed to by the camera
     * @param z the float z coordinate location pointed to by the camera
     */
    public void setLookAt(float x, float y, float z) {
        lookAt = new Point3D(x, y, z);
    }

    /**
     * Get the scene x angle of the camera.
     *
     * @return the float x angle of the camera
     */
    public float getSceneXAngle() {
        return sceneXAngle;
    }

    /**
     * Set the scene x angle of the camera.
     *
     * @param val the float x angle of the camera
     */
    public void setSceneXAngle(float val) {
        sceneXAngle = val;
        notifyListeners();
    }

    /**
     * Get the scene y angle of the camera.
     *
     * @return the float y angle of the camera
     */
    public float getSceneYAngle() {
        return sceneYAngle;
    }

    /**
     * Set the scene y angle of the camera.
     *
     * @param val the float y angle of the camera
     */
    public void setSceneYAngle(float val) {
        sceneYAngle = val;
        notifyListeners();
    }

    /**
     * Notify all listeners that the camera has changed.
     */
    protected void notifyListeners() {
        CameraEventListener listener;
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            listener = (CameraEventListener) it.next();
            listener.cameraChanged(this);
        }
    }
}
