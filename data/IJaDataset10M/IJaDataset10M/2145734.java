package org.xith3d.xin;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.types.twodee.Sized2iRO;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.loop.InputAdapterRenderLoop;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.Canvas3DFactory;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Cube;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.base.AbstractButton;
import org.xith3d.ui.hud.listeners.ButtonListener;
import org.xith3d.ui.hud.widgets.Button;

/**
 * XIN - HUD example coding.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Chapter15a extends InputAdapterRenderLoop implements ButtonListener {

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
        }
    }

    public void onButtonClicked(AbstractButton button, Object userObject) {
        if (userObject.equals("EXIT_BUTTON")) {
            this.end();
        }
    }

    private BranchGroup createMainScene() {
        Cube cube = new Cube(3.0f, "stone.jpg");
        TransformGroup tg = new TransformGroup();
        tg.getTransform().rotY((float) Math.toRadians(45.0));
        tg.addChild(cube);
        return (new BranchGroup(tg));
    }

    private HUD createHUD(Sized2iRO canvasSize) {
        HUD hud = new HUD(canvasSize, 800f);
        Button button = new Button(200f, 60f, "Click me to exit");
        button.setUserObject("EXIT_BUTTON");
        button.addButtonListener(this);
        hud.getContentPane().addWidgetCentered(button);
        return (hud);
    }

    public Chapter15a() throws Exception {
        super(120f);
        Xith3DEnvironment env = new Xith3DEnvironment(this);
        Canvas3D canvas = Canvas3DFactory.createWindowed(800, 600, "HUD example");
        env.addCanvas(canvas);
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        ResourceLocator resLoc = ResourceLocator.create("test-resources/");
        resLoc.createAndAddTSL("textures");
        env.addPerspectiveBranch(createMainScene());
        env.addHUD(createHUD(canvas));
    }

    public static void main(String[] args) throws Exception {
        Chapter15a rl = new Chapter15a();
        rl.begin();
    }
}
