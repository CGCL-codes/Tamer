package org.expasy.jpl.core.mol.chem.impl;

import java.io.Serializable;
import org.expasy.jpl.commons.base.Evaluable;
import org.expasy.jpl.commons.base.Registrable;
import org.expasy.jpl.core.mol.chem.PhysicalConstants;
import org.expasy.jpl.core.mol.chem.MassCalculator.Accuracy;
import org.expasy.jpl.core.mol.chem.api.ChargedParticle;
import org.expasy.jpl.core.mol.chem.api.Weighable;

public final class ChargedParticleImpl implements Evaluable, Weighable, ChargedParticle, Registrable, Serializable {

    private static final long serialVersionUID = -4705805530470603425L;

    public static final ChargedParticleImpl ELECTRON = new ChargedParticleImpl("e-", PhysicalConstants.ELECTRON_MASS, -1);

    public static final ChargedParticleImpl POSITRON = new ChargedParticleImpl("e+", PhysicalConstants.ELECTRON_MASS, 1);

    private String id;

    private double mass;

    private int charge;

    private ChargedParticleImpl(String id, double mass, int charge) {
        this.id = id;
        this.mass = mass;
        this.charge = charge;
    }

    public final double getMass(Accuracy accuracy) {
        return mass;
    }

    public final String getId() {
        return id;
    }

    public final double eval() {
        return mass;
    }

    public final int getCharge() {
        return charge;
    }

    public final String toString() {
        return id;
    }
}
