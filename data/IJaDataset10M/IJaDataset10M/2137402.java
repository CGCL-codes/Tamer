package com.captiveimagination.game.GText;

import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;

/**
*
* @author Victor Porof, blue_veek@yahoo.com
*/
public class GBaseElement extends Node {

    /**
  *
  */
    private static final long serialVersionUID = 1L;

    protected Timer timer = Timer.getTimer();

    protected KeyInput keyInput = KeyInput.get();

    protected MouseInput mouseInput = MouseInput.get();

    protected DisplaySystem display = DisplaySystem.getDisplaySystem();

    protected Renderer renderer = display.getRenderer();

    protected Camera camera = renderer.getCamera();

    protected float width;

    protected float height;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    private int cMouseButton;

    private int delay;

    private static int delayMax = 5;

    private boolean startTimer;

    private boolean mousePressed;

    private boolean mouseReleased;

    private boolean mousePressed2;

    private boolean mouseReleased2;

    public void update() {
        if (mouseReleased) {
            mouseReleased = false;
            mousePressed = false;
        } else if (mouseInput.isButtonDown(cMouseButton)) {
            mousePressed = true;
        }
        if (isMouseReleased(cMouseButton) || startTimer) {
            if (mouseReleased2) {
                mouseReleased2 = false;
                mousePressed2 = false;
            } else if (mouseInput.isButtonDown(cMouseButton)) {
                mousePressed2 = true;
            }
            startTimer = true;
            if (startTimer) {
                delay++;
            }
            if (delay > delayMax) {
                startTimer = false;
                delay = 0;
            }
        }
    }

    private float mposX, mposY, iposX, iposY, deltaX, deltaY;

    private boolean moving;

    private static boolean currentMovingTrigger;

    private static boolean globalMovingTrigger = !currentMovingTrigger;

    public void drag(boolean event) {
        if (event) {
            if (currentMovingTrigger != globalMovingTrigger) {
                if (isMouseOver()) {
                    moving = true;
                    globalMovingTrigger = currentMovingTrigger;
                    mposX = mouseInput.getXAbsolute();
                    iposX = getLocalTranslation().x;
                    mposY = mouseInput.getYAbsolute();
                    iposY = getLocalTranslation().y;
                }
            }
            if (moving && currentMovingTrigger == globalMovingTrigger) {
                deltaX = mposX - mouseInput.getXAbsolute();
                deltaY = mposY - mouseInput.getYAbsolute();
                getLocalTranslation().set(iposX - deltaX, iposY - deltaY, 0);
            }
        } else {
            moving = false;
            currentMovingTrigger = false;
            globalMovingTrigger = !currentMovingTrigger;
        }
    }

    public boolean isMouseOver() {
        if (getRenderQueueMode() != Renderer.QUEUE_ORTHO) {
            if (isPick() != null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (mouseInput.getXAbsolute() >= getLocalTranslation().x - width / 2f && mouseInput.getXAbsolute() <= getLocalTranslation().x + width / 2f && mouseInput.getYAbsolute() >= getLocalTranslation().y - height / 2f && mouseInput.getYAbsolute() <= getLocalTranslation().y + height / 2f) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isMousePressed(int cMouseButton) {
        this.cMouseButton = cMouseButton;
        return mouseInput.isButtonDown(cMouseButton) && isMouseOver();
    }

    public boolean isMouseReleased(int cMouseButton) {
        this.cMouseButton = cMouseButton;
        if (!mouseInput.isButtonDown(cMouseButton) && mousePressed && isMouseOver()) {
            mouseReleased = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean isMouseDoubleClicked(int cMouseButton) {
        this.cMouseButton = cMouseButton;
        if (!mouseInput.isButtonDown(cMouseButton) && mousePressed2 && isMouseOver()) {
            mouseReleased2 = true;
            return true;
        } else {
            return false;
        }
    }

    private PickResults pickResults;

    private Ray ray;

    private Vector2f mousePosition;

    private Vector3f worldCoords;

    private Vector3f[] vert;

    private Vector3f intersection;

    private TriMesh target;

    public Vector3f isPick() {
        pickResults = new TrianglePickResults();
        pickResults.setCheckDistance(true);
        pickResults.clear();
        mousePosition = new Vector2f(mouseInput.getXAbsolute(), mouseInput.getYAbsolute());
        worldCoords = display.getWorldCoordinates(mousePosition, 0);
        ray = new Ray();
        ray.setOrigin(camera.getLocation());
        ray.setDirection(worldCoords.subtractLocal(camera.getLocation()));
        findPick(ray, pickResults);
        if (pickResults.getNumber() > 0) {
            intersection = new Vector3f();
            vert = new Vector3f[3];
            try {
                target = (TriMesh) pickResults.getPickData(0).getTargetMesh();
                for (int i = 0; i < target.getTriangleCount(); i++) {
                    target.getTriangle(i, vert);
                    if (ray.intersectWhere(vert[0].addLocal(target.getWorldTranslation()), vert[1].addLocal(target.getWorldTranslation()), vert[2].addLocal(target.getWorldTranslation()), intersection)) {
                        break;
                    }
                }
            } catch (Exception e) {
            }
        } else {
            intersection = null;
        }
        return intersection;
    }
}
