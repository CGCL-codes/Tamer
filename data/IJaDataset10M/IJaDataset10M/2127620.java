package org.xith3d.test.coloring;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.vecmath2.Colorf;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.InputAdapterRenderLoop;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.Canvas3DFactory;
import org.xith3d.render.config.CanvasConstructionInfo;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.ColoringAttributes;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Cube;
import org.xith3d.scenegraph.primitives.Rectangle;
import org.xith3d.schedops.movement.RotatableGroup;
import org.xith3d.schedops.movement.TransformationDirectives;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;

@Xith3DTest.Description(fulltext = { "imple Xith3D Coloring Attributes change test" }, authors = { "YVG" })
public class ColoringAttributesChangeTest extends InputAdapterRenderLoop implements Xith3DTest {

    private Shape3D[] planes = new Shape3D[4];

    private int redIndex = 0;

    private Xith3DTest.FinishListener finishListener;

    @Override
    protected void exit() {
        if (finishListener != null) finishListener.onTestFinished(); else super.exit();
    }

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
            case SPACE:
                planes[redIndex].getAppearance().getColoringAttributes().setColor(Colorf.WHITE);
                redIndex = (redIndex + 1) % planes.length;
                planes[redIndex].getAppearance().getColoringAttributes().setColor(Colorf.RED);
                break;
        }
    }

    private void createSceneGraph(Xith3DEnvironment env) throws Exception {
        RotatableGroup testRotateYGroup = new RotatableGroup(new TransformationDirectives(0f, 0.1f, 0f));
        TransformGroup scaleTransform = new TransformGroup();
        Transform3D t = new Transform3D();
        t.setIdentity();
        scaleTransform.setTransform(t);
        testRotateYGroup.addChild(scaleTransform);
        Texture texture = TextureLoader.getInstance().getTexture("stone.jpg");
        Rectangle rect;
        Appearance app;
        rect = new Rectangle(0.6f, 0.6f, Colorf.WHITE);
        StaticTransform.translate(rect, -0.4f, -0.4f, 0.0f);
        app = rect.getAppearance(true);
        app.getColoringAttributes(true).setShadeModel(ColoringAttributes.SHADE_FLAT);
        app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
        scaleTransform.addChild(rect);
        planes[0] = rect;
        rect = new Rectangle(0.6f, 0.6f, Colorf.RED);
        StaticTransform.translate(rect, 0.4f, -0.4f, 0.0f);
        app = rect.getAppearance(true);
        app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
        app.getColoringAttributes(true).setShadeModel(ColoringAttributes.SHADE_FLAT);
        scaleTransform.addChild(rect);
        planes[1] = rect;
        rect = new Rectangle(0.6f, 0.6f, Colorf.GREEN);
        StaticTransform.translate(rect, -0.4f, 0.4f, 0.0f);
        app = rect.getAppearance(true);
        app.getColoringAttributes(true).setShadeModel(ColoringAttributes.SHADE_FLAT);
        app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
        scaleTransform.addChild(rect);
        planes[2] = rect;
        rect = new Rectangle(0.6f, 0.6f, Colorf.BLUE);
        StaticTransform.translate(rect, 0.4f, 0.4f, 0.0f);
        app = rect.getAppearance(true);
        app.getColoringAttributes(true).setShadeModel(ColoringAttributes.SHADE_FLAT);
        app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
        scaleTransform.addChild(rect);
        planes[3] = rect;
        Appearance a = new Appearance();
        a.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK));
        a.setTexture(texture);
        Cube cube = new Cube(0.8f, a);
        StaticTransform.translate(cube, 0.0f, 0.0f, -0.6f);
        scaleTransform.addChild(cube);
        env.addPerspectiveBranch().getBranchGroup().addChild(testRotateYGroup);
        this.getAnimator().addAnimatableObject(testRotateYGroup);
    }

    public ColoringAttributesChangeTest(CanvasConstructionInfo canvasInfo, boolean mouseYInverted, Xith3DTest.FinishListener finishListener) throws Exception {
        super(120f);
        Xith3DEnvironment env = new Xith3DEnvironment(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 3f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        createSceneGraph(env);
        Canvas3D canvas = Canvas3DFactory.create(canvasInfo);
        env.addCanvas(canvas);
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        this.finishListener = finishListener;
        this.begin();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hit SPACE to change color of planes, or ESC to exit");
        new ColoringAttributesChangeTest(new CanvasConstructionInfo(Xith3DTest.DEFAULT_DISPLAY_MODE, Xith3DTest.DEFAULT_FULLSCREEN, "ColoringAttributesChangeTest"), true, null);
    }
}
