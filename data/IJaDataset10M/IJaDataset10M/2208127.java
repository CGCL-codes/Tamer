package org.jrman.util;

public class TriangleSamplesFilter extends SamplesFilter {

    protected float filterFunc(float x, float y, float xWidth, float yWidth) {
        return ((1f - Math.abs(x)) / (xWidth * .5f)) * ((1f - Math.abs(y)) / (yWidth * .5f));
    }
}
