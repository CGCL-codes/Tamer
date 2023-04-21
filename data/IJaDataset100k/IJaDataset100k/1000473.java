package org.xith3d.test.coloring;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;
import net.jtank.input.KeyCode;
import org.xith3d.render.base.Xith3DEnvironment;
import org.xith3d.render.canvas.Canvas3DWrapper;
import org.xith3d.render.canvas.CanvasConstructionInfo;
import org.xith3d.render.loop.ExtRenderLoop;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.resources.ResourceLocator;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.Background;
import org.xith3d.scenegraph.Group;
import org.xith3d.scenegraph.Foreground;
import org.xith3d.scenegraph.GeometryArray;
import org.xith3d.scenegraph.RenderingAttributes;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.Texture2D;
import org.xith3d.scenegraph.TransparencyAttributes;
import org.xith3d.scenegraph.TriangleArray;
import org.xith3d.scenegraph.TriangleStripArray;
import org.xith3d.scenegraph.View.CameraMode;

@Xith3DTest.Description(fulltext = { "Test of the Xith3D Background Geom capabilities (basic skybox)" }, authors = { "William Denniss", "Marvin Froehlich (aka Qudus) [code streamlining]" })
public class BackgroundGeomTest extends ExtRenderLoop implements Xith3DTest {

    private Xith3DEnvironment env;

    private long numFrames;

    private int x = 10;

    private int viewmode = 1;

    private Xith3DTest.FinishListener finishListener;

    @Override
    protected void exit() {
        if (finishListener != null) finishListener.onTestFinished(); else super.exit();
    }

    @Override
    public void onKeyReleased(int key) {
        switch(key) {
            case KeyCode.VK_ESCAPE:
                this.end();
                break;
            case KeyCode.VK_0:
                viewmode = 0;
                break;
            case KeyCode.VK_1:
                viewmode = 1;
                break;
            case KeyCode.VK_2:
                viewmode = 2;
                break;
            case KeyCode.VK_3:
                viewmode = 3;
                break;
            case KeyCode.VK_4:
                viewmode = 4;
                break;
            case KeyCode.VK_Q:
                x--;
                break;
            case KeyCode.VK_A:
                x++;
                break;
        }
    }

    @Override
    public void onRenderLoopStarted() {
        System.out.println("Keys 0 to 4 change the camera.  If camera 1 is selected, 'q' and 'a' can be used to change the x-coordinate of the camera location");
    }

    @Override
    public void onRenderLoopStopped(long gameTime) {
        System.out.println("done frame speed test at " + (int) getFPS() + " fps");
        System.out.println("there are " + env.getCanvas().get3DPeer().getTriangles() + " triangles in scene");
        System.out.println("rendering " + env.getCanvas().get3DPeer().getTriangles() * (int) getFPS() + " triangles/sec");
        System.out.println("   Num frames = " + numFrames);
        System.out.println("   Delta ms = " + gameTime);
    }

    @Override
    protected void loopIteration(long gameTime, long frameTime) {
        if (viewmode == 0) {
            env.getView().getTransform().lookAt(new Vector3f(5f, 0, 5f), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        } else if (viewmode == 1) {
            env.getView().getTransform().lookAt(new Vector3f(x, 0, 3f), new Vector3f(0, -5, 0), new Vector3f(0, 1, 0));
        } else if (viewmode == 2) {
            env.getView().getTransform().lookAt(new Vector3f(2f, 0, 2f), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        } else if (viewmode == 3) {
            env.getView().getTransform().lookAt(new Vector3f(-1f, 0, 2f), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        } else if (viewmode == 4) {
            env.getView().getTransform().lookAt(new Vector3f(50f, 0, 2f), new Vector3f(1, 4, 0), new Vector3f(0, 1, 0));
        }
        super.loopIteration(gameTime, frameTime);
        numFrames++;
    }

    private void init(Xith3DEnvironment env) {
        Group m = new Group();
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
            TriangleArray ta = new TriangleArray(coords.length, GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);
            ta.setCoordinates(0, coords);
            ta.setTextureCoordinates(0, 0, texCoords);
            ta.calculateFaceNormals();
            sh.setGeometry(ta);
            Texture2D texture = (Texture2D) TextureLoader.getInstance().getTexture("stone.jpg");
            Appearance a = new Appearance();
            a.setRenderingAttributes(new RenderingAttributes());
            a.getRenderingAttributes().setDepthBufferWriteEnable(false);
            a.setTexture(texture);
            sh.setAppearance(a);
            Group group = new Group();
            group.addChild(sh);
            Background bg = new Background(group);
            m.addChild(bg);
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
            Color3f[] colors = new Color3f[] { new Color3f(1f, 1f, 1f), new Color3f(1f, 0f, 0f), new Color3f(0f, 1f, 0f), new Color3f(0f, 1f, 0f), new Color3f(0f, 0f, 1f), new Color3f(1f, 1f, 1f), new Color3f(1f, 1f, 1f), new Color3f(1f, 0f, 0f), new Color3f(0f, 1f, 0f), new Color3f(0f, 1f, 0f), new Color3f(0f, 0f, 1f), new Color3f(1f, 1f, 1f), new Color3f(1f, 1f, 1f), new Color3f(1f, 0f, 0f), new Color3f(0f, 1f, 0f), new Color3f(0f, 1f, 0f), new Color3f(0f, 0f, 1f), new Color3f(1f, 1f, 1f) };
            TriangleArray ta = new TriangleArray(coords.length, GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.COLOR_3 | GeometryArray.TEXTURE_COORDINATE_2);
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
            int flag = GeometryArray.TEXTURE_COORDINATE_2 | GeometryArray.COORDINATES;
            TriangleStripArray geometry = new TriangleStripArray(4, flag, new int[] { 4 });
            Appearance appearance = new Appearance();
            TexCoord2f textureCoordinates[] = { new TexCoord2f(0, 0.35f), new TexCoord2f(0.04f, 0.35f), new TexCoord2f(0, 1f), new TexCoord2f(0.04f, 1f) };
            Point3f vertices[] = { new Point3f(-width + x, -height + y, depth), new Point3f(width + x, -height + y, depth), new Point3f(-width + x, height + y, depth), new Point3f(width + x, height + y, depth) };
            appearance.setTexture(TextureLoader.getInstance().getTexture("hud.png", Texture.RGBA, true));
            appearance.setRenderingAttributes(new RenderingAttributes());
            appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.1f));
            geometry.setCoordinates(0, vertices);
            geometry.setTextureCoordinates(0, 0, textureCoordinates);
            Group foregroundBG = new Group();
            foregroundBG.addChild(new Shape3D(geometry, appearance));
            Foreground fg = new Foreground(foregroundBG, CameraMode.VIEW_FIXED);
            m.addChild(fg);
        }
        env.addChild(m);
    }

    public BackgroundGeomTest(CanvasConstructionInfo canvasInfo, Xith3DTest.FinishListener finishListener) throws Exception {
        super(128L);
        env = new Xith3DEnvironment(new Vector3f(1f, 0f, 2f), new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f), this);
        ResourceLocator resLoc = TestUtils.createDemoResourceLocator();
        resLoc.createAndAddTSL("textures");
        init(env);
        Canvas3DWrapper canvas = new Canvas3DWrapper(canvasInfo);
        env.addCanvas(canvas);
        this.registerKeyboardAndMouse(canvas);
        this.finishListener = finishListener;
        this.begin();
    }

    public static void main(String[] args) throws Exception {
        new BackgroundGeomTest(new CanvasConstructionInfo(Xith3DTest.DEFAULT_RESOLUTION, Xith3DTest.DEFAULT_DISPLAY_MODE, "BackgroundGeomTest"), null);
    }
}
