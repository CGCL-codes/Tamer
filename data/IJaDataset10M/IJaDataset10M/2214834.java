package org.micthemodel.micWizard.elements;

/**
 *
 * @author sbishnoi
 */
public class PowderPhase {

    private String name;

    private double density;

    private double massFraction;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the density
     */
    public double getDensity() {
        return density;
    }

    /**
     * @param density the density to set
     */
    public void setDensity(double density) {
        this.density = density;
    }

    /**
     * @return the massFraction
     */
    public double getMassFraction() {
        return massFraction;
    }

    /**
     * @param massFraction the massFraction to set
     */
    public void setMassFraction(double massFraction) {
        this.massFraction = massFraction;
    }
}
