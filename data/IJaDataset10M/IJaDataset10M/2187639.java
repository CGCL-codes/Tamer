package logic.nodes.lod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import logic.nodes.SlotNode;
import logic.nodes.collision.CollidableNode;
import logic.nodes.collision.DummyType;
import logic.nodes.lod.blocks.LeafBlock;
import logic.ships.mothership.MotherShip;
import logic.weapons.Weapon;
import settings.Config;
import settings.GraphicSettings;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingVolume;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.BillboardNode;
import com.jme.scene.DistanceSwitchModel;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.lod.DiscreteLodNode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.RenderState.StateType;
import fileHandling.ModelImporter;
import fileHandling.TextureLoader;
import fileHandling.language.options.OptionValues;
import gameStates.absGamesStates.AbsIngameState;

public abstract class LODNode extends CollidableNode {

    private static final long serialVersionUID = 1L;

    protected Collection<Node> detachedSlots, lodNodes;

    protected HashMap<TriMesh, BoundingVolume> meshBounds;

    protected HashMap<TriMesh, Vector3f> meshDirs;

    private int maxSwitches;

    public LODNode(String path, String title, Node model, AbsIngameState ingameState) {
        this(path, title, model, DummyType.None, ingameState);
    }

    public LODNode(String path, String title, Node model, DummyType dummyType, AbsIngameState ingameState) {
        super(path, title, model, dummyType, ingameState);
        updateGeometryQuality();
    }

    @Override
    protected void lockThis() {
        super.lockThis();
        Node lodNode = getModel();
        for (Spatial child : lodNode.getChildren()) {
            child.lockMeshes();
        }
    }

    public void updateGeometryQuality() {
        if (Config.get().useLOD()) {
            Node newModel = ModelImporter.getModel(path);
            setModel(getLODNode(newModel));
            assert (getModel() != null);
        } else setScaleOf(getModel());
        if (isLocked) lockThis();
        if (isLarge() && meshBounds == null) {
            if (this instanceof SlotNode) ((SlotNode) this).forceLODNode();
            meshBounds = new HashMap<TriMesh, BoundingVolume>();
            meshDirs = new HashMap<TriMesh, Vector3f>();
            Node n = getModel();
            if (Config.get().useLOD()) n = (Node) getLODModel().getChild(getLowIndex());
            getTriMeshes(n);
        }
    }

    protected int getLowIndex() {
        int index = 0;
        String qual = GraphicSettings.get().getGeometryQualityString();
        if (qual.equals(OptionValues.Medium.toString())) index = 1; else if (qual.equals(OptionValues.High.toString())) index = 2;
        return index;
    }

    protected void getTriMeshes(Node parent) {
        for (Spatial child : parent.getChildren()) {
            if (child instanceof Node) getTriMeshes((Node) child); else if (child instanceof TriMesh) {
                TriMesh mesh = (TriMesh) child;
                mesh.setModelBound(new BoundingBox());
                mesh.updateModelBound();
                mesh.updateWorldBound();
                TriMeshBoundController c = new TriMeshBoundController(mesh, this, meshBounds, meshDirs);
                boundControllers.put(mesh, c);
                addController(c);
            }
        }
    }

    protected DiscreteLodNode getLODNode(Node maxQualModel) {
        String geomQual = GraphicSettings.get().getGeometryQuality();
        maxSwitches = 6;
        if (geomQual.equals("medium")) maxSwitches = 5; else if (geomQual.equals("low")) maxSwitches = 4;
        detachedSlots = new HashSet<Node>();
        lodNodes = new ArrayList<Node>();
        DistanceSwitchModel switchModel = new DistanceSwitchModel(maxSwitches);
        DiscreteLodNode dlodNode = getDiscreteLodNode(switchModel);
        float notVisibleDist = LeafBlock.VIEW_DIST - LeafBlock.MIN_LOD_VIEW_DIST;
        float visibleDist = LeafBlock.MIN_LOD_VIEW_DIST + (notVisibleDist * GraphicSettings.get().getLOD());
        float distDiv = visibleDist / maxSwitches;
        float dist = distDiv * getDistanceFactor();
        assert (dist > 0f);
        String texQual = GraphicSettings.get().getTextureQuality();
        setScaleOf(maxQualModel);
        updateTextureQualityOf(maxQualModel, texQual);
        dlodNode.attachChild(maxQualModel);
        switchModel.setModelDistance(0, 0f, dist);
        detachedSlots.add(maxQualModel);
        lodNodes.add(maxQualModel);
        int lastSwitch = maxSwitches;
        boolean isMS = this instanceof MotherShip;
        boolean useBB = !(this instanceof Weapon || isMS);
        boolean lastTillInf = isMS;
        for (int i = 1; i <= lastSwitch; i++) {
            String newTexQual = ModelImporter.nextLevel(texQual, true);
            Node node = null;
            if (i != lastSwitch) {
                if (newTexQual != null) texQual = newTexQual;
                geomQual = ModelImporter.nextLevel(geomQual, true);
                assert (geomQual != null);
                float maxDist = (i + 1) * dist;
                if (i == lastSwitch - 1 && lastTillInf) maxDist = Float.POSITIVE_INFINITY;
                switchModel.setModelDistance(i, i * dist, maxDist);
                node = ModelImporter.getModelOfQuality(path, geomQual);
                assert (node != null);
                setScaleOf(node);
                updateTextureQualityOf(node, texQual);
                detachedSlots.add(node);
            } else if (useBB) {
                node = new Node("Dummy_BBNode");
                node.attachChild(new Quad("Dummy_Quad"));
                switchModel.setModelDistance(i, i * dist, Float.POSITIVE_INFINITY);
                addController(new BBNodeController(this));
            } else break;
            dlodNode.attachChildAt(node, i);
            lodNodes.add(node);
            if (i == lastSwitch - 1 && lastTillInf) break;
        }
        assert (ModelImporter.nextLevel(geomQual, true) == null);
        return dlodNode;
    }

    public void setBillboardNode(BillboardNode bbNode) {
        getLODModel().attachChildAt(bbNode, maxSwitches);
    }

    public void updateTextureFilters() {
        for (Node node : lodNodes) {
            updateTextureFilters(node);
        }
        updateRenderState();
    }

    protected void updateTextureFilters(Node model) {
        if (model == null || model.getChildren() == null) return;
        for (Spatial child : model.getChildren()) {
            if (child instanceof Node) updateTextureFilters((Node) child); else {
                TextureState ts = (TextureState) child.getRenderState(StateType.Texture);
                if (ts == null) continue;
                Texture tex = ts.getTexture();
                if (tex == null) continue;
                TextureLoader.upateTextureFilter(tex);
                child.updateRenderState();
            }
        }
    }

    public void updateTextureQuality() {
        String tempQual = GraphicSettings.get().getTextureQuality();
        for (Node child : lodNodes) {
            updateTextureQualityOf(child, tempQual);
            String newTexQual = ModelImporter.nextLevel(tempQual, true);
            if (newTexQual != null) tempQual = newTexQual;
        }
        updateRenderState();
    }

    protected void updateTextureQualityOf(Node model, String quality) {
        assert (quality != null);
        assert (model != null);
        assert (model.getChildren() != null);
        for (Spatial child : model.getChildren()) {
            if (child instanceof Node && ((Node) child).getChildren() != null) updateTextureQualityOf((Node) child, quality); else if (child instanceof TriMesh) {
                TextureLoader.updateTextureQuality((TriMesh) child, quality);
                child.updateRenderState();
            }
        }
    }

    public Collection<TriMesh> getTriMeshes() {
        return meshBounds.keySet();
    }

    public BoundingVolume getTriMeshBounding(TriMesh mesh) {
        return meshBounds.get(mesh);
    }

    public Vector3f getTriMeshDir(TriMesh mesh) {
        return meshDirs.get(mesh);
    }

    public DiscreteLodNode getLODModel() {
        return (DiscreteLodNode) getModel();
    }

    public Node getLowQualityNode() {
        if (!Config.get().useLOD()) return getModel();
        DiscreteLodNode lodNode = getLODModel();
        String qual = GraphicSettings.get().getGeometryQualityString();
        int index = 2;
        if (qual.equals(OptionValues.Medium.toString())) index = 1; else if (qual.equals(OptionValues.Low.toString())) index = 0;
        return (Node) lodNode.getChild(index);
    }

    public Node getActiveLODModel() {
        DiscreteLodNode lod = getLODModel();
        return (Node) lod.getChild(lod.getActiveChild());
    }

    protected DiscreteLodNode getDiscreteLodNode(DistanceSwitchModel switchModel) {
        return new DiscreteLodNode("LOD-Node", switchModel);
    }

    protected float getDistanceFactor() {
        return 1f;
    }

    public float getDefaultScale() {
        return 0.08f;
    }

    protected void setScaleOf(Spatial child) {
        child.setLocalScale(getDefaultScale());
    }

    public void updateGeometrySettings() {
        updateGeometryQuality();
        if (!isVisibleToPlayer()) detachModel();
    }
}
