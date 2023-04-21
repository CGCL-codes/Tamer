package xutils.geomview;

import xmage.math.Point3d;
import xmage.turbine.AABB;
import xmage.turbine.Camera;
import xmage.turbine.Light;
import xmage.turbine.Node;
import xmage.turbine.TriMeshVertAnim;
import xmage.turbine.render.CanvasFactory;
import xmage.turbine.render.IRenderingCanvas;
import xmage.turbine.render.RenderConfig;
import huf.data.WBoolean;
import huf.data.WDouble;
import huf.data.Wrapper;
import huf.log.Log;
import huf.misc.HMath;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/**
 * This is a base class for geometry viewers.
 */
public class BaseGeomViewer {

    private final IRenderingCanvas canvas;

    private final Wrapper<Node> root = new Wrapper<Node>();

    public BaseGeomViewer(int canvasWidth, int canvasHeight) {
        this(canvasWidth, canvasHeight, 5, 12.0f);
    }

    public BaseGeomViewer(int canvasWidth, int canvasHeight, final int frameDelay, float animFps) {
        canvas = CanvasFactory.create(canvasWidth, canvasHeight);
        final IRenderingCanvas fCanvas = canvas;
        this.frameDelay = frameDelay;
        setAnimFps(animFps);
        canvas.getRenderer().getConfig().setFlag(RenderConfig.BACKFACE_CULLING, false);
        final JFrame frame = new JFrame(this.getClass().getName());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(canvas.getAsComponent(), BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton help = new JButton("Help");
        help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JOptionPane.showMessageDialog(frame, "Zoom in/out: roll mouse wheel or drag mouse up/down with left button pressed\n" + "Rotate horizontally: drag mouse left-right with left button pressed\n" + "Rotate vertically: drag drag mouse up/down with left button and Control key pressed\n" + "Move horizontally: drag mouse with right button pressed\n", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttons.add(help);
        JButton bWireframe = new JButton("Toggle wireframe");
        bWireframe.addActionListener(new ActionListener() {

            private boolean wireframe = false;

            public void actionPerformed(ActionEvent evt) {
                wireframe = !wireframe;
                fCanvas.getRenderer().getConfig().setFlag(RenderConfig.WIREFRAME, wireframe);
                fCanvas.getAsComponent().repaint();
            }
        });
        buttons.add(bWireframe);
        JButton bQuit = new JButton("Quit");
        bQuit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        buttons.add(bQuit);
        panel.add(buttons, BorderLayout.SOUTH);
        final WBoolean autoRefresh = new WBoolean(true);
        final Wrapper<Node> fRoot = root;
        final WDouble fAngleX = angleX;
        final WDouble fAngleY = angleY;
        MouseInputAdapter mouseInputAdapter = new MouseInputAdapter() {

            private int baseX = 0;

            private int baseY = 0;

            @Override
            public void mousePressed(MouseEvent evt) {
                autoRefresh.val = false;
                baseX = evt.getX();
                baseY = evt.getY();
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                autoRefresh.val = true;
            }

            @Override
            public void mouseDragged(MouseEvent evt) {
                int dx = evt.getX() - baseX;
                int dy = evt.getY() - baseY;
                baseX = evt.getX();
                baseY = evt.getY();
                if ((evt.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                    if ((evt.getModifiers() & MouseEvent.CTRL_MASK) != 0) {
                        fAngleX.val -= dy * 0.01;
                        fRoot.val.getRotation().setToEuler(fAngleX.val, fAngleY.val, 0.0);
                    } else {
                        if (Math.abs(dx) > Math.abs(dy)) {
                            fAngleY.val -= dx * 0.01;
                            fRoot.val.getRotation().setToEuler(fAngleX.val, fAngleY.val, 0.0);
                        } else {
                            setCameraFov(getCameraFov() + (dy * 0.1));
                        }
                    }
                } else if ((evt.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                    fRoot.val.getTranslation().add(dx / 10.0, 0.0, dy / 10.0);
                }
                fCanvas.repaint();
            }
        };
        canvas.getAsComponent().addMouseListener(mouseInputAdapter);
        canvas.getAsComponent().addMouseMotionListener(mouseInputAdapter);
        panel.addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent evt) {
                double delta = (evt.getScrollAmount()) * 0.5;
                if (evt.getWheelRotation() < 0) {
                    delta = -delta;
                }
                setCameraFov(getCameraFov() + delta);
            }
        });
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animThread = new AnimThread(this, autoRefresh);
        frame.pack();
        frame.setVisible(true);
    }

    private final AnimThread animThread;

    private static final class AnimThread extends Thread {

        private final BaseGeomViewer viewer;

        AnimThread(BaseGeomViewer viewer, WBoolean autoRefresh) {
            this.viewer = viewer;
            this.autoRefresh = autoRefresh;
        }

        private final WBoolean autoRefresh;

        @Override
        public void run() {
            while (true) {
                if (autoRefresh.val) {
                    viewer.nextFrame();
                }
                try {
                    Thread.sleep(viewer.getFrameDelay());
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public void start() {
        lastFrameTime = System.currentTimeMillis();
        animThread.start();
    }

    private final int frameDelay;

    public int getFrameDelay() {
        return frameDelay;
    }

    /** Camera positions. */
    public enum CameraMode {

        /** Camera views scene from angle. */
        ANGLE, /** Camera views scene from top. */
        TOP
    }

    private TriMeshVertAnim animScene = null;

    public void setScene(final Node scene) {
        setScene(scene, CameraMode.ANGLE);
    }

    public void setScene(Node scene, CameraMode camMode) {
        assert scene != null : "failed assert scene != null";
        if (scene instanceof TriMeshVertAnim) {
            animScene = (TriMeshVertAnim) scene;
        }
        root.val = scene;
        Node superRoot = new Node();
        superRoot.add(root.val);
        canvas.getRenderer().setRoot(superRoot);
        AABB sceneBox = new AABB(scene);
        Log.log("Scene bounding box: " + sceneBox);
        root.val.getOrigin().set(new Point3d(sceneBox.getMin()).add(sceneBox.getMax()).div(2.0));
        setUpLights(sceneBox);
        switch(camMode) {
            case ANGLE:
                setCameraFromAngle(sceneBox);
                break;
            case TOP:
                setCameraFromTop(sceneBox);
                break;
        }
    }

    private void setUpLights(AABB sceneBox) {
        if (globalLighting) {
            canvas.getRenderer().setAmbientLightColor(1.0f, 1.0f, 1.0f);
        } else {
            Light frontLeftLight = new Light();
            frontLeftLight.setPosition(sceneBox.getMin().x, sceneBox.getMax().y, sceneBox.getMax().z);
            frontLeftLight.setColor(1.0f, 1.0f, 1.0f);
            canvas.getRenderer().getRoot().add(frontLeftLight);
            canvas.getRenderer().addLight(frontLeftLight);
            Light frontRightLight = new Light();
            frontRightLight.setPosition(sceneBox.getMax().x, sceneBox.getMax().y, sceneBox.getMax().z);
            frontRightLight.setColor(1.0f, 1.0f, 1.0f);
            canvas.getRenderer().getRoot().add(frontRightLight);
            canvas.getRenderer().addLight(frontRightLight);
            canvas.getRenderer().setAmbientLightColor(0.1f, 0.1f, 0.1f);
        }
    }

    /**
	 * Set camera to view scene from angle.
	 *
	 * @param sceneBox scene bounding box
	 */
    private void setCameraFromAngle(AABB sceneBox) {
        double objMaxSize = sceneBox.getMin().distance(sceneBox.getMax());
        Log.log("axis size: " + objMaxSize);
        cameraFov.val = 60.0;
        double dist = objMaxSize / Math.tan(cameraFov.val * HMath.DEGTORAD / 2.0) / 2.0;
        Log.log("dist: " + dist);
        double farClip = dist * 5.0;
        Log.log("farClip: " + farClip);
        Camera camera = canvas.getRenderer().getCamera();
        camera.setType(Camera.PERSPECTIVE);
        camera.setNear(0.1);
        camera.getTranslation().set(dist, dist, dist);
        camera.setTarget(0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        camera.setFar(farClip);
        camera.setFov(cameraFov.val);
    }

    /**
	 * Set camera to view scene from the top.
	 *
	 * @param sceneBox scene bounding box
	 */
    private void setCameraFromTop(AABB sceneBox) {
        double objMaxSize = sceneBox.getMin().distance(sceneBox.getMax());
        Log.log("axis size: " + objMaxSize);
        cameraFov.val = 60.0;
        double dist = objMaxSize / Math.tan(cameraFov.val * HMath.DEGTORAD / 2.0);
        Log.log("dist: " + dist);
        double farClip = dist * 5.0;
        Log.log("farClip: " + farClip);
        Point3d center = new Point3d(sceneBox.getMin()).add(sceneBox.getMax()).div(2.0);
        Camera camera = canvas.getRenderer().getCamera();
        camera.setType(Camera.PERSPECTIVE);
        camera.setNear(0.1);
        camera.getTranslation().set(center.x, center.y + dist, center.z);
        camera.setTarget(0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        camera.setFar(farClip);
        camera.setFov(cameraFov.val);
    }

    /** Delay between animation frames in miliseconds. */
    private float animFrameDelay = 0.0f;

    /**
	 * Set animation playing speed.
	 *
	 * @param animFps animation playing speed (in frames per second)
	 */
    public void setAnimFps(float animFps) {
        if (animFps < 0.0f || animFps > 100.0f) {
            throw new IllegalArgumentException("Invalid animation speed: " + animFps + " FPS");
        }
        animFrameDelay = 1000.0f / animFps;
    }

    public interface IFrameListener {

        void nextFrame();
    }

    private IFrameListener listener = null;

    public void setFrameListener(IFrameListener listener) {
        this.listener = listener;
    }

    private final WDouble angleX = new WDouble();

    private final WDouble angleY = new WDouble();

    private long lastFrameTime = 0;

    private long now = 0;

    private int beforeFrame = 0;

    private int lastFrame = 1;

    private int nextFrame = 2;

    private int afterFrame = 3;

    /**
	 * Display next frame.
	 */
    void nextFrame() {
        if (listener != null) {
            listener.nextFrame();
        }
        if (animScene != null) {
            now = System.currentTimeMillis();
            float where = (now - lastFrameTime) / animFrameDelay;
            if (where >= 1.0) {
                beforeFrame++;
                if (beforeFrame >= animScene.getNumFrames()) {
                    beforeFrame = 0;
                }
                lastFrame++;
                if (lastFrame >= animScene.getNumFrames()) {
                    lastFrame = 0;
                }
                nextFrame++;
                if (nextFrame >= animScene.getNumFrames()) {
                    nextFrame = 0;
                }
                afterFrame++;
                if (afterFrame >= animScene.getNumFrames()) {
                    afterFrame = 0;
                }
                where -= 1.0;
                assert where >= 0.0 && where <= 1.0 : "assert where >= 0.0 && where <= 1.0: " + where;
                lastFrameTime = now;
            }
            animScene.setActiveFrame(beforeFrame, lastFrame, nextFrame, afterFrame, where);
        }
        if (autoRotation.val) {
            angleY.val += autoRotationSpeed;
            root.val.getRotation().setToEuler(angleX.val, angleY.val, 0.0);
        }
        canvas.repaint();
    }

    private double autoRotationSpeed = 0.005;

    /**
	 * Set speed of automatic rotation.
	 *
	 * <p>
	 * Valid value range is <code>0.001 - 0.1</code>
	 * </p>
	 *
	 * @param autoRotationSpeed auto rotation speed
	 */
    public void setAutoRotationSpeed(double autoRotationSpeed) {
        this.autoRotationSpeed = autoRotationSpeed;
    }

    private final WBoolean autoRotation = new WBoolean(true);

    /**
	 * Enable or disable automatic rotation.
	 *
	 * @param autoRotation enable or disable automatic rotation
	 */
    public void setAutoRotation(boolean autoRotation) {
        this.autoRotation.val = autoRotation;
    }

    /** Current camera field of view. */
    private WDouble cameraFov = new WDouble();

    /**
	 * Set camera field of view.
	 *
	 * @param fov camera field of view
	 */
    public void setCameraFov(double fov) {
        if (fov < 3.0) {
            cameraFov.val = 3.0;
        } else if (fov > 70.0) {
            cameraFov.val = 70.0;
        } else {
            cameraFov.val = fov;
        }
        canvas.getRenderer().getCamera().setFov(cameraFov.val);
        canvas.getAsComponent().repaint();
    }

    /**
	 * Return camera field of view.
	 *
	 * @return camera field of view
	 */
    public double getCameraFov() {
        return cameraFov.val;
    }

    private boolean globalLighting = false;

    public void setGlobalLighting(boolean globalLighting) {
        this.globalLighting = globalLighting;
    }
}
