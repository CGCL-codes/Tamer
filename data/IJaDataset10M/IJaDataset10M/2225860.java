package net.irrisor.physicsdefense;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.intersection.TrianglePickData;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.batch.TriangleBatch;
import com.jme.scene.shape.Box;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsCollisionGeometry;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.material.Material;

public class VisualUtils {

    private TextureState ironTextureState;

    private TextureState woodTextureState;

    private TextureState graniteTextureState;

    private AlphaState alphaState;

    private CullState cullState;

    public static final ColorRGBA RED = new ColorRGBA(0.9f, 0.2f, 0.2f, 0.7f);

    public static final ColorRGBA GREEN = new ColorRGBA(0.2f, 0.9f, 0.2f, 0.7f);

    public static final String RESOURCE_FIREFLARE = "flaresmall.jpg";

    public static final String RESOURCE_DUSTFLARE = "flaresmall.png";

    public static final Material ASHES = new Material("ashes");

    public static final Material STOPPER = setupStopperMaterial();

    public static final ColorRGBA PLAYER_COLOR = new ColorRGBA(0.2f, 0.9f, 0.2f, 1);

    public static final ColorRGBA ENEMY_COLOR = new ColorRGBA(0.9f, 0.2f, 0.2f, 1);

    private VisualUtils() {
        try {
            URL url = Application.class.getResource("data/texture/");
            Logger.getAnonymousLogger().log(Level.FINE, "using texture directory " + url);
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, new SimpleResourceLocator(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Renderer renderer = DisplaySystem.getDisplaySystem().getRenderer();
        alphaState = renderer.createAlphaState();
        alphaState.setEnabled(true);
        alphaState.setBlendEnabled(true);
        alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
        alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
        alphaState.setTestEnabled(true);
        alphaState.setTestFunction(AlphaState.TF_GREATER);
        cullState = renderer.createCullState();
        cullState.setCullMode(CullState.CS_BACK);
        ASHES.setDebugColor(new ColorRGBA(0.8f, 0.7f, 0.0f, 1));
        ASHES.setDensity(0.1f);
        ASHES.setSpringPenetrationDepth(0.2f);
        MutableContactInfo info = new MutableContactInfo();
        info.setMu(6);
        info.setDampingCoefficient(20);
        info.setSpringConstant(2);
        info.setBounce(0.1f);
        ASHES.putContactHandlingDetails(null, info);
        {
            Texture texture = TextureManager.loadTexture(RESOURCE_BACKGROUND, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
            texture.setScale(new Vector3f(0.5f, 0.5f, 1));
            ironTextureState = renderer.createTextureState();
            ironTextureState.setTexture(texture);
        }
        {
            Texture texture = TextureManager.loadTexture(RESOURCE_WOOD, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
            woodTextureState = renderer.createTextureState();
            woodTextureState.setTexture(texture);
        }
        {
            Texture texture = TextureManager.loadTexture(RESOURCE_GRANITE, Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
            texture.setScale(new Vector3f(0.1f, 0.1f, 1));
            graniteTextureState = renderer.createTextureState();
            graniteTextureState.setTexture(texture);
        }
    }

    private static VisualUtils instance;

    public static VisualUtils get() {
        if (instance == null) {
            instance = new VisualUtils();
        }
        return instance;
    }

    private final Map<ColorRGBA, Map<Integer, MaterialState>> materialStates = new HashMap<ColorRGBA, Map<Integer, MaterialState>>();

    /**
     * Little helper method to color a spatial.
     *
     * @param spatial   the spatial to be colored
     * @param color     desired color
     * @param shininess desired shininess
     */
    public void color(Spatial spatial, ColorRGBA color, int shininess) {
        Map<Integer, MaterialState> map = materialStates.get(color);
        if (map == null) {
            map = new HashMap<Integer, MaterialState>();
            materialStates.put(color, map);
        }
        MaterialState materialState = map.get(shininess);
        if (materialState == null) {
            materialState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
            materialState.setDiffuse(color);
            materialState.setAmbient(color.mult(new ColorRGBA(0.3f, 0.3f, 0.3f, 1)));
            materialState.setShininess(shininess);
            float mul = 1 + shininess > 18 ? (shininess - 28) * 0.01f : 0;
            materialState.setSpecular(color.mult(new ColorRGBA(mul, mul, mul, 1)));
            map.put(shininess, materialState);
        }
        spatial.setRenderState(materialState);
        if (color.a < 1) {
            spatial.setRenderState(alphaState);
            spatial.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        }
        spatial.setRenderState(cullState);
    }

    static final URL RESOURCE_WOOD = Application.class.getResource("data/texture/crate.png");

    static final URL RESOURCE_BACKGROUND = Application.class.getResource("data/texture/rust.jpg");

    static final URL RESOURCE_GRANITE = Application.class.getResource("data/texture/granite.jpg");

    public static final URL RESOURCE_BLOCK = Application.class.getResource("data/texture/block.png");

    static final URL RESOURCE_VOID = Application.class.getResource("data/texture/cloudsblack.png");

    private final Vector3f tmpSecondVector = new Vector3f();

    private final Vector3f[] triangle = new Vector3f[] { new Vector3f(), new Vector3f(), new Vector3f() };

    public void applyMaterial(DynamicPhysicsNode node, Material material) {
        node.setMaterial(material);
        clearChildColorAndMaterial(node);
        node.computeMass();
        applyMaterialTexture(node, material);
    }

    public void applyMaterialTexture(Spatial spatial, Material material) {
        spatial.clearRenderState(RenderState.RS_TEXTURE);
        if (material == Material.IRON) {
            spatial.setRenderState(ironTextureState);
            color(spatial, new ColorRGBA(1, 1, 1, 1), 100);
        } else if (material == Material.OSMIUM) {
            spatial.setRenderState(ironTextureState);
            color(spatial, new ColorRGBA(0.3f, 0.3f, 0.6f, 1), 128);
        } else if (material == Material.ICE) {
            color(spatial, new ColorRGBA(0.5f, 0.5f, 0.9f, 0.6f), 128);
        } else if (material == Material.GLASS) {
            color(spatial, new ColorRGBA(0.5f, 0.5f, 0.6f, 0.2f), 128);
        } else if (material == ASHES) {
            color(spatial, new ColorRGBA(0.1f, 0.1f, 0.1f, 1), 128);
        } else if (material == Material.PLASTIC) {
            color(spatial, new ColorRGBA(0.9f, 1, 0.9f, 1), 100);
        } else if (material == Material.WOOD) {
            spatial.setRenderState(woodTextureState);
            color(spatial, new ColorRGBA(1, 1, 1, 1), 0);
        } else if (material == Material.RUBBER) {
            color(spatial, new ColorRGBA(0.2f, 0, 0, 1), 64);
        } else if (material == Material.GRANITE) {
            spatial.setRenderState(graniteTextureState);
            color(spatial, new ColorRGBA(1, 1, 1, 1), 30);
        }
    }

    private final Ray pickRay = new Ray();

    private final TrianglePickResults pickResults = new TrianglePickResults();

    public void applyBoundingAndStates(SharedMesh sharedMesh) {
        TriMesh actualGeom = sharedMesh.getTarget();
        sharedMesh.setRenderState(actualGeom.getRenderState(RenderState.RS_TEXTURE));
        sharedMesh.setRenderState(actualGeom.getRenderState(RenderState.RS_MATERIAL));
        sharedMesh.updateRenderState();
        if (actualGeom instanceof Box) {
            sharedMesh.setModelBound(new BoundingBox());
        } else {
            sharedMesh.setModelBound(new BoundingSphere());
        }
        sharedMesh.updateModelBound();
    }

    public void clearChildColorAndMaterial(Node node) {
        for (Spatial child : node.getChildren()) {
            child.clearRenderState(RenderState.RS_MATERIAL);
            child.clearRenderState(RenderState.RS_TEXTURE);
            if (child instanceof PhysicsCollisionGeometry) {
                PhysicsCollisionGeometry collisionGeometry = (PhysicsCollisionGeometry) child;
                collisionGeometry.setMaterial(null);
            }
            if (child instanceof Node) {
                clearChildColorAndMaterial((Node) child);
            }
        }
    }

    static Material setupStopperMaterial() {
        Material stopper = new Material("stopper");
        stopper.setDebugColor(new ColorRGBA(0.1f, 0.5f, 0.1f, 1));
        stopper.setDensity(0.95f);
        MutableContactInfo info = new MutableContactInfo();
        info.setMu(5f);
        info.setBounce(0.3f);
        stopper.putContactHandlingDetails(null, info);
        stopper.putContactHandlingDetails(Material.DEFAULT, info);
        stopper.putContactHandlingDetails(Material.CONCRETE, info);
        stopper.putContactHandlingDetails(Material.GLASS, info);
        stopper.putContactHandlingDetails(Material.PLASTIC, info);
        stopper.putContactHandlingDetails(Material.GRANITE, info);
        stopper.putContactHandlingDetails(Material.IRON, info);
        stopper.putContactHandlingDetails(Material.WOOD, info);
        info.setMu(50f);
        info.setBounce(0.85f);
        stopper.putContactHandlingDetails(Material.RUBBER, info);
        info.setMu(0.2f);
        info.setBounce(0.2f);
        stopper.putContactHandlingDetails(Material.GLASS, info);
        info.setMu(0.002f);
        info.setBounce(0.2f);
        stopper.putContactHandlingDetails(Material.ICE, info);
        return stopper;
    }

    public PhysicsCollisionGeometry createBox(PhysicsNode node, Vector3f center, float sx, float sy, float sz, Material material, boolean visible) {
        PhysicsBox physicsBox = node.createBox("");
        physicsBox.getLocalTranslation().set(center);
        physicsBox.getLocalScale().set(sx, sy, sz);
        physicsBox.setMaterial(material);
        if (visible) {
            Box box = new Box("", new Vector3f(), 0.5f, 0.5f, 0.5f);
            box.setLocalTranslation(physicsBox.getLocalTranslation());
            box.setLocalScale(physicsBox.getLocalScale());
            box.setModelBound(new BoundingBox());
            box.updateModelBound();
            get().applyMaterialTexture(box, material);
            node.attachChild(box);
        }
        return physicsBox;
    }

    public static class Pick {

        private TriangleBatch batch;

        private Vector3f worldLocation;

        private final Vector3f normal;

        public Pick(TriangleBatch batch, Vector3f worldLocation, Vector3f normal) {
            this.batch = batch;
            this.worldLocation = worldLocation;
            this.normal = normal;
        }

        public Vector3f getNormal() {
            return normal;
        }

        public TriangleBatch getBatch() {
            return batch;
        }

        public Vector3f getWorldLocation() {
            return worldLocation;
        }
    }

    public Pick pick(Spatial root, Vector2f screenPosition) {
        DisplaySystem.getDisplaySystem().getWorldCoordinates(screenPosition, 0, pickRay.origin);
        DisplaySystem.getDisplaySystem().getWorldCoordinates(screenPosition, 0.3f, pickRay.direction);
        pickRay.direction.subtractLocal(pickRay.origin).normalizeLocal();
        pickResults.clear();
        pickResults.setCheckDistance(true);
        root.findPick(pickRay, pickResults);
        if (pickResults.getNumber() > 0) {
            TrianglePickData pickData = (TrianglePickData) pickResults.getPickData(0);
            if (pickData.getTargetTris().size() > 0) {
                Vector3f location = new Vector3f();
                float distance = pickData.getDistance();
                location.set(pickRay.origin).addLocal(pickRay.direction.multLocal(distance));
                int index = pickData.getTargetTris().get(0);
                ((TriangleBatch) pickData.getTargetMesh()).getTriangle(index, triangle);
                for (Vector3f vertice : triangle) {
                    pickData.getTargetMesh().getParentGeom().localToWorld(vertice, vertice);
                }
                Vector3f normal = new Vector3f();
                if (triangle != null) {
                    normal.set(triangle[1]).subtractLocal(triangle[0]);
                    normal.crossLocal(tmpSecondVector.set(triangle[2]).subtractLocal(triangle[1]));
                    normal.normalizeLocal();
                } else {
                    normal.set(1, 0, 0);
                }
                return new Pick((TriangleBatch) pickData.getTargetMesh(), location, normal);
            }
        }
        return null;
    }
}
