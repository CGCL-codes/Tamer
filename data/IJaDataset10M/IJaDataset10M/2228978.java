package it.tukano.jps.engines.ardor3d1;

import com.ardor3d.framework.Scene;
import com.ardor3d.framework.Updater;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.math.Ray3;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.state.ZBufferState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.Timer;

/**
 * The scene implementation used by Canvas3D. I'm not really sure if this id
 * a correct implementation...
 */
public class Canvas3DScene implements Scene, Updater {

    private final Timer timer = new Timer();

    private final Node ROOT = new Node("root");

    private final LogicalLayer logicalLayer = new LogicalLayer();

    private volatile boolean exit = false;

    /**
     * Default no arg constructor
     */
    public Canvas3DScene() {
    }

    /**
     * Sets the renderUndo method to return false. Maybe.
     */
    public void shutdown() {
        exit = true;
    }

    /**
     * Renders the root node if it hasn't been shutdown.
     * @param rndr the... renderer?
     * @return false if shutdown has been called. true otherwise.
     */
    public boolean renderUnto(Renderer rndr) {
        if (exit) {
            return false;
        } else {
            rndr.draw(ROOT);
            return true;
        }
    }

    /**
     * Not implemented.
     * @param ray3 the ray to use for picking
     * @return always null.
     */
    public PickResults doPick(Ray3 ray3) {
        return null;
    }

    /**
     * Initializes the scene.
     */
    public void init() {
        final ZBufferState buf = new ZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        ROOT.setRenderState(buf);
    }

    /**
     * Updates the scene.
     * @param rot
     */
    public void update(ReadOnlyTimer rot) {
        logicalLayer.checkTriggers(rot.getTimePerFrame());
        ROOT.updateGeometricState(rot.getTimePerFrame(), true);
    }
}
