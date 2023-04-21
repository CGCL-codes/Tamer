package org.interspace.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.interspace.datamodel.generator.DefaultGalaxyGenerator;
import org.interspace.datamodel.stellar.Galaxy;
import org.interspace.datamodel.stellar.StarSystem;
import org.interspace.gui.shape.OrtedShape;
import org.interspace.gui.shape.StarConnection;
import org.interspace.gui.shape.OrtedShape.OrtType;
import com.jme.app.AbstractGame;
import com.jme.app.BaseSimpleGame;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.shape.Sphere;
import com.jme.util.geom.Debugger;

public class GuiContext extends AbstractGuiContext {

    private static GuiContext instance;

    private boolean drawOrts;

    private boolean drawGrid;

    private boolean drawConnections;

    public static GuiContext getInstance() {
        if (instance == null) {
            instance = new GuiContext();
        }
        return GuiContext.instance;
    }

    /**
     * Called every frame to update scene information.
     * 
     * @param interpolation
     *            unused in this implementation
     * @see BaseSimpleGame#update(float interpolation)
     */
    protected final void update(float interpolation) {
        super.update(interpolation);
        if (!pause) {
            simpleUpdate();
            rootNode.updateGeometricState(tpf, true);
            statNode.updateGeometricState(tpf, true);
        }
    }

    /**
     * This is called every frame in BaseGame.start(), after update()
     * 
     * @param interpolation
     *            unused in this implementation
     * @see AbstractGame#render(float interpolation)
     */
    protected final void render(float interpolation) {
        super.render(interpolation);
        Renderer r = display.getRenderer();
        r.draw(rootNode);
        simpleRender();
        r.draw(statNode);
        doDebug(r);
    }

    @Override
    protected void doDebug(Renderer r) {
        super.doDebug(r);
        if (showDepth) {
            r.renderQueue();
            Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST, r);
        }
    }

    @Override
    protected void simpleInitGame() {
        addXZGrid();
        DefaultGalaxyGenerator generator = new DefaultGalaxyGenerator(20, 20, 20);
        Galaxy galaxy = generator.generate();
        Iterator<StarSystem> it = galaxy.getSystems().iterator();
        while (it.hasNext()) {
            Sphere sphere = new Sphere("sphere", 30, 30, 0.25f);
            StarSystem system = it.next();
            sphere.updateGeometry(new Vector3f(system.getX(), system.getY(), system.getZ()), sphere.getZSamples(), sphere.getRadialSamples(), sphere.getRadius());
            OrtedShape shape = new OrtedShape(sphere, sphere.getCenter(), OrtType.XZ_ORT);
            rootNode.attachChild(shape);
        }
    }

    private void addXZGrid() {
        ColorRGBA[] color = new ColorRGBA[2];
        color[0] = new ColorRGBA(1f, 1f, 0, 1f);
        color[1] = new ColorRGBA(1f, 1f, 0, 1f);
        for (int i = 0; i < 101; i++) {
            Vector3f[] v1 = new Vector3f[2];
            v1[0] = new Vector3f(i, 0, 0);
            v1[1] = new Vector3f(i, 0, 100);
            Line l1 = new Line("Line1", v1, null, color, null);
            rootNode.attachChild(l1);
        }
        for (int i = 0; i < 101; i++) {
            Vector3f[] v1 = new Vector3f[2];
            v1[0] = new Vector3f(0, 0, i);
            v1[1] = new Vector3f(100, 0, i);
            Line l1 = new Line("Line1", v1, null, color, null);
            rootNode.attachChild(l1);
        }
    }

    @Override
    public void registerKeyBindings() {
        super.registerKeyBindings();
        KeyBindingManager.getKeyBindingManager().set(KeyBinderCommandConstants.TOGGLE_ORTS, KeyInput.KEY_O);
        KeyBindingManager.getKeyBindingManager().set(KeyBinderCommandConstants.TOGGLE_GRID, KeyInput.KEY_O);
    }

    @Override
    public void checkKeyBindings() {
        super.checkKeyBindings();
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(KeyBinderCommandConstants.TOGGLE_GRID, false)) {
            drawGrid = !drawGrid;
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(KeyBinderCommandConstants.TOGGLE_ORTS, false)) {
            drawOrts = !drawOrts;
        }
    }

    public void setDrawOrts(boolean drawOrts) {
        this.drawOrts = drawOrts;
    }

    public boolean isDrawOrts() {
        return drawOrts;
    }
}
