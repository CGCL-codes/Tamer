package org.jrman.util;

public class SincSamplesFilter extends SamplesFilter {

    protected float filterFunc(float x, float y, float xWidth, float yWidth) {
        float s;
        float t;
        if (x > -0.001f && x < 0.001f) s = 1f; else s = (float) Math.sin(x) / x;
        if (y > -0.001f && y < 0.001f) t = 1f; else t = (float) Math.sin(y) / y;
        return s * t;
    }
}
