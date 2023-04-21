package imi.objects;

import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import imi.collision.EntityCollisionComponent;
import imi.repository.MaterialRepoComponent;
import imi.repository.MaterialRepoComponent.Materials;
import imi.repository.RRL;
import imi.repository.Repository;
import imi.scene.PTransform;
import imi.scene.polygonmodel.BillboardMeshInstance;
import imi.scene.polygonmodel.PMeshMaterial;
import imi.scene.polygonmodel.ModelInstance;
import imi.scene.utils.PMeshUtils;
import imi.utils.Cosmic;
import java.util.Properties;

/**
 * A simple quad Object
 * @author Lou Hayt
 */
public class QuadObject extends ObjectComponent {

    private BillboardMeshInstance meshInst = null;

    protected PMeshMaterial material = null;

    private float height, width;

    private boolean centeredQuad = true;

    /** enable orthographic draw mode */
    private boolean ortho = false;

    protected QuadObject(String name) {
        super(name);
    }

    public QuadObject(String name, float width, float height, PMeshMaterial material, boolean buildCollisionComponent) {
        this(name);
        initialize(width, height, material, buildCollisionComponent, true, 1.0f);
    }

    public QuadObject(String name, float width, float height, PMeshMaterial material, boolean buildCollisionComponent, boolean centeredQuad, float UVScale) {
        this(name);
        initialize(width, height, material, buildCollisionComponent, centeredQuad, UVScale);
    }

    public QuadObject(Properties props) {
        this(props.getProperty("name", "Quad"));
        String mat = props.getProperty("material", Materials.UnlitSingleTexture.name());
        String texture = props.getProperty("texture");
        float widthP = 1;
        float heightP = 1;
        float UVScale = 1.0f;
        boolean buildCollisionComponent = true;
        boolean centeredQuadP = true;
        try {
            widthP = Float.parseFloat(props.getProperty("width", "1.0"));
            heightP = Float.parseFloat(props.getProperty("height", "1.0"));
            UVScale = Float.parseFloat(props.getProperty("UVScale", "1.0"));
            buildCollisionComponent = Boolean.parseBoolean(props.getProperty("buildCollisionComponent", "true"));
            centeredQuad = Boolean.parseBoolean(props.getProperty("centeredQuad", "true"));
        } catch (Exception ex) {
        }
        PMeshMaterial materialP = Cosmic.getRepository().getRepositoryComponent(MaterialRepoComponent.class).getMaterial(mat);
        materialP.setTexture(new RRL(texture), 0);
        initialize(widthP, heightP, materialP, buildCollisionComponent, centeredQuadP, UVScale);
    }

    @Override
    public void save(Properties props) {
        super.save(props);
        props.setProperty("type", getClass().getCanonicalName());
        props.setProperty("width", Float.toString(width));
        props.setProperty("height", Float.toString(height));
        props.setProperty("centeredQuad", Boolean.toString(centeredQuad));
    }

    private void initialize(float width, float height, PMeshMaterial material, boolean buildCollisionComponent, boolean centeredQuad, float UVScale) {
        this.material = material;
        modelInstance = new ModelInstance("Quad Model");
        modelInstance.setEntity(getEntity());
        setDimentions(width, height, centeredQuad, UVScale);
        initializeRenderComponent(modelInstance);
        if (buildCollisionComponent) initializeCollisionComponent(modelInstance);
    }

    public synchronized void setDimentions(float width, float height, boolean centeredQuad) {
        setDimentions(width, height, centeredQuad, 1.0f);
    }

    public synchronized void setDimentions(float width, float height, boolean centeredQuad, float UVScale) {
        this.width = width;
        this.height = height;
        this.centeredQuad = centeredQuad;
        System.out.println("Regenerating quad height: " + height + " width: " + width);
        EntityCollisionComponent cc = getEntityCollisionComponent();
        if (cc != null) {
            cc.setCollidable(false);
            cc.setPickable(false);
        }
        if (meshInst != null) modelInstance.removeChild(meshInst);
        meshInst = PMeshUtils.createQuad("quad object", width, height, false, centeredQuad, UVScale);
        setMaterial();
        applyMaterial();
        modelInstance.addChild(meshInst);
        modelInstance.calculateWorldBounds();
        if (cc != null) {
            cc.setCollidable(true);
            cc.setPickable(true);
        }
    }

    private void setMaterial() {
        if (material == null) {
            material = Repository.get().getMaterial(Materials.UnlitSingleTexture);
            material.setTexture(new RRL("assets/textures/imilogo.png"), 0);
            material.setTransparencyType(PMeshMaterial.TransparencyType.ALPHA_ENABLED);
        }
        setMaterial(material);
    }

    public PMeshMaterial getMaterial() {
        return material;
    }

    public void setMaterial(PMeshMaterial material) {
        this.material = material;
        meshInst.setMaterial(material);
    }

    public void applyMaterial() {
        meshInst.applyMaterial();
    }

    public BillboardMeshInstance getMeshInst() {
        return meshInst;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    /**
     * is set to draw in orthographic mode
     * @return
     */
    public boolean isOrtho() {
        return ortho;
    }

    private boolean orthoPrevBillboardPoint;

    private PTransform orthoPrevModelTransform = null;

    private PTransform orthoPrevMeshTransform = null;

    /**
     * enable orthographic draw mode
     * @param ortho
     */
    public void setOrtho(boolean ortho) {
        System.out.println(getName() + " setOrtho " + ortho);
        if (this.ortho != ortho) {
            this.ortho = ortho;
            if (ortho) {
                orthoPrevBillboardPoint = meshInst.isBillboardPoint();
                meshInst.setBillboardPoint(false);
                modelInstance.setCullOverride(true);
                meshInst.getMaterialRef().setTransparencyType(PMeshMaterial.TransparencyType.HUD_QUEUE_ORTHO);
                meshInst.getSharedMesh().setRenderQueueMode(Renderer.QUEUE_ORTHO);
                setDimentions(width, height, false);
                orthoPrevModelTransform = new PTransform(modelInstance.getTransform());
                orthoPrevMeshTransform = new PTransform(meshInst.getTransform());
                modelInstance.setTransform(new PTransform());
                meshInst.setTransform(new PTransform());
            } else {
                meshInst.setBillboardPoint(orthoPrevBillboardPoint);
                modelInstance.setCullOverride(false);
                meshInst.getMaterialRef().setTransparencyType(PMeshMaterial.TransparencyType.ALPHA_ENABLED);
                meshInst.getSharedMesh().setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
                setDimentions(width, height, centeredQuad);
                modelInstance.setTransform(orthoPrevModelTransform);
                meshInst.setTransform(orthoPrevMeshTransform);
                orthoPrevModelTransform = null;
                orthoPrevMeshTransform = null;
            }
        }
    }

    /**
     * Local space translation offset, can be used to move the
     * origin of the quad, by default the origin of the quad is its center.
     * @param offset
     */
    public void setPositionOffset(Vector3f offset) {
        getMeshInst().getTransform().getLocalMatrix().setTranslation(offset);
    }
}
