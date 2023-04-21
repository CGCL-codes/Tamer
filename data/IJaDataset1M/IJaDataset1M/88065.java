package fr.scenerenderer.objects3d.shapes3d.materials.localilluminationmodels;

import fr.scenerenderer.geometry.Vector3D;
import fr.scenerenderer.objects3d.Object3D;
import fr.scenerenderer.RGBColor;
import fr.scenerenderer.Intersection;
import fr.scenerenderer.Ray;
import fr.scenerenderer.geometry.Point3D;

public class BlinnLocalIlluminationModel extends LocalIlluminationModel {

    protected float diffusion;

    /**
     * Get accessor for diffusion
     * @return  value of diffusion
     */
    public float getDiffusion() {
        return this.diffusion;
    }

    /**
     * Set accessor for diffusion
     * @param value the value to set in diffusion
     */
    public void setDiffusion(float value) {
        this.diffusion = value;
    }

    protected float specularity;

    /**
     * Get accessor for specularity
     * @return  value of specularity
     */
    public float getSpecularity() {
        return this.specularity;
    }

    /**
     * Set accessor for specularity
     * @param value the value to set in specularity
     */
    public void setSpecularity(float value) {
        this.specularity = value;
    }

    protected float specularPower;

    /**
     * Get accessor for specularPower
     * @return  value of specularPower
     */
    public float getSpecularPower() {
        return this.specularPower;
    }

    /**
     * Set accessor for specularPower
     * @param value the value to set in specularPower
     */
    public void setSpecularPower(float value) {
        this.specularPower = value;
    }

    protected RGBColor specularColor;

    /**
     * Get accessor for specularColor
     * @return  value of specularColor
     */
    public RGBColor getSpecularColor() {
        return this.specularColor;
    }

    /**
     * Set accessor for specularColor
     * @param value the value to set in specularColor
     */
    public void setSpecularColor(RGBColor value) {
        this.specularColor = value;
    }

    public BlinnLocalIlluminationModel(final float diffusion, final float specularity, final float specularPower, final RGBColor specularColor) {
        this.diffusion = diffusion;
        this.specularity = specularity;
        this.specularPower = (specularPower % 2 == 0) ? specularPower : specularPower + 1;
        this.specularColor = specularColor;
    }

    public BlinnLocalIlluminationModel(final float diffusion, final float specularity, final float specularPower) {
        this(diffusion, specularity, specularPower, RGBColor.WHITE);
    }

    public BlinnLocalIlluminationModel(final float diffusion, final float specularity) {
        this(diffusion, specularity, 10);
    }

    public BlinnLocalIlluminationModel() {
        this(1, 1, 10);
    }

    public RGBColor computeBRDF(final Intersection intersection, final Ray ray, final Point3D lightPosition) {
        Vector3D L = new Vector3D(intersection, lightPosition).normalize();
        Vector3D V = ray.getDirection().opposite();
        float NscalaireL = intersection.getNormal().dot(L);
        if (NscalaireL <= Object3D.EPSILON) return new RGBColor(0, 0, 0);
        Vector3D H = (L.plus(V)).normalize();
        float HscalaireNPow = (float) Math.pow(H.dot(intersection.getNormal()), this.specularPower);
        return (intersection.getColor().cross(NscalaireL * this.diffusion)).plus(this.specularColor.cross(HscalaireNPow * specularity));
    }
}
