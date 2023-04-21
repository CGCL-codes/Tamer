package jmetest.renderer;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme.animation.AnimationController;
import com.jme.animation.Bone;
import com.jme.animation.BoneAnimation;
import com.jme.animation.SkinNode;
import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.util.ColorMipMapGenerator;
import com.jme.input.FirstPersonHandler;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.TextureRenderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.JmeException;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.collada.ColladaImporter;

/**
 * <code>TestMipMaps</code> shows off mipmapping in a regular scene and in the
 * context of render to texture. Different colors indicate different levels of
 * mipmaps with blue being highest and red being lowest.
 * 
 * @author Joshua Slack
 */
public class TestMipMaps extends SimpleGame {

    private static final Logger logger = Logger.getLogger(TestMipMaps.class.getName());

    private TextureRenderer tRenderer;

    private Node monitorNode;

    private Texture2D fakeTex;

    private float lastRend = 1;

    private float throttle = 1 / 30f;

    private SkinNode sn;

    /**
     * Entry point for the test,
     * 
     * @param args
     */
    public static void main(String[] args) {
        TestMipMaps app = new TestMipMaps();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    @Override
    protected void simpleInitGame() {
        setupModel();
        setupMonitor();
    }

    protected void cleanup() {
        super.cleanup();
        tRenderer.cleanup();
    }

    protected void simpleRender() {
        lastRend += tpf;
        if (lastRend > throttle) {
            tRenderer.render(sn, fakeTex);
            lastRend = 0;
        }
    }

    private void setupMonitor() {
        tRenderer = display.createTextureRenderer(256, 256, TextureRenderer.Target.Texture2D);
        tRenderer.getCamera().setAxes(new Vector3f(-1, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));
        tRenderer.getCamera().setLocation(new Vector3f(0, -100, 20));
        monitorNode = new Node("Monitor Node");
        monitorNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        Quad quad = new Quad("Monitor");
        quad.updateGeometry(100, 100);
        quad.setLocalTranslation(new Vector3f(110, 110, 0));
        quad.setZOrder(1);
        monitorNode.attachChild(quad);
        Quad quad2 = new Quad("Monitor Back");
        quad2.updateGeometry(110, 110);
        quad2.setLocalTranslation(new Vector3f(110, 110, 0));
        quad2.setZOrder(2);
        monitorNode.attachChild(quad2);
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(false);
        monitorNode.setRenderState(buf);
        tRenderer.setBackgroundColor(new ColorRGBA(0f, 0f, 0f, 1f));
        fakeTex = new Texture2D();
        fakeTex.setRenderToTextureType(Texture.RenderToTextureType.RGBA);
        tRenderer.setupTexture(fakeTex);
        TextureState screen = display.getRenderer().createTextureState();
        screen.setTexture(fakeTex);
        screen.setEnabled(true);
        quad.setRenderState(screen);
        monitorNode.setLightCombineMode(Spatial.LightCombineMode.Off);
        rootNode.attachChild(monitorNode);
    }

    private void setupModel() {
        try {
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, new SimpleResourceLocator(TestMipMaps.class.getClassLoader().getResource("jmetest/data/model/collada/")));
        } catch (URISyntaxException e1) {
            logger.warning("Unable to add texture directory to RLT: " + e1.toString());
        }
        cam.setAxes(new Vector3f(-1, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));
        cam.setLocation(new Vector3f(0, -100, 20));
        input = new FirstPersonHandler(cam, 80, 1);
        InputStream mobboss = TestMipMaps.class.getClassLoader().getResourceAsStream("jmetest/data/model/collada/man.dae");
        InputStream animation = TestMipMaps.class.getClassLoader().getResourceAsStream("jmetest/data/model/collada/man_walk.dae");
        if (mobboss == null) {
            logger.info("Unable to find file, did you include jme-test.jar in classpath?");
            System.exit(0);
        }
        ColladaImporter.load(mobboss, "model");
        sn = ColladaImporter.getSkinNode(ColladaImporter.getSkinNodeNames().get(0));
        Bone skel = ColladaImporter.getSkeleton(ColladaImporter.getSkeletonNames().get(0));
        ColladaImporter.cleanUp();
        ColladaImporter.load(animation, "anim");
        ArrayList<String> animations = ColladaImporter.getControllerNames();
        logger.info("Number of animations: " + animations.size());
        for (int i = 0; i < animations.size(); i++) {
            logger.info(animations.get(i));
        }
        BoneAnimation anim1 = ColladaImporter.getAnimationController(animations.get(0));
        AnimationController ac = new AnimationController();
        ac.addAnimation(anim1);
        ac.setRepeatType(Controller.RT_CYCLE);
        ac.setActive(true);
        ac.setActiveAnimation(anim1);
        skel.addController(ac);
        stripTexturesAndMaterials(sn);
        Texture texture = new Texture2D();
        texture.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
        texture.setMinificationFilter(Texture.MinificationFilter.BilinearNearestMipMap);
        try {
            texture.setImage(ColorMipMapGenerator.generateColorMipMap(512, new ColorRGBA[] { ColorRGBA.blue.clone(), ColorRGBA.green.clone(), ColorRGBA.white.clone() }, ColorRGBA.red.clone()));
        } catch (JmeException e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "setupModel()", "Exception", e);
        }
        TextureState ts = display.getRenderer().createTextureState();
        ts.setTexture(texture);
        sn.setRenderState(ts);
        rootNode.attachChild(sn);
        rootNode.attachChild(skel);
        ColladaImporter.cleanUp();
        lightState.detachAll();
        lightState.setEnabled(false);
    }

    private void stripTexturesAndMaterials(Spatial sp) {
        sp.clearRenderState(RenderState.StateType.Texture);
        sp.clearRenderState(RenderState.StateType.Material);
        if (sp instanceof Node) {
            Node n = (Node) sp;
            for (Spatial child : n.getChildren()) {
                stripTexturesAndMaterials(child);
            }
        }
    }
}
