package org.mbari.gis;

import org.mbari.geometry.Envelope;

/**
 *
 * @author brian
 */
public interface DEMAccess {

    Envelope<Double> getEnvelope();

    double[] getX();

    double[] getY();

    float getZ(double x, double y);
}
