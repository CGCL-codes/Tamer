package imi.character;

import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector3f;
import imi.repository.MaterialRepoComponent.Materials;
import imi.repository.RRL;
import imi.repository.Repository;
import imi.scene.PMatrix;
import imi.scene.polygonmodel.ModelInstance;
import imi.scene.polygonmodel.PMeshMaterial;
import imi.scene.polygonmodel.PPolygonSkinnedMeshInstance;
import imi.utils.MathUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jdesktop.mtgame.WorldManager;

/**
 * This class provides eyeball behavior by wrapping the mesh and setting particular
 * attributes on it. The EyeBall class is capable of tracking a point within
 * constraints (to avoid unnatural gazing angles).
 * @author Lou Hayt
 * @author Ronald E. Dahlgren
 * @author Shawn Kendall
 */
public final class EyeBall extends PPolygonSkinnedMeshInstance implements Serializable {

    /** Serialization version number **/
    private static final long serialVersionUID = 1l;

    /** The Character who owns this eyeball **/
    private transient Character character;

    /** The Model Instance that owns this eyeball **/
    private ModelInstance modelInst;

    /** World space coordinates of the view target **/
    private final Vector3f target = new Vector3f(0.0f, 1.0f, 0.0f);

    /** Used as a limiting factor to restrict the range of motion of the eyeball **/
    private float limitCone = 0.57f;

    /**
     * This is used to warp space such that the eye's rotational range of motion
     * is more ellipsoid rather than circular. Humans have a broader range
     * horizontally than they do vertically.
     **/
    private float yScale = 2.0f;

    /** True when the target is within a certain angular range **/
    private boolean bInCone = false;

    /** Reference to the other side **/
    private EyeBall otherEye = null;

    /** Math utils execution context **/
    private transient MathUtils.MathUtilsContext mathContext = MathUtils.getContext();

    /** algorithm scratch space **/
    private transient Vector3f translationStorage = new Vector3f();

    private transient Vector3f forwardVec = new Vector3f();

    private transient Vector3f directionToTarget = new Vector3f();

    private transient PMatrix eyeWorldMatrix = new PMatrix();

    /**
     * Construct a new eyeball using the provided mesh as the eye mesh, the model
     * instance provided is the eyeball's owner, and the pscene containing both.
     * Arguments must not be null.
     * @param meshInstance The eyeball mesh
     * @param modelInst The owning model
     * @param pscene The owning pscene
     */
    public EyeBall(PPolygonSkinnedMeshInstance meshInstance, Character character) {
        super(meshInstance);
        setAndLinkSkeletonNode(character.getSkeleton());
        this.modelInst = character.getModelInstance();
        this.character = character;
    }

    /**
     * Performs the eyeball lookAt behavior.
     * @param matrix The matrix being modified
     * @param jointIndex Joint to modify
     */
    void lookAtTarget(PMatrix matrix) {
        matrix.getLocalX(translationStorage);
        float scale = translationStorage.length();
        matrix.getTranslation(translationStorage);
        PMatrix modelWorldRef = modelInst.getTransform().getWorldMatrix();
        eyeWorldMatrix.mul(modelWorldRef, matrix);
        modelWorldRef.getLocalZ(forwardVec);
        directionToTarget.set(target);
        directionToTarget.x -= eyeWorldMatrix.getTranslationX();
        directionToTarget.y -= eyeWorldMatrix.getTranslationY();
        directionToTarget.z -= eyeWorldMatrix.getTranslationZ();
        directionToTarget.y *= yScale;
        directionToTarget.normalizeLocal();
        float dot = directionToTarget.dot(forwardVec);
        eyeWorldMatrix.getTranslation(directionToTarget);
        if (dot > limitCone) {
            bInCone = true;
            if (otherEye.isInCone()) {
                MathUtils.lookAtFlippedZ(target, directionToTarget, Vector3f.UNIT_Y, matrix, mathContext);
                matrix.mul(modelWorldRef.inverse(), matrix);
                matrix.setTranslation(translationStorage);
            }
        } else bInCone = false;
        matrix.normalizeCP();
        matrix.setScale(scale);
    }

    /**
     * Set the target vector
     * @param target
     */
    public void setTarget(Vector3f target) {
        this.target.set(target);
    }

    /**
     * Retrieve a reference to the model instance owning this eyeball
     * @return Model instance
     */
    public ModelInstance getModelInst() {
        return modelInst;
    }

    /**
     * Sets the 'other' eye. Assumes that a character has two eyes.
     * @param otherOne
     */
    public void setOtherEye(EyeBall otherOne) {
        otherEye = otherOne;
    }

    /**
     * True if the target is within a certain angular distance of the eye
     * @return True if in cone
     */
    public boolean isInCone() {
        return bInCone;
    }

    /**
     * Package private method to apply particular shading effects to the eyeball
     * instance.
     * @param wm
     * @throws NullPointerException If (texture == null)
     */
    void setEyeBallMaterial(String texture) {
        if (texture == null) throw new NullPointerException("Null texture path specified!");
        PMeshMaterial myMaterial = getMaterialRef();
        if (myMaterial == null) {
            Repository repo = (Repository) WorldManager.getDefaultWorldManager().getUserData(Repository.class);
            setMaterial(repo.getMaterial(Materials.CharacterShiny), true, true);
            myMaterial = getMaterialRef();
        }
        myMaterial.setTexture(new RRL(texture), 0);
        myMaterial.getTextureRef(0).setMinFilter(MinificationFilter.BilinearNoMipMaps);
        myMaterial.setTransparencyType(PMeshMaterial.TransparencyType.NO_TRANSPARENCY);
    }

    /****************************
     * SERIALIZATION ASSISTANCE *
     ****************************/
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mathContext = MathUtils.getContext();
    }
}
