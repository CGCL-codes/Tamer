package de.lessvoid.nifty.jme.render;

import java.awt.Canvas;
import java.io.InputStream;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.jme.input.JmeInputSystem;
import de.lessvoid.nifty.jme.sound.JmeSoundDevice;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * <code>NiftyNode</code> must be attached to the jme2 scenegraph. It is responsible for calling nifty.render during its draw call.
 * @author larynx
 *
 */
public class NiftyNode extends Node {

    /**
	 * This is the main entrypoint into NiftyGUI
	 */
    private Nifty nifty;

    private JmeInputSystem inputSystem;

    private static final long serialVersionUID = 2L;

    /**
	 * A <code>NiftyNode</code> encapsulates a Nifty object.
	 * Example: new NiftyNode("GUI:NIFTYNODE", "console/console.xml", "start");  
	 * @param name the node name
	 * @param filename the filename of the nifty xml
	 * @param startscreen the name of the nifty startscreen
	 */
    public NiftyNode(String name, String filename, String startscreen) {
        super(name);
        initialize();
        nifty.fromXml(filename, startscreen);
    }

    public NiftyNode(String name, String filename, String startscreen, ScreenController... screenController) {
        super(name);
        initialize();
        nifty.fromXml(filename, startscreen, screenController);
    }

    public NiftyNode(String name, String filename, InputStream inputFile, String startscreen) {
        super(name);
        initialize();
        nifty.fromXml(filename, inputFile, startscreen);
    }

    public NiftyNode(String name, String filename, InputStream inputFile, String startscreen, ScreenController... screenController) {
        super(name);
        initialize();
        nifty.fromXml(filename, inputFile, startscreen, screenController);
    }

    private void initialize() {
        this.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        this.setLocalTranslation(DisplaySystem.getDisplaySystem().getWidth() / 2.0f, DisplaySystem.getDisplaySystem().getHeight() / 2.0f, 0.0f);
        this.setLightCombineMode(LightCombineMode.Off);
        this.setCullHint(CullHint.Never);
        this.updateRenderState();
        inputSystem = new JmeInputSystem();
        nifty = new Nifty(new JmeRenderDevice(), new JmeSoundDevice(), inputSystem, new TimeProvider());
    }

    /**
     * <code>draw</code> abstract method that handles drawing data to the
     * renderer if it is geometry and passing the call to it's children if it is
     * a node.
     * 
     * @param r the renderer used for display.
     */
    public void draw(Renderer r) {
        nifty.render(false);
    }

    /**
     * Should be called to allow a clean exit
     */
    public void cleanup() {
        nifty.exit();
    }

    public Nifty getNifty() {
        return nifty;
    }

    public void setCanvas(Canvas canvas) {
        inputSystem.setCanvas(canvas);
    }
}
