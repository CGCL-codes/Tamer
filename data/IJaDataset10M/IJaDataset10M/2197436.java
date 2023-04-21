package de.binfalse.martin.tools;

/**
 * Scaling through square root
 * 
 * @author Martin Scharm
 *
 */
public class ScalerSQRT extends Scaler {

    @Override
    public double scale(double s) {
        return Math.sqrt(s);
    }

    @Override
    public double unscale(double s) {
        return s * s;
    }
}
