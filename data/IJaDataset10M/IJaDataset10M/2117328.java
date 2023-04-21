package com.vividsolutions.jts.algorithm.match;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.algorithm.distance.*;

/**
 * Measures the degree of similarity between two {@link Geometry}s
 * using the Hausdorff distance metric.
 * The measure is normalized to lie in the range [0, 1].
 * Higher measures indicate a great degree of similarity.
 * <p>
 * The measure is computed by computing the Hausdorff distance
 * between the input geometries, and then normalizing
 * this by dividing it by the diagonal distance across 
 * the envelope of the combined geometries.
 * 
 * @author mbdavis
 *
 */
public class HausdorffSimilarityMeasure implements SimilarityMeasure {

    public HausdorffSimilarityMeasure() {
    }

    private static final double DENSIFY_FRACTION = 0.25;

    public double measure(Geometry g1, Geometry g2) {
        double distance = DiscreteHausdorffDistance.distance(g1, g2, DENSIFY_FRACTION);
        Envelope env = new Envelope(g1.getEnvelopeInternal());
        env.expandToInclude(g2.getEnvelopeInternal());
        double envSize = diagonalSize(env);
        double measure = 1 - distance / envSize;
        return measure;
    }

    public static double diagonalSize(Envelope env) {
        if (env.isNull()) return 0.0;
        double width = env.getWidth();
        double hgt = env.getHeight();
        return Math.sqrt(width * width + hgt * hgt);
    }
}
