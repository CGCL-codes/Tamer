package org.xith3d.test.picking;

import java.util.List;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.MouseButton;
import org.jagatoo.input.devices.components.MouseButtons;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.input.events.MouseButtonPressedEvent;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.picking.PickListener;
import org.xith3d.picking.PickResult;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.GroupNode;
import org.xith3d.scenegraph.View;
import org.xith3d.scenegraph.View.ProjectionPolicy;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Simple Xith3D PickRender test for OpenGL's GL_SELECT picking." }, authors = { "Yuri Vl. Gushchin (aka YVG)", "Marvin Froehlich (aka Qudus)", "Amos Wenger (aka BlueSky)", "Marvin Froehlich (aka Qudus)" })
public class GLSelectPickingViewTest extends Xith3DTest implements PickListener {

    private Canvas3D canvas;

    private View view;

    private GroupNode pickGroup;

    @Override
    public void onKeyPressed(KeyPressedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
            case SPACE:
                if (view.getProjectionPolicy() == ProjectionPolicy.PERSPECTIVE_PROJECTION) {
                    view.setProjectionPolicy(ProjectionPolicy.PARALLEL_PROJECTION);
                } else {
                    view.setProjectionPolicy(ProjectionPolicy.PERSPECTIVE_PROJECTION);
                }
                break;
        }
    }

    @Override
    public void onMouseButtonPressed(MouseButtonPressedEvent e, MouseButton button) {
        System.out.println("Picking at " + e.getX() + ", " + e.getY());
        if (button == MouseButtons.LEFT_BUTTON) canvas.pickNearest(pickGroup, e.getButton(), e.getX(), e.getY(), this); else if (button == MouseButtons.RIGHT_BUTTON) canvas.pickAll(pickGroup, e.getButton(), e.getX(), e.getY(), this);
    }

    public boolean testIntersectionsInWorldSpaceForPicking() {
        return (false);
    }

    public void onObjectPicked(PickResult nearest, Object userObject, long pickTime) {
        System.out.println("  Hit Shape3D: \"" + nearest.getNode().getName() + "\", distance=" + nearest.getMinimumDistance() + " position=" + nearest.getPos());
    }

    public void onObjectsPicked(List<PickResult> results, Object userObject, long pickTime) {
        System.out.println("Detected " + results.size() + " hits");
        int i = 0;
        for (PickResult pr : results) {
            System.out.println("  Hit (" + i++ + "): Shape3D: \"" + pr.getNode().getName() + "\", distance=" + pr.getMinimumDistance() + " position=" + pr.getPos());
        }
    }

    public void onPickingMissed(Object userObject, long pickTime) {
        System.out.println("Picking missed");
    }

    public GLSelectPickingViewTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        this.view = env.getView();
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        BranchGroup scene = PickingTestUtils.createSceneGraph(this.getAnimator());
        this.pickGroup = scene;
        env.addPerspectiveBranch(scene);
        this.canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        System.out.println("Hit ESC to exit, SPACE to toggle projection policy, click mouse on object to perform pick");
        System.out.println("Note that due to Linux workarounds planes with CULL_NONE are pickable only from one (face) side...");
    }

    public static void main(String[] args) throws Throwable {
        GLSelectPickingViewTest test = new GLSelectPickingViewTest(parseCommandLine(args));
        test.begin();
    }
}
