package org.micthemodel.plugins.kinetics;

import org.micthemodel.elements.Product;
import org.micthemodel.elements.Reactant;
import org.micthemodel.elements.Reaction;
import org.micthemodel.particles.ReactingParticle;

/**
 * Kinetics goverened by Avrami law.
 * @author bishnoi
 */
public class AvramiKineticsProductControlled extends Kinetics {

    /**
     * This field stores the value of the combined rate constant
     * of the Avrami equation.
     */
    public double k;

    /**
     * This field stores the value of the time exponential factor
     * of the Avrami equation.
     */
    public double n;

    /**
     * This field stores the time at which the reaction following
     * this kinetics started.
     */
    public double t0;

    /**
     * This field stores the degree of reaction at the time that this
     * reaction starts.
     */
    public double alpha0;

    public AvramiKineticsProductControlled() {
    }

    /**
     * Creates a new instance of AvramiKinetics with the given parameters,
     * initial time and initial degree of reaction.
     */
    public AvramiKineticsProductControlled(double k, double n, double t0, double alpha0, int order, Reaction reaction, Product reactant) {
        super(order, reaction, reactant);
        Object[] initialisationValueArray = { k, n, t0, alpha0, order, reaction, reactant };
        this.initialisationValues = initialisationValueArray;
        this.k = k;
        this.n = n;
        this.t0 = t0;
        this.alpha0 = alpha0;
        this.layerNo = reactant.getMaterial().getLayerNo();
        this.setName("Avrami Kinetics");
    }

    /**
     * This method implements the react method of the interface Kinetics.
     * The reaction, particle, layer number, time, time step and correction
     * factor are taken as input and the change of radius is returned.
     */
    @Override
    public double getAmount(double time, double dt, ReactingParticle particle) {
        time -= Math.max(this.t0, this.reaction.getLastInactiveTime());
        if (time <= 0) {
            return 0.0;
        }
        double exp = Math.exp(-this.k * Math.pow(time, this.n));
        return -this.k * this.n * Math.pow(time, this.n - 1.0) * exp * dt;
    }

    @Override
    public double getAmount(double time, double dt) {
        time -= Math.max(this.t0, this.reaction.getLastInactiveTime());
        if (time <= 0) {
            return 0.0;
        }
        return this.k * this.n * Math.pow(time, n - 1.0) * Math.exp(-this.k * Math.pow(time, this.n)) * dt * this.reactant.getMaterial().getInitialAmount();
    }

    @Override
    public Class[] constructorParameterClasses() {
        Class[] parameters = { double.class, double.class, double.class, double.class, int.class, Reaction.class, Product.class };
        return parameters;
    }

    @Override
    public String[] constructorParameterNames() {
        String[] names = { "Avrami constant k", "Avrami constant n", "Starting time", "Initial degree of hydration", "Order of implementation of kinetics", "Reaction", "Product" };
        return names;
    }

    /**
     * This field stores the layer number of the relevant materials.
     */
    public int layerNo;
}
