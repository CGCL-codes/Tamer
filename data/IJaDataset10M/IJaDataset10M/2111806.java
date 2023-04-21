package gui.gl;

import static gui.gl.Util3D.*;
import javax.vecmath.*;
import com.xith3d.scenegraph.*;

public class Camera {

    private Vector3f rot;

    private Vector3f mov;

    private boolean isRotationScheduled = false;

    private boolean isTranslationScheduled = false;

    private Transform3D trans;

    public Camera(Transform3D startpos) {
        trans = startpos;
        mov = new Vector3f();
        rot = new Vector3f((float) (-Math.PI / 2), 0.0f, 0.0f);
        startpos.getTranslation(mov);
        startpos.setEuler(rot);
        startpos.setTranslation(mov);
    }

    /**
     * Updates the camera position and angle.
     */
    public synchronized void performCameraMovement() {
        if (isRotationScheduled) {
            performRotation();
        }
        if (isTranslationScheduled) {
            performTranslation();
        }
    }

    public Transform3D getTransform() {
        return trans;
    }

    private void performRotation() {
        trans.setEuler(rot);
        isRotationScheduled = false;
    }

    private void performTranslation() {
        trans.setTranslation(mov);
        isTranslationScheduled = false;
    }

    public synchronized void setRotation(Tuple3f newrot) {
        rot.set(newrot);
        isRotationScheduled = true;
    }

    public synchronized void setTranslation(Tuple3f newmov) {
        mov.set(newmov);
        isTranslationScheduled = true;
    }

    public void getRotation(Vector3f r) {
        r.set(rot);
    }

    public void getTranslation(Vector3f r) {
        r.set(mov);
    }
}
