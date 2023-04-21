package org.xith3d.test.shadows;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.FastMath;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.loop.opscheduler.Animator;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.DirectionalLight;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.SceneGraph;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.primitives.Rectangle;
import org.xith3d.schedops.movement.RotatableGroup;
import org.xith3d.schedops.movement.TransformationDirectives;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Simple Xith3D shadows test" }, authors = { "Marvin Froehlich (aka Qudus)" })
public class ShadowsTest extends Xith3DTest {

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
        }
    }

    private void createSceneGraph(SceneGraph sg, Animator animator) {
        BranchGroup scene = new BranchGroup();
        scene.addChild(new DirectionalLight(Colorf.WHITE, Vector3f.POSITIVE_X_AXIS));
        Shape3D shp = new Rectangle(2.0f, 2.0f, "stone.jpg");
        Material mat = new Material();
        mat.setDiffuseColor(Colorf.WHITE);
        mat.setSpecularColor(Colorf.WHITE);
        mat.setLightingEnabled(true);
        shp.setIsOccluder(true);
        shp.getAppearance(true).setMaterial(mat);
        RotatableGroup rotator = new RotatableGroup(new TransformationDirectives(0.0f, 0.5f, 0.0f));
        rotator.addChild(shp);
        scene.addChild(rotator);
        Rectangle target = new Rectangle(0.0f, 5.0f, 5.0f, Colorf.DARK_GRAY);
        StaticTransform.rotate(target, 0.0f, 1.0f, 0.0f, FastMath.toRad(-45.0f));
        StaticTransform.translate(target, 3.0f, 0.0f, -2.0f);
        Material mat2 = new Material();
        mat2.setDiffuseColor(Colorf.DARK_GRAY);
        mat2.setSpecularColor(Colorf.WHITE);
        mat2.setLightingEnabled(true);
        target.getAppearance(true).setMaterial(mat2);
        scene.addChild(target);
        sg.addPerspectiveBranch(scene);
        animator.addAnimatableObject(rotator);
    }

    public ShadowsTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(0f, 0f, 5f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        createSceneGraph(env, this.getAnimator());
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        canvas.setBackgroundColor(Colorf.CYAN);
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
    }

    public static void main(String[] args) throws Throwable {
        ShadowsTest test = new ShadowsTest(parseCommandLine(args));
        test.begin();
    }
}
