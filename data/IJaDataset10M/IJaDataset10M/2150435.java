package org.xith3d.test.render;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.TexCoord2f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.BackgroundRenderPass;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.ForegroundRenderPass;
import org.xith3d.render.RenderPass;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.RenderingAttributes;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.TransparencyAttributes;
import org.xith3d.scenegraph.TriangleArray;
import org.xith3d.scenegraph.TriangleStripArray;
import org.xith3d.scenegraph.View.CameraMode;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Test of the Xith3D Background Geom capabilities (basic skybox)" }, authors = { "William Denniss" })
public class BackgroundGeomTest extends Xith3DTest {

    private Xith3DEnvironment env;

    private int x = 10;

    private int viewmode = 1;

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
            case _0:
                viewmode = 0;
                break;
            case _1:
                viewmode = 1;
                break;
            case _2:
                viewmode = 2;
                break;
            case _3:
                viewmode = 3;
                break;
            case _4:
                viewmode = 4;
                break;
            case Q:
                x--;
                break;
            case A:
                x++;
                break;
        }
    }

    @Override
    public void onRenderLoopStarted() {
        System.out.println("Keys 0 to 4 change the camera.  If camera 1 is selected, 'q' and 'a' can be used to change the x-coordinate of the camera location");
    }

    @Override
    public void onRenderLoopStopped(long gameTime, TimingMode timingMode, float averageFPS) {
        System.out.println("done frame speed test at " + (int) averageFPS + " fps");
        System.out.println("there are " + env.getCanvas().getPeer().getTriangles() + " triangles in scene");
        System.out.println("rendering " + env.getCanvas().getPeer().getTriangles() * averageFPS + " triangles/sec");
        System.out.println("   Num frames = " + getIterationsCount());
        System.out.println("   Delta ms = " + timingMode.getMilliSeconds(gameTime));
    }

    @Override
    protected void prepareNextFrame(long gameTime, long frameTime, TimingMode timingMode) {
        super.prepareNextFrame(gameTime, frameTime, timingMode);
        if (viewmode == 0) env.getView().getTransform().lookAt(new Vector3f(5f, 0f, 5f), new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f)); else if (viewmode == 1) env.getView().getTransform().lookAt(new Vector3f(x, 0f, 3f), new Vector3f(0f, -5f, 0f), new Vector3f(0f, 1f, 0f)); else if (viewmode == 2) env.getView().getTransform().lookAt(new Vector3f(2f, 0f, 2f), new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f)); else if (viewmode == 3) env.getView().getTransform().lookAt(new Vector3f(-1f, 0f, 2f), new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f)); else if (viewmode == 4) env.getView().getTransform().lookAt(new Vector3f(50f, 0f, 2f), new Vector3f(1f, 4f, 0f), new Vector3f(0f, 1f, 0f));
    }

    private void init(Xith3DEnvironment env) {
        BranchGroup b = env.addRenderPass(BackgroundRenderPass.createPerspective(CameraMode.VIEW_FIXED_POSITION));
        BranchGroup m = env.addRenderPass(RenderPass.createPerspective());
        BranchGroup f = env.addRenderPass(ForegroundRenderPass.createPerspective(CameraMode.VIEW_FIXED));
        {
            Shape3D sh = new Shape3D();
            Point3f[] pts = new Point3f[5];
            pts[0] = new Point3f(-100f, -100f, 100f);
            pts[1] = new Point3f(-100f, -100f, -100f);
            pts[2] = new Point3f(100f, -100f, -100f);
            pts[3] = new Point3f(100f, -100f, 100f);
            pts[4] = new Point3f(0f, 100f, 0f);
            Point3f[] coords = new Point3f[] { pts[0], pts[2], pts[1], pts[0], pts[3], pts[2], pts[0], pts[4], pts[1], pts[1], pts[4], pts[2], pts[2], pts[4], pts[3], pts[3], pts[4], pts[0] };
            TexCoord2f[] texCoords = new TexCoord2f[] { new TexCoord2f(0f, 0f), new TexCoord2f(1f, 0f), new TexCoord2f(1f, 1f), new TexCoord2f(1f, 1f), new TexCoord2f(0f, 1f), new TexCoord2f(0f, 0f), new TexCoord2f(0f, 0f), new TexCoord2f(1f, 0f), new TexCoord2f(1f, 1f), new TexCoord2f(1f, 1f), new TexCoord2f(0f, 1f), new TexCoord2f(0f, 0f), new TexCoord2f(0f, 0f), new TexCoord2f(1f, 0f), new TexCoord2f(1f, 1f), new TexCoord2f(1f, 1f), new TexCoord2f(0f, 1f), new TexCoord2f(0f, 0f) };
            TriangleArray ta = new TriangleArray(coords.length);
            ta.setCoordinates(0, coords);
            ta.setTextureCoordinates(0, 0, texCoords);
            ta.calculateFaceNormals();
            sh.setGeometry(ta);
            Texture texture = TextureLoader.getInstance().getTexture("stone.jpg");
            Appearance a = new Appearance();
            a.setRenderingAttributes(new RenderingAttributes());
            a.getRenderingAttributes().setDepthBufferWriteEnabled(false);
            a.setTexture(texture);
            sh.setAppearance(a);
            b.addChild(sh);
        }
        {
            Shape3D sh = new Shape3D();
            Point3f[] pts = new Point3f[5];
            pts[0] = new Point3f(-1f, -1f, 1f);
            pts[1] = new Point3f(-1f, -1f, -1f);
            pts[2] = new Point3f(1f, -1f, -1f);
            pts[3] = new Point3f(1f, -1f, 1f);
            pts[4] = new Point3f(1f, 1f, 1f);
            Point3f[] coords = new Point3f[] { pts[0], pts[2], pts[1], pts[0], pts[3], pts[2], pts[0], pts[4], pts[1], pts[1], pts[4], pts[2], pts[2], pts[4], pts[3], pts[3], pts[4], pts[0] };
            Colorf[] colors = new Colorf[] { new Colorf(1f, 1f, 1f), new Colorf(1f, 0f, 0f), new Colorf(0f, 1f, 0f), new Colorf(0f, 1f, 0f), new Colorf(0f, 0f, 1f), new Colorf(1f, 1f, 1f), new Colorf(1f, 1f, 1f), new Colorf(1f, 0f, 0f), new Colorf(0f, 1f, 0f), new Colorf(0f, 1f, 0f), new Colorf(0f, 0f, 1f), new Colorf(1f, 1f, 1f), new Colorf(1f, 1f, 1f), new Colorf(1f, 0f, 0f), new Colorf(0f, 1f, 0f), new Colorf(0f, 1f, 0f), new Colorf(0f, 0f, 1f), new Colorf(1f, 1f, 1f) };
            TriangleArray ta = new TriangleArray(coords.length);
            ta.setColors(0, colors);
            ta.setCoordinates(0, coords);
            ta.calculateFaceNormals();
            sh.setGeometry(ta);
            Appearance a = new Appearance();
            sh.setAppearance(a);
            m.addChild(sh);
        }
        {
            float depth = -2.5f, width = 0.06f, height = 1f, x = 2f, y = 0.3f;
            TriangleStripArray geometry = new TriangleStripArray(4);
            Appearance appearance = new Appearance();
            TexCoord2f textureCoordinates[] = { new TexCoord2f(0, 0.35f), new TexCoord2f(0.04f, 0.35f), new TexCoord2f(0, 1f), new TexCoord2f(0.04f, 1f) };
            Point3f vertices[] = { new Point3f(-width + x, -height + y, depth), new Point3f(width + x, -height + y, depth), new Point3f(-width + x, height + y, depth), new Point3f(width + x, height + y, depth) };
            appearance.setTexture("hud.png");
            appearance.setRenderingAttributes(new RenderingAttributes());
            appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.1f));
            geometry.setCoordinates(0, vertices);
            geometry.setTextureCoordinates(0, 0, textureCoordinates);
            f.addChild(new Shape3D(geometry, appearance));
        }
        env.addPerspectiveBranch(m);
    }

    public BackgroundGeomTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        env = new Xith3DEnvironment(1f, 0f, 2f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        init(env);
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
    }

    public static void main(String[] args) throws Throwable {
        BackgroundGeomTest test = new BackgroundGeomTest(parseCommandLine(args));
        test.begin();
    }
}
