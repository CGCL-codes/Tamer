package org.thenesis.planetino2.test;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import org.thenesis.planetino2.engine.GameCore;
import org.thenesis.planetino2.graphics.Color;
import org.thenesis.planetino2.graphics.Screen;
import org.thenesis.planetino2.graphics3D.PolygonRenderer;
import org.thenesis.planetino2.graphics3D.SolidPolygonRenderer;
import org.thenesis.planetino2.input.GameAction;
import org.thenesis.planetino2.input.InputManager;
import org.thenesis.planetino2.math3D.Polygon3D;
import org.thenesis.planetino2.math3D.SolidPolygon3D;
import org.thenesis.planetino2.math3D.Transform3D;
import org.thenesis.planetino2.math3D.Vector3D;
import org.thenesis.planetino2.math3D.ViewWindow;

public class Simple3DTest2 extends GameCore {

    protected PolygonRenderer polygonRenderer;

    protected ViewWindow viewWindow;

    protected Vector polygons;

    private boolean drawFrameRate = false;

    private boolean drawInstructions = true;

    private int numFrames;

    private long startTime;

    private float frameRate;

    protected InputManager inputManager;

    private GameAction exit = new GameAction("exit");

    private GameAction smallerView = new GameAction("smallerView", GameAction.DETECT_INITAL_PRESS_ONLY);

    private GameAction largerView = new GameAction("largerView", GameAction.DETECT_INITAL_PRESS_ONLY);

    private GameAction frameRateToggle = new GameAction("frameRateToggle", GameAction.DETECT_INITAL_PRESS_ONLY);

    private GameAction goForward = new GameAction("goForward");

    private GameAction goBackward = new GameAction("goBackward");

    private GameAction goUp = new GameAction("goUp");

    private GameAction goDown = new GameAction("goDown");

    private GameAction goLeft = new GameAction("goLeft");

    private GameAction goRight = new GameAction("goRight");

    private GameAction turnLeft = new GameAction("turnLeft");

    private GameAction turnRight = new GameAction("turnRight");

    private GameAction tiltUp = new GameAction("tiltUp");

    private GameAction tiltDown = new GameAction("tiltDown");

    private GameAction tiltLeft = new GameAction("tiltLeft");

    private GameAction tiltRight = new GameAction("tiltRight");

    public Simple3DTest2(Screen screen, InputManager inputManager) {
        super(screen);
        this.inputManager = inputManager;
    }

    public void init() {
        super.init();
        inputManager.mapToKey(goForward, Canvas.UP);
        inputManager.mapToKey(goForward, Canvas.KEY_NUM2);
        inputManager.mapToKey(goBackward, Canvas.DOWN);
        inputManager.mapToKey(goBackward, Canvas.KEY_NUM8);
        inputManager.mapToKey(goLeft, Canvas.LEFT);
        inputManager.mapToKey(goLeft, Canvas.KEY_NUM4);
        inputManager.mapToKey(goRight, Canvas.RIGHT);
        inputManager.mapToKey(goRight, Canvas.KEY_NUM6);
        inputManager.mapToKey(goUp, Canvas.GAME_A);
        inputManager.mapToKey(goUp, Canvas.KEY_NUM3);
        inputManager.mapToKey(goDown, Canvas.GAME_B);
        inputManager.mapToKey(goDown, Canvas.KEY_NUM1);
        inputManager.mapToMouse(turnLeft, InputManager.MOUSE_MOVE_LEFT);
        inputManager.mapToMouse(turnRight, InputManager.MOUSE_MOVE_RIGHT);
        inputManager.mapToMouse(tiltUp, InputManager.MOUSE_MOVE_UP);
        inputManager.mapToMouse(tiltDown, InputManager.MOUSE_MOVE_DOWN);
        createPolygonRenderer();
        polygons = new Vector();
        createPolygons();
    }

    public void createPolygons() {
        SolidPolygon3D poly;
        poly = new SolidPolygon3D(new Vector3D(-200, 0, -1000), new Vector3D(200, 0, -1000), new Vector3D(200, 250, -1000), new Vector3D(-200, 250, -1000));
        poly.setColor(Color.WHITE);
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(-200, 0, -1400), new Vector3D(-200, 250, -1400), new Vector3D(200, 250, -1400), new Vector3D(200, 0, -1400));
        poly.setColor(Color.WHITE);
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(-200, 0, -1400), new Vector3D(-200, 0, -1000), new Vector3D(-200, 250, -1000), new Vector3D(-200, 250, -1400));
        poly.setColor(Color.GRAY);
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(200, 0, -1000), new Vector3D(200, 0, -1400), new Vector3D(200, 250, -1400), new Vector3D(200, 250, -1000));
        poly.setColor(Color.GRAY);
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(0, 0, -1000), new Vector3D(75, 0, -1000), new Vector3D(75, 125, -1000), new Vector3D(0, 125, -1000));
        poly.setColor(new Color(0x660000));
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(-150, 150, -1000), new Vector3D(-100, 150, -1000), new Vector3D(-100, 200, -1000), new Vector3D(-150, 200, -1000));
        poly.setColor(new Color(0x660000));
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(-200, 250, -1000), new Vector3D(200, 250, -1000), new Vector3D(75, 400, -1200), new Vector3D(-75, 400, -1200));
        poly.setColor(new Color(0x660000));
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(-200, 250, -1400), new Vector3D(-200, 250, -1000), new Vector3D(-75, 400, -1200));
        poly.setColor(new Color(0x330000));
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(200, 250, -1400), new Vector3D(-200, 250, -1400), new Vector3D(-75, 400, -1200), new Vector3D(75, 400, -1200));
        poly.setColor(new Color(0x660000));
        polygons.addElement(poly);
        poly = new SolidPolygon3D(new Vector3D(200, 250, -1000), new Vector3D(200, 250, -1400), new Vector3D(75, 400, -1200));
        poly.setColor(new Color(0x330000));
        polygons.addElement(poly);
    }

    public void createPolygonRenderer() {
        viewWindow = new ViewWindow(0, 0, screen.getWidth(), screen.getHeight(), (float) Math.toRadians(75));
        Transform3D camera = new Transform3D(0, 100, 0);
        polygonRenderer = new SolidPolygonRenderer(camera, viewWindow);
    }

    /**
	 Sets the view bounds, centering the view on the screen.
	 */
    public void setViewBounds(int width, int height) {
        width = Math.min(width, screen.getWidth());
        height = Math.min(height, screen.getHeight());
        width = Math.max(64, width);
        height = Math.max(48, height);
        viewWindow.setBounds((screen.getWidth() - width) / 2, (screen.getHeight() - height) / 2, width, height);
    }

    public void update(long elapsedTime) {
        if (exit.isPressed()) {
            stop();
            return;
        }
        if (largerView.isPressed()) {
            setViewBounds(viewWindow.getWidth() + 64, viewWindow.getHeight() + 48);
        } else if (smallerView.isPressed()) {
            setViewBounds(viewWindow.getWidth() - 64, viewWindow.getHeight() - 48);
        }
        if (frameRateToggle.isPressed()) {
            drawFrameRate = !drawFrameRate;
        }
        elapsedTime = Math.min(elapsedTime, 100);
        float angleChange = 0.0002f * elapsedTime;
        float distanceChange = .5f * elapsedTime;
        Transform3D camera = polygonRenderer.getCamera();
        Vector3D cameraLoc = camera.getLocation();
        if (goForward.isPressed()) {
            cameraLoc.x -= distanceChange * camera.getSinAngleY();
            cameraLoc.z -= distanceChange * camera.getCosAngleY();
        }
        if (goBackward.isPressed()) {
            cameraLoc.x += distanceChange * camera.getSinAngleY();
            cameraLoc.z += distanceChange * camera.getCosAngleY();
        }
        if (goLeft.isPressed()) {
            cameraLoc.x -= distanceChange * camera.getCosAngleY();
            cameraLoc.z += distanceChange * camera.getSinAngleY();
        }
        if (goRight.isPressed()) {
            cameraLoc.x += distanceChange * camera.getCosAngleY();
            cameraLoc.z -= distanceChange * camera.getSinAngleY();
        }
        if (goUp.isPressed()) {
            cameraLoc.y += distanceChange;
        }
        if (goDown.isPressed()) {
            cameraLoc.y -= distanceChange;
        }
        int tilt = tiltUp.getAmount() - tiltDown.getAmount();
        tilt = Math.min(tilt, 200);
        tilt = Math.max(tilt, -200);
        float newAngleX = camera.getAngleX() + tilt * angleChange;
        newAngleX = Math.max(newAngleX, (float) -Math.PI / 2);
        newAngleX = Math.min(newAngleX, (float) Math.PI / 2);
        camera.setAngleX(newAngleX);
        int turn = turnLeft.getAmount() - turnRight.getAmount();
        turn = Math.min(turn, 200);
        turn = Math.max(turn, -200);
        camera.rotateAngleY(turn * angleChange);
        if (tiltLeft.isPressed()) {
            camera.rotateAngleZ(10 * angleChange);
        }
        if (tiltRight.isPressed()) {
            camera.rotateAngleZ(-10 * angleChange);
        }
    }

    public void draw(Graphics g) {
        polygonRenderer.startFrame(screen);
        for (int i = 0; i < polygons.size(); i++) {
            polygonRenderer.draw(g, (Polygon3D) polygons.elementAt(i));
        }
        polygonRenderer.endFrame(screen);
        drawText(g);
    }

    public void drawText(Graphics g) {
    }

    public void calcFrameRate() {
        numFrames++;
        long currTime = System.currentTimeMillis();
        if (currTime > startTime + 500) {
            frameRate = (float) numFrames * 1000 / (currTime - startTime);
            startTime = currTime;
            numFrames = 0;
        }
    }
}
