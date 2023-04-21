package org.micthemodel.plugins.materialDistributionProfile;

import org.micthemodel.elements.Material;
import org.micthemodel.particles.ReactingParticle;

/**
 * This profile distributes the available material proportional to the
 * available surface area of each grain.
 *
 * @author bishnoi
 */
public class SurfaceDistributionProfile extends MaterialDistributionProfile {

    private double minRad;

    /**
     * Creates a new instance of SurfaceDistributionProfile
     */
    public SurfaceDistributionProfile() {
    }

    public SurfaceDistributionProfile(Material material, double minRad) {
        super(material);
        Object[] initialisationValueArray = { material, minRad };
        this.initialisationValues = initialisationValueArray;
        this.minRad = minRad;
    }

    public double getPortion(ReactingParticle grain, double time) {
        if (grain.getRadius() >= minRad) {
            return grain.getRadius() * grain.getRadius() * (double) grain.getFreeArea();
        }
        return minRad * minRad * (double) grain.getFreeArea();
    }

    public double getReportion(ReactingParticle grain, double time) {
        if (grain.getRadius() >= minRad) {
            return grain.getRadius() * grain.getRadius() * (double) grain.getFreeArea();
        }
        return minRad * minRad * (double) grain.getFreeArea();
    }

    public Class[] constructorParameterClasses() {
        Class[] parameters = { Material.class, double.class };
        return parameters;
    }

    public String[] constructorParameterNames() {
        String[] names = { "Material", "Minimum radius" };
        return names;
    }
}
