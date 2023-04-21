package jkepler.events;

import jkepler.kepler.*;
import jfalib.base.math.Angles;

;

/**
 * Methods in the class find the time of apoapsis (aphelion, apogee),
 * his period and a maximal true radius i.e. apoapsis distance
 * by using orbital elements.
 * @author Peter Hristozov.
 * @version 3.0 12.Apr.2010
 */
public class Apoapsis {

    /**
     * Moment of apoapsis in MJD.
     */
    protected double mjdApoapsis;

    /**
     * period of apoapsis in days.
     */
    protected double period;

    /**
     * Apoapsis distance in AU.
     */
    protected double maxDist;

    /**
     * Method is constructor for class and finds time of apoapsis,
     * his period and poapsis distance.
     * @param orbel base class for orbital elements.
     */
    public Apoapsis(OrbElem orbel) {
        if (orbel.isValid()) {
            if (orbel.e() < 1.0) {
                this.mjdApoapsis = orbel.EpochM() + (Angles.PI - orbel.M()) / orbel.n();
                this.maxDist = orbel.q() * (1 + orbel.e()) / (1 - orbel.e());
            } else {
                this.mjdApoapsis = Double.POSITIVE_INFINITY;
                this.maxDist = Double.POSITIVE_INFINITY;
            }
            this.period = orbel.Period();
        } else {
            this.mjdApoapsis = Double.NaN;
            this.period = Double.NaN;
            this.maxDist = Double.NaN;
        }
    }

    /**
     * @return Moment of apoapsis in MJD.
     */
    public double mjdApoapsis() {
        return this.mjdApoapsis;
    }

    /**
     * @return period of apoapsis in days.
     */
    public double period() {
        return this.period;
    }

    /**
     * @return Apoapsis distance in AU.
     */
    public double maxDist() {
        return this.maxDist;
    }
}
