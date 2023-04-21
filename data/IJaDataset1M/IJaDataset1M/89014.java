package org.xith3d.test.coloring;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.DeviceComponent;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.Keys;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.vecmath2.Colorf;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
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
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Simple Xith3D Coloring Attributes test." }, authors = { "YVG" })
public class ColoringAttributesTest extends Xith3DTest {

    private Shape3D[] planes = new Shape3D[4];

    private Colorf[] colors = new Colorf[4];

    private int whiteIndex = 0;

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case SPACE:
                planes[whiteIndex].getAppearance().getColoringAttributes().setColor(colors[whiteIndex]);
                whiteIndex = (whiteIndex + 1) % planes.length;
                planes[whiteIndex].getAppearance().getColoringAttributes().setColor(Colorf.WHITE);
                break;
            case ESCAPE:
                this.end();
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
        colors[0] = Colorf.YELLOW;
        rect = new Rectangle(0.6f, 0.6f, Colorf.WHITE);
        StaticTransform.translate(rect, -0.4f, -0.4f, 0.0f);
        app = rect.getAppearance(true);
        app.getColoringAttributes(true).setShadeModel(ColoringAttributes.SHADE_FLAT);
        app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
        scaleTransform.addChild(rect);
        planes[0] = rect;
        colors[1] = Colorf.RED;
        rect = new Rectangle(0.6f, 0.6f, colors[1]);
        StaticTransform.translate(rect, 0.4f, -0.4f, 0.0f);
        app = rect.getAppearance(true);
        app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
        app.getColoringAttributes(true).setShadeModel(ColoringAttributes.SHADE_FLAT);
        scaleTransform.addChild(rect);
        planes[1] = rect;
        colors[2] = Colorf.GREEN;
        rect = new Rectangle(0.6f, 0.6f, colors[2]);
        StaticTransform.translate(rect, -0.4f, 0.4f, 0.0f);
        app = rect.getAppearance(true);
        app.getColoringAttributes(true).setShadeModel(ColoringAttributes.SHADE_FLAT);
        app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
        scaleTransform.addChild(rect);
        planes[2] = rect;
        colors[3] = Colorf.BLUE;
        rect = new Rectangle(0.6f, 0.6f, colors[3]);
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

    public ColoringAttributesTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        createSceneGraph(env);
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        DeviceComponent[] comps = new DeviceComponent[] { Keys.SPACE };
        String[] boundActions = new String[] { "Change the color of the planes" };
        TestUtils.displayInputBindings(canvas, comps, boundActions, env);
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
    }

    public static void main(String[] args) throws Throwable {
        ColoringAttributesTest test = new ColoringAttributesTest(parseCommandLine(args));
        test.begin();
    }
}
