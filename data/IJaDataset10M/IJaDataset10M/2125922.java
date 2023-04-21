package org.xith3d.test.geometry;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.vecmath2.Point3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.IndexedTriangleArray;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.TexCoordGeneration;
import org.xith3d.scenegraph.Texture;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Simple test of IndexedTriangleArray." }, authors = { "William Denniss" })
public class IndexedTriangleTest extends Xith3DTest {

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
        }
    }

    private BranchGroup createScene() {
        BranchGroup m = new BranchGroup();
        {
            Shape3D sh = new Shape3D();
            Point3f[] pts = new Point3f[5];
            pts[0] = new Point3f(-1f, -1f, 1f);
            pts[1] = new Point3f(-1f, -1f, -1f);
            pts[2] = new Point3f(1f, -1f, -1f);
            pts[3] = new Point3f(1f, -1f, 1f);
            pts[4] = new Point3f(0f, 1f, 0f);
            int[] indicies = { 0, 2, 1, 0, 3, 2, 0, 1, 4, 1, 2, 4, 2, 3, 4, 3, 0, 4 };
            IndexedTriangleArray ita = new IndexedTriangleArray(5, indicies.length);
            ita.setCoordinates(0, pts);
            ita.setIndex(indicies);
            ita.setInitialIndexIndex(0);
            ita.calculateFaceNormals();
            ita.setIndex(indicies);
            ita.setValidIndexCount(indicies.length);
            ita.calculateFaceNormals();
            sh.setGeometry(ita);
            Texture texture = TextureLoader.getInstance().getTexture("rustycan.jpg");
            Material mat = new Material();
            mat.setAmbientColor(4f, 4f, 4f);
            mat.setLightingEnabled(true);
            Appearance a = new Appearance();
            a.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE));
            a.setTexture(texture);
            TexCoordGeneration texGen = new TexCoordGeneration(TexCoordGeneration.SPHERE_MAP, TexCoordGeneration.TEXTURE_COORDINATE_2);
            a.setTexCoordGeneration(texGen);
            a.setMaterial(mat);
            sh.setAppearance(a);
            m.addChild(sh);
        }
        return (m);
    }

    public IndexedTriangleTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(1f, 0f, 2f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        env.addPerspectiveBranch(createScene());
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        TestUtils.displayInputBindings(canvas, null, (String[]) null, env);
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
    }

    public static void main(String[] args) throws Throwable {
        IndexedTriangleTest test = new IndexedTriangleTest(parseCommandLine(args));
        test.begin();
    }
}
