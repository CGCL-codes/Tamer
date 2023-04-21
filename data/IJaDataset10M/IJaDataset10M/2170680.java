package br.furb.inf.tcc.tankcoders.scene.terrain;

import java.net.URL;
import javax.swing.ImageIcon;
import br.furb.inf.tcc.tankcoders.scene.flag.HeadquarterFlag;
import br.furb.inf.tcc.util.scene.ModelUtils;
import br.furb.inf.tcc.util.scene.TextureUtils;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.AbstractHeightMap;
import com.jmex.terrain.util.ImageBasedHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

/**
 * This class is a height-map image based implementation of ITerrain.
 * @author Germano Fronza
 */
public class Terrain extends Node implements ITerrain {

    private static final long serialVersionUID = 1L;

    public static final float ZLOC_TEAM1_HEADQUARTER = 4033;

    public static final float ZLOC_TEAM2_HEADQUARTER = -4195;

    private static final String PATH_MODEL_TREE_COMPLEX = "data/model/tree/tree_3.xml";

    private TerrainPage terrainBlock = null;

    private AbstractHeightMap heightMap;

    private static ImageBasedHeightMapAdapter heightMapAdapter;

    public Terrain(PhysicsSpace pSpace, String heightMapImageFilePath) {
        super("terrain");
        makeTopology(heightMapImageFilePath);
        makePhysicsRepresentation(pSpace);
        makeModelBound();
        makeFlags(pSpace);
    }

    /**
	 * Makes the new terrain.
	 * @param heightMapImageFilePath
	 */
    private void makeTopology(String heightMapImageFilePath) {
        URL grayScale = Terrain.class.getClassLoader().getResource(heightMapImageFilePath);
        heightMap = new ImageBasedHeightMap(new ImageIcon(grayScale).getImage());
        heightMapAdapter = new ImageBasedHeightMapAdapter((ImageBasedHeightMap) heightMap);
        Vector3f terrainScale = new Vector3f(200, 10, 200);
        Vector3f position = new Vector3f(-200, -45, -200);
        terrainBlock = new TerrainPage("terrainPage", 33, heightMap.getSize() + 1, terrainScale, heightMap.getHeightMap(), true);
        terrainBlock.setLocalTranslation(position);
        applyTexture(heightMap);
        this.attachChild(terrainBlock);
    }

    /**
	 * Aply texture to terrain base.
	 * @param heightMap
	 */
    private void applyTexture(AbstractHeightMap heightMap) {
        ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
        pt.addTexture(new ImageIcon(getClass().getClassLoader().getResource("data/texture/terrain/dirt12.jpg")), -128, 0, 128);
        pt.addTexture(new ImageIcon(getClass().getClassLoader().getResource("data/texture/terrain/grass.png")), 0, 80, 160);
        pt.createTexture(1024);
        Texture basicTexture = TextureManager.loadTexture(pt.getImageIcon().getImage(), Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, true);
        TextureUtils.configTexture(basicTexture);
        Texture detailedTexture = TextureUtils.loadTexture("data/texture/terrain/dirtd.png");
        detailedTexture.setWrap(Texture.WM_WRAP_S_WRAP_T);
        TextureState textureApplyer = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        textureApplyer.setTexture(basicTexture, 0);
        textureApplyer.setTexture(detailedTexture, 1);
        textureApplyer.setEnabled(true);
        terrainBlock.setDetailTexture(1, 256);
        terrainBlock.setRenderState(textureApplyer);
        terrainBlock.updateRenderState();
    }

    /**
	 * Creates physics representation of the terrain.
	 * @param pSpace
	 */
    private void makePhysicsRepresentation(PhysicsSpace pSpace) {
        StaticPhysicsNode staticNode = pSpace.createStaticNode();
        staticNode.attachChild(terrainBlock);
        staticNode.generatePhysicsGeometry(true);
        staticNode.setMaterial(Material.WOOD);
        this.attachChild(staticNode);
    }

    /**
	 * Makes bouding box.
	 */
    private void makeModelBound() {
        this.setModelBound(new BoundingBox());
        this.updateModelBound();
    }

    /**
	 * Make the Team Headquarter flags.
	 * @param pSpace
	 */
    private void makeFlags(PhysicsSpace pSpace) {
        HeadquarterFlag flag = new HeadquarterFlag(pSpace, ColorRGBA.blue, new Vector3f(-201, 306, ZLOC_TEAM1_HEADQUARTER), true);
        this.attachChild(flag);
        flag = new HeadquarterFlag(pSpace, ColorRGBA.blue, new Vector3f(618, 303, ZLOC_TEAM1_HEADQUARTER - 15), true);
        this.attachChild(flag);
        flag = new HeadquarterFlag(pSpace, ColorRGBA.red, new Vector3f(-524, 310, ZLOC_TEAM2_HEADQUARTER), false);
        this.attachChild(flag);
        flag = new HeadquarterFlag(pSpace, ColorRGBA.red, new Vector3f(850, 312, ZLOC_TEAM2_HEADQUARTER + 15), false);
        this.attachChild(flag);
    }

    @SuppressWarnings("unused")
    private void makeVegetation(PhysicsSpace pSpace) {
        float currentH = getTerrainBaseHeight();
        float[][] matrix = { { 30, 210 }, { -566, 290 }, { -762, 394 }, { 668, 1172 }, { 1147, 2378 }, { -834, 3220 }, { -2629, 2299 }, { -2629, 2100 }, { -1924, 283 }, { -2290, -1721 }, { 19, -2887 }, { 2036, -271 }, { 2000, -271 } };
        StaticPhysicsNode staticNode;
        for (int i = 0; i < matrix.length; i++) {
            Vector3f position = new Vector3f();
            position.y = currentH;
            position.x = matrix[i][0];
            position.z = matrix[i][1];
            staticNode = pSpace.createStaticNode();
            staticNode.setName("trees");
            staticNode.setLocalTranslation(position);
            staticNode.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.PI / 2, new Vector3f(1, 0, 0)));
            staticNode.attachChild(ModelUtils.getNodeByXMLJmeModel(PATH_MODEL_TREE_COMPLEX));
            staticNode.generatePhysicsGeometry(true);
            staticNode.setMaterial(Material.WOOD);
            staticNode.setModelBound(new BoundingBox());
            staticNode.updateModelBound();
            this.attachChild(staticNode);
        }
    }

    /**
	 * Gets the terrain block instance.
	 */
    public TerrainPage getTerrainBlock() {
        return terrainBlock;
    }

    /**
	 * Gets the terrain base (floor) height.
	 */
    public float getTerrainBaseHeight() {
        return 260.46f;
    }

    /**
	 * Gets the array of initial tank locations.
	 * @see GameRulesConstants.MAX_TANKS 
	 */
    public Vector3f[] getTanksInitialLocation() {
        return new Vector3f[] { new Vector3f(-1785.54f, 267, 4450.41f), new Vector3f(-1695.53f, 267, 4455.00f), new Vector3f(-1617.87f, 267, 4459.58f), new Vector3f(-1526.56f, 267, 4465.10f), new Vector3f(-1442.48f, 267, 4469.98f), new Vector3f(-1329.40f, 267, 4476.10f), new Vector3f(-1785.54f, 266, -4871.92f), new Vector3f(-1695.53f, 266, -4855.48f), new Vector3f(-1617.87f, 266, -4845.59f), new Vector3f(-1526.56f, 266, -4836.89f), new Vector3f(-1442.48f, 266, -4833.40f), new Vector3f(-1329.40f, 266, -4826.54f) };
    }

    /**
	 * Gets the height map adapter to access informations about the terrain topology.
	 * @return ImageBasedHeightMapAdapter The Adapter
	 */
    public static ImageBasedHeightMapAdapter getHeightMapAdapter() {
        return heightMapAdapter;
    }
}
