package imi.scene;

import imi.loaders.LoaderParams;
import imi.repository.AssetInitializer;
import imi.repository.RRL;
import imi.repository.Repository;
import imi.repository.RepositoryUser;
import imi.repository.SharedAsset;
import imi.repository.SharedAssetPlaceHolder;
import imi.scene.polygonmodel.PPolygonMesh;
import imi.scene.polygonmodel.PPolygonMeshInstance;
import imi.scene.polygonmodel.ModelInstance;
import imi.scene.polygonmodel.PMeshMaterial;
import imi.scene.polygonmodel.PPolygonSkinnedMesh;
import imi.scene.polygonmodel.PPolygonSkinnedMeshInstance;
import imi.utils.BooleanPointer;
import imi.utils.FileUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastTable;
import org.jdesktop.mtgame.WorldManager;
import org.jdesktop.wonderland.common.InternalAPI;

/**
 * This scene is capable of loading in procedural geometry and cache it locally,
 * it is also able to load shared assets that are shared globaly via the repository.
 * A PScene could be submited to its JScene for rendering (deprecated feature).
 * 
 * @author Chris Nagle
 * @author Lou Hayt
 * @author Ronald E Dahlgren
 */
public class PScene extends PNode implements RepositoryUser, Serializable {

    /** Serialization version number **/
    private static final long serialVersionUID = 1l;

    private final List<PPolygonMesh> localGeometry = new FastList<PPolygonMesh>();

    private transient List<SharedAsset> sharedAssets = new FastList<SharedAsset>();

    private transient List<SharedAssetPlaceHolder> sharedAssetWaitingList = new FastList<SharedAssetPlaceHolder>();

    private final PNode instancesRoot = new PNode("instances", new PTransform());

    private transient JScene jscene = null;

    private transient WorldManager worldManager = null;

    /**
     * Used to buffer transform submission. This is marked volatile because
     * multiple threads may act on its state.
     */
    @Deprecated
    private transient volatile boolean transformSubmitted = false;

    /**
     * The name attribute will be set to "PScene"
     * @param wm A non-null world manager
     */
    public PScene() {
        this("PScene");
    }

    /**
     * param must be non-null
     * @param name
     */
    public PScene(String name) {
        assert (name != null) : "Must provide non-null params!";
        worldManager = WorldManager.getDefaultWorldManager();
        setName(name);
        this.addChild(instancesRoot);
    }

    /**
     * Set the jscene this pscene belongs to
     * @param j
     */
    @Deprecated
    void setJScene(JScene j) {
        assert (j != null) : "Must provide a non-null jscene!";
        jscene = j;
    }

    /**
     * The PScene will flatten its hierarchy (build the transform matrices)
     */
    @Deprecated
    public void submitTransformsIfNeeded() {
        if (!transformSubmitted) submitTransforms();
    }

    /**
     * Must be on the render thread!
     * The PScene will flatten its hierarchy (build the transform world matrices)
     * This method will clear all the children from m_JScene and resubmit thier
     * references according to the current PScene structure.
     */
    @Deprecated
    synchronized void submitTransforms() {
        jscene.submitTransforms(instancesRoot);
        transformSubmitted = true;
    }

    /**
     * Any geometry data that is shared localy within this scene will check 
     * its dirty boolean and if true it will reconstruct its TriMesh.
     */
    @Deprecated
    public synchronized void submitGeometry() {
        for (PPolygonMesh geometry : localGeometry) {
            geometry.submit();
        }
    }

    /**
     * Flip normals on local geometry
     */
    @Deprecated
    public synchronized void flipNormals() {
        for (PPolygonMesh geometry : localGeometry) geometry.flipNormals();
    }

    /**
     * Add geometry to this scene to be shared locally
     * @param   meshAsset
     * @return  index of the meshAsset geometry
     */
    @InternalAPI
    public synchronized int addMeshGeometry(PPolygonMesh mesh) {
        int index = localGeometry.indexOf(mesh);
        if (index == -1) {
            localGeometry.add(mesh);
            index = localGeometry.size() - 1;
        }
        return index;
    }

    /**
     * Add an instance node directly to the instances graph
     * @param node A non-null node
     */
    public void addInstanceNode(PNode node) {
        assert (node != null) : "Null node provided.";
        instancesRoot.addChild(node);
    }

    /**
     * Add a model instance to this scene.
     * 
     * Adding an instance makes the scene dirty
     * @param model
     * @return modelInstance (PPolygonModelInstance)
     */
    public ModelInstance addModelInstance(ModelInstance model) {
        assert (model != null) : "Null model provided";
        PNode node = processNode(model);
        model.removeAllChildren();
        List<PNode> children = node.getChildren();
        while (children.isEmpty() == false) model.addChild(children.get(0));
        instancesRoot.addChild(model);
        model.setDirty();
        return model;
    }

    /**
     * Add a model instance to this scene by supplying a PNode and a PMatrix
     * 
     * Adding an instance makes the scene dirty
     * @param node
     * @param origin
     * @return modelInstance (PPolygonModelInstance)
     */
    public ModelInstance addModelInstance(PNode node, PMatrix origin) {
        assert (node != null && origin != null) : "Must not have any null parameters.";
        final ModelInstance modelInstance = new ModelInstance(node.getName());
        modelInstance.addChild(processNode(node));
        modelInstance.getTransform().getLocalMatrix().set(origin);
        instancesRoot.addChild(modelInstance);
        modelInstance.setDirty();
        return modelInstance;
    }

    /**
     * Add an instance to this scene by supplying a PNode
     * 
     * Adding an instance makes the scene dirty
     * @param node
     * @param origin
     * @return meshInstance (PPolygonMeshInstance)
     */
    public PPolygonMeshInstance addMeshInstance(PNode node, PMatrix origin) {
        assert (node != null && origin != null) : "Must not have any null parameters.";
        PNode pNewNode = processNode(node);
        pNewNode.getTransform().getLocalMatrix().set(origin);
        instancesRoot.addChild(pNewNode);
        pNewNode.setDirty();
        return (PPolygonMeshInstance) pNewNode;
    }

    /**
     * Retruns the root node of all instances in this scene
     * @return Root of the instances graph
     */
    public PNode getInstances() {
        return instancesRoot;
    }

    /** 
     * @return the list of shared assets in the repository
     */
    Iterable<SharedAsset> getAssetList() {
        return sharedAssets;
    }

    /**
     * Clears the scene's shared asset collection, shared asset waiting list,
     * local geometry collection, and the instances graph.
     */
    @InternalAPI
    public synchronized void clear() {
        sharedAssetWaitingList.clear();
        instancesRoot.removeAllChildren();
        localGeometry.clear();
        sharedAssets.clear();
    }

    /***
     * Process a graph
     * @param node a non-null node
     * @return
     */
    private PNode processNode(PNode node) {
        assert (node != null) : "Null node provided.";
        if (node instanceof PPolygonSkinnedMesh) return processSkinnedMesh((PPolygonSkinnedMesh) node); else if (node instanceof PPolygonMesh) return processMesh((PPolygonMesh) node); else if (node instanceof SkinnedMeshJoint) return processSkinnedMeshJoint((SkinnedMeshJoint) node); else if (node instanceof PJoint) return processJoint((PJoint) node); else if (node instanceof PPolygonMeshInstance) return processMeshInstance((PPolygonMeshInstance) node); else if (node instanceof SkeletonNode) return processSkeletonNode((SkeletonNode) node);
        PNode nodeCopy = new PNode(node.getName(), node.getTransform());
        for (int i = 0; i < node.getChildrenCount(); i++) nodeCopy.addChild(processNode(node.getChild(i)));
        return nodeCopy;
    }

    /**
     * Process a conceret meshAsset (contains geometry, not an instance)
     * and produce a meshAsset instance 
     * @param mesh
     * @return meshInst (PPolygonMeshInstance)
     */
    @InternalAPI
    public synchronized PPolygonMeshInstance processMesh(PPolygonMesh mesh) {
        int index = addMeshGeometry(mesh);
        PPolygonMesh geometry = localGeometry.get(index);
        PPolygonMeshInstance meshInst = new PPolygonMeshInstance(geometry.getName(), geometry, geometry.getTransform().getLocalMatrix());
        meshInst.setName(mesh.getName());
        meshInst.getTransform().getLocalMatrix().set(mesh.getTransform().getLocalMatrix());
        for (PNode kid : mesh.getChildren()) meshInst.addChild(processNode(kid));
        return meshInst;
    }

    /**
     * This method takes an instance and converts it to belong this
     * PScene.
     * @param originalMeshInstance
     * @return PPolygonMeshInstance
     */
    private PPolygonMeshInstance processMeshInstance(PPolygonMeshInstance originalMeshInstance) {
        PPolygonMesh geometry = null;
        for (SharedAsset shared : sharedAssets) {
            if (shared.getAssetData() == originalMeshInstance.getGeometry()) {
                geometry = originalMeshInstance.getGeometry();
                break;
            }
        }
        if (geometry == null) {
            SharedAsset asset = new SharedAsset(new RRL(originalMeshInstance.getName()));
            asset.setAssetData(originalMeshInstance.getGeometry());
            sharedAssets.add(asset);
            geometry = originalMeshInstance.getGeometry();
        }
        PPolygonMeshInstance newMeshInst = null;
        if (originalMeshInstance instanceof PPolygonSkinnedMeshInstance) newMeshInst = new PPolygonSkinnedMeshInstance((PPolygonSkinnedMeshInstance) originalMeshInstance); else {
            newMeshInst = new PPolygonMeshInstance(geometry.getName(), geometry, geometry.getTransform().getLocalMatrix());
            if (originalMeshInstance.getMaterialRef() != null) newMeshInst.setMaterial(new PMeshMaterial(originalMeshInstance.getMaterialRef()));
        }
        newMeshInst.setName(originalMeshInstance.getName());
        newMeshInst.getTransform().getLocalMatrix().set(originalMeshInstance.getTransform().getLocalMatrix());
        for (PNode kid : originalMeshInstance.getChildren()) newMeshInst.addChild(processNode(kid));
        return newMeshInst;
    }

    /**
     * Process a conceret skinned meshAsset (contains geometry, not an instance)
     * and produce a skinned meshAsset instance 
     * @param mesh
     * @return meshInst (PNode)
     */
    @InternalAPI
    public synchronized PPolygonSkinnedMeshInstance processSkinnedMesh(PPolygonSkinnedMesh mesh) {
        FastTable<PNode> kids = new FastTable<PNode>(mesh.getChildren());
        int index = addMeshGeometry(mesh);
        PPolygonSkinnedMesh geometry = (PPolygonSkinnedMesh) localGeometry.get(index);
        PPolygonSkinnedMeshInstance meshInst = new PPolygonSkinnedMeshInstance(geometry.getName(), geometry, geometry.getTransform().getLocalMatrix());
        meshInst.setName(mesh.getName());
        if (kids != null) {
            for (PNode kid : kids) meshInst.addChild(processNode(kid));
        }
        return meshInst;
    }

    /**
     * Process a conceret joint (may have children that contain geometry, not instances)
     * and produce a joint instance 
     * @param joint
     * @return jointInst (PNode)
     */
    private PNode processJoint(PJoint joint) {
        PJoint jointInst = new PJoint(joint.getName(), joint.getTransform());
        FastTable<PNode> kids = new FastTable<PNode>(joint.getChildren());
        for (int i = 0; i < kids.size(); i++) {
            PNode kid = kids.get(i);
            jointInst.addChild(processNode(kid));
        }
        return jointInst;
    }

    /**
     * Removes a modelAsset instance from this scene by reference
     * @param instance
     */
    public void removeModelInstance(PNode instance) {
        instancesRoot.removeChild(instance);
    }

    /**
     * Removes a modelAsset instance from this scene by name
     * @param name
     * @return result (PPolygonModelInstance)
     */
    public ModelInstance removeModelInstance(String name) {
        ModelInstance result = (ModelInstance) instancesRoot.removeChild(name);
        return result;
    }

    /**
     * Installs a rechieved asset (received from the repository) to
     * its apropriate place that is defined by the placeholder
     * @param placeHolder
     * @param asset
     */
    private void installAsset(SharedAssetPlaceHolder placeHolder, SharedAsset asset) {
        sharedAssets.add(asset);
        PNode parent = placeHolder.getParent();
        if (parent == null) logger.info("Deprecated \"target\" usage."); else if (asset.getAssetData() instanceof PScene) {
            PScene otherScene = ((PScene) asset.getAssetData());
            for (SharedAsset sa : otherScene.getAssetList()) sharedAssets.add(sa);
            PNode newInstance = processNode(otherScene.getInstances());
            newInstance.setName(asset.getLocation().getRelativePath());
            parent.removeChild(placeHolder);
            while (newInstance.getChildrenCount() > 0) parent.addChild(newInstance.getChild(0));
            if (placeHolder.getInitializer() != null) placeHolder.getInitializer().initialize(parent);
        } else {
            if (asset.getAssetData() instanceof SkeletonNode) {
                SkeletonNode skeleton = (SkeletonNode) processSkeletonNode((SkeletonNode) asset.getAssetData());
                skeleton.setName(placeHolder.getName());
                parent.replaceChild(placeHolder, skeleton, true);
                if (placeHolder.getInitializer() != null) placeHolder.getInitializer().initialize(skeleton);
            } else if (asset.getAssetData() instanceof PPolygonMesh) {
                PPolygonMesh geometry = (PPolygonMesh) asset.getAssetData();
                PPolygonMeshInstance newInstance = new PPolygonMeshInstance(geometry.getName(), geometry, geometry.getTransform().getLocalMatrix());
                newInstance.setName(placeHolder.getName());
                parent.replaceChild(placeHolder, newInstance, true);
                parent.flattenHierarchy();
                if (placeHolder.getInitializer() != null) placeHolder.getInitializer().initialize(newInstance);
            }
        }
    }

    /**
     * Implements RepositoryUser interface
     * @param asset
     */
    public void receiveAsset(SharedAsset asset) {
        if (asset.getAssetData() == null) logger.log(Level.SEVERE, "PScene - receiveAsset - timed out (data is null) - {0}", asset.getLocation());
        SharedAssetPlaceHolder target = null;
        synchronized (sharedAssetWaitingList) {
            for (SharedAssetPlaceHolder placeHolder : sharedAssetWaitingList) {
                if (asset.getLocation().equals(placeHolder.getLocation())) {
                    target = placeHolder;
                    sharedAssetWaitingList.remove(target);
                    break;
                }
            }
        }
        if (target != null) {
            if (asset.getAssetData() != null) installAsset(target, asset); else {
                target.setName(target.getName() + " ERROR : Asset was unable to load");
                logger.log(Level.SEVERE, "Unable to load asset for {0}", target.getName());
            }
            for (SharedAssetPlaceHolder freeLoader : target.getFreeLoaders()) installAsset(freeLoader, asset);
            target.clearFreeloaders();
        } else logger.log(Level.WARNING, "{0}''s SharedAsset received but no one came to pick it up! {1}", new Object[] { getName(), asset.getLocation() });
        if (jscene != null) jscene.geometryModified();
    }

    /**
     * Add an instance to this scene by supplying a SharedAsset that contains
     * a descriptor
     * @param meshAsset
     * @param name
     * @return PNode
     */
    public PNode addMeshInstance(String name, SharedAsset meshAsset) {
        return addMeshInstance(name, meshAsset, null);
    }

    private PNode processSkeletonNode(SkeletonNode skeletonNode) {
        SkeletonNode result = new SkeletonNode(skeletonNode);
        for (PNode kid : skeletonNode.getChildren()) result.addChild(processNode(kid));
        result.setSkeletonRoot(result.getSkeletonRoot());
        result.refresh();
        return result;
    }

    /**
     * Add an instance to this scene by supplying a SharedAsset that contains
     * a descriptor
     * @param meshAsset
     * @param origin
     * @param forAModel
     * @return
     */
    private PNode addMeshInstance(String name, SharedAsset meshAsset, ModelInstance forThisModelInstance) {
        BooleanPointer load = new BooleanPointer(false);
        PNode asset = processSharedAsset(meshAsset, load);
        asset.setName(name);
        if (forThisModelInstance == null) {
            asset.setDirty();
            if (!(asset instanceof SharedAssetPlaceHolder)) {
                while (asset.getChildrenCount() > 0) instancesRoot.addChild(asset.getChild(0));
            } else instancesRoot.addChild(asset);
        } else {
            if (!(asset instanceof SharedAssetPlaceHolder)) {
                while (asset.getChildrenCount() > 0) forThisModelInstance.addChild(asset.getChild(0));
            } else forThisModelInstance.addChild(asset);
        }
        if (load.get()) ((Repository) worldManager.getUserData(Repository.class)).loadColladaMT(meshAsset, this);
        return asset;
    }

    /***
     * Add an instance to this scene by supplying a SharedAsset that contains
     * a descriptor
     * @param name
     * @param modelAsset
     * @param origin
     * @return PPolygonModelInstance
     */
    public ModelInstance addModelInstance(String name, SharedAsset modelAsset, PMatrix origin) {
        ModelInstance modelInstance = new ModelInstance(name, origin);
        if (modelAsset != null) addMeshInstance(name, modelAsset, modelInstance);
        instancesRoot.addChild(modelInstance);
        modelInstance.setDirty();
        return modelInstance;
    }

    /**
     * Process a shared asset, if this scene holds a local reference to 
     * the required asset it will use it, otherwise it will either 
     * tag along to an existing pending request or start a new one.
     * 
     * @param asset
     * @param load
     * @return
     */
    private PNode processSharedAsset(SharedAsset asset, BooleanPointer load) {
        PNode result = null;
        int index = sharedAssets.indexOf(asset);
        if (-1 == index && asset.getAssetData() == null) {
            SharedAssetPlaceHolder assetInstance = new SharedAssetPlaceHolder("SharedAssetPlaceHolder instance", asset.getLocation(), null, asset.getInitializer());
            result = assetInstance;
            boolean freeloader = false;
            for (SharedAssetPlaceHolder holder : sharedAssetWaitingList) {
                if (holder.getLocation().equals(asset.getLocation())) {
                    holder.addFreeloader(assetInstance);
                    freeloader = true;
                    break;
                }
            }
            if (!freeloader) {
                sharedAssetWaitingList.add(assetInstance);
                load.set(true);
            }
        } else {
            SharedAsset loadedModel = null;
            if (index == -1) {
                sharedAssets.add(asset);
                loadedModel = asset;
            } else {
                loadedModel = sharedAssets.get(index);
            }
            if (loadedModel.getAssetData() instanceof PPolygonSkinnedMesh) {
                PPolygonSkinnedMesh geometry = (PPolygonSkinnedMesh) loadedModel.getAssetData();
                PPolygonSkinnedMeshInstance newInstance = new PPolygonSkinnedMeshInstance(geometry.getName(), geometry, geometry.getTransform().getLocalMatrix());
                newInstance.recalculateInverseBindPose();
                result = newInstance;
            } else if (loadedModel.getAssetData() instanceof PPolygonMesh) {
                PPolygonMesh geometry = (PPolygonMesh) loadedModel.getAssetData();
                PPolygonMeshInstance newInstance = new PPolygonMeshInstance(geometry.getName(), geometry, geometry.getTransform().getLocalMatrix());
                result = newInstance;
            } else if (loadedModel.getAssetData() instanceof PScene) {
                PScene otherScene = ((PScene) loadedModel.getAssetData());
                PNode newInstance = processNode(otherScene.getInstances());
                newInstance.setName(loadedModel.getLocation().getRelativePath());
                for (SharedAsset sa : otherScene.getAssetList()) {
                    sharedAssets.add(sa);
                }
                result = newInstance;
            } else if (loadedModel.getAssetData() instanceof SkeletonNode) {
                SkeletonNode newSkeleton = (SkeletonNode) processNode((SkeletonNode) loadedModel.getAssetData());
                result = newSkeleton;
            }
            Repository.get().submitWork(new InitWork(asset.getInitializer(), result));
            setDirty();
            if (jscene != null) jscene.geometryModified();
        }
        return result;
    }

    static class InitWork implements Runnable {

        AssetInitializer init = null;

        PNode node = null;

        public InitWork(AssetInitializer initializer, PNode result) {
            init = initializer;
            node = result;
        }

        public void run() {
            if (init != null) init.initialize(node);
        }
    }

    /**
     * Proccess a skinned mesh joint
     * @param skinnedMeshJoint
     * @return
     */
    private PNode processSkinnedMeshJoint(SkinnedMeshJoint skinnedMeshJoint) {
        SkinnedMeshJoint result = new SkinnedMeshJoint(skinnedMeshJoint.getName(), new PTransform(skinnedMeshJoint.getTransform()));
        for (PNode kid : skinnedMeshJoint.getChildren()) result.addChild(processNode(kid));
        return result;
    }

    /**
     * Accessor
     * @return The JScene, may be null if unset
     */
    public JScene getJScene() {
        return jscene;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        sharedAssetWaitingList = new FastList<SharedAssetPlaceHolder>();
        sharedAssets = new FastList<SharedAsset>();
    }

    /**
     * This method clears out all the locally cached geometry references.
     * Use with caution.
     */
    @Deprecated
    public synchronized void clearLocalCache() {
        sharedAssets.clear();
        localGeometry.clear();
    }

    @Deprecated
    synchronized void setTransformsSubmitted(boolean submitted) {
        transformSubmitted = submitted;
    }

    /**
     * Loads a simple model, without a skeleton and\or animations.
     * @param path - relative path to the model file
     * @param block - if true this call will block until the model is loaded, it is recommanded to set this to false and use an initializer if needed
     * @param initializer - the initializer will operate when the asset is received from the repository, may be null
     * @return
     */
    public static ModelInstance loadSimpleGeometryFromCollada(RRL resource, boolean block, AssetInitializer initializer) {
        LoaderParams params = new LoaderParams.Builder().setKeepPPolygonMeshData(true).setLoadGeometry(true).setLoadSkeleton(false).setLoadAnimation(false).build();
        SharedAsset worldAsset = new SharedAsset(resource, initializer, params);
        PScene scene = new PScene();
        ModelInstance modInst = scene.addModelInstance(FileUtils.getFileNameWithoutExtension(resource.getRelativePath()), worldAsset, new PMatrix());
        if (modInst.getChildrenCount() == 0) throw new RuntimeException("no children in scene after loading a model");
        if (block) {
            while (modInst.getChild(0) instanceof SharedAssetPlaceHolder) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PScene.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return modInst;
    }
}
