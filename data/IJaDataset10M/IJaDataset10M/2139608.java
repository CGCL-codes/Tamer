package com.golemgame.tool;

import com.golemgame.constructor.Updatable;
import com.golemgame.constructor.UpdateManager;
import com.golemgame.constructor.UpdateManager.Stream;
import com.golemgame.settings.ActionSettingsListener;
import com.golemgame.settings.SettingChangedEvent;
import com.golemgame.states.GeneralSettings;
import com.golemgame.states.StateManager;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

public class CameraTool implements ITool {

    static final float MAX_ROTATION = 0.07f;

    private static Vector3f cameraVelocity = new Vector3f();

    private static Quaternion rotationXVelocity = new Quaternion();

    private static Quaternion negativeRotationXVelocity = new Quaternion();

    private static float velocityY = 0;

    private static Vector2f initialMouse = new Vector2f();

    private Vector3f defaultOrientation = new Vector3f(0, 1, 0);

    private static Vector3f unitY = new Vector3f(0, 1, 0);

    private static float xVelocity = 0;

    private static float yVelocity = 0;

    private static float approxTime = 0.002f;

    private static final float timeAccuracy = 0.001f;

    private static Vector2f velocity = new Vector2f();

    private static final Vector3f xAxis = (new Vector3f(0, 1, 0));

    private static final Vector3f yAxis = (new Vector3f(1, 0, 0));

    private boolean left = false;

    private boolean right = false;

    private float startingZoomLevel = 0;

    private float zoomVelocity;

    public CameraTool() {
        cameraVelocity.set(0, 0, 0);
        rotationXVelocity.set(0, 0, 0, 1);
        velocityY = 0;
        initCameraUpdate();
    }

    public void focus(Vector3f focusOn) {
        StateManager.getCameraManager().focusOn(focusOn);
        StateManager.getCameraManager().showCentroid();
    }

    public void deselect() {
        left = false;
        right = false;
    }

    public boolean mouseButton(int button, boolean pressed, int x, int y) {
        if (button == 0) {
            if (left = pressed) {
                zoomVelocity = 0;
                initialMouse.set(x, y);
                cameraVelocity.set(0, 0, 0);
                startingZoomLevel = StateManager.getCameraManager().getCameraZoom();
            }
        } else if (button == 1) {
            if (right = pressed) {
                initialMouse.set(x, y);
                rotationXVelocity.set(0, 0, 0, 1);
                negativeRotationXVelocity.set(0, 0, 0, 1);
                velocityY = 0;
            }
        }
        return true;
    }

    public void showPrimaryEffect(boolean show) {
    }

    public void mouseMovementAction(Vector2f mousePos, boolean left, boolean right) {
        if (left) {
            if (GeneralSettings.getInstance().getCameraZoom().isValue()) {
                float distY = mousePos.y - initialMouse.y;
                float dist = FastMath.sqr(distY) / 5000f;
                if (FastMath.abs(dist) > 5f) dist = 5f;
                dist *= FastMath.sign(distY);
                zoomVelocity = dist;
                cameraVelocity.zero();
            } else {
                cameraVelocity.set(StateManager.getCameraManager().getCameraRotation().mult(new Vector3f(-(initialMouse.x - mousePos.x) / 50f, (StateManager.IS_AWT_MOUSE ? 1 : -1) * (initialMouse.y - mousePos.y) / 50f, 0)));
                zoomVelocity = 0;
            }
        }
        if (right) {
            velocity = new Vector2f(mousePos.x - initialMouse.x, (StateManager.IS_AWT_MOUSE ? 1 : -1) * (mousePos.y - initialMouse.y));
            Vector3f dir = StateManager.getCameraManager().getCameraRotation().mult(StateManager.getCameraManager().getCameraPosition());
            dir.normalizeLocal();
            xVelocity = (velocity.x * velocity.x) / (7000f);
            if (xVelocity * approxTime > MAX_ROTATION) xVelocity = MAX_ROTATION / approxTime;
            yVelocity = (velocity.y * velocity.y) / (7000f);
            if (yVelocity * approxTime > MAX_ROTATION) yVelocity = MAX_ROTATION / approxTime;
            rotationXVelocity.fromAngleAxis(Math.signum(velocity.x) * xVelocity * approxTime, xAxis);
            negativeRotationXVelocity.fromAngleAxis(-Math.signum(velocity.x) * xVelocity * approxTime, xAxis);
            velocityY = Math.signum(velocity.y) * yVelocity * approxTime;
        }
    }

    public void scrollMove(int wheelDelta, int x, int y) {
        zoomVelocity = 0;
        this.scrollZoom(wheelDelta);
    }

    private final ActionSettingsListener focusListener = new ActionSettingsListener() {

        public void valueChanged(SettingChangedEvent<Object> e) {
            standardFocus();
        }
    };

    public ActionSettingsListener getFocusListener() {
        return focusListener;
    }

    public void standardFocus() {
        StateManager.getCameraManager().resetCamera();
    }

    private Updatable cameraUpdate = new Updatable() {

        public void update(float time) {
            if (left && MouseInput.get().isButtonDown(0)) {
                if (GeneralSettings.getInstance().getCameraZoom().isValue()) {
                    if (Math.abs(zoomVelocity) > 0f) StateManager.getCameraManager().showCentroid();
                    StateManager.getCameraManager().zoomCamera(zoomVelocity);
                } else {
                    if (cameraVelocity.lengthSquared() > 0) StateManager.getCameraManager().showCentroid();
                    zoomVelocity = 0;
                    StateManager.getCameraManager().getCameraPosition().addLocal(cameraVelocity.mult(time * 2f));
                }
            }
            if (right && MouseInput.get().isButtonDown(1)) {
                if (Math.abs(velocityY) > 0 || Math.abs(rotationXVelocity.x) > 0 || Math.abs(rotationXVelocity.y) > 0 || Math.abs(rotationXVelocity.z) > 0) StateManager.getCameraManager().showCentroid();
                Vector3f yAxis = StateManager.getCameraManager().getCameraRotation().mult(new Vector3f(1, 0, 0));
                Quaternion rotationYVelocity = new Quaternion().fromAngleNormalAxis(velocityY, yAxis);
                StateManager.getCameraManager().getCameraRotation().set(rotationXVelocity.mult(rotationYVelocity).mult(StateManager.getCameraManager().getCameraRotation()));
            }
            StateManager.getCameraManager().update();
        }
    };

    public void initCameraUpdate() {
        UpdateManager.getInstance().add(cameraUpdate, Stream.GL_UPDATE);
    }

    public void scrollZoom(float delta) {
        StateManager.getCameraManager().showCentroid();
        if (StateManager.getCameraManager().getCameraZoom() <= 4f) delta /= 4f;
        StateManager.getCameraManager().zoomCamera((StateManager.IS_AWT_MOUSE ? 1 : -1) * delta / 60f);
        StateManager.getCameraManager().update();
    }

    public void setDefaultOrientation(Vector3f defaultOrientation) {
        this.defaultOrientation = defaultOrientation;
    }

    public Vector3f getDefaultOrientation() {
        return defaultOrientation;
    }

    public static Vector3f getCameraDirection() {
        return StateManager.getCameraManager().getCameraRotation().mult(StateManager.getCameraManager().getCameraPosition()).normalizeLocal();
    }

    public void focus() {
        this.standardFocus();
    }

    public void copy() {
    }

    public void delete() {
    }

    public void properties() {
    }

    public void xyPlane(boolean value) {
    }

    public void yzPlane(boolean value) {
    }

    public void xzPlane(boolean value) {
    }
}
