package homura.hde.awt;

import homura.hde.app.HDEView;
import homura.hde.core.renderer.Camera;
import homura.hde.core.scene.Node;
import homura.hde.core.scene.state.ZBufferState;
import homura.hde.util.Timer;
import homura.hde.util.colour.ColorRGBA;
import homura.hde.util.maths.Vector3f;

/**
 * <code>SimpleCanvasImpl</code>
 * 
 * @author Joshua Slack
 * @version $Id: SimpleCanvasImpl.java,v 1.8 2007/09/21 15:45:30 nca Exp $
 */
public class SimpleCanvasImpl extends JMECanvasImplementor {

    protected Node rootNode;

    protected Timer timer;

    protected float tpf;

    protected Camera cam;

    protected int width, height;

    /**
     * This class should be subclasses - not directly instantiated.
     * @param width canvas width
     * @param height canvas height
     */
    protected SimpleCanvasImpl(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void doSetup() {
        HDEView display = HDEView.getDisplaySystem();
        display.initForCanvas(width, height);
        renderer = display.getRenderer();
        cam = renderer.createCamera(width, height);
        cam.setFrustumPerspective(45.0f, (float) width / (float) height, 1, 1000);
        Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
        Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
        cam.setFrame(loc, left, up, dir);
        cam.update();
        renderer.setCamera(cam);
        renderer.setBackgroundColor(ColorRGBA.black.clone());
        timer = Timer.getTimer();
        rootNode = new Node("rootNode");
        ZBufferState buf = renderer.createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.CF_LEQUAL);
        rootNode.setRenderState(buf);
        simpleSetup();
        rootNode.updateGeometricState(0.0f, true);
        rootNode.updateRenderState();
        setup = true;
    }

    public void doUpdate() {
        timer.update();
        tpf = timer.getTimePerFrame();
        simpleUpdate();
        rootNode.updateGeometricState(tpf, true);
    }

    public void doRender() {
        renderer.clearBuffers();
        renderer.draw(rootNode);
        simpleRender();
        renderer.displayBackBuffer();
    }

    public void simpleSetup() {
    }

    public void simpleUpdate() {
    }

    public void simpleRender() {
    }

    public Camera getCamera() {
        return cam;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public float getTimePerFrame() {
        return tpf;
    }
}
