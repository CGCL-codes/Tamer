package com.mw3d.swt.terrain;

import com.jmex.terrain.util.MidPointHeightMap;

/**
 * @author Tareq doufish
 * Created on May 13, 2005
 * Generates noise using the Minpoint noies generator.
 */
public class MidPointNoiseGenerator extends NoiseGenerator {

    private float roughness;

    private int size;

    private static MidPointNoiseGenerator instance;

    public static MidPointNoiseGenerator getInstance() {
        if (instance == null) {
            return instance = new MidPointNoiseGenerator();
        } else {
            return instance;
        }
    }

    public void setParameters(int size, float rough) {
        this.size = size;
        this.roughness = rough;
    }

    public void generateNoise() {
        heightmap = new MidPointHeightMap(size, roughness);
    }
}
