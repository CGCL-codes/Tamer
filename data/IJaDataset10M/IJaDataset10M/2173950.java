package toolkit;

import java.util.logging.Logger;
import javax.swing.ImageIcon;
import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.NodeHandler;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.CameraNode;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.CombinerHeightMap;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.FluidSimHeightMap;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

/**
 * <code>TerrainGenerator</code>
 *
 * @author Syberex
 */
public class TerrainGenerator extends SimpleGame {

    private static final Logger logger = Logger.getLogger(TerrainGenerator.class.getName());

    private CameraNode camNode;

    private TerrainPage page;

    public static void main(String[] args) {
        TerrainGenerator app = new TerrainGenerator();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    /**
	 * builds the trimesh.
	 *
	 * @see com.jme.app.SimpleGame#initGame()
	 */
    protected void simpleInitGame() {
        rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
        DirectionalLight dl = new DirectionalLight();
        dl.setDiffuse(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        dl.setDirection(new Vector3f(1, -0.5f, 1));
        dl.setEnabled(true);
        lightState.attach(dl);
        DirectionalLight dr = new DirectionalLight();
        dr.setEnabled(true);
        dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        dr.setDirection(new Vector3f(0.5f, -0.5f, 0).normalizeLocal());
        lightState.attach(dr);
        cam.setFrustum(1.0f, 10000.0f, -0.55f, 0.55f, 0.4125f, -0.4125f);
        cam.update();
        camNode = new CameraNode("Camera Node", cam);
        camNode.setLocalTranslation(new Vector3f(0, 1000, -500));
        camNode.updateWorldData(0);
        input = new NodeHandler(camNode, 150, 1);
        rootNode.attachChild(camNode);
        display.setTitle("Terrain Generator");
        display.getRenderer().setBackgroundColor(new ColorRGBA(0.0f, 0.0f, 0.0f, 1));
        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        cs.setEnabled(true);
        rootNode.setRenderState(cs);
        generateTerrain();
    }

    public void generateTerrain() {
        int sizeMap = 256;
        PerlinNoiseHeightMap heightMap = new PerlinNoiseHeightMap(sizeMap, 2);
        Vector3f terrainScale = new Vector3f(10, 1, 10);
        TerrainBlock tb = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale, heightMap.getHeightMap(), new Vector3f(0, 0, 0));
        tb.setDetailTexture(1, 32);
        tb.setModelBound(new BoundingBox());
        tb.updateModelBound();
        logger.info("" + tb.getWorldBound());
        tb.setLocalTranslation(new Vector3f(0, 0, 0));
        rootNode.attachChild(tb);
        ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
        pt.addTexture(new ImageIcon(TerrainGenerator.class.getClassLoader().getResource("data/texture/dirt.jpg")), 0, 100, 120);
        pt.addTexture(new ImageIcon(TerrainGenerator.class.getClassLoader().getResource("data/texture/grassb.png")), 100, 125, 155);
        pt.addTexture(new ImageIcon(TerrainGenerator.class.getClassLoader().getResource("data/texture/highest.jpg")), 140, 175, 250);
        pt.createTexture(2048);
        pt.saveTexture("c:\\terrain_proceduraltex.png");
        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
        ts.setTexture(t1, 0);
        Texture t2 = TextureManager.loadTexture(TerrainGenerator.class.getClassLoader().getResource("data/texture/Detail.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        t2.setWrap(Texture.WrapMode.Repeat);
        t1.setApply(Texture.ApplyMode.Combine);
        t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Modulate);
        t1.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
        t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
        t1.setCombineSrc1RGB(Texture.CombinerSource.PrimaryColor);
        t1.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);
        t2.setApply(Texture.ApplyMode.Combine);
        t2.setCombineFuncRGB(Texture.CombinerFunctionRGB.AddSigned);
        t2.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
        t2.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
        t2.setCombineSrc1RGB(Texture.CombinerSource.Previous);
        t2.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);
        rootNode.setRenderState(ts);
    }
}
